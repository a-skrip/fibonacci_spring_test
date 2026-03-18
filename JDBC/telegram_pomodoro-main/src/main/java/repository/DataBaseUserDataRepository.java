package repository;

import config.Config;

import java.sql.*;
import java.time.LocalDateTime;

public class DataBaseUserDataRepository implements UserDataRepository {
    private final String DB_URL;
    private final String DB_LOGIN;
    private final String DB_PASSWORD;

    public DataBaseUserDataRepository(Config config) {
        this.DB_URL = config.dbUrl();
        this.DB_LOGIN = config.dbUser();
        this.DB_PASSWORD = config.dbPassword();
    }

    @Override
    public void recordSession(long chatId, String sessionType, int durationMinutes, LocalDateTime startAt) {
        String query = """
                INSERT INTO user_sessions (chat_id, type, duration, start_at, completed)
                values (?, ?, ?, ?, ?);
                """;

        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, chatId);
            preparedStatement.setString(2, sessionType);
            preparedStatement.setInt(3, durationMinutes);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(startAt));
            preparedStatement.setBoolean(5, false);

            preparedStatement.execute();
        } catch (Exception e) {
            System.err.println("ERROR записи SESSION: " + e.getMessage());
        }
    }

    @Override
    public void completeSession(long chatId, String sessionType, LocalDateTime stopAt) {
        String query = """
                UPDATE user_sessions SET stop_at = ?, completed = true
                WHERE id = (
                SELECT id
                FROM user_sessions
                WHERE chat_id = ? AND type = ? AND stop_at IS NULL
                ORDER BY start_at DESC
                LIMIT 1);
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
             PreparedStatement prepareStatement = connection.prepareStatement(query)
        ) {
            prepareStatement.setTimestamp(1, Timestamp.valueOf(stopAt));
            prepareStatement.setLong(2, chatId);
            prepareStatement.setString(3, sessionType);

            int updateRows = prepareStatement.executeUpdate();

            if (updateRows > 0) {
                System.out.println("Обновление успешно");
            } else {
                System.err.println("Не найдена активная сессия " + sessionType + " для chatId= " + chatId);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка обновления поля stop_at " + e.getMessage());
        }
    }

    @Override
    public void markSessionStopped(long chatId, LocalDateTime stopAt) {
        String query = """
                UPDATE user_sessions SET stop_at = ?
                        WHERE id = (
                        SELECT id
                        FROM user_sessions
                        WHERE chat_id = ? AND stop_at IS NULL
                        ORDER BY start_at DESC
                        LIMIT 1);
                """;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
             PreparedStatement prepareStatement = connection.prepareStatement(query)
        ) {
            prepareStatement.setTimestamp(1, Timestamp.valueOf(stopAt));
            prepareStatement.setLong(2, chatId);

            int updateRows = prepareStatement.executeUpdate();
            if (updateRows > 0) {
                System.out.println("Обновление успешно");
            } else {
                System.err.println("Не найдена активная сессия для chatId = " + chatId);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка обновления поля stop_at " + e.getMessage());
        }
    }

    @Override
    public String getStatistics(long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Статистика: \n");

        String query = """
                SELECT type, sum(duration) AS sum_duration, count(*) AS total_cycles
                FROM user_sessions
                WHERE chat_id = ? AND completed = true
                GROUP BY type
                ORDER BY type DESC;
                """;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
             PreparedStatement prepareStatement = connection.prepareStatement(query)) {

            prepareStatement.setLong(1, chatId);

            ResultSet resultSet = prepareStatement.executeQuery();
            boolean hasData = false;

            while (resultSet.next()) {
                hasData = true;
                String type = resultSet.getString("type");
                int sumDuration = resultSet.getInt("sum_duration");
                int totalCycles = resultSet.getInt("total_cycles");

                if (type.equalsIgnoreCase("WORK")) {
                    stringBuilder.append("• Рабочее время: ").append(sumDuration).append(" мин").append("\n")
                            .append("• Рабочих циклов: ").append(totalCycles).append("\n");
                } else {
                    stringBuilder.append("• Отдых: ").append(sumDuration).append(" мин").append("\n")
                            .append("• Циклов отдыха: ").append(totalCycles).append("\n");
                }
            }

            if (!hasData) {
                stringBuilder.append(" ОТСУТСТВУЕТ");
            }
        } catch (Exception e) {
            System.out.println("Ошибка выполнения запроса" + e.getMessage());
        }
        return stringBuilder.toString();
    }

    @Override
    public String getAchievements(long chatId) {
        return "";
    }

    @Override
    public byte[] exportStatistics(long chatId) {
        return new byte[0];
    }
}
