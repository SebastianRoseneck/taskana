package pro.taskana.task.internal;

import pro.taskana.task.api.models.ObjectReference;

public class ObjectReferenceBuilder {

  private final ObjectReference objectReference = new ObjectReference();

  public static ObjectReferenceBuilder newObjectReference() {
    return new ObjectReferenceBuilder();
  }

  public ObjectReferenceBuilder company(String company) {
    objectReference.setCompany(company);
    return this;
  }

  public ObjectReferenceBuilder system(String system) {
    objectReference.setSystem(system);
    return this;
  }

  public ObjectReferenceBuilder systemInstance(String systemInstance) {
    objectReference.setSystemInstance(systemInstance);
    return this;
  }

  public ObjectReferenceBuilder type(String type) {
    objectReference.setType(type);
    return this;
  }

  public ObjectReferenceBuilder value(String value) {
    objectReference.setValue(value);
    return this;
  }

  public ObjectReference build() {
    objectReference.setId(null);
    return objectReference.copy();
  }

  public static ObjectReferenceBuilder defaultTestObjectReference() {
    return ObjectReferenceBuilder.newObjectReference()
        .company("Company1")
        .system("System1")
        .systemInstance("Instance1")
        .type("Type1")
        .value("Value1");
  }
}
