import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import logics.FileBackedTasksManager;
import logics.Managers;
import logics.TaskManager;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("src\\data\\text.txt");

        Task task = new Task("Таск 1", "Описание таск 1", Status.NEW); // id 1
        Epic epic = new Epic("Эпик 1", "описание эпик 1", Status.NEW); // id 2
        Subtask subtask = new Subtask("Сабтаск 1", "описание саб 1", 2, Status.NEW); // id 3
        Task task1 = new Task("Таск 2", "описание 2", Status.NEW); // id 4
        Subtask subtask1 = new Subtask("Сабтаск 2", "описание саб 2", 2, Status.DONE); // id 5
        Epic epic1 = new Epic("Эпик 2", "описание эпик 2", Status.NEW); // id 6
        Subtask subtask3 = new Subtask("Сабтаск 3", "описание саб 3", 6, Status.DONE); // id 7

        fileManager.createTask(task);
        fileManager.createEpic(epic);
        fileManager.createSubtask(subtask);
        int idTask = fileManager.createTask(task1);
        int idSabTask = fileManager.createSubtask(subtask1);
        int idEpic = fileManager.createEpic(epic1);
        fileManager.createSubtask(subtask3);

        System.out.println(fileManager.getTasks());
        System.out.println(fileManager.getSabTasks());
        System.out.println(fileManager.getEpics());

        fileManager.getIdTask(1);
        fileManager.getIdEpic(2);
        fileManager.getIdSubTask(5);
        fileManager.getIdTask(1);
        System.out.println(fileManager.getHistory());

        FileBackedTasksManager fileManagerNew = FileBackedTasksManager.loadFromFile("src\\data\\text.txt");
        System.out.println(fileManagerNew.getTasks());
        System.out.println(fileManagerNew.getSabTasks());
        System.out.println(fileManagerNew.getEpics());
        System.out.println(fileManagerNew.getHistory());

        fileManagerNew.clearTask();
        fileManagerNew.clearSubTask();
        fileManagerNew.clearEpic();
    }
}