package tests;

import logics.InMemoryTaskManager;
import logics.server.HTTPTaskManager;
import logics.server.KVServer;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;

public class HTTPTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    KVServer kvServer;

    @Override
    void setTaskManager() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = new HTTPTaskManager("http://localhost:8078");
    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
    }
}