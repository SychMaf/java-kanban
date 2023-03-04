package logics.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    protected URI url;
    protected String Token;
    HttpClient httpClient = HttpClient.newHttpClient();

    public KVTaskClient(String path) {
        this.url = URI.create(path);
    }

    public String register() {
        URI url = URI.create(this.url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse.BodyHandler<String> body = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, body);
            if (response.statusCode() == 200) {
                return Token = response.body();
            } else System.out.println("Токен не получен");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при регистрации");
        }
        System.out.println("Не верный токен");
        return null;
    }

    public void put(String key, String json) {
        if (Token == null) {
            System.out.println("Токен не назначен, на put");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + Token))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse.BodyHandler<String> body = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, body);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при вложении");
        }
    }

    public String load(String key) {
        if (Token == null) {
            System.out.println("Токен не назначен, на load");
            return null;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + Token))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse.BodyHandler<String> body = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, body);
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при загрузке");
        }
        System.out.println("неверный запрос");
        return null;
    }
}