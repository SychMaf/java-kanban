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
    protected KVTaskClient kvTaskClient;
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter().nullSafe())
            .create();

    public HTTPTaskManager(String url) {
        super(url);
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
        kvTaskClient.register();
    }

    @Override
    public void save() {
        if (kvTaskClient == null) {
            System.out.println("Нет токена на сейве");
            return;
        }
        kvTaskClient.put("/tasks", gson.toJson(tasks.values()));
        kvTaskClient.put("/epics", gson.toJson(epics.values()));
        kvTaskClient.put("/subtasks", gson.toJson(subtasks.values()));
        kvTaskClient.put("/history", gson.toJson(getHistory()));
    }

    public void loadTasks() {
        String json = kvTaskClient.load("/tasks");
        ArrayList<Task> taskList = gson.fromJson(json,
                new TypeToken<ArrayList<Task>>(){}.getType());
        if (taskList != null) {
            for (Task task : taskList) {
                super.createTask(task, task.getId());
            }
        }
        json = kvTaskClient.load("/epics");
        ArrayList<Epic> epicList = gson.fromJson(json,
                new TypeToken<ArrayList<Epic>>(){}.getType());
        if (epicList != null) {
            for (Epic epic : epicList) {
                super.createEpic(epic, epic.getId());
            }
        }
        json = kvTaskClient.load("/subtasks");
        ArrayList<Subtask> subList = gson.fromJson(json,
                new TypeToken<ArrayList<Subtask>>(){}.getType());
        if (subList != null) {
            for (Subtask subtask : subList) {
                super.createSubtask(subtask, subtask.getId());
            }
        }
        json = kvTaskClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("")) {
            String[] lineSpit = historyLine.split(",");
            for (String sab : lineSpit) {
                getHistoryManager().getHistory();
            }
        }
        save();
    }
}