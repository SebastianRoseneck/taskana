package pro.taskana.classification.graphql;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import pro.taskana.classification.api.exceptions.ClassificationInUseException;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.test.rest.TaskanaSpringBootTest;

@TaskanaSpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin")
public class ClassificationResolverIntTest {

  private final MockMvc mockMvc;

  @Autowired
  ClassificationResolverIntTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void testCreateClassification() throws Exception {

    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{createClassification(classification:"
                            + "{category:\"EXTERNAL\",key:\"TestKey\",name:\"TestName\",priority:1,type:\"TASK\"})"
                            + "{classification{classificationId}}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.createClassification.classification.classificationId", startsWith("CLI:")));
  }

  @Test
  void testGetClassification() throws Exception {

    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "{getClassification(classificationId:\"CLI:000000000000000000000000000000000001\")"
                            + " {key}}"))
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassification.key", containsString("L10000")));
  }

  @Test
  void testGetMultipleClassifications() throws Exception {

    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content("{getClassifications {content{key}}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassifications.content", hasSize(78)));
  }

  @Test
  void testDeleteClassification() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{deleteClassification(classificationId:\"CLI:200000000000000000000000000000000018\"){message}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.deleteClassification.message",
                containsString("Classification successfully deleted")));
  }

  @Test
  void should_ReturnAllMasterClassifications_When_DomainIsSetWithEmptyString() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getClassifications(filterParameter:{domain:\"\"}){content{domain}}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassifications.content", hasSize(39)));
  }

  @Test
  void testGetAllClassificationsFilterByCustomAttribute() throws Exception {

    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getClassifications(filterParameter:{domain:\"DOMAIN_A\",custom1Like:\"RVNR\"})"
                            + "{content{key}}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassifications.content", hasSize(33)));
  }

  @Test
  void testGetAllClassificationsKeepingFilters() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getClassifications(sortParameter:{sortBy:KEY,order:ASCENDING},"
                            + "filterParameter:{domain:\"DOMAIN_A\"}){content{key}}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassifications.content", hasSize(37)));
  }

  @Test
  void testGetSecondPageSortedByKey() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "query{getClassifications(pagingParameter:{page:2,pageSize:5},"
                            + "sortParameter:{sortBy:KEY,order:ASCENDING},"
                            + "filterParameter:{domain:\"DOMAIN_A\"}){content{key}}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassifications.content", hasSize(5)));
  }

  @Test
  @DirtiesContext
  void testCreateClassificationWithParentId() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{createClassification(classification:"
                            + "{type:\"TASK\",category:\"MANUAL\",domain:\"DOMAIN_B\",key:\"newClassification\",name:\"new"
                            + " classification\","
                            + "serviceLevel:\"P1D\",parentId:\"CLI:200000000000000000000000000000000015\","
                            + "priority:1}){message}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.createClassification.message",
                containsString("Classification successfully created")));
  }

  @Test
  @DirtiesContext
  void testCreateClassificationWithParentKey() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "mutation{createClassification(classification:"
                            + "{type:\"TASK\",category:\"MANUAL\",domain:\"DOMAIN_B\","
                            + "key:\"newClassification2\",name:\"new classification\","
                            + "serviceLevel:\"P1D\",parentKey:\"T2100\","
                            + "priority:1}){message}}"))
            .andExpect(status().isOk())
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath(
                "data.createClassification.message",
                containsString("Classification successfully created")));
  }

  @Test
  @DirtiesContext
  void testReturn400IfCreateClassificationWithIncompatibleParentIdAndKey() throws Exception {

    this.mockMvc
        .perform(
            post("/graphql")
                .contentType("application/graphql")
                .content(
                    "mutation{createClassification(classification:"
                        + "{category:\"MANUAL\",domain:\"DOMAIN_B\",key:\"newClass3\",name:\"new"
                        + " classification\",type:\"TASK\",parentId:\"CLI:200000000000000000000000000000000015\","
                        + "parentKey:\"T2000\",serviceLevel:\"P1D\",priority:1}){message}}"))
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof InvalidArgumentException))
        .andReturn();
  }

  @Test
  @DirtiesContext
  void testCreateClassificationWithClassificationIdReturnsError400() throws Exception {
    this.mockMvc
        .perform(
            post("/graphql")
                .contentType("application/graphql")
                .content(
                    "mutation{createClassification(classification:"
                        + "{classificationId:\"id\",category:\"MANUAL\",domain:\"DOMAIN_A\","
                        + "key:\"NEW_CLASS\",name:\"new classification\",type:\"TASK\","
                        + "serviceLevel:\"P1D\",priority:1}){message}}"))
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof InvalidArgumentException))
        .andReturn();
  }

  @Test
  void testGetClassificationWithSpecialCharacter() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/graphql")
                    .contentType("application/graphql")
                    .content(
                        "{getClassification(classificationId:\"CLI:100000000000000000000000000000000009\")"
                            + " {name}}"))
            .andReturn();
    this.mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data.getClassification.name", containsString("Zustimmungserklärung")));
  }

  @Test
  @DirtiesContext
  void should_ReturnStatusCodeLocked_When_DeletingClassificationInUse() throws Exception {
    this.mockMvc
        .perform(
            post("/graphql")
                .contentType("application/graphql")
                .content(
                    "mutation{deleteClassification(classificationId:\"CLI:000000000000000000000000000000000003\")"
                        + " {message}}"))
        .andExpect(
            result ->
                assertTrue(result.getResolvedException() instanceof ClassificationInUseException))
        .andReturn();
  }
}
