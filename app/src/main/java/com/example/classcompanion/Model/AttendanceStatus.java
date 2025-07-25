package com.example.classcompanion.Model;

public class AttendanceStatus {
    public boolean present;

    public AttendanceStatus() {} // required for Firestore

    public AttendanceStatus(boolean present) {
        this.present = present;
    }
}

