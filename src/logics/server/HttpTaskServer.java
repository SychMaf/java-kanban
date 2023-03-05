package logics.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import data.*;
import logics.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter().nullSafe())
            .create();
    public HttpServer httpServer;
    private HTTPTaskManager httpManager;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void startServer() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpManager = Managers.getDefaultHTTPTaskManager();
    }

    public void stopServer() {
        httpServer.stop(1);
        System.out.println("сервер остановлен");
    }

    public void loadData() {
        httpManager.loadTasks();
    }

    public class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String request = httpExchange.getRequestURI().getPath();
            String requestUri = httpExchange.getRequestURI().toString();
            String response = "";
            int responseCode = 404;
            switch (method) {
                case "GET": {
                    System.out.println("вызван GET");
                    if (request.endsWith("/tasks")) {
                        response = gson.toJson(httpManager.getPrioritizedTasks());
                        responseCode = 200;
                    } else if (query == null && request.endsWith("/tasks/task")) {
                        response = gson.toJson(httpManager.getTasks());
                        responseCode = 200;
                    } else if (query == null && request.endsWith("/tasks/epic")) {
                        response = gson.toJson(httpManager.getEpics());
                        responseCode = 200;
                    } else if (query == null && request.endsWith("/tasks/subtask")) {
                        response = gson.toJson(httpManager.getSabTasks());
                        responseCode = 200;
                    } else if (request.endsWith("/tasks/history")) {
                        response = gson.toJson(httpManager.getHistory());
                        responseCode = 200;
                    } else if (requestUri.startsWith("/tasks/subtask/epic?id=")) {
                        response = gson.toJson(httpManager.getSubtaskListFromEpic(findId(query)));
                        responseCode = 200;
                    } else if (query != null && request.contains("/tasks/task")) {
                        response = gson.toJson(httpManager.getIdTask(findId(query)));
                        responseCode = 200;
                    } else if (query != null && request.contains("/tasks/subtask")) {
                        response = gson.toJson(httpManager.getIdSubTask(findId(query)));
                        responseCode = 200;
                    } else if (query != null && request.contains("/tasks/epic")) {
                        response = gson.toJson(httpManager.getIdEpic(findId(query)));
                        responseCode = 200;
                    }
                    break;
                }
                case "POST": {
                    System.out.println("вызван POST");
                    if (requestUri.startsWith("/tasks/task?id=")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(body, Task.class);
                        httpManager.updateTask(task, findId(query));
                        responseCode = 201;
                    } else if (requestUri.startsWith("/tasks/subtask?id=")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        httpManager.updateSubtask(subtask, findId(query));
                        responseCode = 201;
                    } else if (requestUri.startsWith("/tasks/epic?id=")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);
                        httpManager.updateEpic(epic, findId(query));
                        responseCode = 201;
                    } else if (request.contains("/tasks/task")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(body, Task.class);
                        httpManager.createTask(task);
                        responseCode = 201;
                    } else if (request.contains("/tasks/subtask")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        httpManager.createSubtask(subtask);
                        responseCode = 201;
                    } else if (request.contains("/tasks/epic")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);
                        httpManager.createEpic(epic);
                        responseCode = 201;
                    }
                    break;
                }
                case "DELETE": {
                    if (request.contains("/tasks/task") && query != null) {
                        httpManager.removeIdTask(findId(query));
                        response = gson.toJson("Task deleted");
                        responseCode = 200;
                    } else if (request.contains("/tasks/subtask") && query != null) {
                        httpManager.removeIdSubTask(findId(query));
                        response = gson.toJson("Subtask deleted");
                        responseCode = 200;
                    } else if (request.contains("/tasks/epic") && query != null) {
                        httpManager.removeIdEpic(findId(query));
                        response = gson.toJson("Epic deleted");
                        responseCode = 200;
                    } else if (request.contains("/tasks/task")) {
                        httpManager.clearTask();
                        response = gson.toJson("All task deleted");
                        responseCode = 200;
                    } else if (request.contains("/tasks/subtask")) {
                        httpManager.clearSubTask();
                        response = gson.toJson("All subtask deleted");
                        responseCode = 200;
                    } else if (request.contains("/tasks/epic")) {
                        httpManager.clearEpic();
                        response = gson.toJson("All epic deleted");
                        responseCode = 200;
                    }
                }
            }
            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static int findId(String query) {
        String[] arr = query.split("=");
        return Integer.parseInt(arr[1]);
    }
}