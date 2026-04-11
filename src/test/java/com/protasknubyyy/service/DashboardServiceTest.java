package com.protasknubyyy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.protasknubyyy.domain.TaskStatus;
import com.protasknubyyy.repository.TaskRepository;
import com.protasknubyyy.service.dto.DashboardDataDTO;
import com.protasknubyyy.service.impl.DashboardServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test if necessary
    }

    @Test
    void getDashboardData_shouldReturnCorrectCounts() {
        // Given
        when(taskRepository.count()).thenReturn(10L);
        when(taskRepository.countByStatus(TaskStatus.TODO)).thenReturn(3L);
        when(taskRepository.countByStatus(TaskStatus.IN_PROGRESS)).thenReturn(5L);
        when(taskRepository.countByStatus(TaskStatus.DONE)).thenReturn(2L);
        when(taskRepository.countByStatus(TaskStatus.CANCELED)).thenReturn(0L);

        // When
        DashboardDataDTO result = dashboardService.getDashboardData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalTasks()).isEqualTo(10L);
        assertThat(result.getCompletedTasks()).isEqualTo(2L);
        assertThat(result.getInProgressTasks()).isEqualTo(5L);

        assertThat(result.getTasksByStatus()).isNotNull().hasSize(TaskStatus.values().length);
        assertThat(result.getTasksByStatus().get(TaskStatus.TODO.name())).isEqualTo(3L);
        assertThat(result.getTasksByStatus().get(TaskStatus.IN_PROGRESS.name())).isEqualTo(5L);
        assertThat(result.getTasksByStatus().get(TaskStatus.DONE.name())).isEqualTo(2L);
        assertThat(result.getTasksByStatus().get(TaskStatus.CANCELED.name())).isEqualTo(0L);

        verify(taskRepository, times(1)).count();
        EnumSet.allOf(TaskStatus.class).forEach(status -> {
            verify(taskRepository, times(1)).countByStatus(status);
        });
    }

    @Test
    void getDashboardData_shouldHandleEmptyRepository() {
        // Given
        when(taskRepository.count()).thenReturn(0L);
        EnumSet.allOf(TaskStatus.class).forEach(status -> {
            when(taskRepository.countByStatus(status)).thenReturn(0L);
        });

        // When
        DashboardDataDTO result = dashboardService.getDashboardData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalTasks()).isEqualTo(0L);
        assertThat(result.getCompletedTasks()).isEqualTo(0L);
        assertThat(result.getInProgressTasks()).isEqualTo(0L);

        assertThat(result.getTasksByStatus()).isNotNull().hasSize(TaskStatus.values().length);
        EnumSet.allOf(TaskStatus.class).forEach(status -> {
            assertThat(result.getTasksByStatus().get(status.name())).isEqualTo(0L);
        });

        verify(taskRepository, times(1)).count();
        EnumSet.allOf(TaskStatus.class).forEach(status -> {
            verify(taskRepository, times(1)).countByStatus(status);
        });
    }
}