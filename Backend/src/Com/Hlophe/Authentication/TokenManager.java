package Com.Hlophe.Authentication;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TokenManager {

    private static final Map<String, String> tokens = new ConcurrentHashMap<>();

    public static String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, username);
        return token;
    }

    public static boolean isValid(String token) {
        return tokens.containsKey(token);
    }

    public static String getUser(String token) {
        return tokens.get(token);
    }
}
