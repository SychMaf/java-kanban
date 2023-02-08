package data;

import java.util.ArrayList;

public class Epic extends Task {
    private String type = Type.EPIC.toString();
    private ArrayList<Integer> subtaskIdList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
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

    @Override
    public String toString() {
        return getId() + "," + type + "," + getName() + "," + getStatus().toString() + "," + getDescription() + ",\n";
    }
}