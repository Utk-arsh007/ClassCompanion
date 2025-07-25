package com.example.classcompanion.Model;

public class AssignmentModel {

    private String id ;
    private String assignSub;
    private long dueDate;
    private boolean isCompleted;

    public AssignmentModel(String id, String assignSub, long dueDate, boolean isCompleted) {
        this.id = id;
        this.assignSub = assignSub;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssignSub() {
        return assignSub;
    }

    public void setAssignSub(String assignSub) {
        this.assignSub = assignSub;
    }

    public long  getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public AssignmentModel() {
    }


}
