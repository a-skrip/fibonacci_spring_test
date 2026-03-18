package config;

public record Config(String botApiToken, int workDuration, int restDuration, String dbUrl, String dbUser, String dbPassword) {
    public Config {
        if (botApiToken == null || botApiToken.isEmpty()) {
            throw new RuntimeException("Токен не задан!");
        }
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new RuntimeException();
        }
        if (dbUser == null || dbUser.isEmpty()) {
            throw new RuntimeException();
        }
        if (dbPassword == null || dbPassword.isEmpty()) {
            throw new RuntimeException();
        }
    }
}
