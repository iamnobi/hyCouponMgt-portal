package com.cherri.acs_portal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Slf4j
@UtilityClass
public class JsonUtils {

    /**
     * To Json user Jackson
     *
     * @param obj onj
     * @return JSON String
     */
    public String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    /**
     * To Pretty Json user Jackson
     *
     * @param obj onj
     * @return JSON String
     */
    public String toPrettyJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    /**
     * To Pretty Json user Jackson
     *
     * @param jsonString onj
     * @return JSON String
     */
    public String toPrettyJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObject = mapper.readValue(jsonString, Object.class);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return "";
        }
    }

    public Map<String, Object> convertJsonToMap(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

    public Object[] convertJsonToArray(String json) {
        try {
            return new ObjectMapper().readValue(json, Object[].class);
        } catch (IOException e) {
            return new Object[0];
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convertJson(String json, Class<T> clz) {
        try {
            return new ObjectMapper().readValue(json, clz);
        } catch (IOException e) {
            return (T) new Object();
        }
    }

}
