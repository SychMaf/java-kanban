package logics;

import data.Epic;
import data.Subtask;
import data.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    Integer createTask(Task task);
    Integer createSubtask(Subtask subtask);
    Integer createEpic(Epic epic);
    Map<Integer, Task> getTasks();
    Map<Integer, Subtask> getSabTasks();
    Map<Integer, Epic> getEpics();
    void clearTask();
    void clearEpic();
    void clearSubTask();
    Task getIdTask(int taskId);
    Epic getIdEpic(int epicId);
    Subtask getIdSubTask(int subId);
    void removeIdTask(int taskId);
    void removeIdEpic(int epicId);
    void removeIdSubTask(int subId);
    List<Subtask> getSubtaskListFromEpic(int subId);
    void updateTask(Task task, int taskId);
    void updateSubtask(Subtask subtask, int subId);
    void updateEpic(Epic epic, int epicId);
    List<Task> getHistory();
}