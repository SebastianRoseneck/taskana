package pro.taskana.workbasket.graphql;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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
public class WorkbasketAccessItemResolverIntTest {

  private final MockMvc mockMvc;

  @Autowired
  WorkbasketAccessItemResolverIntTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void testGetMultipleWorkbasketAccessItems() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getWorkbasketAccessItems(filterParameter:{workbasketKey:\"TEAMLEAD-1\"})"
                            + "{content{workbasketId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getWorkbasketAccessItems.content", hasSize(2)));
  }

  @Test
  void testRemoveWorkbasketAccessItems() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{removeWorkbasketAccessItems(accessId:\"teamlead-1\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.removeWorkbasketAccessItems.message",
                containsString("Successfully removed all WorkbasketAccessItems for Workbasket")));
  }
}
