package JustWork.server.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.InputStream;

public class JsonMapper {
    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static ObjectMapper objectMapper = null;

    private JsonMapper() {
    }

    private static ObjectMapper mapper() {
        return objectMapper == null ? defaultObjectMapper : objectMapper;
    }

    public static JsonNode toJson(Object var0) throws RuntimeExceptionHandler {
        try {
            return mapper().valueToTree(var0);
        } catch (Exception var2) {
            throw new RuntimeExceptionHandler(var2);
        }
    }

    public static JsonNode toJson(Object obj, Class<?> view) throws RuntimeExceptionHandler {
        var objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        var objectWriter = objectMapper.writerWithView(view);

        try {
            return parse(objectWriter.writeValueAsString(obj));
        } catch (JsonProcessingException | RuntimeExceptionHandler e) {
            throw new RuntimeExceptionHandler(e);
        }
    }

    public static <A> A fromJson(JsonNode var0, Class<A> var1) throws RuntimeExceptionHandler {
        try {
            return mapper().treeToValue(var0, var1);
        } catch (Exception var3) {
            throw new RuntimeExceptionHandler(var3);
        }
    }

    public static ObjectNode newObject() {
        return mapper().createObjectNode();
    }

    public static String stringify(JsonNode var0) {
        return var0.toString();
    }

    public static JsonNode parse(String var0) throws RuntimeExceptionHandler {
        try {
            return mapper().readValue(var0, JsonNode.class);
        } catch (Exception var2) {
            throw new RuntimeExceptionHandler(var2);
        }
    }

    public static JsonNode parse(InputStream var0) throws RuntimeExceptionHandler {
        try {
            return mapper().readValue(var0, JsonNode.class);
        } catch (Exception var2) {
            throw new RuntimeExceptionHandler(var2);
        }
    }

    public static void setObjectMapper(ObjectMapper var0) {
        objectMapper = var0;
    }
}
