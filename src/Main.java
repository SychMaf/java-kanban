public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task = new Task("Таск 1", "Описание таск 1", "NEW");                                 //id 1
        Epic epic = new Epic("Эпик 1", "описание эпик 1", "NEW");                                    //id 2
        Subtask subtask = new Subtask("Сабтаск 1", "описание саб 1", 2, "NEW");              //id 3
        Task task1 = new Task("Таск 2", "описание таск 2", "NEW");                                 //id 4
        Subtask subtask1 = new Subtask("Сабтаск 2", "описание саб 2", 2, "DONE");            //id 5
        Epic epic1 = new Epic("Эпик 2", "описание эпик 2", "NEW");                                  //id 6
        Subtask subtask2 = new Subtask("Сабтаск 3", "описание саб 3", 6, "DONE");     //id 7

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        int idTask = manager.createTask(task1);
        int idSabTask = manager.createSubtask(subtask1);
        int idEpic = manager.createEpic(epic1);
        manager.createSubtask(subtask2);

        manager.getTasks();
        manager.getSabTasks();
        manager.getEpics();

        manager.getIdTask(1);
        manager.getIdEpic(2);
        manager.getIdSubTask(5);

        Task upgradedTask = new Task("Таск NEW", "Описание таск NEW", "DONE");                                 //id 1
        Epic upgradedEpic = new Epic("Эпик NEW", "описание эпик NEW", "NEW");                                    //id 2
        Subtask upgradedSubtask = new Subtask("Сабтаск NEW", "описание саб NEW", 2, "NEW");              //id 3
        manager.upgradeTask(upgradedTask, 1);
        manager.upgradeSubtask(upgradedSubtask, 3);
        manager.upgradeEpic(upgradedEpic, 2);

        manager.getSubtaskListFromEpic(2);
        manager.getSubtaskListFromEpic(6);

        manager.removeIdSubTask(7);
        manager.removeIdTask(1);
        manager.removeIdEpic(2);

        manager.clearTask();
        manager.clearSubTask();
        manager.clearEpic();
    }
}
