package logics;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int globalId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Integer createTask(Task task) {
        task.setId(globalId);
        tasks.put(globalId, task);
        globalId++;
        return globalId - 1;
    }

    @Override
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

    @Override
    public Integer createEpic(Epic epic) {
        epic.setId(globalId);
        epics.put(globalId, epic);
        globalId++;
        return globalId - 1;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSabTasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void clearTask() {
        for (int key : tasks.keySet()) { //очистка истории task
            historyManager.remove(key);
        }
        tasks.clear();
    }

    @Override
    public void clearEpic() {
        for (int key : epics.keySet()) { //очистка истории epic
            historyManager.remove(key);
        }
        for (int key : subtasks.keySet()) { //очистка истории subtask
            historyManager.remove(key);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubTask() {
        for (int key : subtasks.keySet()) { //очистка истории subtask
            historyManager.remove(key);
        }
        subtasks.clear();
        for (int key : epics.keySet()) {
            // зачистка сабов в эпиках
            Epic epic = epics.get(key);
            epic.clearSubTaskIdList();
            epics.put(key, epic);
            fillStatus(epic);
        }
    }

    @Override
    public Task getIdTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            historyManager.add(tasks.get(taskId));
        }
        return tasks.get(taskId);
    }

    @Override
    public Epic getIdEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            historyManager.add(epics.get(epicId));
        }
        return epics.get(epicId);
    }

    @Override
    public Subtask getIdSubTask(int subId) {
        if (subtasks.containsKey(subId)) {
            historyManager.add(subtasks.get(subId));
        }
        return subtasks.get(subId);
    }

    @Override
    public void removeIdTask(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
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
            historyManager.remove(subtasks.get(keys.get(i)).getId());
            subtasks.remove(keys.get(i));

        }
        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void removeIdSubTask(int subId) {
        if (subtasks.containsKey(subId)) {
            Epic epic = epics.get(subtasks.get(subId).getEpicBind());
            epic.removeSabTaskId(subId);
            historyManager.remove(subId);
            subtasks.remove(subId);
            fillStatus(epic);
            // удалили сабтаск обновили статус эпика
        }
    }

    @Override
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

    @Override
    public void updateTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            task.setId(taskId);
            tasks.put(taskId, task);
        }
    }

    @Override
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

    @Override
    public void updateEpic(Epic epic, int epicId) {
        if (epics.containsKey(epicId)) {
            Epic reEpic = epics.get(epicId);
            reEpic.setDescription(epic.getDescription());
            reEpic.setName(epic.getName());
            epics.put(epicId, reEpic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
                if (findStatus.getStatus().equals(Status.IN_PROGRESS)) {
                    flagProgress = true;
                } else if (findStatus.getStatus().equals(Status.NEW)) {
                    flagNew = true;
                } else if (findStatus.getStatus().equals(Status.DONE)) {
                    flagDone = true;
                }
            }
        }
        if (flagNew && !flagProgress && !flagDone) {
            epic.setStatus(Status.NEW);
        } else if (flagDone && !flagProgress && !flagNew) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}