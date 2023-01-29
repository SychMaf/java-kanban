package logics;

import data.Task;
import data.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyMap = new HashMap<>();
    CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            customLinkedList.removeNode(historyMap.get(task.getId()));
            historyMap.remove(task.getId());
            historyMap.put(task.getId(), customLinkedList.linkLast(task));
        } else {
            historyMap.put(task.getId(), customLinkedList.linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            customLinkedList.removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }
}

class CustomLinkedList {
    public Node first;
    public Node last;
    private int size = 0;

    public Node linkLast(Task task) {
        Node newNode = new Node(task);
        if (first == null) {
            newNode.next = null;
            newNode.previous = null;
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            newNode.previous = last; //ссылаем новый узел на предыдущий
            last = newNode; // последним становится новый
        }
        size++;
        return newNode;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> data = new ArrayList<>();
        Node returnTask = first;
        for (int i = 0; i < size; i++) {
            data.add(returnTask.data);
            returnTask = returnTask.next;
        }
        return data;
    }

    public void removeNode(Node node) {
        if (node.previous == null && node.next == null) {
            first = null;
            last = null;
            size--;
        } else if (node.previous == null) {
            node.next.previous = null;
            first = node.next;
            size--;
        } else if (node.next == null) {
            node.previous.next = null;
            last = node.previous;
            size--;
        } else {
            node.previous.next = node.next;
            node.next.previous = node.previous;
            node.next = node.previous = null;
            node.data = null;
            size--;
        }
    }
}