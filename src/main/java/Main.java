import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            int PORT = 8989;
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    try (Socket socket = serverSocket.accept();//подключение
                         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        System.out.println("Соединение с портом " + PORT);
                        out.println("Введи слова для поиска, например 'бизнес'");
                        String word = in.readLine();
                        List<PageEntry> searchResult = engine.search(word);
                        out.println(listToJson(searchResult));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String listToJson(List<PageEntry> list) {
        Type listType = new TypeToken<List<PageEntry>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }
}