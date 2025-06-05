package io.github.alberes.register.manager.authorization.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtils {

    private final ObjectMapper objectMapper;

    public <T> T fromJson(String json, Class<T> clazz){
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize object: " + clazz + " - Exception: " + e.getMessage());
        }
    }

    public String toJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not deserializer object: " + object + " to String. Exception: " + e.getMessage());
        }
    }
}