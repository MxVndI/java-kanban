package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> taskList = new CustomLinkedList<>();
    private final HashMap<Integer, Node> history = new HashMap<>();

    public void add(Task task) {
        Node<Task> temp = new Node<>(task);
        taskList.linkLast(temp);
        history.put(task.getId(), temp);
    }

    public List<Task> getHistory() {
        return taskList.getTasks();
    }

    public void remove(int id) {
        taskList.removeNode(history.get(id));
        history.remove(id);
    }

    private class CustomLinkedList<T> {
        public Node<T> head;
        public Node<T> tail;
        private int size = 0;

        private void linkLast(Node<T> node) {
            if (size == 0) {
                head = tail = node;
            } else if (size == 1) {
                head.next = node;
                node.prev = head;
                tail = node;
            } else {
                node.prev = tail;
                tail.next = node;
                tail = node;
            }
            size++;
            removeNode(node);
        }

        private List<Task> getTasks() {
            if (taskList.size != 0) {
                ArrayList<Task> list = new ArrayList<>();
                Node<T> temp = head;
                while (temp != null) {
                    list.add(temp.data);
                    temp = temp.next;
                }
                return list;
            } else {
                System.out.println("История пуста");
                return null;
            }
        }

        private void removeNode(Node<T> node) {
            Set<Task> set = new HashSet<>();
            Node<T> temp = head;
            if (taskList.size != 0) {
                while (temp != null) {
                    if (node.data.equals(temp.data) && set.contains(node.data)) {
                        temp.prev.next = temp.next;
                        temp.next.prev = temp.prev;
                        size--;
                        return;
                    }
                    set.add(temp.data);
                    temp = temp.next;
                }

            }
        }
    }
}

