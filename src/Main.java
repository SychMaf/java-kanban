import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import logics.Managers;
import logics.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Таск 1", "Описание таск 1", Status.NEW); // id 1
        Epic epic = new Epic("Эпик 1", "описание эпик 1", Status.NEW); // id 2
        Subtask subtask = new Subtask("Сабтаск 1", "описание саб 1", 2, Status.NEW); // id 3
        Task task1 = new Task("Таск 2", "описание таск 2", Status.NEW); // id 4
        Subtask subtask1 = new Subtask("Сабтаск 2", "описание саб 2", 2, Status.DONE); // id 5
        Epic epic1 = new Epic("Эпик 2", "описание эпик 2", Status.DONE); // id 6
        Subtask subtask2 = new Subtask("Сабтаск 3", "описание саб 3", 6, Status.DONE); // id 7

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        int idTask = taskManager.createTask(task1);
        int idSabTask = taskManager.createSubtask(subtask1);
        int idEpic = taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask2);

        taskManager.getTasks();
        taskManager.getSabTasks();
        taskManager.getEpics();

        taskManager.getIdTask(1);
        System.out.println(taskManager.getHistory());
        taskManager.getIdEpic(2);
        System.out.println(taskManager.getHistory());
        taskManager.getIdSubTask(5);
        System.out.println(taskManager.getHistory());

        Task upgradedTask = new Task("Таск NEW", "Описание таск NEW", Status.DONE); // id 1
        Epic upgradedEpic = new Epic("Эпик NEW", "описание эпик NEW", Status.NEW); // id 2
        Subtask upgradedSubtask = new Subtask("Сабтаск NEW", "описание саб NEW", 2, Status.NEW); // id 3
        taskManager.updateTask(upgradedTask, 1);
        taskManager.updateSubtask(upgradedSubtask, 3);
        taskManager.updateEpic(upgradedEpic, 2);

        taskManager.getSubtaskListFromEpic(2);
        taskManager.getSubtaskListFromEpic(6);

        taskManager.removeIdSubTask(7);
        taskManager.removeIdTask(1);
        taskManager.removeIdEpic(2);

        taskManager.clearTask();
        taskManager.clearSubTask();
        taskManager.clearEpic();
    }
}