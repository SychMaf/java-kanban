package data;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIdList = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    public void setSubtaskIdList(ArrayList<Subtask> subtaskList) {
        this.subtaskIdList = subtaskIdList;
    }

    public void clearSubTaskIdList() {
        subtaskIdList.clear();
    }

    public void removeSabTaskId(Integer sabId) {
        subtaskIdList.remove(sabId);
    }

    public void addSubTaskId(int sabId) {
        subtaskIdList.add(sabId);
    }
}