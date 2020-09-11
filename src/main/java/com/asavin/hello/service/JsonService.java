package com.asavin.hello.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonService {
    @Autowired
    ObjectMapper objectMapper;
    public <T>T fromJson(JsonNode sourceJson, Class<T> parseClass) throws Throwable {


        return objectMapper.readValue(objectMapper.writeValueAsString(sourceJson),parseClass); }

    public   JsonNode toJson(Object sourceObject, Class... views) throws Exception {
        ObjectWriter writer = objectMapper.writer();

        for(int i = 0;i<views.length;i++)
            writer = writer.withView(views[i]);

        return objectMapper.readTree(writer.writeValueAsString(sourceObject));
    }
}

