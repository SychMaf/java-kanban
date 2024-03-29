package data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIdList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
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
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return getId() + "; " + getType() + "; " + getName() + "; " + getStatus().toString() + "; " + getDescription()
                + "; \n";
    }
}