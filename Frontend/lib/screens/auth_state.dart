class AuthState {
  static String? token;

  static bool get isLoggedIn => token != null;

  static void logout() {
    token = null;
  }
}
