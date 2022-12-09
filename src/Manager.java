import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int globalId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public void CreateTask(Task task) {
        tasks.put(globalId, task);
        globalId++;
    }

    public void CreateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.epicBind)) {
            Epic epic = epics.get(subtask.epicBind);
            epic.subtaskList.add(subtask);
            epic.checkStatus();                                            //обновили статус
            epics.put(subtask.epicBind, epic);                             //вложили саб в епик
            subtasks.put(globalId, subtask);
            globalId++;
        }
    }

    public void CreateEpic(Epic epic) {
        epics.put(globalId, epic);
        globalId++;
    }

    public HashMap Get(String taskType) {
        switch (taskType) {
            case "task":
                return tasks;
            case "subtask":
                return subtasks;
            case "epic":
                return epics;
        }
        return null;
    }

    public void Clear(String taskType) {
        switch (taskType) {
            case "task":
                tasks.clear();
                return;
            case "subtask":
                subtasks.clear();
                for (int key : epics.keySet()) {          //зачистка сабов в эпиках
                    Epic epic = epics.get(key);
                    epic.subtaskList.clear();
                    epics.put(key, epic);
                    epic.checkStatus();
                }
                return;
            case "epic":
                epics.clear();
                subtasks.clear();
        }
    }

    public Task GetId(int putId) {
        if (tasks.containsKey(putId))
            return tasks.get(putId);
        else if (epics.containsKey(putId))
            return epics.get(putId);
        else if (subtasks.containsKey(putId))
            return subtasks.get(putId);
        else
            return null;
    }

    public void removeId(int putId) {
        if (tasks.containsKey(putId)) {
            tasks.remove(putId);
        } else if (epics.containsKey(putId)) {
            ArrayList<Integer> keys = new ArrayList<>();
            for (int key : subtasks.keySet()) {                   //удаление связанных сабов
                Subtask subtask = subtasks.get(key);
                if (subtask.epicBind == putId)
                    keys.add(key);
            }
            for (int i = 0; i < keys.size(); i++)
                subtasks.remove(keys.get(i));

            epics.remove(putId);

        } else if (subtasks.containsKey(putId)) {
            Epic epic = epics.get(subtasks.get(putId).epicBind);
            epic.subtaskList.remove(subtasks.get(putId));
            subtasks.remove(putId);
            epic.checkStatus();                                 //удалили сабтаск обновили статус эпика
        }
    }

    public ArrayList<Subtask> GetSubtaskListFromEpic(int putId) {
        if (epics.containsKey(putId)) {
            Epic epic = epics.get(putId);
            return epic.subtaskList;
        } else
            return null;
    }

    public void UpgradeTask(Task task, int taskId) {
        if (tasks.containsKey(taskId))
            tasks.put(taskId, task);
    }

    public void UpgradeSubtask(Subtask subtask, int taskId) {
        if (subtasks.containsKey(taskId)) {
            removeId(taskId);                                       //удалили сабтаск
            Epic epic = epics.get(subtask.epicBind);
            epic.subtaskList.add(subtask);
            epic.checkStatus();                                     //обновили статус
            epics.put(subtask.epicBind, epic);                      //вложили саб в епик

            subtasks.put(taskId, subtask);
        }
    }

    public void UpgradeEpic(Epic epic, int taskId) {
        if (epics.containsKey(taskId)) {
            Epic reEpic = epics.get(taskId);
            reEpic.description = epic.description;
            reEpic.name = epic.name;
            epics.put(taskId, reEpic);
        }
    }
}
