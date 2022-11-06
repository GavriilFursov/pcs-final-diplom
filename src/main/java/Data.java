import com.google.gson.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Data {
    public static void main(String[] args) {
        String HOST = "localhost";
        int PORT = 8989;
        Scanner scanner = new Scanner(System.in);
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String question = in.readLine();
            System.out.println(question);
            String answer = scanner.nextLine();
            out.println(answer);
            String jsonFromServer = in.readLine();
            System.out.println(getPrettyJson(jsonFromServer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPrettyJson(String jsonFrom) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();//печать
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonFrom);
        return gson.toJson(jsonElement);
    }
}
