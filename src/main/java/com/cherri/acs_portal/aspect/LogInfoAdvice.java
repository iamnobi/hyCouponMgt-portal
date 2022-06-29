package com.cherri.acs_portal.aspect;

import com.cherri.acs_portal.annotation.LogInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Aspect
@Component
@AllArgsConstructor
public class LogInfoAdvice {

    private final ObjectMapper objectMapper;

    @Around("@annotation(com.cherri.acs_portal.annotation.LogInfo)")
    public Object logInfo(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogInfo logInfo = method.getAnnotation(LogInfo.class);

        String className = signature.getDeclaringType().getSimpleName();
        String methodName = method.getName();
        String classMethodName = String.format("[%s.%s]", className, methodName); // e.g. [ClassName.methodName]

        printStart(classMethodName,
                logInfo, method.getParameters(), joinPoint.getArgs());

        Object object = joinPoint.proceed();

        printEnd(classMethodName, logInfo.end());
        return object;
    }

    private String getParamsMapJsonString(Parameter[] inputParameters, Object[] inputArgs) {
        Map<String, Object> paramResultMap = new HashMap<>();
        for (int i = 0; i < inputParameters.length; i++) {
            paramResultMap.put(inputParameters[i].getName(), inputArgs[i]);
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(paramResultMap);
        } catch (JsonProcessingException e) {
            log.error("[getParamsMapJsonString] parse args to json string fail, args={}", inputArgs);
            return "";
        }
    }

    private void printStart(
            String classMethodName,
            LogInfo logInfo,
            Parameter[] inputParams,
            Object[] inputArgs) {

        if (logInfo.printParams()) {
            String paramsMapJsonString = getParamsMapJsonString(inputParams, inputArgs);
            log.info("{} start {}, params=\n{}",
                    classMethodName, logInfo.message(), paramsMapJsonString);
        } else {
            log.info("{} start {}",
                    classMethodName, logInfo.message());
        }
    }

    private void printEnd(String classMethodName, boolean hasEnd) {
        if (hasEnd) {
            log.info(classMethodName + " end");
        }
    }

    @AfterThrowing(value = "@annotation(com.cherri.acs_portal.annotation.LogInfo)", throwing = "ex")
    public void logInfoException(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("[{}.{}] throw exception={}", className, methodName, ex.toString());
    }
}
