package pro.taskana.task.graphql;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import pro.taskana.common.test.rest.TaskanaSpringBootTest;

@TaskanaSpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin")
public class TaskCommentResolverIntTest {

  private final MockMvc mockMvc;

  @Autowired
  TaskCommentResolverIntTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void testCreateTaskComment() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{createTaskComment(taskComment:"
                            + "{taskId:\"TKI:000000000000000000000000000000000000\","
                            + "textField:\"Some text\"}){taskComment{taskCommentId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("data.createTaskComment.taskComment.taskCommentId", startsWith("TCI:")));
  }

  @Test
  void testGetTaskComment() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getTaskComment(taskCommentId:\"TCI:000000000000000000000000000000000012\")"
                            + "{textField}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("data.getTaskComment.textField", containsString("some text in textfield")));
  }

  @Test
  void testGetMultipleTaskComments() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getTaskComments(taskId:\"TKI:000000000000000000000000000000000000\"){content{textField}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getTaskComments.content", hasSize(3)));
  }

  @Test
  void testUpdateTaskComment() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{updateTaskComment(taskCommentId:\"TCI:000000000000000000000000000000000000\","
                            + "taskComment:{taskCommentId:\"TCI:000000000000000000000000000000000000\",textField:\"Updated"
                            + " Text\",modified:\"2018-01-30T15:55:00Z\"}){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.updateTaskComment.message",
                containsString("Successfully updated TaskComment")));
  }

  @Test
  void testDeleteTaskComment() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getDistributionTargetsByWorkbasket(workbaskteId:\"WBI:100000000000000000000000000000000001\")"
                            + "{content{workbasketId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.deleteTaskComment.message", startsWith("WBI:")));
  }
}
