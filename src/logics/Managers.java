package logics;

import logics.server.HTTPTaskManager;

import java.io.IOException;

public class Managers {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileManager(String path) {
        return new FileBackedTasksManager(path);
    }

    public static HTTPTaskManager getDefaultHTTPTaskManager(){
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        return httpTaskManager;
    }
}