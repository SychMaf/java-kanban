package logics;

import data.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int globalId = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final TimeFilling timeFilling = new TimeFilling();


    public int getGlobalId() {
        return globalId;
    }

    protected void setGlobalId(int globalId) {
        this.globalId = globalId;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Integer createTask(Task task) {
        if (timeFilling.checkTimeOverlay(task)) { //проверяем пересечение
            timeFilling.fillTimeOverlay(task);
            task.setId(globalId);
            tasks.put(globalId, task);
            globalId++;
            return globalId - 1;
        }
        return -1;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicBind()) && timeFilling.checkTimeOverlay(subtask)) { //сущ. эпика и пересечения времени
            Epic epic = epics.get(subtask.getEpicBind());
            timeFilling.fillTimeOverlay(subtask);
            subtask.setId(globalId);
            epic.addSubTaskId(globalId);
            epics.put(subtask.getEpicBind(), epic);
            subtasks.put(globalId, subtask);
            epic = timeFilling.findEndTimeEpic(epic, subtasks);
            fillStatus(epic);
            globalId++;
            return globalId - 1;
        }
        return -1;
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
        for (int key : tasks.keySet()) { //очистка истории task и времени
            historyManager.remove(key);
            timeFilling.removeTimeOverlay(tasks.get(key));
        }
        tasks.clear();
    }

    @Override
    public void clearEpic() {
        for (int key : epics.keySet()) { //очистка истории epic
            historyManager.remove(key);
        }
        for (int key : subtasks.keySet()) { //очистка истории subtask, и времени
            historyManager.remove(key);
            timeFilling.removeTimeOverlay(subtasks.get(key));
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubTask() {
        for (int key : subtasks.keySet()) { //очистка истории subtask
            historyManager.remove(key);
            timeFilling.removeTimeOverlay(subtasks.get(key));
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
        timeFilling.removeTimeOverlay(tasks.get(taskId));
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
        for (Integer key : keys) {
            historyManager.remove(subtasks.get(key).getId());
            timeFilling.removeTimeOverlay(subtasks.get(key));
            subtasks.remove(key);
        }
        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void removeIdSubTask(int subId) {
        if (subtasks.containsKey(subId)) {
            timeFilling.removeTimeOverlay(subtasks.get(subId));
            Epic epic = epics.get(subtasks.get(subId).getEpicBind());
            epic.removeSabTaskId(subId);
            historyManager.remove(subId);
            subtasks.remove(subId);
            epic = timeFilling.findEndTimeEpic(epic, subtasks);
            fillStatus(epic); // удалили сабтаск обновили статус эпика
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
        if (tasks.containsKey(taskId) && timeFilling.checkTimeOverlay(task)) { //проверка наличия и возможного пересечения
            timeFilling.removeTimeOverlay(task);
            timeFilling.fillTimeOverlay(task);
            task.setId(taskId);
            tasks.put(taskId, task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, int subId) {
        if (subtasks.containsKey(subId) && timeFilling.checkTimeOverlay(subtask)) { //проверка наличия и возможного пересечения
            timeFilling.removeTimeOverlay(subtask);
            timeFilling.fillTimeOverlay(subtask);
            removeIdSubTask(subId);
            // удалили сабтаск
            Epic epic = epics.get(subtask.getEpicBind());
            epic.addSubTaskId(subId);
            subtask.setId(subId);
            // вложили саб в епик
            subtasks.put(subId, subtask);
            epic = timeFilling.findEndTimeEpic(epic, subtasks);
            epics.put(subtask.getEpicBind(), epic);
            fillStatus(epic);
            // обновили статус
        }
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        if (epics.containsKey(epicId)) {
            epic.setId(epicId);
            Epic reEpic = epics.get(epicId);
            reEpic.setDescription(epic.getDescription());
            reEpic.setName(epic.getName());
            reEpic.setId(epicId);
            epics.put(epicId, reEpic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Set<Task> tree = new TreeSet<>((t1, t2) -> {
            if (t1.getStartTime() != null && t2.getStartTime() != null) {
                return t1.getStartTime().compareTo(t2.getStartTime());
            } else if (t1.getStartTime() != null && t2.getStartTime() == null) {
                return -1;
            } else {
                return 1;
            }
        });
        tree.addAll(subtasks.values());
        tree.addAll(tasks.values());
        return tree;
    }

    protected void fillStatus(Epic epic) {
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