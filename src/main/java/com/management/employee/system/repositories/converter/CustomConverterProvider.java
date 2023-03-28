package com.management.employee.system.repositories.converter;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;

import java.util.HashMap;
import java.util.Map;

public class CustomConverterProvider implements AttributeConverterProvider {

    private static final Map<EnhancedType<?>, AttributeConverter<?>> CACHED = build();

    public static Map<EnhancedType<?>, AttributeConverter<?>> build() {
        var map = new HashMap<EnhancedType<?>, AttributeConverter<?>>();
        addConverter(map, new JsonNodeAttributeConverter());
        return map;
    }

    private static void addConverter(Map<EnhancedType<?>, AttributeConverter<?>> map, AttributeConverter<?> attributeConverter) {
        map.put(attributeConverter.type(), attributeConverter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> AttributeConverter<T> converterFor(EnhancedType<T> enhancedType) {
        return (AttributeConverter<T>) CACHED.get(enhancedType);
    }
}
