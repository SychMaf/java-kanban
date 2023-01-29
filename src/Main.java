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

        System.out.println(taskManager.getHistory());
        taskManager.getIdTask(1);
        System.out.println(taskManager.getHistory());
        taskManager.getIdEpic(2);
        System.out.println(taskManager.getHistory());
        taskManager.getIdSubTask(5);
        System.out.println(taskManager.getHistory());
        taskManager.getIdTask(1);
        System.out.println(taskManager.getHistory());

        Task upgradedTask = new Task("Таск NEW", "Описание таск NEW", Status.DONE); // id 1
        Epic upgradedEpic = new Epic("Эпик NEW", "описание эпик NEW", Status.NEW); // id 2
        Subtask upgradedSubtask = new Subtask("Сабтаск NEW", "описание саб NEW", 2, Status.NEW); // id 3
        taskManager.updateTask(upgradedTask, 1);
        taskManager.updateSubtask(upgradedSubtask, 3);
        taskManager.updateEpic(upgradedEpic, 2);

        taskManager.getSubtaskListFromEpic(2);
        taskManager.getSubtaskListFromEpic(6);

        taskManager.removeIdSubTask(5);
        System.out.println(taskManager.getHistory()); //без сабтаска
        taskManager.removeIdTask(1);
        System.out.println(taskManager.getHistory()); //без таска
        taskManager.removeIdEpic(2);
        System.out.println(taskManager.getHistory()); //без эпика

        Task tasktest1 = new Task("test 1", "Описание test 1", Status.NEW); // id 8
        taskManager.createTask(tasktest1);
        Task tasktest2 = new Task("test 2", "Описание test 2", Status.NEW); // id 9
        taskManager.createTask(tasktest2);
        Epic epictest1 = new Epic("test 1", "описание epictest 1", Status.DONE); // id 10
        taskManager.createEpic(epictest1);
        Subtask subtasktest1 = new Subtask("Сабтаскtest 1", "описание сабtest 1", 10, Status.DONE);
        taskManager.createSubtask(subtasktest1);
        Subtask subtasktest2 = new Subtask("Сабтаскtest 2", "описание сабtest 2", 10, Status.DONE);
        taskManager.createSubtask(subtasktest2);
        Subtask subtasktest3 = new Subtask("Сабтаскtest 3", "описание сабtest 3", 10, Status.DONE);
        taskManager.createSubtask(subtasktest3);
        Epic epictest2 = new Epic("test 2", "описание epictest 2", Status.DONE);
        taskManager.createEpic(epictest2);
        taskManager.getIdTask(8);
        taskManager.getIdEpic(10);
        taskManager.getIdTask(9);
        taskManager.getIdSubTask(11);
        taskManager.getIdSubTask(13);
        taskManager.getIdSubTask(12);
        System.out.println(taskManager.getHistory());
        taskManager.getIdTask(8); // проверка повторения
        taskManager.getIdEpic(14);
        System.out.println(taskManager.getHistory());
        taskManager.removeIdEpic(10);
        System.out.println(taskManager.getHistory());
        taskManager.removeIdTask(9);
        System.out.println(taskManager.getHistory());
    }
}