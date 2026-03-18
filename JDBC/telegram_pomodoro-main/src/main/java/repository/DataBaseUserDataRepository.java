package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DataBaseUserDataRepository implements UserDataRepository {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/pomodoro";
    private static final String DB_LOGIN = "postgres";
    private static final String DB_PASSWORD = "root";

    public DataBaseUserDataRepository() {
    }

    @Override
    public void recordSession(long chatId, String sessionType, int durationMinutes, LocalDateTime startAt) {
        String query = "INSERT INTO user_sessions (chat_id, type, duration, start_at, completed) values (?, ?, ?, ?, ?);";

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
            System.err.println("ERROR в записи SESSION: " + e.getMessage());
        }
    }

    @Override
    public void completeSession(long chatId, String sessionType, LocalDateTime stopAt) {

    }

    @Override
    public void markSessionStopped(long chatId, LocalDateTime stopAt) {

    }

    @Override
    public String getStatistics(long chatId) {
        return "";
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
