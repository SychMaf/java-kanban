package tests;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import logics.FileBackedTasksManager;
import logics.exception.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    void setTaskManager() {
        taskManager = new FileBackedTasksManager("src\\tests\\testTextSaveFile\\textTest.txt");
    }

    @Test
    void testLoadFromDoesNotExistFile() {
        ManagerSaveException ex = Assertions.assertThrows(
                ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile("src\\tests\\testTextSaveFile\\textNotExistTest.txt")
        );
        assertEquals("Ошибка ввода/вывода", ex.getMessage());
    }

    @Test
    void testLoadFromEmptyFile() {
        FileBackedTasksManager manager;
        manager = FileBackedTasksManager.loadFromFile("src\\tests\\testTextSaveFile\\textEmptyTest.txt");
        assertTrue(manager.getEpics().isEmpty() && manager.getTasks().isEmpty() && manager.getSabTasks().isEmpty());
    }

    @Test
    void testReloadFromNewWorkManager() {
        FileBackedTasksManager manager = new FileBackedTasksManager
                ("src\\tests\\testTextSaveFile\\textEmptyEpicTest.txt");
        Task testTask = new Task("test task", "description test task", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(60));
        Epic testEpic = new Epic("test epic", "description test epic", Status.NEW);
        manager.createTask(testTask);
        manager.createEpic(testEpic);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile
                ("src\\tests\\testTextSaveFile\\textEmptyEpicTest.txt");
        assertEquals(0, newManager.getIdEpic(2).getSubtaskIdList().size());
    }

    @Test
    void testDefaultLoadHistoryFromFile() {
        FileBackedTasksManager manager = new FileBackedTasksManager
                ("src\\tests\\testTextSaveFile\\textDefaultLoadHistory.txt");
        Task testTask1 = new Task("test task1", "description test task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 2, 0), Duration.ofMinutes(60));
        Epic testEpic = new Epic("test epic", "description test epic", Status.NEW);
        Subtask subtask = new Subtask("Test subtask", "test sub description", 2, Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(15));
        Task testTask2 = new Task("Test task2", "test description2", Status.NEW);
        manager.createTask(testTask1);
        manager.createEpic(testEpic);
        manager.createSubtask(subtask);
        manager.createTask(testTask2);
        manager.getIdTask(1);
        manager.getIdTask(4);
        manager.getIdEpic(2);
        manager.getIdSubTask(3);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile
                ("src\\tests\\testTextSaveFile\\textDefaultLoadHistory.txt");
        assertEquals(newManager.getHistory().toString(), List.of(testTask1, testTask2, testEpic, subtask).toString());
    }
}