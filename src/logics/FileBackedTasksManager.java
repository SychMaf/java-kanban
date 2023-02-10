package logics;

import data.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String outputFileName;

    public FileBackedTasksManager(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    public Integer createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    private void createTask(Task task, int id) {
        task.setId(id);
        tasks.put(id, task);
        setGlobalId(getGlobalId() + 1);
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    private void createSubtask(Subtask subtask, int id) {
        if (epics.containsKey(subtask.getEpicBind())) {
            Epic epic = epics.get(subtask.getEpicBind());
            subtask.setId(id);
            epic.addSubTaskId(id);
            epics.put(subtask.getEpicBind(), epic);
            // вложили саб в епик
            subtasks.put(id, subtask);
            fillStatus(epic);
            // обновили статус
            setGlobalId(getGlobalId() + 1);
        }
    }

    @Override
    public Integer createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    private void createEpic(Epic epic, int id) {
        epic.setId(id);
        epics.put(id, epic);
        setGlobalId(getGlobalId() + 1);
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, false))) {
            writer.write(CSVTaskFormatter.headline());
            for (int i = 1; i < getGlobalId(); i++) {
                if (getTasks().containsKey(i)) {
                    writer.write(CSVTaskFormatter.toString(getTasks().get(i)));
                } else if (getEpics().containsKey(i)) {
                    writer.write(CSVTaskFormatter.toString(getEpics().get(i)));
                } else if (getSabTasks().containsKey(i)) {
                    writer.write(CSVTaskFormatter.toString(getSabTasks().get(i)));
                }
            }
            writer.write("\n");
            writer.write(CSVTaskFormatter.toString(getHistoryManager()));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка ввода/вывода");
        }
    }

    public static FileBackedTasksManager loadFromFile(String outputFileName) {
        FileBackedTasksManager fileManager = Managers.getDefaultFileManager("src\\data\\text.txt");
        List<String> content = new ArrayList<>();
        String history = null;
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFileName))) {
            while (reader.ready()) {
                content.add(reader.readLine());
                if (content.contains("")) {
                    history = reader.readLine();
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка ввода/вывода");
        }
        for (int i = 1; i < content.size() - 1; i++) {
            Type type = CSVTaskFormatter.taskFromString(content.get(i)).getType();
            int id = CSVTaskFormatter.getGlobalIdFromString(content.get(i));
            switch (type) {
                case TASK:
                    fileManager.createTask(CSVTaskFormatter.taskFromString(content.get(i)), id);
                    break;
                case EPIC:
                    fileManager.createEpic((Epic) CSVTaskFormatter.taskFromString(content.get(i)), id);
                    break;
                case SUBTASK:
                    fileManager.createSubtask((Subtask) CSVTaskFormatter.taskFromString(content.get(i)), id);
            }
            if (maxId < id) {
                maxId = id;
            }
        }
        fileManager.setGlobalId(maxId + 1); // предотвращение наложения id
        if (history != null) {
            for (Integer historyId : CSVTaskFormatter.historyFromString(history)) {
                if (fileManager.getTasks().containsKey(historyId)) {
                    fileManager.getHistoryManager().add(fileManager.getTasks().get(historyId));
                } else if (fileManager.getEpics().containsKey(historyId)) {
                    fileManager.getHistoryManager().add(fileManager.getEpics().get(historyId));
                } else if (fileManager.getSabTasks().containsKey(historyId)) {
                    fileManager.getHistoryManager().add(fileManager.getSabTasks().get(historyId));
                }
            }
        }
        return fileManager;
    }
}