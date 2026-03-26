import java.util.Collection;
import java.util.Random;

public class RedisTest {

    public static void main(String[] args) {

        RedisStorage redisStorage = new RedisStorage();
        redisStorage.init();

        redisStorage.listKeys();
//        Добавляем пользаков
        redisStorage.addUsers();

        Random random = new Random();

        int countCase;
        int currentCase = 1;

        while (true) {
            Collection<String> allUser = redisStorage.getAllUser();

            countCase = random.nextInt(10);

            int payedUser = random.nextInt(redisStorage.calculateUsersNumber());

            for (String user : allUser) {
                if (currentCase > 10) {
                    currentCase = 1;

                }
                if (countCase == currentCase) {
                    //достаю пользователя
                    String name = redisStorage.getNameUserByRank(payedUser).stream().findFirst()
                            .get();
                    //оплачивает
                    System.out.println("Пользователь " + name + " оплатил услугу");
                    //вывести его
                    System.out.println("— На главной странице показываем пользователя " + name);
                } else {
                    System.out.println("— На главной странице показываем пользователя " + user);
                }
                try {
                    Thread.sleep(500);
                    currentCase++;
                } catch (InterruptedException e) {
                    System.err.println("проблема в for (String user : allUser)");
                }
            }
            currentCase = 0;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("Что-то пошло не так");
            }
        }
    }
}
