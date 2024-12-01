package meetup.user_service.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
    }

    @Test
    void createUser_Success() throws Exception {
        NewUserRequest request = new NewUserRequest("John", "john@example.com", "Password123!", "Hello");
        UserDto response = new UserDto(1L, "John", "john@example.com", null, "Hello");

        Mockito.when(userService.createUser(eq(1L), any(NewUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.aboutMe").value("Hello"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Ensure password is not exposed
    }

    @Test
    void getUser_Success() throws Exception {
        UserDto response = new UserDto(1L, "John", "john@example.com", null, "Hello");

        Mockito.when(userService.getUser(1L, 1L)).thenReturn(response);

        mockMvc.perform(get("/users/1")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.aboutMe").value("Hello"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Ensure password is not exposed
    }

    @Test
    void getUsers_Success() throws Exception {
        UserDto user1 = new UserDto(1L, "John", "john@example.com", null, "Hello");
        UserDto user2 = new UserDto(2L, "Jane", "jane@example.com", null, "Hi");

        Mockito.when(userService.getUsers(eq(1L), anyInt(), anyInt())).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users")
                        .header("X-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].aboutMe").value("Hello"))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"))
                .andExpect(jsonPath("$[1].aboutMe").value("Hi"))
                .andExpect(jsonPath("$[1].password").doesNotExist());
    }

    @Test
    void updateUser_Success() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("UpdatedName", "UpdatedPassword123!", "Updated bio");
        UserDto response = new UserDto(1L, "UpdatedName", "updated@example.com", null, "Updated bio");

        Mockito.when(userService.updateUser(eq(1L), eq("currentPassword"), any(UpdateUserRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "currentPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.aboutMe").value("Updated bio"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void deleteUser_Success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(eq(1L), eq("currentPassword"));

        mockMvc.perform(delete("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "currentPassword"))
                .andExpect(status().isNoContent());
    }

}