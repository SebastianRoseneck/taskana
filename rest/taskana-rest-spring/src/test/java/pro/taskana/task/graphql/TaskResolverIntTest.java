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
public class TaskResolverIntTest {

  private final MockMvc mockMvc;

  @Autowired
  TaskResolverIntTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void testCreateTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{createTask(task:{"
                            + "classificationSummary:{category:\"TASK\",key:\"L11010\",name:\"MyName\",priority:1,type:\"EXTERNAL\"},primaryObjRef:{company:\"MyCompany1\","
                            + " type:\"MyType1\",value:\"MySystem1\"},workbasketSummary:"
                            + "{workbasketId:\"WBI:100000000000000000000000000000000004\",domain:\"DOMAIN_A\",key:\"TEAMLEAD-1\",name:\"PPK"
                            + " Teamlead KSC 1\", type: PERSONAL}}){task{taskId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.createTask.task.taskId", startsWith("TKI:")));
  }

  @Test
  void testGetSingleTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getTask(taskId:\"TKI:000000000000000000000000000000000000\"){name}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getTask.name", containsString("Task99")));
  }

  @Test
  void testGetMultipleTasks() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getTasks(filterParameter:{workbasketId:\"WBI:100000000000000000000000000000000015\"})"
                            + "{content{taskId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getTasks.content", hasSize(24)));
  }

  @WithMockUser(username = "user-1-2")
  @Test
  void testClaimTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{claimTask(taskId:\"TKI:000000000000000000000000000000000025\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.claimTask.message", containsString("Task successfully claimed")));
  }

  @Test
  void testSelectAndClaimTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{selectAndClaimTask(filterParameter:{state:READY}){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.selectAndClaimTask.message",
                containsString("Task successfully selected and claimed")));
  }

  @Test
  void testCancelClaimTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{cancelClaimTask(taskId:\"TKI:000000000000000000000000000000000025\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.cancelClaimTask.message",
                containsString("Successfully canceled claim of a Task")));
  }

  @Test
  void testForceCancelClaimTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{forceCancelClaimTask(taskId:\"TKI:000000000000000000000000000000000025\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.forceCancelClaimTask.message",
                containsString("Successfully force canceled claim of a Task")));
  }

  @Test
  void testCompleteTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{completeTask(taskId:\"TKI:000000000000000000000000000000000025\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("data.completeTask.message", containsString("Task successfully completed")));
  }

  @Test
  void testCancelTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{cancelTask(taskId:\"TKI:000000000000000000000000000000000003\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("data.cancelTask.message", containsString("Task successfully canceled")));
  }

  @Test
  void testTransferTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{transferTask(taskId:\"TKI:000000000000000000000000000000000000\","
                            + " workbasketId:\"WBI:100000000000000000000000000000000001\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("data.transferTask.message", containsString("Task successfully transferred")));
  }

  @Test
  void testDeleteTask() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{deleteTask(taskId:\"TKI:000000000000000000000000000000000024\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("data.deleteTask.message", containsString("Task successfully deleted")));
  }

  @Test
  void testDeleteTasks() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{deleteTasks(filterParameter:{workbasketId:\"WBI:100000000000000000000000000000000007\"}){ids}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.deleteTasks.ids", hasSize(5)));
  }
}
