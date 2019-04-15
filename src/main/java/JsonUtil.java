import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    public static ObjectMapper mapper = JacksonMapper.getInstance();

    public static <T> T parse(String json, Class<T> clazz) {
        T result = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                result = mapper.readValue(json, clazz);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("json转换异常" + e);
            }
        }
        return result;
    }

    public static String toJson(Object o) {
        String result;
        try {
            result = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("json转换异常" + e);
        }
        return result;
    }

    /**
     * @param json            字符串
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static <T> T parse(String json, Class<?> collectionClass, Class<?>... elementClasses) {
        T result = null;
        if (StringUtils.isNotBlank(json)) {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            try {
                result = mapper.readValue(json, javaType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static String getLastCode(String json) {
        Map<String, Map<String, String>> jsonMap = parse(json, HashMap.class);
        return jsonMap.get("last").get("code");
    }

    @SuppressWarnings("unchecked")
    public static String getLastValue(String json) {
        Map<String, Map<String, String>> jsonMap = parse(json, HashMap.class);
        return jsonMap.get("last").get("value");
    }

}   