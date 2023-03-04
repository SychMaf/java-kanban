package data;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;
    private LocalDateTime startTime;
    private Duration durationWork;


    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration durationWork) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.durationWork = durationWork;
    }

    public Duration getDurationWork() {
        return durationWork;
    }

    public void setDurationWork(Duration durationWork) {
        this.durationWork = durationWork;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null)
            return null;
        return startTime.plus(durationWork);
    }

    public LocalDateTime getStartTime() {
        if (startTime == null)
            return null;
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Type getType() {
        return Type.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId() + "; " + getType() + "; " + getName() + "; " + getStatus().toString() + "; " + getDescription()
                + "; \n";
    }
}