package meetup.user_service.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ErrorHandler.class)
class ErrorHandlerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ErrorHandler errorHandler;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID = "X-User-Id";

    private final ErrorResponse errorResponseNumberOne = new ErrorResponse(Map.of("error", "User id = 111 not found"),
            404, LocalDateTime.now());

    private final ErrorResponse errorResponseNumberTwo = new ErrorResponse(Map.of("error", "Bad Request"),
            400, LocalDateTime.now());

    private final ErrorResponse errorResponseNumberThree = new ErrorResponse(Map.of("error", "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"fff\""),
            500, LocalDateTime.now());

    @Test
    void handleNotFound() throws Exception {
        when(errorHandler.handleNotFound(new NotFoundException("User id = 111 not found!")))
                .thenReturn(errorResponseNumberOne);

        mvc.perform(get("/users/111")
                        .content(mapper.writeValueAsString(errorResponseNumberOne))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void handleValidation() throws Exception {
        when(errorHandler.handleNotFound(any()))
                .thenReturn(errorResponseNumberTwo);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(errorResponseNumberTwo))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void handleAllException() throws Exception {
        when(errorHandler.handleNotFound(any()))
                .thenReturn(errorResponseNumberThree);

        mvc.perform(post("/users/fff")
                        .content(mapper.writeValueAsString(errorResponseNumberThree))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isInternalServerError());
    }
}