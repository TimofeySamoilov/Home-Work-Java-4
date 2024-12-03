import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // я взял users и posts
        String usersUrl = "https://fake-json-api.mock.beeceptor.com/users";
        String postsUrl = "https://dummy-json.mock.beeceptor.com/posts";

        // Получение и вывод информации о users
        System.out.println("Fetching users...");
        fetchAndPrintUsers(usersUrl);

        System.out.println();

        // Получение и вывод информации о posts
        System.out.println("Fetching posts...");
        fetchAndPrintPosts(postsUrl);
    }

    private static void fetchAndPrintUsers(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // используем метод GET
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            // Считываем все данные из буффера
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // передача в функцию парсинга
            parseAndPrint(response);

        } catch (Exception e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }

    private static void fetchAndPrintPosts(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // передача в функцию парсинга
            parseAndPrint(response);

        } catch (Exception e) {
            System.out.println("Error fetching posts: " + e.getMessage());
        }
    }

    private static void parseAndPrint(StringBuilder str) {
        // мой парсинг основан на методе split() по пробелам
        // поэтому сначала нужно заменить все пробелы на '_'
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '"') {
                int j = i + 1;
                while (str.charAt(j) != '"') {
                    if (str.charAt(j) == ' ') {
                        str.setCharAt(j, '_');
                    }
                    ++j;
                }
                i = j;
            }
        }
        // после делаем нарезку
        String[] parts = str.toString().split(" ");
        ArrayList<String> ans = new ArrayList<>();
        // теперь убираем лишние символы и в конце меняем '_' обратно на пробел
        for (int i = 0; i < parts.length; ++i) {
            String value = parts[i].replace("[", "").replace("\"", "")
                    .replace(",", "").replace("{", "").replace("}", "")
                    .replace("]", "")
                    // и наконец можно заменить "_" на " "
                    .replace("_", " ");
            if (!value.isEmpty()) {
                ans.add(value);
            }
        }
        // теперь выводим по парам
        for (int i = 0; i < ans.size(); ++i) {
            if (i % 2 == 0) {
                if ((i > 1 && ans.get(i).equals("id:") && !ans.get(i-2).equals("userId:")) || ans.get(i).equals("userId:")) {
                    System.out.println();
                }
                System.out.print(ans.get(i) + " ");
            }
            else {
                System.out.println(ans.get(i));
            }
        }
    }
}
