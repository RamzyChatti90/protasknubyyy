package com.protasknubyyy.service.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class DashboardDataDTO implements Serializable {

    private Long totalTasks;
    private Long completedTasks;
    private Long inProgressTasks;
    private Map<String, Long> tasksByStatus; // Map<TaskStatus.name(), count>

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Long getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(Long inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public Map<String, Long> getTasksByStatus() {
        return tasksByStatus;
    }

    public void setTasksByStatus(Map<String, Long> tasksByStatus) {
        this.tasksByStatus = tasksByStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardDataDTO that = (DashboardDataDTO) o;
        return Objects.equals(totalTasks, that.totalTasks) &&
               Objects.equals(completedTasks, that.completedTasks) &&
               Objects.equals(inProgressTasks, that.inProgressTasks) &&
               Objects.equals(tasksByStatus, that.tasksByStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalTasks, completedTasks, inProgressTasks, tasksByStatus);
    }

    @Override
    public String toString() {
        return "DashboardDataDTO{" +
               "totalTasks=" + totalTasks +
               ", completedTasks=" + completedTasks +
               ", inProgressTasks=" + inProgressTasks +
               ", tasksByStatus=" + tasksByStatus +
               '}';
    }
}