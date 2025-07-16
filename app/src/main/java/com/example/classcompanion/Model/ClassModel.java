package com.example.classcompanion.Model;

public class ClassModel {
    String subject;
    String time;
    String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public ClassModel() {
    }

    public ClassModel(String subject, String time,String docId) {
        this.subject = subject;
        this.time = time;
        this.docId =docId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }






}
