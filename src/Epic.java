import java.util.ArrayList;

class Epic extends Task {
    ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public void checkStatus() {
        boolean flagProgress = false;
        boolean flagNew = false;
        boolean flagDone = false;
        if (subtaskList.isEmpty())
            flagNew = true;
        else {
            for (Subtask findStatus : subtaskList) {
                if (findStatus.status.equals("IN_PROGRESS"))
                    flagProgress = true;
                else if (findStatus.status.equals("NEW"))
                    flagNew = true;
                else if (findStatus.status.equals("DONE"))
                    flagDone = true;
            }
        }
        if (flagNew && !flagProgress && !flagDone)
            status = "NEW";
        else if (flagDone && !flagProgress && !flagNew)
            status = "DONE";
        else
            status = "IN_PROGRESS";
    }
}



