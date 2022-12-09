public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task = new Task("Таск 1", "Описание таск 1", "DONE");                                 //id 1
        Epic epic = new Epic("Эпик 1", "описание эпик 1", "NEW");                                    //id 2
        Subtask subtask = new Subtask("Сабтаск 1", "описание саб 1", 2, "NEW");              //id 3
        Task task1 = new Task("Таск 2", "описание таск 2", "NEW");                                 //id 4
        Subtask subtask1 = new Subtask("Сабтаск 2", "описание саб 2", 2, "DONE");            //id 5
        Epic epic1 = new Epic("Эпик 2", "описание эпик 2", "NEW");                                  //id 6
        Subtask subtask2 = new Subtask("Сабтаск 3", "описание саб 3", 6, "IN_PROGRESS");     //id 7

        manager.CreateTask(task);
        manager.CreateEpic(epic);
        manager.CreateSubtask(subtask);
        manager.CreateTask(task1);
        manager.CreateSubtask(subtask1);
        manager.CreateEpic(epic1);
        manager.CreateSubtask(subtask2);

    }
}
