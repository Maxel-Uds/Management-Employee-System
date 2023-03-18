package com.management.employee.system.repositories.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.lang.reflect.Type;
import java.util.Map;

public class StringToMapAttributeConverter implements AttributeConverter<Map<String, String>> {

    private final Gson gson;

    public StringToMapAttributeConverter() {
        this.gson = new Gson();
    }

    @Override
    public AttributeValue transformFrom(Map<String, String> stringStringMap) {
        String json = gson.toJson(stringStringMap);
        SdkBytes bytes = SdkBytes.fromByteArray(json.getBytes());
        return AttributeValue.builder().b(bytes).build();
    }

    @Override
    public Map<String, String> transformTo(AttributeValue attributeValue) {
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        String jsonString = attributeValue.s();
        return gson.fromJson(jsonString, type);
    }

    @Override
    public EnhancedType<Map<String, String>> type() {
        return EnhancedType.mapOf(String.class, String.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
