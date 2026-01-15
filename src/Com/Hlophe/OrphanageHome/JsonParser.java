package Com.Hlophe.OrphanageHome;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    
    public JsonObject parseObject(String json) {
        JsonObject jsonObject = new JsonObject();
        json = json.trim();
        
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();
            
            String[] pairs = smartSplit(json);
            
            for (String pair : pairs) {
                if (!pair.trim().isEmpty()) {
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = unquote(keyValue[0].trim());
                        String value = keyValue[1].trim();
                        Object parsedValue = parseValue(value);
                        jsonObject.put(key, parsedValue);
                    }
                }
            }
        }
        
        return jsonObject;
    }
    
    private String[] smartSplit(String json) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceCount = 0;
        int bracketCount = 0;
        boolean inQuotes = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i-1) != '\\')) {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                if (c == '[') bracketCount++;
                if (c == ']') bracketCount--;
            }
            
            if (c == ',' && !inQuotes && braceCount == 0 && bracketCount == 0) {
                parts.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            parts.add(current.toString().trim());
        }
        
        return parts.toArray(new String[0]);
    }
    
    private Object parseValue(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return unquote(value);
        } else if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.equals("null")) {
            return null;
        } else if (value.contains(".")) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return value;
            }
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return value;
            }
        }
    }
    
    private String unquote(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1, str.length() - 1);
        }
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }
    
    public static class JsonObject {
        private Map<String, Object> map = new HashMap<>();
        
        public void put(String key, Object value) {
            map.put(key, value);
        }
        
        public Object get(String key) {
            return map.get(key);
        }
        
        public String getString(String key) {
            Object value = map.get(key);
            return value instanceof String ? (String) value : null;
        }
        
        public Integer getInt(String key) {
            Object value = map.get(key);
            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof String) {
                try {
                    return Integer.parseInt((String) value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        }
    }
}