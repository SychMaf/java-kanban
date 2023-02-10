package data;

public class Subtask extends Task {
    private int epicBind;

    public Subtask(String name, String description, int epicBind, Status status) {
        super(name, description, status);
        this.epicBind = epicBind;
    }

    public int getEpicBind() {
        return epicBind;
    }

    public void setEpicBind(int epicBind) {
        this.epicBind = epicBind;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return getId() + "; " + getType() + "; " + getName() + "; " + getStatus().toString() + "; " + getDescription() + "; " + getEpicBind() + "; \n";
    }
}