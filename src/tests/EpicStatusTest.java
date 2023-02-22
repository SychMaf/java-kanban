package tests;

import data.Epic;
import data.Status;
import data.Subtask;
import logics.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicStatusTest {
    private final FileBackedTasksManager fileManager = new FileBackedTasksManager
            ("src\\tests\\testTextSaveFile\\textStatusTest.txt");

    @Test
    public void changeStatusWithEmptySubtask() {
        Epic epic = new Epic("Test epic", "Test description", Status.NEW);
        int id = fileManager.createEpic(epic);
        final Epic savedEpic = fileManager.getIdEpic(id);
        assertEquals(savedEpic.getStatus(), Status.NEW, "Статус совпадает"); // мб добавить чет
    }

    @Test
    public void changeStatusWithAllNewSubtask() {
        Epic epic = new Epic("Test epic", "Test description", Status.NEW);
        Subtask TestSubtask1 = new Subtask("Test subtask 1", "Test sub description 1", 1, Status.NEW);
        Subtask TestSubtask2 = new Subtask("Test subtask 2", "Test sub description 2", 1, Status.NEW);
        int epicId = fileManager.createEpic(epic);
        fileManager.createSubtask(TestSubtask1);
        fileManager.createSubtask(TestSubtask2);
        final Epic savedEpic = fileManager.getIdEpic(epicId);
        assertEquals(savedEpic.getStatus(), Status.NEW, "Статус совпадает");
    }

    @Test
    public void changeStatusWithAllDoneSubtask() {
        Epic epic = new Epic("Test epic", "Test description", Status.NEW);
        Subtask TestSubtask1 = new Subtask("Test subtask 1", "Test sub description 1", 1, Status.DONE);
        Subtask TestSubtask2 = new Subtask("Test subtask 2", "Test sub description 2", 1, Status.DONE);
        int epicId = fileManager.createEpic(epic);
        fileManager.createSubtask(TestSubtask1);
        fileManager.createSubtask(TestSubtask2);
        final Epic savedEpic = fileManager.getIdEpic(epicId);
        assertEquals(savedEpic.getStatus(), Status.DONE, "Статус совпадает");
    }

    @Test
    public void changeStatusWithNewAndDoneSubtask() {
        Epic epic = new Epic("Test epic", "Test description", Status.NEW);
        Subtask TestSubtask1 = new Subtask("Test subtask 1", "Test sub description 1", 1, Status.NEW);
        Subtask TestSubtask2 = new Subtask("Test subtask 2", "Test sub description 2", 1, Status.DONE);
        int epicId = fileManager.createEpic(epic);
        fileManager.createSubtask(TestSubtask1);
        fileManager.createSubtask(TestSubtask2);
        final Epic savedEpic = fileManager.getIdEpic(epicId);
        assertEquals(savedEpic.getStatus(), Status.IN_PROGRESS, "Статус совпадает");
    }

    @Test
    public void changeStatusWithInProgressSubtask() {
        Epic epic = new Epic("Test epic", "Test description", Status.NEW);
        Subtask TestSubtask1 = new Subtask("Test subtask 1", "Test sub description 1", 1, Status.IN_PROGRESS);
        Subtask TestSubtask2 = new Subtask("Test subtask 2", "Test sub description 2", 1, Status.IN_PROGRESS);
        int epicId = fileManager.createEpic(epic);
        fileManager.createSubtask(TestSubtask1);
        fileManager.createSubtask(TestSubtask2);
        final Epic savedEpic = fileManager.getIdEpic(epicId);
        assertEquals(savedEpic.getStatus(), Status.IN_PROGRESS, "Статус совпадает");
    }
}