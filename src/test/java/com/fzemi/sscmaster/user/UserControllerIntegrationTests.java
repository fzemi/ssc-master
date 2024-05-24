package com.fzemi.sscmaster.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fzemi.sscmaster.user.entity.User;
import com.fzemi.sscmaster.user.service.UserService;
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
import java.util.TimeZone;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserControllerIntegrationTests(
            MockMvc mockMvc,
            UserService userService,
            MongoTemplate mongoTemplate
    ) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = new ObjectMapper();

        // fixes issue with serializing Date by writeValueAsString method
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(User.class);
    }

    @Test
    public void testThatGetUsersReturnsStatus200AndAllUsers() throws Exception {
        User userA = TestDataUtils.createTestUserA();
        User userC = TestDataUtils.createTestUserC();
        User savedUserA = userService.createUser(userA);
        User savedUserC = userService.createUser(userC);

        List<User> allUsers = List.of(savedUserA, savedUserC);
        String expectedJson = objectMapper.writeValueAsString(allUsers);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().json(expectedJson)
        );
    }

    @Test
    public void testThatGetDeletedUsersReturnsStatus200AndAllDeletedUsers() throws Exception {
        User userA = TestDataUtils.createTestUserA();
        User userB = TestDataUtils.createTestUserB(); // isDeleted = true
        User savedUserA = userService.createUser(userA);
        User savedUserB = userService.createUser(userB);

        List<User> deletedUsers = List.of(savedUserB);
        String expectedJson = objectMapper.writeValueAsString(deletedUsers);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/deleted")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().json(expectedJson)
        );
    }

    @Test
    public void testThatGetUserByIdReturnsStatus200AndUser() throws Exception {
        User user = TestDataUtils.createTestUserA();
        User savedUser = userService.createUser(user);

        String expectedJson = objectMapper.writeValueAsString(savedUser);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/" + savedUser.getID())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().json(expectedJson)
        );
    }

    @Test
    public void testThatGetNonExistingUserReturnsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/123")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatCreateUserReturnsStatus201AndSavedUser() throws Exception {
        User userA = TestDataUtils.createTestUserA();
        User userAWithModificationHistory = TestDataUtils.createTestUserAWithModificationHistory();

        String userAJson = objectMapper.writeValueAsString(userA);
        String userAJsonWithModificationHistory = objectMapper.writeValueAsString(userAWithModificationHistory);

        String resultJson = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isCreated()
                ).andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(resultJson)
                .whenIgnoringPaths("id", "creationDate", "modificationHistory[*].timestamp")
                .isEqualTo(userAJsonWithModificationHistory);
    }

    @Test
    public void testThatUpdateUserReturnsStatus200AndUpdatedUser() throws Exception {
        User userA = TestDataUtils.createTestUserA();
        User savedUserA = userService.createUser(userA);
        User updatedUserA = TestDataUtils.createTestUserA();
        updatedUserA.setTeam("Team master");
        User updatedUserAWithModificationHistory = TestDataUtils.createTestUserAWithModificationHistory(
                List.of("team: " + userA.getTeam() + " -> " + updatedUserA.getTeam())
        );
        updatedUserAWithModificationHistory.setTeam("Team master");

        String updatedUserAJson = objectMapper.writeValueAsString(updatedUserA);
        String updatedUserAWithModificationJson = objectMapper.writeValueAsString(updatedUserAWithModificationHistory);

        String resultJson = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/" + savedUserA.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(resultJson)
                .whenIgnoringPaths("id", "creationDate", "modificationHistory[*].timestamp")
                .isEqualTo(updatedUserAWithModificationJson);
    }
}
