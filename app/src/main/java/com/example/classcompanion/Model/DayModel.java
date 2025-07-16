package com.example.classcompanion.Model;

import java.util.List;

public class DayModel {
    String dayName;
    List<ClassModel> classList;
    boolean isExpanded;

    public DayModel(String dayName, List<ClassModel> classList, boolean isExpanded) {
        this.dayName = dayName;
        this.classList = classList;
        this.isExpanded = isExpanded;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<ClassModel> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassModel> classList) {
        this.classList = classList;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public DayModel() {
    }
}
