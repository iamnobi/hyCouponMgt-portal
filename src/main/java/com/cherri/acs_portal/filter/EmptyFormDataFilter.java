package com.cherri.acs_portal.filter;

import com.cherri.acs_portal.constant.MessageConstants;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import ocean.acs.commons.enumerator.ResultStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class EmptyFormDataFilter implements Filter {

    public EmptyFormDataFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        JsonObject errMsg = new JsonObject();
        if ("PUT".equals(req.getMethod()) || "POST".equals(req.getMethod())) {
            String contentType = req.getContentType();
            boolean isMultipartFormData =
              MediaType.MULTIPART_FORM_DATA_VALUE.equalsIgnoreCase(contentType);
            try {
                if (isMultipartFormData) {
                    boolean noParts = req.getParts() == null || req.getParts().isEmpty();
                    if (noParts) {
                        errMsg.addProperty("status", ResultStatus.COLUMN_NOT_EMPTY.getCode());
                        errMsg.addProperty("message",
                          "Request body " + MessageConstants.get(MessageConstants.COLUMN_NOT_EMPTY));
                        printMsg(response, errMsg);
                        return;
                    }
                }
            } catch (Exception e) {
                errMsg.addProperty("status", ResultStatus.SERVER_ERROR.getCode());
                errMsg.addProperty("message", e.getMessage());
                printMsg(response, errMsg);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void printMsg(ServletResponse response, JsonObject errMsgJson) throws IOException {
        try (PrintWriter printWriter = response.getWriter()) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            printWriter.print(errMsgJson.toString());
        }
    }

    @Override
    public void destroy() {
    }
}
