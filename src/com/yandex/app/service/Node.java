package com.yandex.app.service;

import com.yandex.app.model.Task;

public class Node<T> {
    public Task data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

}
