package com.protasknubyyy.repository;

import com.protasknubyyy.domain.Task;
import com.protasknubyyy.domain.enumeration.TaskStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByStatus(TaskStatus status);
}