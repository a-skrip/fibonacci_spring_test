package config;

public record Config(String botApiToken, int workDuration, int restDuration, String dbUrl, String dbUser, String dbPassword) {
    public Config {
        if (botApiToken == null || botApiToken.isEmpty()) {
            throw new RuntimeException("TOKEN не задан!");
        }
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new RuntimeException("URL не задан!");
        }
        if (dbUser == null || dbUser.isEmpty()) {
            throw new RuntimeException("LOGIN не задан!");
        }
        if (dbPassword == null || dbPassword.isEmpty()) {
            throw new RuntimeException("PASSWORD не задан!");
        }
    }
}
