package com.management.employee.system.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DynamoDbUtils {

    private DynamoDbUtils() {
    }

    public static Map<String, Object> convertFromMapAttributeValue(Map<String, AttributeValue> map) {
        Map<String, Object> mapObjects = new HashMap<>();
        map.forEach((key, value) -> mapObjects.put(key, convertFromAttributeValue(value)));
        return mapObjects;
    }

    public static Object convertFromAttributeValue(AttributeValue value) {
        if (value.s() != null) {
            return value.s();
        } else if (value.bool() != null) {
            return value.bool();
        } else if (value.n() != null) {
            return Integer.valueOf(value.n());
        } else if (value.b() != null) {
            return value.b().asByteArray();

        } else if (value.hasL()) {
            return value.l().stream().map(DynamoDbUtils::convertFromAttributeValue).collect(Collectors.toList());
        } else if (value.hasM()) {
            return convertFromMapAttributeValue(value.m());
        } else if (value.hasBs()) {
            return value.bs().stream().map(BytesWrapper::asByteArray).collect(Collectors.toList());
        } else if (value.hasNs()) {
            return value.ns().stream().map(BigDecimal::new).collect(Collectors.toList());
        } else if (value.hasSs()) {
            return value.ss();
        } else if (value.nul() != null) {
            return null;
        }
        throw new IllegalArgumentException("Valor do tipo " + value.getClass() + " não suportado.");
    }

    public static AttributeValue convertToAttributeValue(JsonNode jsonNode) throws IOException {
        return convertToAttributeValue(jsonNode, new ArrayList<>());
    }

    private static AttributeValue convertToAttributeValue(JsonNode value, List<Object> visited) throws IOException {
        //Verifica se o map já foi visitado. Foi utilizado o comparador '==' porque as coleções são consideradas iquais
        //se todos os elementos forem iguais(equals) ou se ambos forem vazios, o que pode causar problemas na verificação.
        if (visited.stream().anyMatch(o -> o == value)) {
            throw new IllegalArgumentException("Não é suportado ciclos.");
        }
        if (value.getNodeType() == JsonNodeType.OBJECT || value.getNodeType() == JsonNodeType.POJO) {
            Iterator<Map.Entry<String, JsonNode>> iterator = value.fields();
            var mapAttributeValue = new HashMap<String, AttributeValue>();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> childField = iterator.next();
                mapAttributeValue.put(childField.getKey(), convertToAttributeValue(childField.getValue(), visited));
            }
            return AttributeValue.builder().m(mapAttributeValue).build();
        } else if (value.getNodeType() == JsonNodeType.ARRAY) {
            Iterator<JsonNode> iterator = value.iterator();
            List<AttributeValue> attributeValues = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonNode childNode = iterator.next();
                attributeValues.add(convertToAttributeValue(childNode, visited));
            }
            return AttributeValue.builder().l(attributeValues).build();
        } else if (value.getNodeType() == JsonNodeType.STRING) {
            if (StringUtils.isEmpty(value.textValue())) {
                return AttributeValue.builder().s(" ").build();
            }
            return AttributeValue.builder().s(value.textValue()).build();
        } else if (value.getNodeType() == JsonNodeType.NUMBER) {
            return AttributeValue.builder().n(value.numberValue().toString()).build();
        } else if (value.getNodeType() == JsonNodeType.BOOLEAN) {
            return AttributeValue.builder().bool(value.booleanValue()).build();
        } else if (value.getNodeType() == JsonNodeType.NULL) {
            return AttributeValue.builder().nul(true).build();
        } else if (value.getNodeType() == JsonNodeType.BINARY) {
            return AttributeValue.builder().b(SdkBytes.fromByteArray(value.binaryValue())).build();
        } else {
            throw new IllegalArgumentException("Valor " + value.getClass() + " não suportado.");
        }
    }
}
