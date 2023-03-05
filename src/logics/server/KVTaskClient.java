package logics.server;

import logics.exception.KVTaskClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    protected URI url;
    protected String token;
    private HttpClient httpClient = HttpClient.newHttpClient();

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
                return token = response.body();
            } else {
                System.out.println("Токен не получен");
            }
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка при регистрации");
        }
        System.out.println("Не верный токен");
        return null;
    }

    public void put(String key, String json) {
        if (token == null) {
            System.out.println("Токен не назначен, на put");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + token))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse.BodyHandler<String> body = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, body);
            if (response.statusCode() != 200) {
                System.out.println("Ошибка запроса на put, статус не 200");
            }
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка при вложении");
        }
    }

    public String load(String key) {
        if (token == null) {
            System.out.println("Токен не назначен, на load");
            return null;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + token))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse.BodyHandler<String> body = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, body);
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("Ошибка запроса на load, статус не 200");
            }
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка при загрузке");
        }
        System.out.println("неверный запрос");
        return null;
    }
}