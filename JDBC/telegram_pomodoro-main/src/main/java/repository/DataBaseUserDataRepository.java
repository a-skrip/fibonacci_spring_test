package repository;

import config.Config;

import java.sql.*;
import java.time.LocalDateTime;

public class DataBaseUserDataRepository implements UserDataRepository {
    private final Config config;

    public DataBaseUserDataRepository(Config config) {
        this.config = config;
    }

    @Override
    public void recordSession(long chatId, String sessionType, int durationMinutes, LocalDateTime startAt) {
        String query = """
                INSERT INTO user_sessions (chat_id, type, duration, start_at, completed)
                values (?, ?, ?, ?, ?);
                """;

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
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

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(stopAt));
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, sessionType);

            int updateRows = preparedStatement.executeUpdate();

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
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(stopAt));
            preparedStatement.setLong(2, chatId);

            int updateRows = preparedStatement.executeUpdate();
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
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, chatId);

            ResultSet resultSet = preparedStatement.executeQuery();
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
            System.err.println("Ошибка выполнения запроса" + e.getMessage());
        }
        return stringBuilder.toString();
    }

    @Override
    public String getAchievements(long chatId) {
        String first = checkCompletedFirstWorkCycles(chatId);
        String ten = checkAndSetCompletedTenWorkCycles(chatId);
        String night = checkCompletedNightWorkCycles(chatId);
        return "";
    }


    @Override
    public byte[] exportStatistics(long chatId) {
        return new byte[0];
    }

    public String checkCompletedFirstWorkCycles(long chaiId) {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("");
        String query = """
                SELECT start_at
                 FROM user_sessions
                 WHERE chat_id = ? AND completed = true
                 ORDER BY start_at
                 LIMIT 1;
                """;
        try (PreparedStatement prepareStatement = getConnection().prepareStatement(query)) {
            prepareStatement.setLong(1, chaiId);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                Timestamp start_at = resultSet.getTimestamp("start_at");
                stringBuilder.append(start_at);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса" + e.getMessage());
        }
        return stringBuilder.toString();
    }

    public String checkCompletedNightWorkCycles(long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        String query = """
                SELECT start_at
                FROM user_sessions
                WHERE chat_id = ?
                  AND completed = true
                  AND (SELECT COUNT(*)
                       FROM user_sessions
                       WHERE chat_id = ? AND completed = true) >= 10
                ORDER BY start_at DESC
                LIMIT 1;
                """;
        try (PreparedStatement prepareStatement = getConnection().prepareStatement(query)) {
            prepareStatement.setLong(1, chatId);
            prepareStatement.setLong(2, chatId);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                Timestamp start_at = resultSet.getTimestamp("start_at");
                stringBuilder.append(start_at);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса" + e.getMessage());
        }
        return stringBuilder.toString();
    }

    public String checkAndSetCompletedTenWorkCycles(long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        String query = """
                SELECT start_at
                        FROM user_sessions
                        WHERE chat_id = ?
                        AND completed  = true
                        and start_at::time BETWEEN '00:00:00' AND '06:00:00'
                        order by start_at
                        LIMIT 1;
                """;
        try (PreparedStatement prepareStatement = getConnection().prepareStatement(query)) {
            prepareStatement.setLong(1, chatId);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                Timestamp start_at = resultSet.getTimestamp("start_at");
                stringBuilder.append(start_at);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса" + e.getMessage());
        }
        return stringBuilder.toString();
    }

    private Connection getConnection() throws SQLException {
        String dbUrl = this.config.dbUrl();
        String dbUser = this.config.dbUser();
        String dbPassword = this.config.dbPassword();
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
