package logics;

import data.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task){
        if (history.size() > 9 ){
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }
}
