package com.sougata.form_service.util;

public class JsonUtil {

    private static final com.fasterxml.jackson.databind.ObjectMapper oldMapper =
            new com.fasterxml.jackson.databind.ObjectMapper();
    private static final tools.jackson.databind.ObjectMapper newMapper =
            new tools.jackson.databind.ObjectMapper();

    public static String oldJsonNodeToString(com.fasterxml.jackson.databind.JsonNode jsonNode) {
        try {
            return oldMapper.writeValueAsString(jsonNode);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return null;
        }
    }

    public static com.fasterxml.jackson.databind.JsonNode objectToOldJsonNode(Object obj) {
        return oldMapper.valueToTree(obj);
    }

    public static <T> T oldJsonNodeToObject(com.fasterxml.jackson.databind.JsonNode jsonNode, Class<T> returnType) throws com.fasterxml.jackson.core.JsonProcessingException {
        return oldMapper.treeToValue(jsonNode, returnType);
    }

    public static String getValueFromOldJsonNode(com.fasterxml.jackson.databind.JsonNode jsonNode, String fieldName) throws com.fasterxml.jackson.core.JsonProcessingException {
        return jsonNode.get(fieldName).asText();
    }

    public static String getValueFromNewJsonNode(tools.jackson.databind.JsonNode jsonNode, String fieldName) throws tools.jackson.databind.exc.JsonNodeException {
        return jsonNode.get(fieldName).asString();
    }

    public static void removeFieldRecursiveNewJsonNode(tools.jackson.databind.JsonNode node, String field) {
        if (node.isObject()) {
            var objectNode = (tools.jackson.databind.node.ObjectNode) node;
            objectNode.remove(field);

            for (tools.jackson.databind.JsonNode child : objectNode) {
                removeFieldRecursiveNewJsonNode(child, field);
            }
        } else if (node.isArray()) {
            for (tools.jackson.databind.JsonNode child : node) {
                removeFieldRecursiveNewJsonNode(child, field);
            }
        }
    }

}
