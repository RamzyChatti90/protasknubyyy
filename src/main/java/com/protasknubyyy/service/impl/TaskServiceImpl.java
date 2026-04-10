package com.protasknubyyy.service.impl;

import com.protasknubyyy.domain.Task;
import com.protasknubyyy.repository.TaskRepository;
import com.protasknubyyy.service.TaskService;
import com.protasknubyyy.service.dto.TaskDTO;
import com.protasknubyyy.service.mapper.TaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.protasknubyyy.domain.Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        LOG.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        LOG.debug("Request to update Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDTO> partialUpdate(TaskDTO taskDTO) {
        LOG.debug("Request to partially update Task : {}", taskDTO);

        return taskRepository
            .findById(taskDTO.getId())
            .map(existingTask -> {
                taskMapper.partialUpdate(existingTask, taskDTO);

                return existingTask;
            })
            .map(taskRepository::save)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        LOG.debug("Request to get Task : {}", id);
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }
}
