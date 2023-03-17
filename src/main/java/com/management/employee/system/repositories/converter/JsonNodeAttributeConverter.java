package com.management.employee.system.repositories.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.management.employee.system.util.DynamoDbUtils;
import com.management.employee.system.util.JsonUtils;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;

class JsonNodeAttributeConverter implements AttributeConverter<JsonNode> {

    static JsonNodeAttributeConverter create() {
        return new JsonNodeAttributeConverter();
    }

    @Override
    public AttributeValue transformFrom(JsonNode input) {
        try {
            return DynamoDbUtils.convertToAttributeValue(input);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Erro ao converter JsonNode[%s] em " +
                    "AttributeValue: %s.", input, e.getMessage()), e);
        }
    }

    @Override
    public JsonNode transformTo(AttributeValue input) {
        return input.m() == null ? null : JsonUtils.mapAsJsonNode(
                DynamoDbUtils.convertFromMapAttributeValue(input.m()));
    }

    @Override
    public EnhancedType<JsonNode> type() {
        return EnhancedType.of(JsonNode.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.M;
    }
}
