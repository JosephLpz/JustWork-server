package JustWork.server.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonResponse {

    private static final String ERROR_NODO = " se produjo un error al construir los nodos ex:";

    private JsonResponse() {
    }

    public static ObjectNode unsuccess(String text) {
        return new Builder()
                .success(false)
                .message(text)
                .build();
    }

    public static ObjectNode unsuccess(String text, Object obj) {
        final var headLog = "[unsuccess(String text, Object obj)] ";
        ObjectNode nodes = null;
        try {
            nodes = new Builder()
                    .success(false)
                    .message(text)
                    .data(JsonMapper.toJson(obj))
                    .build();
        } catch (RuntimeExceptionHandler e) {
            log.error(headLog + ERROR_NODO + e.getMessage());
        }

        return nodes;
    }

    public static ObjectNode success(String text) {
        return new Builder()
                .success(true)
                .message(text)
                .build();
    }

    public static ObjectNode success(String text, JsonNode data) {
        return new Builder()
                .success(true)
                .message(text)
                .data(data)
                .build();
    }

    public static ObjectNode success(String text, Object obj) {
        final var headLog = "[success(String text, Object obj)] ";
        ObjectNode nodes = null;
        try {
            nodes = new Builder()
                    .success(true)
                    .message(text)
                    .data(JsonMapper.toJson(obj))
                    .build();
        } catch (RuntimeExceptionHandler e) {
            log.error(headLog + ERROR_NODO + e.getMessage());
        }
        return nodes;
    }

    public static ObjectNode success(String text, Object obj, Class<?> view) {
        final var headLog = "[success(String text, Object obj, Class<?> view)] ";
        ObjectNode nodes = null;
        try {
            nodes = new Builder()
                    .success(true)
                    .message(text)
                    .data(JsonMapper.toJson(obj, view))
                    .build();
        } catch (RuntimeExceptionHandler e) {
            log.error(headLog + ERROR_NODO + e.getMessage());
        }
        return nodes;
    }

    public static class Builder {
        private static final String MESSAGE_TAG = "msg";
        private static final String SUCCESS_TAG = "success";
        private static final String DATA_TAG = "data";

        private Boolean success;
        private String message;
        private JsonNode data;
        private ObjectNode response;

        public Builder() {
            this.success = null;
            this.message = null;
            this.data = null;
            this.response = JsonMapper.newObject();
        }

        public Builder success(Boolean success) {
            this.success = success;
            this.response.put(SUCCESS_TAG, success);
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            this.response.put(MESSAGE_TAG, message);
            return this;
        }

        public Builder custom(String name, String value) {
            this.response.put(name, value);
            return this;
        }

        public Builder data(JsonNode data) {
            this.data = data;
            this.response.set(DATA_TAG, data);
            return this;
        }

        public ObjectNode build() {
            if (this.success == null)
                this.response.put(SUCCESS_TAG, true);

            if (this.message == null)
                this.response.put(MESSAGE_TAG, "Sin mensaje");

            if (this.data == null)
                this.response.putNull(DATA_TAG);

            if (log.isDebugEnabled()) {
                if (response == null) {
                    log.debug("JsonResponse response:" + null);
                } else {
                    log.debug("JsonResponse response:" + response.toString());
                }
            }
            return response;
        }
    }
}
