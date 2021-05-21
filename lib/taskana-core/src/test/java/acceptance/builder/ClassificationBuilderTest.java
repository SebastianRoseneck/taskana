package acceptance.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.taskana.classification.internal.ClassificationBuilder.newClassification;

import acceptance.FooBar;
import java.time.Instant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.classification.api.ClassificationService;
import pro.taskana.classification.api.models.Classification;
import pro.taskana.classification.internal.ClassificationBuilder;
import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;

@ExtendWith(JaasExtension.class)
public class ClassificationBuilderTest {

  private static ClassificationService classificationService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    classificationService = taskanaEngine.getClassificationService();
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CreateClassification_When_UsingClassificationBuilder() throws Exception {
    Classification classification =
        newClassification().key("key0_A").domain("DOMAIN_A").buildAndStore(classificationService);
    Classification receivedClassification =
        classificationService.getClassification(classification.getId());
    assertThat(receivedClassification).isEqualTo(classification);
  }

  @Test
  void should_CreateClassificationAsUser_When_UsingClassificationBuilder() throws Exception {
    Classification classification =
        newClassification()
            .key("key1_A")
            .domain("DOMAIN_A")
            .buildAndStore(classificationService, "businessadmin");
    Classification receivedClassification =
        classificationService.getClassification(classification.getId());
    assertThat(receivedClassification).isEqualTo(classification);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CreateClassificationWithCreatedTimestamp_When_UsingClassificationBuilder()
      throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");
    Classification classification =
        newClassification()
            .key("key2_A")
            .domain("DOMAIN_A")
            .created(created)
            .buildAndStore(classificationService);
    Classification receivedClassification =
        classificationService.getClassification(classification.getId());
    assertThat(receivedClassification.getCreated()).isEqualTo(created);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CreateClassificationWithModifiedTimestamp_When_UsingClassificationBuilder()
      throws Exception {
    Instant modified = Instant.parse("2021-05-17T07:16:26.747Z");
    Classification classification =
        newClassification()
            .key("key3_A")
            .domain("DOMAIN_A")
            .modified(modified)
            .buildAndStore(classificationService);
    Classification receivedClassification =
        classificationService.getClassification(classification.getId());
    assertThat(receivedClassification.getModified()).isEqualTo(modified);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_AllowReuseOfChangedDefaultValues_When_UsingClassificationBuilder() throws Exception {
    Instant modified1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant modified2 = Instant.parse("2021-05-17T07:17:26.747Z");
    Instant created = Instant.parse("2021-05-17T07:16:25.747Z");

    ClassificationBuilder classificationBuilder =
        newClassification().domain("DOMAIN_A").created(created);
    Classification classification1 =
        classificationBuilder
            .modified(modified1)
            .key("key4_A")
            .buildAndStore(classificationService);
    Classification classification2 =
        classificationBuilder
            .modified(modified2)
            .key("key5_A")
            .buildAndStore(classificationService);

    Classification receivedClassification1 =
        classificationService.getClassification(classification1.getId());
    Classification receivedClassification2 =
        classificationService.getClassification(classification2.getId());
    assertThat(receivedClassification1.getModified()).isEqualTo(modified1);
    assertThat(receivedClassification2.getModified()).isEqualTo(modified2);
    assertThat(receivedClassification1.getCreated())
        .isEqualTo(receivedClassification2.getCreated())
        .isEqualTo(created);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_AllowReassignmentOfCreatedValue_When_UsingClassificationBuilder() throws Exception {
    Instant created1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant created2 = Instant.parse("2021-05-17T07:17:26.747Z");

    ClassificationBuilder classificationBuilder =
        newClassification().domain("DOMAIN_A").created(created1);
    Classification classification1 =
        classificationBuilder.key("key6_A").buildAndStore(classificationService);
    Classification classification2 =
        classificationBuilder.key("key7_A").created(created2).buildAndStore(classificationService);

    Classification receivedClassification1 =
        classificationService.getClassification(classification1.getId());
    Classification receivedClassification2 =
        classificationService.getClassification(classification2.getId());
    assertThat(receivedClassification1.getCreated()).isEqualTo(created1);
    assertThat(receivedClassification2.getCreated()).isEqualTo(created2);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_AllowOverwritingOfCreatedValue_When_UsingClassificationBuilder() throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");

    Classification classification =
        newClassification()
            .key("key8_A")
            .domain("DOMAIN_A")
            .created(created)
            .created(null)
            .buildAndStore(classificationService);

    Classification receivedClassification =
        classificationService.getClassification(classification.getId());
    assertThat(receivedClassification.getCreated()).isNotNull().isNotEqualTo(created);
  }
}
