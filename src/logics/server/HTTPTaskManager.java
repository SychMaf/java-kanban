package logics.server;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.*;

import logics.FileBackedTasksManager;
import logics.LocalDateAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HTTPTaskManager extends FileBackedTasksManager {

    protected String url;
    protected KVTaskClient KVClient;
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter().nullSafe())
            .create();

    public HTTPTaskManager (String url){
        super(url);
        this.url = url;
        KVClient = new KVTaskClient(url);
        KVClient.register();
    }

    @Override
    public void save() {
        if (KVClient == null) {
            System.out.println("Нет токена на сейве");
            return;
        }
        KVClient.put("/tasks", gson.toJson(tasks.values()));
        KVClient.put("/epics", gson.toJson(epics.values()));
        KVClient.put("/subtasks", gson.toJson(subtasks.values()));
        KVClient.put("/history", gson.toJson(getHistory()));
    }

    public void loadTasks() {
        String json = KVClient.load("/tasks");
        ArrayList<Task> taskList = gson.fromJson(json,
                new TypeToken<ArrayList<Task>>(){}.getType());
        if (taskList != null) {
            for (Task task : taskList) {
                super.createTask(task, task.getId());
            }
        }
        json = KVClient.load("/epics");
        ArrayList<Epic> epicList = gson.fromJson(json,
                new TypeToken<ArrayList<Epic>>(){}.getType());
        if (epicList != null) {
            for (Epic epic : epicList) {
                super.createEpic(epic, epic.getId());
            }
        }
        json = KVClient.load("/subtasks");
        ArrayList<Subtask> subList = gson.fromJson(json,
                new TypeToken<ArrayList<Subtask>>(){}.getType());
        if (subList != null) {
            for (Subtask subtask : subList) {
                super.createSubtask(subtask, subtask.getId());
            }
        }
        json = KVClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("")) {
            String[] lineSpit = historyLine.split(",");
            for (String sab : lineSpit) {
                getHistoryManager().getHistory();
            }
        }
        save();
    }

    @Override
    public Integer createTask(Task task) {
        return super.createTask(task);
    }

    @Override
    public Integer createEpic(Epic epic) {
        return super.createEpic(epic);
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        return super.createSubtask(subtask);
    }

    @Override
    public void clearTask() {
        super.clearTask();
    }

    @Override
    public void removeIdTask(int taskId) {
        super.removeIdTask(taskId);
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
    }

    @Override
    public void removeIdEpic(int epicId) {
        super.removeIdEpic(epicId);
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
    }

    @Override
    public void removeIdSubTask(int subId) {
        super.removeIdSubTask(subId);
    }

    @Override
    public void updateTask(Task task, int taskId) {
        super.updateTask(task, taskId);
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        super.updateEpic(epic, epicId);
    }

    @Override
    public void updateSubtask(Subtask subtask, int subId) {
        super.updateSubtask(subtask, subId);
        save();
    }

    @Override
    public Task getIdTask(int taskId) {
        return super.getIdTask(taskId);
    }

    @Override
    public Subtask getIdSubTask(int subId) {
        return super.getIdSubTask(subId);
    }

    @Override
    public Epic getIdEpic(int epicId) {
        return super.getIdEpic(epicId);
    }
}