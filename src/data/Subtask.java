package data;

public class Subtask extends Task {
    private int epicBind;
    private String type = Type.SUBTASK.toString();

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
    public String toString() {
        return getId() + "," + type + "," + getName() + "," + getStatus().toString() + "," + getDescription() + "," + getEpicBind() + ",\n";
    }
}