package pro.taskana.workbasket.internal;

import static pro.taskana.common.internal.util.CheckedFunction.wrap;

import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.function.Function;
import javax.security.auth.Subject;

import pro.taskana.common.api.security.UserPrincipal;
import pro.taskana.workbasket.api.WorkbasketCustomField;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.WorkbasketType;
import pro.taskana.workbasket.api.models.Workbasket;

public class WorkbasketBuilder {

  private final WorkbasketTestImpl testWorkbasket = new WorkbasketTestImpl();

  public static WorkbasketBuilder newWorkbasket() {
    return new WorkbasketBuilder();
  }

  public WorkbasketBuilder key(String key) {
    testWorkbasket.setKey(key);
    return this;
  }

  public WorkbasketBuilder name(String name) {
    testWorkbasket.setName(name);
    return this;
  }

  public WorkbasketBuilder description(String description) {
    testWorkbasket.setDescription(description);
    return this;
  }

  public WorkbasketBuilder owner(String owner) {
    testWorkbasket.setOwner(owner);
    return this;
  }

  public WorkbasketBuilder domain(String domain) {
    testWorkbasket.setDomain(domain);
    return this;
  }

  public WorkbasketBuilder type(WorkbasketType type) {
    testWorkbasket.setType(type);
    return this;
  }

  public WorkbasketBuilder customAttribute(String value, WorkbasketCustomField customField) {
    testWorkbasket.setCustomAttribute(customField, value);
    return this;
  }

  public WorkbasketBuilder orgLevel1(String orgLevel1) {
    testWorkbasket.setOrgLevel1(orgLevel1);
    return this;
  }

  public WorkbasketBuilder orgLevel2(String orgLevel2) {
    testWorkbasket.setOrgLevel2(orgLevel2);
    return this;
  }

  public WorkbasketBuilder orgLevel3(String orgLevel3) {
    testWorkbasket.setOrgLevel3(orgLevel3);
    return this;
  }

  public WorkbasketBuilder orgLevel4(String orgLevel4) {
    testWorkbasket.setOrgLevel4(orgLevel4);

    return this;
  }

  public WorkbasketBuilder markedForDeletion(boolean markedForDeletion) {
    testWorkbasket.setMarkedForDeletion(markedForDeletion);
    return this;
  }

  public WorkbasketBuilder created(Instant created) {
    testWorkbasket.setCreatedIgnoringFreeze(created);
    if (created != null) {
      testWorkbasket.freezeCreated();
    } else {
      testWorkbasket.unfreezeCreated();
    }
    return this;
  }

  public WorkbasketBuilder modified(Instant modified) {
    testWorkbasket.setModifiedIgnoringFreeze(modified);
    if (modified != null) {
      testWorkbasket.freezeModified();
    } else {
      testWorkbasket.unfreezeModified();
    }
    return this;
  }

  public Workbasket buildAndStore(WorkbasketService workbasketService) throws Exception {
    testWorkbasket.setId(null);
    Workbasket w = workbasketService.createWorkbasket(testWorkbasket);
    return workbasketService.getWorkbasket(w.getId());
  }

  public Workbasket buildAndStore(WorkbasketService taskService, String userId) throws Exception {
    Subject subject = new Subject();
    subject.getPrincipals().add(new UserPrincipal(userId));
    Function<WorkbasketService, Workbasket> buildAndStore = wrap(this::buildAndStore);
    PrivilegedAction<Workbasket> performBuildAndStore = () -> buildAndStore.apply(taskService);

    return Subject.doAs(subject, performBuildAndStore);
  }

  public static WorkbasketBuilder defaultTestWorkbasket() {
    return WorkbasketBuilder.newWorkbasket()
        .domain("DOMAIN_A")
        .name("Megabasket")
        .type(WorkbasketType.GROUP)
        .orgLevel1("company");
  }
}
