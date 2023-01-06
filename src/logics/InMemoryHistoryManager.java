package logics;

import data.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LIMIT = 9;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() > HISTORY_LIMIT) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}