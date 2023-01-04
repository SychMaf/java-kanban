package logics;

import data.Epic;
import data.Subtask;
import data.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    Integer createTask(Task task);
    Integer createSubtask(Subtask subtask);
    Integer createEpic(Epic epic);
    HashMap<Integer, Task> getTasks();
    HashMap<Integer, Subtask> getSabTasks();
    HashMap<Integer, Epic> getEpics();
    void clearTask();
    void clearEpic();
    void clearSubTask();
    Task getIdTask(int taskId);
    Epic getIdEpic(int epicId);
    Subtask getIdSubTask(int subId);
    void removeIdTask(int taskId);
    void removeIdEpic(int epicId);
    void removeIdSubTask(int subId);
    ArrayList<Subtask> getSubtaskListFromEpic(int subId);
    void updateTask(Task task, int taskId);
    void updateSubtask(Subtask subtask, int subId);
    void updateEpic(Epic epic, int epicId);
    HistoryManager getHistoryManager();
}
