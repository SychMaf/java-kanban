package logics;

import data.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    static String headline() {
        return "id,type,name,status,description,epic \n";
    }

    static String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," +
                task.getStatus().toString() + "," + task.getDescription() + ",\n";

    }

    static String toString(Subtask subtask) {
        return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                subtask.getStatus().toString() + "," + subtask.getDescription() + "," + subtask.getEpicBind() + ",\n";
    }

    static String toString(HistoryManager historyManager) {
        String history = "";
        for (Task historyTask : historyManager.getHistory()) {
            history += (historyTask.getId() + ",");
        }
        return history;
    }

    static Task taskFromString(String line) {
        String[] parts = line.split(",");
        Type type = Type.valueOf(parts[1]);
        Status status = Status.valueOf(parts[3]);
        switch (type) {
            case TASK:
                Task task = new Task(parts[2], parts[4], status);
                return task;
            case EPIC:
                Epic epic = new Epic(parts[2], parts[4], status);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]), status);
                return subtask;
        }
        return null;
    }

    static Integer getGlobalIdFromString(String line) {
        return Integer.parseInt(line.substring(0, line.indexOf(",")));
    }

    static List<Integer> historyFromString(String history) {
        List<Integer> historyId = new ArrayList<>();
        String[] value = history.split(",");
        for (String part : value) {
            historyId.add(Integer.parseInt(part));
        }
        return historyId;
    }
}