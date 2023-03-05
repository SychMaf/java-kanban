package tests;

import data.*;
import logics.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract void setTaskManager();

    @BeforeEach
    void setUp() {
        setTaskManager();
    }

    @Test
    public void testAddTask() {
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        assertEquals(taskManager.getIdTask(1), testTask, "done");
    }

    @Test
    public void testAddSubtask() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        assertEquals(taskManager.getIdSubTask(2), subtask, "done");
    }

    @Test
    public void testAddEpic() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        assertEquals(taskManager.getIdEpic(1), testEpic, "done");
    }

    @Test
    public void testGetTasks() {
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        assertEquals(taskManager.getTasks().get(1), testTask, "Done");
    }

    @Test
    public void testGetSabTask() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        assertEquals(taskManager.getSabTasks().get(2), subtask, "Done");
    }

    @Test
    public void testGetEpics() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        assertEquals(taskManager.getEpics().get(1), testEpic, "Done");
    }

    @Test
    public void testClearTask() {
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        taskManager.clearTask();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void testClearSabTask() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        taskManager.clearSubTask();
        assertTrue(taskManager.getSabTasks().isEmpty());
    }

    @Test
    public void testClearEpic() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        taskManager.clearEpic();
        assertTrue(taskManager.getEpics().isEmpty() && taskManager.getSabTasks().isEmpty());
    }

    @Test
    public void testRemoveIdTask() {
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        taskManager.removeIdTask(1);
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void testRemoveIdEpic() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        taskManager.removeIdEpic(1);
        assertTrue(taskManager.getEpics().isEmpty() && taskManager.getSabTasks().isEmpty());
    }

    @Test
    public void testRemoveIdSubTask() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        taskManager.removeIdSubTask(2);
        assertTrue(!taskManager.getEpics().isEmpty() && taskManager.getSabTasks().isEmpty());
    }

    @Test
    public void testGetSubtaskListFromEpic() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        assertEquals(taskManager.getSubtaskListFromEpic(1).get(0), subtask);
    }

    @Test
    public void testUpdateTask() {
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        Task testTaskUpdate = new Task("Test task update", "test description update", Status.NEW);
        taskManager.updateTask(testTaskUpdate, 1);
        assertEquals(taskManager.getIdTask(1), testTaskUpdate);
    }

    @Test
    public void testUpdateSubTask() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        Subtask testSubTaskUpdate = new Subtask("Test subtask update", "test sub description update", 1, Status.NEW);
        taskManager.updateSubtask(testSubTaskUpdate, 2);
        assertEquals(taskManager.getIdSubTask(2), testSubTaskUpdate);
    }

    @Test
    public void testUpdateEpic() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        Epic updateEpic = new Epic("Test epic update", "test description epic update", Status.NEW);
        taskManager.updateEpic(updateEpic, 1);
        assertEquals(taskManager.getIdSubTask(2), subtask);
        assertEquals(taskManager.getEpics().get(1).toString(), updateEpic.toString());
    }

    @Test
    public void testGetPrioritizedTasks() {
        Task testTask1 = new Task("Test task1", "description test1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(60));
        Epic testEpic = new Epic("Test epic", "description epic", Status.NEW);
        Subtask testSubtask = new Subtask("Test subtask", "description subtask", 3, Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(15));
        Task testTask2 = new Task("Test task2", "description test2", Status.NEW);
        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        taskManager.createEpic(testEpic);
        taskManager.createSubtask(testSubtask);
        assertEquals(taskManager.getPrioritizedTasks().toArray()[0], testSubtask);
    }

    @Test
    public void testGetStandardHistory() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 1, Status.NEW);
        taskManager.createSubtask(subtask);
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        taskManager.getIdTask(3);
        taskManager.getIdSubTask(2);
        taskManager.getIdEpic(1);
        assertEquals(taskManager.getHistory().get(1), subtask);
    }

    @Test
    public void testGetEmptyHistory() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void testGetDoubleHistory() {
        Task testTask = new Task("Test task", "test description", Status.NEW);
        taskManager.createTask(testTask);
        taskManager.getIdTask(1);
        taskManager.getIdTask(1);
        taskManager.getIdTask(1);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void testGetRemoveSmfFromHistory() {
        Epic testEpic = new Epic("Test epic", "test description epic", Status.NEW);
        taskManager.createEpic(testEpic);
        Task testTask1 = new Task("Test task1", "test description1", Status.NEW);
        Subtask subtask1 = new Subtask("Test subtask1", "test sub description1", 1, Status.NEW);
        Subtask subtask2 = new Subtask("Test subtask2", "test sub description2", 1, Status.NEW);
        Task testTask2 = new Task("Test task2", "test description2", Status.NEW);
        Task testTask3 = new Task("Test task3", "test description3", Status.NEW);
        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        taskManager.createTask(testTask3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.getIdTask(2);
        taskManager.getIdSubTask(5);
        taskManager.getIdTask(3);
        taskManager.getIdTask(4);
        taskManager.getIdSubTask(6);
        taskManager.removeIdTask(2);
        taskManager.removeIdTask(3);
        taskManager.removeIdSubTask(6);
        assertEquals(taskManager.getHistory().get(0), subtask1);
        assertEquals(taskManager.getHistory().get(1), testTask3);
    }

    @Test
    public void testTimeOverlayWhenCreateTask() {
        Task testTask = new Task("test task", "description task", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(60)); // id 1
        Epic testEpic = new Epic("test epic", "description epic", Status.NEW); // id 2
        Subtask testSubtask = new Subtask("test subtask", "description", 2, Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(15));
        taskManager.createTask(testTask);
        taskManager.createEpic(testEpic);
        taskManager.createSubtask(testSubtask);
        Task testTaskOverlay = new Task("test taskOverlay", "description taskOverlay", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 30), Duration.ofMinutes(60));
        taskManager.createTask(testTaskOverlay);
        assertEquals(taskManager.getIdTask(1), testTask);
        taskManager.removeIdTask(1);
        taskManager.createTask(testTaskOverlay);
        assertEquals(taskManager.getIdTask(4), testTaskOverlay);
    }
}