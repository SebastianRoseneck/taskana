package pro.taskana.workbasket.graphql;

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
public class WorkbasketResolverIntTest {

  private final MockMvc mockMvc;

  @Autowired
  WorkbasketResolverIntTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void testCreateWorkbasket() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{createWorkbasket(workbasket:"
                            + "{domain:\"DOMAIN_C\",key:\"testKey\",name:\"testName\",type:PERSONAL})"
                            + "{workbasket{workbasketId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.createWorkbasket.workbasket.workbasketId", startsWith("WBI:")));
  }

  @Test
  void testGetWorkbasket() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getWorkbasket(workbasketId:\"WBI:100000000000000000000000000000000001\"){key}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getWorkbasket.key", containsString("GPK_KSC")));
  }

  @Test
  void testGetMultipleWorkbaskets() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getWorkbaskets(filterParameter:{domain:\"DOMAIN_A\"}){content{key}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getWorkbaskets.content", hasSize(22)));
  }

  @Test
  void testGetWorkbasketAccessItemsTargets() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getWorkbasketAccessItems(filterParameter:{accessId:\"teamlead-1\"}){content{workbasketId}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getWorkbasketAccessItems.content", hasSize(4)));
  }

  @Test
  void testGetDistributionTargets() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getWorkbaskets(filterParameter:{domain:\"DOMAIN_B\"}){content{key}}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getWorkbaskets.content", hasSize(5)));
  }

  @Test
  void testUpdateWorkbasket() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{updateWorkbasket(workbasketId:\"WBI:100000000000000000000000000000000001\","
                            + "workbasket:{workbasketId:\"WBI:100000000000000000000000000000000001\","
                            + "modified:\"2018-02-01T12:00:00Z\",domain:\"DOMAIN_A\",key:\"GPK_KSC\","
                            + "name:\"UpdatedName\",type:GROUP}){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.updateWorkbasket.message",
                containsString("Workbasket successfully updated")));
  }

  @Test
  void testRemoveWorkbasketAsDistributionTarget() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{removeDistributionTarget(workbasketId:\"WBI:100000000000000000000000000000000001\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.removeDistributionTarget.message",
                containsString(
                    "Successfully removed all DistributionTarget references for a Workbasket")));
  }

  @Test
  void testSetWorkbasketAccessItems() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{setWorkbasketAccessItems(workbasketId:\"WBI:100000000000000000000000000000000002\","
                            + "workbasketAccessItems:{accessItems:{accessId:\"WAI:100000000000000000000000000000000001\","
                            + "workbasketId:\"WBI:100000000000000000000000000000000002\"}}){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.setWorkbasketAccessItems.message",
                containsString("Successfully set WorkbasketAccessItems for Workbasket")));
  }

  @Test
  void testSetDistributionTarget() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{setDistributionTargets(sourceWorkbasketId:\"WBI:100000000000000000000000000000000003\","
                            + "targetWorkbasketIds:\"WBI:100000000000000000000000000000000004\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.setDistributionTargets.message",
                containsString("Successfully set Target-Workbaskets for Workbasket")));
  }

  @Test
  void testDeleteWorkbasket() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{deleteWorkbasket(workbasketId:\"WBI:000000000000000000000000000000000909\"){message}}"))
            .andReturn();

    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.deleteWorkbasket.message",
                containsString("Workbasket successfully deleted.")));
  }
}
