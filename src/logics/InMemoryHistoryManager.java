package logics;

import data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyMap = new HashMap<>();
    private final CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            customLinkedList.removeNode(historyMap.get(task.getId()));
            historyMap.remove(task.getId());
        }
        historyMap.put(task.getId(), customLinkedList.linkLast(task));
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


    private static class CustomLinkedList {
        public Node first;
        public Node last;
        private int size = 0;

        public Node linkLast(Task task) {
            Node newNode = new Node(task);
            if (first == null) {
                newNode.setNext(null);
                newNode.setPrevious(null);
                first = newNode;
                last = newNode;
            } else {
                last.setNext(newNode);
                newNode.setPrevious(last); //ссылаем новый узел на предыдущий
                last = newNode; // последним становится новый
            }
            size++;
            return newNode;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> data = new ArrayList<>();
            Node returnTask = first;
            while (returnTask != null) {
                data.add(returnTask.getData());
                returnTask = returnTask.getNext();
            }
            return data;
        }

        public void removeNode(Node node) {
            if (node.getPrevious() == null && node.getNext() == null) {
                first = null;
                last = null;
                size--;
            } else if (node.getPrevious() == null) {
                node.getNext().setPrevious(null);
                first = node.getNext();
                size--;
            } else if (node.getNext() == null) {
                node.getPrevious().setNext(null);
                last = node.getPrevious();
                size--;
            } else {
                node.getPrevious().setNext(node.getNext());
                node.getNext().setPrevious(node.getPrevious());
                node.setNext(null);
                node.setPrevious(null);
                node.setData(null);
                size--;
            }
        }
    }
}