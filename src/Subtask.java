
class Subtask extends Task {
    int epicBind;

    public Subtask(String name, String description, int epicBind, String status) {
        super(name, description, status);
        this.epicBind = epicBind;
    }
}
