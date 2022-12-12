package logics;

import data.Epic;
import data.Subtask;
import data.Task;
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
        return globalId - 1;
    }

    public Integer createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicBind())) {
            Epic epic = epics.get(subtask.getEpicBind());
            subtask.setId(globalId);
            epic.addSubTaskId(globalId);
            epics.put(subtask.getEpicBind(), epic);
            // вложили саб в епик
            subtasks.put(globalId, subtask);
            fillStatus(epic);
            // обновили статус
            globalId++;
            return globalId - 1;
        }
        return null;
    }

    public Integer createEpic(Epic epic) {
        epic.setId(globalId);
        epics.put(globalId, epic);
        globalId++;
        return globalId - 1;
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
        for (int key : epics.keySet()) {
            // зачистка сабов в эпиках
            Epic epic = epics.get(key);
            epic.clearSubTaskIdList();
            epics.put(key, epic);
            fillStatus(epic);
        }
    }

    public Task getIdTask(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getIdEpic(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getIdSubTask(int subId) {
        return subtasks.get(subId);
    }

    public void removeIdTask(int taskId) {
        tasks.remove(taskId);
    }

    public void removeIdEpic(int epicId) {
        ArrayList<Integer> keys = new ArrayList<>();
        // удаление связанных сабов
        for (int key : subtasks.keySet()) {
            Subtask subtask = subtasks.get(key);
            if (subtask.getEpicBind() == epicId) {
                keys.add(key);
            }
        }
        for (int i = 0; i < keys.size(); i++) {
            subtasks.remove(keys.get(i));
        }
        epics.remove(epicId);
    }

    public void removeIdSubTask(int subId) {
        if (subtasks.containsKey(subId)) {
            Epic epic = epics.get(subtasks.get(subId).getEpicBind());
            epic.removeSabTaskId(subId);
            subtasks.remove(subId);
            fillStatus(epic);
            // удалили сабтаск обновили статус эпика
        }
    }

    public ArrayList<Subtask> getSubtaskListFromEpic(int subId) {
        if (epics.containsKey(subId)) {
            Epic epic = epics.get(subId);
            ArrayList<Subtask> sabIncludingEpic = new ArrayList<>();
            for (int key : subtasks.keySet()) {
                if (epic.getSubtaskIdList().contains(key)) {
                    sabIncludingEpic.add(subtasks.get(key));
                }
            }
            return sabIncludingEpic;
        } else
            return null;
    }

    public void updateTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            task.setId(taskId);
            tasks.put(taskId, task);
        }
    }

    public void updateSubtask(Subtask subtask, int subId) {
        if (subtasks.containsKey(subId)) {
            removeIdSubTask(subId);
            // удалили сабтаск
            Epic epic = epics.get(subtask.getEpicBind());
            epic.addSubTaskId(subId);
            subtask.setId(subId);
            // вложили саб в епик
            epics.put(subtask.getEpicBind(), epic);
            subtasks.put(subId, subtask);
            fillStatus(epic);
            // обновили статус
        }
    }


    public void updateEpic(Epic epic, int epicId) {
        if (epics.containsKey(epicId)) {
            Epic reEpic = epics.get(epicId);
            reEpic.setDescription(epic.getDescription());
            reEpic.setName(epic.getName());
            epics.put(epicId, reEpic);
        }
    }

    private void fillStatus(Epic epic) {
        boolean flagProgress = false;
        boolean flagNew = false;
        boolean flagDone = false;
        if (epic.getSubtaskIdList().isEmpty()) {
            flagNew = true;
        } else {
            for (int includeId : epic.getSubtaskIdList()) {
                Subtask findStatus = subtasks.get(includeId);
                if (findStatus.getStatus().equals("IN_PROGRESS")) {
                    flagProgress = true;
                } else if (findStatus.getStatus().equals("NEW")) {
                    flagNew = true;
                } else if (findStatus.getStatus().equals("DONE")) {
                    flagDone = true;
                }
            }
        }
        if (flagNew && !flagProgress && !flagDone) {
            epic.setStatus("NEW");
        } else if (flagDone && !flagProgress && !flagNew) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}