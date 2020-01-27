package pro.taskana.exceptions;

import pro.taskana.WorkbasketAccessItem;

public class WorkbasketAccessItemAlreadyExistException extends TaskanaException {
  private static final long serialVersionUID = 4716611657569005013L;

  public WorkbasketAccessItemAlreadyExistException(WorkbasketAccessItem accessItem) {
    super(
        String.format(
            "WorkbasketAccessItem for accessId '%s' "
                + "and WorkbasketId '%s', WorkbasketKey '%s' exists already.",
            accessItem.getAccessId(), accessItem.getWorkbasketId(), accessItem.getWorkbasketKey()));
  }
}