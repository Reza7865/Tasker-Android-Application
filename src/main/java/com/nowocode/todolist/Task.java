package com.nowocode.todolist;

/**
 * This class Represents a Task.
 * priotiy = [1;3] where 1 = high priority, 2 = medium priority and 3 = low priority
 */
public class Task {
    private int priority;
    private String taskName;
    private boolean done;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
