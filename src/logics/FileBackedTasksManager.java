package logics;

import data.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String outputFileName;

    public FileBackedTasksManager(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    public Integer createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Integer createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    @Override
    public Task getIdTask(int taskId) {
        Task task = super.getIdTask(taskId);
        save();
        return task;
    }

    @Override
    public Subtask getIdSubTask(int subId) {
        Subtask subtask = super.getIdSubTask(subId);
        save();
        return subtask;
    }

    @Override
    public Epic getIdEpic(int epicId) {
        Epic epic = super.getIdEpic(epicId);
        save();
        return epic;
    }

    @Override
    public void removeIdTask(int taskId) {
        super.removeIdTask(taskId);
        save();
    }

    @Override
    public void removeIdEpic(int epicId) {
        super.removeIdEpic(epicId);
        save();
    }

    @Override
    public void removeIdSubTask(int subId) {
        super.removeIdSubTask(subId);
        save();
    }

    @Override
    public void updateTask(Task task, int taskId) {
        super.updateTask(task, taskId);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask, int subId) {
        super.updateSubtask(subtask, subId);
        save();
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        super.updateEpic(epic, epicId);
        save();
    }

    private void save() {
        String firstLine = "id,type,name,status,description,epic \n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, false))) {
            writer.write(firstLine);
            for (int i = 1; i < getGlobalId(); i++) {
                if (getTasks().containsKey(i)) {
                    writer.write(getTasks().get(i).toString());
                } else if (getEpics().containsKey(i)) {
                    writer.write(getEpics().get(i).toString());
                } else if (getSabTasks().containsKey(i)) {
                    writer.write(getSabTasks().get(i).toString());
                }
            }
            writer.write("\n");
            for (Task historyTask : getHistory()) {
                writer.write(historyTask.getId() + ",");
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void loadFromFile(String outputFileName) {
        List<String> content = new ArrayList<>();
        String history = null;
        int maxId = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFileName))) {
            while (reader.ready()) {
                content.add(reader.readLine());
                if (content.contains("")) {
                    history = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        for (int i = 1; i < content.size() - 1; i++) {
            String line = content.get(i);
            String[] parts = line.split(",");
            if (Type.valueOf(parts[1]).equals(Type.TASK)) {
                Task task = new Task(parts[2], parts[4], Status.valueOf(parts[3]));
                createTask(task, Integer.parseInt(parts[0]));
            } else if (Type.valueOf(parts[1]).equals(Type.EPIC)) {
                Epic epic = new Epic(parts[2], parts[4], Status.valueOf(parts[3]));
                createEpic(epic, Integer.parseInt(parts[0]));
            } else if (Type.valueOf(parts[1]).equals(Type.SUBTASK)) {
                Subtask subtask = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]), Status.valueOf(parts[3]));
                createSubtask(subtask, Integer.parseInt(parts[0]));
            }
            if (maxId < Integer.parseInt(parts[0])) {
                maxId = Integer.parseInt(parts[0]);
            }
        }
        setGlobalId(maxId); // предотвращение наложения id
        if (history != null) {
            historyFromString(history);
        }
    }

    private void historyFromString(String history) {
        String[] value = history.split(",");
        for (String part : value) {
            int number = Integer.parseInt(part);
            if (getTasks().containsKey(number)) {
                getHistoryManager().add(getTasks().get(number));
            } else if (getEpics().containsKey(number)) {
                getHistoryManager().add(getEpics().get(number));
            } else if (getSabTasks().containsKey(number)) {
                getHistoryManager().add(getSabTasks().get(number));
            }
        }
    }
}
