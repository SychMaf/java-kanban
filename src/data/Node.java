package data;

public class Node {

    public Task data;
    public Node next;
    public Node previous;

    public Node(Task data) {
        this.data = data;
    }
}
