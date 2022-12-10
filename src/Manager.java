import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int globalId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public Integer createTask(Task task) {
        task.setId(globalId);
        tasks.put(globalId, task);
        globalId++;
        return (globalId - 1);
    }

    public Integer createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicBind())) {
            Epic epic = epics.get(subtask.getEpicBind());
            subtask.setId(globalId);
            epic.addSubTaskId(globalId);
            epics.put(subtask.getEpicBind(), epic);                             //вложили саб в епик
            subtasks.put(globalId, subtask);
            fillStatus(epic);                                                    //обновили статус
            globalId++;
            return (globalId - 1);
        }
        return null;
    }

    public Integer createEpic(Epic epic) {
        epic.setId(globalId);
        epics.put(globalId, epic);
        globalId++;
        return (globalId - 1);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSabTasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }


    public void clearTask() {
        tasks.clear();
    }

    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubTask() {
        subtasks.clear();
        for (int key : epics.keySet()) {          //зачистка сабов в эпиках
            Epic epic = epics.get(key);
            epic.clearSubTaskIdList();
            epics.put(key, epic);
            fillStatus(epic);
        }
    }

    public Task getIdTask(int putId) {
        return tasks.get(putId);
    }

    public Epic getIdEpic(int putId) {
        return epics.get(putId);
    }

    public Subtask getIdSubTask(int putId) {
        return subtasks.get(putId);
    }

    public void removeIdTask(int putId) {
        tasks.remove(putId);
    }

    public void removeIdEpic(int putId) {
        ArrayList<Integer> keys = new ArrayList<>();
        for (int key : subtasks.keySet()) {                   //удаление связанных сабов
            Subtask subtask = subtasks.get(key);
            if (subtask.getEpicBind() == putId)
                keys.add(key);
        }
        for (int i = 0; i < keys.size(); i++)
            subtasks.remove(keys.get(i));
        epics.remove(putId);
    }

    public void removeIdSubTask(int putId) {
        Epic epic = epics.get(subtasks.get(putId).getEpicBind());
        epic.removeSabTaskId(putId);
        subtasks.remove(putId);
        fillStatus(epic);                                   //удалили сабтаск обновили статус эпика
    }

    public ArrayList<Subtask> getSubtaskListFromEpic(int putId) {
        if (epics.containsKey(putId)) {
            Epic epic = epics.get(putId);
            ArrayList<Subtask> sabIncludingEpic = new ArrayList<>();
            for (int key : subtasks.keySet()) {
                if (epic.getSubtaskIdList().contains(key))
                    sabIncludingEpic.add(subtasks.get(key));
            }
            return sabIncludingEpic;
        } else
            return null;
    }

    public void upgradeTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            task.setId(taskId);
            tasks.put(taskId, task);
        }
    }

    public void upgradeSubtask(Subtask subtask, int taskId) {
        if (subtasks.containsKey(taskId)) {
            removeIdSubTask(taskId);                                       //удалили сабтаск
            Epic epic = epics.get(subtask.getEpicBind());
            epic.addSubTaskId(taskId);
            subtask.setId(taskId);
            epics.put(subtask.getEpicBind(), epic);                      //вложили саб в епик
            subtasks.put(taskId, subtask);
            fillStatus(epic);                                           //обновили статус
        }
    }

    public void upgradeEpic(Epic epic, int taskId) {
        if (epics.containsKey(taskId)) {
            Epic reEpic = epics.get(taskId);
            reEpic.setDescription(epic.getDescription());
            reEpic.setName(epic.getName());
            epics.put(taskId, reEpic);
        }
    }

    public void fillStatus(Epic epic) {
        boolean flagProgress = false;
        boolean flagNew = false;
        boolean flagDone = false;
        if (epic.getSubtaskIdList().isEmpty())
            flagNew = true;
        else {
            for (int includeId : epic.getSubtaskIdList()) {
                Subtask findStatus = subtasks.get(includeId);
                if (findStatus.getStatus().equals("IN_PROGRESS"))
                    flagProgress = true;
                else if (findStatus.getStatus().equals("NEW"))
                    flagNew = true;
                else if (findStatus.getStatus().equals("DONE"))
                    flagDone = true;
            }
        }
        if (flagNew && !flagProgress && !flagDone)
            epic.setStatus("NEW");
        else if (flagDone && !flagProgress && !flagNew)
            epic.setStatus("DONE");
        else
            epic.setStatus("IN_PROGRESS");
    }
}
