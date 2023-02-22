package data;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicBind;

    public Subtask(String name, String description, int epicBind, Status status) {
        super(name, description, status);
        this.epicBind = epicBind;
    }
    public Subtask(String name, String description, int epicBind, Status status, LocalDateTime startTime, Duration durationWork) {
        super(name, description, status, startTime,durationWork);
        this.epicBind = epicBind;
    }

    public int getEpicBind() {
        return epicBind;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return getId() + "; " + getType() + "; " + getName() + "; " + getStatus().toString() + "; " + getDescription()
                + "; " + getEpicBind() + "; \n";
    }
}