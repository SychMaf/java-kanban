package logics;

import data.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");

    static String headline() {
        return "id,type,name,status,description,epic,yyyy,MM,dd,HH,mm,duration \n";
    }

    static String toString(Task task) {
        if (task.getStartTime() != null) {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus().toString() + "," + task.getDescription() + "," + task.getStartTime().format(formatter)
                    + "," + task.getDurationWork().toMinutes() + ",\n";
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus().toString() + "," + task.getDescription() + ",\n";
        }
    }

    static String toString(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus().toString() + "," + subtask.getDescription() + "," + subtask.getEpicBind() + ","
                    + subtask.getStartTime().format(formatter) + "," + subtask.getDurationWork().toMinutes() + ",\n";
        } else {
            return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus().toString() + "," + subtask.getDescription() + "," + subtask.getEpicBind()
                    + ",\n";
        }
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
                Task task;
                if (parts.length > 6) {
                    task = new Task(parts[2], parts[4], status,
                            LocalDateTime.of(Integer.parseInt(parts[5]), Integer.parseInt(parts[6]), Integer.parseInt(parts[7]),
                                    Integer.parseInt(parts[8]), Integer.parseInt(parts[9])), Duration.ofMinutes(Integer.parseInt(parts[10])));
                } else {
                    task = new Task(parts[2], parts[4], status);
                }
                return task;
            case EPIC:
                Epic epic = new Epic(parts[2], parts[4], status);
                return epic;
            case SUBTASK:
                Subtask subtask;
                if (parts.length > 6) {
                    subtask = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]), status,
                            LocalDateTime.of(Integer.parseInt(parts[6]), Integer.parseInt(parts[7]), Integer.parseInt(parts[8]),
                                    Integer.parseInt(parts[9]), Integer.parseInt(parts[10])), Duration.ofMinutes(Integer.parseInt(parts[11])));
                } else {
                    subtask = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]), status);
                }
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