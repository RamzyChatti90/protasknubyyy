package com.protasknubyyy.service.impl;

import com.protasknubyyy.domain.TaskStatus;
import com.protasknubyyy.repository.TaskRepository;
import com.protasknubyyy.service.DashboardService;
import com.protasknubyyy.service.dto.DashboardDataDTO;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final TaskRepository taskRepository;

    public DashboardServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public DashboardDataDTO getDashboardData() {
        log.debug("Request to get Dashboard Data");
        DashboardDataDTO dashboardDataDTO = new DashboardDataDTO();

        // Total tasks
        dashboardDataDTO.setTotalTasks(taskRepository.count());

        // Tasks by status
        Map<String, Long> tasksByStatus = new HashMap<>();
        EnumSet.allOf(TaskStatus.class).forEach(status -> {
            tasksByStatus.put(status.name(), taskRepository.countByStatus(status));
        });
        dashboardDataDTO.setTasksByStatus(tasksByStatus);

        // Specific counts for dashboard summary
        dashboardDataDTO.setCompletedTasks(taskRepository.countByStatus(TaskStatus.DONE));
        dashboardDataDTO.setInProgressTasks(taskRepository.countByStatus(TaskStatus.IN_PROGRESS));

        return dashboardDataDTO;
    }
}