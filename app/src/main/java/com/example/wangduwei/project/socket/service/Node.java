package com.example.wangduwei.project.socket.service;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  15:22
 **/
public class Node {
    private String id;
    private int type;

    public Node(String id, int type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Node node = (Node) o;
            if (this.type != node.type) {
                return false;
            } else {
                return this.id != null ? this.id.equals(node.id) : node.id == null;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + this.type;
        return result;
    }

    @Override
    public String toString() {
        return "Node{id='" + this.id + '\'' + ", type=" + this.type + '}';
    }
}

