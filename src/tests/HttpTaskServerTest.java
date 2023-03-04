package tests;

import com.google.gson.*;
import data.*;
import logics.LocalDateAdapter;
import logics.Managers;
import logics.server.HTTPTaskManager;
import logics.server.HttpTaskServer;
import logics.server.KVServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    HTTPTaskManager loadedTaskManager;
    HttpClient httpClient = HttpClient.newHttpClient();
    KVServer kvServer;
    String path = "http://localhost:8080";
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter().nullSafe()).
            create();

    public HttpTaskServerTest() {
        loadedTaskManager = Managers.getDefaultHTTPTaskManager();
    }

    @BeforeEach
    void starServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    @AfterEach
    void stopStart() {
        httpTaskServer.stopServer();
        kvServer.stop();
    }

    @Test
    void getTasksIdTest() {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task?id=1"))
                .GET()
                .build();
        Task task = new Task("test task1", "description test task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(60));
        task.setId(1);
        String req = gson.toJson(task);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();
        try {
            HttpResponse<String> responsePost = httpClient.send(requestPost, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            Task getTask = gson.fromJson(responseGet.body(), Task.class);
            assertEquals(task.toString(), getTask.toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }

    @Test
    void getEpicIdTest() {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic?id=1"))
                .GET()
                .build();
        Epic testEpic = new Epic("test epic", "description epic", Status.NEW);
        testEpic.setId(1);
        String req = gson.toJson(testEpic);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();
        try {
            HttpResponse<String> responsePost = httpClient.send(requestPost, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            Epic getEpic = gson.fromJson(responseGet.body(), Epic.class);
            assertEquals(testEpic.toString(), getEpic.toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }

    @Test
    void getSubtaskIdTest() {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask?id=2"))
                .GET()
                .build();
        Epic testEpic = new Epic("test epic", "description epic", Status.NEW);
        testEpic.setId(1);
        Subtask testSubtask = new Subtask("test subtask", "description", 1, Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(15));
        testSubtask.setId(2);
        String reqEpic = gson.toJson(testEpic);
        HttpRequest requestPostEpic = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(reqEpic))
                .build();
        String reqSub = gson.toJson(testSubtask);
        HttpRequest requestPostSub = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(reqSub))
                .build();
        try {
            HttpResponse<String> responsePostEpic = httpClient.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePostEpic.statusCode());
            HttpResponse<String> responsePostSub = httpClient.send(requestPostSub, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePostSub.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            Subtask getSub = gson.fromJson(responseGet.body(), Subtask.class);
            assertEquals(testSubtask.toString(), getSub.toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }


    @Test
    void deleteTasksTest() {
        HttpRequest requestDeleteId = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task?id=1"))
                .DELETE()
                .build();
        HttpRequest requestDeleteAll = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .DELETE()
                .build();
        Task task = new Task("test task1", "description test task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(60));
        task.setId(1);
        Task testTask2 = new Task("Test task2", "test description2", Status.NEW);
        testTask2.setId(2);
        String req1 = gson.toJson(task);
        HttpRequest requestPost1 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(req1))
                .build();
        String req2 = gson.toJson(testTask2);
        HttpRequest requestPost2 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(req2))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .GET()
                .build();
        try {
            HttpResponse<String> responsePost1 = httpClient.send(requestPost1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost1.statusCode());
            HttpResponse<String> responsePost2 = httpClient.send(requestPost2, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost2.statusCode());
            HttpResponse<String> responseDeleteId = httpClient.send(requestDeleteId, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteId.statusCode());
            HttpResponse<String> responseDeleteAll = httpClient.send(requestDeleteAll, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAll.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAll.statusCode());
            assertEquals("{}", responseGet.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }

    @Test
    void deleteEpicTest() {
        HttpRequest requestDeleteId = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic?id=1"))
                .DELETE()
                .build();
        HttpRequest requestDeleteAll = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .DELETE()
                .build();
        Epic testEpic1 = new Epic("Test epic1", "test description epic1", Status.NEW);
        testEpic1.setId(1);
        Epic testEpic2 = new Epic("Test epic1", "test description epic1", Status.NEW);
        testEpic2.setId(2);
        String req1 = gson.toJson(testEpic1);
        HttpRequest requestPost1 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(req1))
                .build();
        String req2 = gson.toJson(testEpic1);
        HttpRequest requestPost2 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(req2))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .GET()
                .build();
        try {
            HttpResponse<String> responsePost1 = httpClient.send(requestPost1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost1.statusCode());
            HttpResponse<String> responsePost2 = httpClient.send(requestPost2, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost2.statusCode());
            HttpResponse<String> responseDeleteId = httpClient.send(requestDeleteId, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteId.statusCode());
            HttpResponse<String> responseDeleteAll = httpClient.send(requestDeleteAll, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAll.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            assertEquals("{}", responseGet.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }

    @Test
    void deleteSubTest() {
        HttpRequest requestDeleteId = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask?id=2"))
                .DELETE()
                .build();
        HttpRequest requestDeleteAll = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask"))
                .DELETE()
                .build();
        Epic testEpic1 = new Epic("Test epic1", "test description epic1", Status.NEW);
        testEpic1.setId(1);
        Subtask testSubtask1 = new Subtask("test subtask1", "description1", 1, Status.NEW,
                LocalDateTime.of(2000, 1, 2, 1, 0), Duration.ofMinutes(15));
        testSubtask1.setId(2);
        Subtask testSubtask2 = new Subtask("test subtask2", "description2", 1, Status.NEW,
                LocalDateTime.of(2000, 1, 3, 1, 0), Duration.ofMinutes(15));
        testSubtask2.setId(3);
        String req1 = gson.toJson(testEpic1);
        HttpRequest requestPost1 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(req1))
                .build();
        String req2 = gson.toJson(testSubtask1);
        HttpRequest requestPost2 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(req2))
                .build();
        String req3 = gson.toJson(testSubtask2);
        HttpRequest requestPost3 = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(req3))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask"))
                .GET()
                .build();
        try {
            HttpResponse<String> responsePost1 = httpClient.send(requestPost1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost1.statusCode());
            HttpResponse<String> responsePost2 = httpClient.send(requestPost2, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost2.statusCode());
            HttpResponse<String> responsePost3 = httpClient.send(requestPost3, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost3.statusCode());
            HttpResponse<String> responseDeleteId = httpClient.send(requestDeleteId, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteId.statusCode());
            HttpResponse<String> responseDeleteAll = httpClient.send(requestDeleteAll, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAll.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            assertEquals("{}", responseGet.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("test task1", "description test task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(30));
        task.setId(1);
        String req = gson.toJson(task);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();
        Task updateTask = new Task("test update", "description test update", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(30));
        updateTask.setId(1);
        String reqUpdate = gson.toJson(updateTask);
        HttpRequest requestPostUpdate = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task?id=1"))
                .POST(HttpRequest.BodyPublishers.ofString(reqUpdate))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task?id=1"))
                .GET()
                .build();
        try {
            HttpResponse<String> responsePost1 = httpClient.send(requestPost, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost1.statusCode());
            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            Task getTask = gson.fromJson(responseGet.body(), Task.class);
            assertEquals(task.toString(), getTask.toString());
            HttpResponse<String> responsePost2 = httpClient.send(requestPostUpdate, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePost2.statusCode());
            HttpResponse<String> responseGet2 = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet2.statusCode());
            Task getTaskUpdate = gson.fromJson(responseGet2.body(), Task.class);
            assertEquals(updateTask.toString(), getTaskUpdate.toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }

    @Test
    void testTryLoadInfo() {
        Task task = new Task("test task1", "description test task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(30));
        task.setId(1);
        String reqTask = gson.toJson(task);
        HttpRequest requestPostTask = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(reqTask))
                .build();
        Epic testEpic = new Epic("test epic", "description epic", Status.NEW);
        String reqEpic = gson.toJson(testEpic);
        HttpRequest requestPostEpic = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(reqEpic))
                .build();
        Subtask testSubtask = new Subtask("test subtask", "description", 2, Status.NEW,
                LocalDateTime.of(2000, 1, 1, 3, 0), Duration.ofMinutes(15));
        String reqSub = gson.toJson(testSubtask);
        HttpRequest requestPostSub = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(reqSub))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(path + "/tasks/task?id=1"))
                .GET()
                .build();
        try {
            HttpResponse<String> responsePostTask = httpClient.send(requestPostTask, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePostTask.statusCode());
            HttpResponse<String> responsePostEpic = httpClient.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePostEpic.statusCode());
            HttpResponse<String> responsePostSub = httpClient.send(requestPostSub, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responsePostSub.statusCode());
            httpTaskServer.stopServer();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.startServer();
            httpTaskServer.loadData();

            HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGet.statusCode());
            Task getTask = gson.fromJson(responseGet.body(), Task.class);
            assertEquals(task.toString(), getTask.toString());

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на тесте");
        }
    }
}