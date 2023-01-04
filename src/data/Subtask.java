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
}