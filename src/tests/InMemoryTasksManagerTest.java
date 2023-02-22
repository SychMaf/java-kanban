package tests;

import logics.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}