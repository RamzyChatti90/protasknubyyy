package com.protasknubyyy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protasknubyyy.ProtasknubyyyApp; // Assuming this is the main application class
import com.protasknubyyy.domain.Task;
import com.protasknubyyy.domain.TaskStatus;
import com.protasknubyyy.repository.TaskRepository;
import com.protasknubyyy.service.dto.DashboardDataDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@SpringBootTest(classes = ProtasknubyyyApp.class)
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.TODO;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.IN_PROGRESS;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(org.springframework.time.Clock.systemDefaultZone()).plusDays(1);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AtomicLong count = new AtomicLong(new Random().nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDashboardMockMvc;

    @Autowired
    private ObjectMapper objectMapper; // To deserialize JSON response

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a relationship to this entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .title(DEFAULT_TITLE + count.incrementAndGet()) // Make title unique for each entity
            .status(DEFAULT_STATUS)
            .dueDate(DEFAULT_DUE_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a relationship to this entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .title(UPDATED_TITLE + count.incrementAndGet())
            .status(UPDATED_STATUS)
            .dueDate(UPDATED_DUE_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return task;
    }

    @BeforeEach
    public void initTest() {
        taskRepository.deleteAll(); // Clear tasks before each test
        task = createEntity(em);
    }

    @Test
    @Transactional
    void getDashboardData() throws Exception {
        // Initialize the database with some tasks
        taskRepository.save(createEntity(em).status(TaskStatus.TODO));
        taskRepository.save(createEntity(em).status(TaskStatus.TODO));
        taskRepository.save(createEntity(em).status(TaskStatus.IN_PROGRESS));
        taskRepository.save(createEntity(em).status(TaskStatus.IN_PROGRESS));
        taskRepository.save(createEntity(em).status(TaskStatus.IN_PROGRESS));
        taskRepository.save(createEntity(em).status(TaskStatus.DONE));
        taskRepository.save(createEntity(em).status(TaskStatus.CANCELED));
        // Total: 7 tasks

        // Get the dashboard data
        restDashboardMockMvc
            .perform(get("/api/dashboard").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalTasks").value(7))
            .andExpect(jsonPath("$.completedTasks").value(1))
            .andExpect(jsonPath("$.inProgressTasks").value(3))
            .andExpect(jsonPath("$.tasksByStatus.TODO").value(2))
            .andExpect(jsonPath("$.tasksByStatus.IN_PROGRESS").value(3))
            .andExpect(jsonPath("$.tasksByStatus.DONE").value(1))
            .andExpect(jsonPath("$.tasksByStatus.CANCELED").value(1));
    }

    @Test
    @Transactional
    void getDashboardData_noTasks() throws Exception {
        // Database is empty due to initTest() and deleteAll()
        restDashboardMockMvc
            .perform(get("/api/dashboard").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalTasks").value(0))
            .andExpect(jsonPath("$.completedTasks").value(0))
            .andExpect(jsonPath("$.tasksByStatus.TODO").value(0))
            .andExpect(jsonPath("$.tasksByStatus.IN_PROGRESS").value(0))
            .andExpect(jsonPath("$.tasksByStatus.DONE").value(0))
            .andExpect(jsonPath("$.tasksByStatus.CANCELED").value(0));
    }
}