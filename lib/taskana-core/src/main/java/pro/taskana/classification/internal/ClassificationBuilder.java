package pro.taskana.classification.internal;

import static pro.taskana.common.internal.util.CheckedFunction.wrap;

import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.function.Function;
import javax.security.auth.Subject;

import pro.taskana.classification.api.ClassificationCustomField;
import pro.taskana.classification.api.ClassificationService;
import pro.taskana.classification.api.models.Classification;
import pro.taskana.common.api.security.UserPrincipal;

public class ClassificationBuilder {

  private final ClassificationTestImpl testClassification = new ClassificationTestImpl();

  public static ClassificationBuilder newClassification() {
    return new ClassificationBuilder();
  }

  public ClassificationBuilder applicationEntryPoint(String applicationEntryPoint) {
    testClassification.setApplicationEntryPoint(applicationEntryPoint);
    return this;
  }

  public ClassificationBuilder category(String category) {
    testClassification.setCategory(category);
    return this;
  }

  public ClassificationBuilder domain(String domain) {
    testClassification.setDomain(domain);
    return this;
  }

  public ClassificationBuilder key(String key) {
    testClassification.setKey(key);
    return this;
  }

  public ClassificationBuilder name(String name) {
    testClassification.setName(name);
    return this;
  }

  public ClassificationBuilder parentId(String parentId) {
    testClassification.setParentId(parentId);
    return this;
  }

  public ClassificationBuilder parentKey(String parentKey) {
    testClassification.setParentKey(parentKey);
    return this;
  }

  public ClassificationBuilder priority(int priority) {
    testClassification.setPriority(priority);
    return this;
  }

  public ClassificationBuilder serviceLevel(String serviceLevel) {
    testClassification.setServiceLevel(serviceLevel);
    return this;
  }

  public ClassificationBuilder type(String type) {
    testClassification.setType(type);
    return this;
  }

  public ClassificationBuilder customAttribute(
      String value, ClassificationCustomField customField) {
    testClassification.setCustomAttribute(customField, value);
    return this;
  }

  public ClassificationBuilder isValidInDomain(boolean isValidInDomain) {
    testClassification.setIsValidInDomain(isValidInDomain);
    return this;
  }

  public ClassificationBuilder created(Instant created) {
    testClassification.setCreatedIgnoreFreeze(created);
    if (created != null) {
      testClassification.freezeCreated();
    } else {
      testClassification.unfreezeCreated();
    }
    return this;
  }

  public ClassificationBuilder modified(Instant modified) {
    testClassification.setModifiedIgnoreFreeze(modified);
    if (modified != null) {
      testClassification.freezeModified();
    } else {
      testClassification.unfreezeModified();
    }
    return this;
  }

  public ClassificationBuilder description(String description) {
    testClassification.setDescription(description);
    return this;
  }

  public Classification buildAndStore(ClassificationService classificationService)
      throws Exception {
    testClassification.setId(null);
    Classification c = classificationService.createClassification(testClassification);
    return classificationService.getClassification(c.getId());
  }

  public Classification buildAndStore(ClassificationService classificationService, String userId)
      throws Exception {
    Subject subject = new Subject();
    subject.getPrincipals().add(new UserPrincipal(userId));
    Function<ClassificationService, Classification> buildAndStore = wrap(this::buildAndStore);
    PrivilegedAction<Classification> performBuildAndStore =
        () -> buildAndStore.apply(classificationService);

    return Subject.doAs(subject, performBuildAndStore);
  }
}
