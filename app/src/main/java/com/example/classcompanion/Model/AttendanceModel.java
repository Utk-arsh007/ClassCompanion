package com.example.classcompanion.Model;

public class AttendanceModel {
    String subject;
    int presentCount;
    int totalCount;

    public AttendanceModel() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public AttendanceModel(String subject, int presentCount, int totalCount) {
        this.subject = subject;
        this.presentCount = presentCount;
        this.totalCount = totalCount;
    }
}
