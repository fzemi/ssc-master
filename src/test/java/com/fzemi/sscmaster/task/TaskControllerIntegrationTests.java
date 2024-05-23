package com.fzemi.sscmaster.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.service.TaskService;
import com.fzemi.sscmaster.utils.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskControllerIntegrationTests(
            MockMvc mockMvc,
            TaskService taskService,
            MongoTemplate mongoTemplate
    ) {
        this.mockMvc = mockMvc;
        this.taskService = taskService;
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = new ObjectMapper();

        // fixes issue with serializing Date by writeValueAsString method
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Task.class);
    }

    @Test
    public void testThatGetTasksReturnsStatus200AndAllTasks() throws Exception {
        Task taskA = TestDataUtils.createTestTaskA();
        Task taskC = TestDataUtils.createTestTaskC();
        Task savedTaskA = taskService.createTask(taskA);
        Task savedTaskC = taskService.createTask(taskC);

        List<Task> allTasks = List.of(savedTaskA, savedTaskC);
        String expectedJson = objectMapper.writeValueAsString(allTasks);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/tasks")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().json(expectedJson)
        );
    }

    @Test
    public void testThatGetDeletedTasksReturnsStatus200AndAllDeletedTasks() throws Exception {
        Task taskA = TestDataUtils.createTestTaskA();
        Task taskB = TestDataUtils.createTestTaskB(); // isDeleted = true
        Task savedTaskA = taskService.createTask(taskA);
        Task savedTaskB = taskService.createTask(taskB);

        List<Task> deletedTasks = List.of(savedTaskB);
        String expectedJson = objectMapper.writeValueAsString(deletedTasks);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/tasks/deleted")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().json(expectedJson)
        );
    }

    @Test
    public void testThatGetTaskByIdReturnsStatus200AndTask() throws Exception {
        Task task = TestDataUtils.createTestTaskA();
        Task savedTask = taskService.createTask(task);

        String expectedJson = objectMapper.writeValueAsString(savedTask);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/tasks/" + savedTask.getID())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().json(expectedJson)
        );
    }

    @Test
    public void testThatGetNonExistingTaskReturnsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/tasks/123")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatCreateTaskReturnsStatus201AndSavedTask() throws Exception {
        Task task = TestDataUtils.createTestTaskAWithModificationHistory();

        String taskJson = objectMapper.writeValueAsString(task);

        String resultJson = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(resultJson)
                .whenIgnoringPaths("id", "creationDate", "modificationHistory[*].timestamp")
                .isEqualTo(taskJson);
    }
}
