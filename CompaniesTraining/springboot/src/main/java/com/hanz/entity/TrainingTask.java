package com.hanz.entity;

import java.time.LocalDateTime;

public class TrainingTask {
    private Integer id;
    private Integer trainingPlanId;
    private Integer staffUserId;
    private Integer staffUserMetId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String imageUrl;
    private String staffUserName;

    private String mentorName;
    private String trainingPlanTitle;

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getTrainingPlanTitle() {
        return trainingPlanTitle;
    }

    public void setTrainingPlanTitle(String trainingPlanTitle) {
        this.trainingPlanTitle = trainingPlanTitle;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrainingPlanId() {
        return trainingPlanId;
    }

    public void setTrainingPlanId(Integer trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }

    public Integer getStaffUserId() {
        return staffUserId;
    }

    public void setStaffUserId(Integer staffUserId) {
        this.staffUserId = staffUserId;
    }

    public Integer getStaffUserMetId() {
        return staffUserMetId;
    }

    public void setStaffUserMetId(Integer staffUserMetId) {
        this.staffUserMetId = staffUserMetId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStaffUserName() {
        return staffUserName;
    }

    public void setStaffUserName(String staffUserName) {
        this.staffUserName = staffUserName;
    }
}
