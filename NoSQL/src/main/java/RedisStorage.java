import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.Collection;
import java.util.Date;

import static java.lang.System.out;

public class RedisStorage {
    // Объект для работы с Redis
    private RedissonClient redisson;
    // Объект для работы с ключами
    private RKeys rKeys;
    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<String> sortedSetUsers;

    private final static String KEY = "users";

    private double getTs() {
        return new Date().getTime() / 1000;
    }

    // Пример вывода всех ключей
    public void listKeys() {
        Iterable<String> keys = rKeys.getKeys();
        for (String key : keys) {
            out.println("KEY: " + key + ", type:" + rKeys.getType(key));
        }
    }

    String addUsers() {
        for (int i = 1; i < 31; i++) {
            sortedSetUsers.add(i, "user" + i);
        }
        return "добавлено " + sortedSetUsers.size();
    }

    Collection<String> getAllUser() {
        return sortedSetUsers.readAll();
    }

    void deleteElement(int num) {
        sortedSetUsers.remove("user" + num);
    }

    Collection<String> getNameUserByRank(int rank) {
        return sortedSetUsers.valueRange(rank, rank);
    }

    double getScoreElement(String value) {
        return sortedSetUsers.getScore(value);
    }

    void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        sortedSetUsers = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    void shutdown() {
        redisson.shutdown();
    }

    void printAllUsers() {
        Collection<String> allUser = sortedSetUsers.readAll();
        allUser.forEach(System.out::println);
    }

    // Фиксирует посещение пользователем страницы
    void logPageVisit(int user_id) {
        //ZADD ONLINE_USERS
        sortedSetUsers.add(getTs(), String.valueOf(user_id));
    }


    // Удаляет
    void deleteForRank(int rank) {
        //ZREVRANGEBYSCORE ONLINE_USERS 0 <time_5_seconds_ago>
        sortedSetUsers.removeRangeByScore(rank, true, rank, true);
    }

    int calculateUsersNumber() {
        //ZCOUNT ONLINE_USERS
        return sortedSetUsers.count(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true);
    }
}

