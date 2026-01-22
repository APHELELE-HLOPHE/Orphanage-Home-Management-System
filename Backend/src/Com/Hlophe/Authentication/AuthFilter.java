package Com.Hlophe.Authentication;

import java.util.Map;

public class AuthFilter {

    public static boolean isAuthorized(Map<String, String> headers) {

        if (!headers.containsKey("Authorization")) {
            return false;
        }

        String authHeader = headers.get("Authorization");

        if (!authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        return TokenManager.isValid(token);
    }
}
