package pro.taskana.workbasket.graphql.resolvers;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.rest.QueryPagingParameter;
import pro.taskana.common.rest.ldap.LdapClient;
import pro.taskana.workbasket.api.WorkbasketAccessItemQuery;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.WorkbasketAccessItem;
import pro.taskana.workbasket.common.assembler.WorkbasketAccessItemRepresentationModelAssembler;
import pro.taskana.workbasket.common.filter.WorkbasketAccessItemQueryFilterParameter;
import pro.taskana.workbasket.common.models.WorkbasketAccessItemPagedRepresentationModel;
import pro.taskana.workbasket.graphql.payloads.RemoveWorkbasketAccessItemsPayload;
import pro.taskana.workbasket.rest.WorkbasketAccessItemController.WorkbasketAccessItemQuerySortParameter;

@GraphQLApi
@Service
public class WorkbasketAccessItemResolver {

  private final WorkbasketService workbasketService;
  private final WorkbasketAccessItemRepresentationModelAssembler workbasketAccessItemModelAssembler;
  private final LdapClient ldapClient;

  @Autowired
  public WorkbasketAccessItemResolver(
      WorkbasketService workbasketService,
      WorkbasketAccessItemRepresentationModelAssembler workbasketAccessItemModelAssembler,
      LdapClient ldapClient) {
    this.workbasketService = workbasketService;
    this.workbasketAccessItemModelAssembler = workbasketAccessItemModelAssembler;
    this.ldapClient = ldapClient;
  }

  public WorkbasketAccessItemPagedRepresentationModel getWorkbasketAccessItems(
      @GraphQLArgument(name = "filterParameter")
          WorkbasketAccessItemQueryFilterParameter filterParameter,
      @GraphQLArgument(name = "sortParameter") WorkbasketAccessItemQuerySortParameter sortParameter,
      @GraphQLArgument(name = "pagingParameter")
          QueryPagingParameter<WorkbasketAccessItem, WorkbasketAccessItemQuery> pagingParameter)
      throws NotAuthorizedException {

    WorkbasketAccessItemQuery query = workbasketService.createWorkbasketAccessItemQuery();

    if (filterParameter != null) {
      filterParameter.apply(query);
    }
    if (sortParameter != null) {
      sortParameter.apply(query);
    }

    if (pagingParameter != null) {
      List<WorkbasketAccessItem> workbasketAccessItems = pagingParameter.apply(query);
      return workbasketAccessItemModelAssembler.toPagedModel(
          workbasketAccessItems, pagingParameter.getPageMetadata());

    } else {
      List<WorkbasketAccessItem> workbasketAccessItems = query.list();
      return workbasketAccessItemModelAssembler.toPagedModel(workbasketAccessItems, null);
    }
  }

  @GraphQLMutation(name = "removeWorkbasketAccessItems")
  public RemoveWorkbasketAccessItemsPayload removeWorkbasketAccessItems(
      @GraphQLArgument(name = "accessId") String id)
      throws NotAuthorizedException, InvalidArgumentException {

    if (ldapClient.isUser(id)) {
      List<WorkbasketAccessItem> workbasketAccessItemList =
          workbasketService.createWorkbasketAccessItemQuery().accessIdIn(id).list();

      if (workbasketAccessItemList != null && !workbasketAccessItemList.isEmpty()) {
        workbasketService.deleteWorkbasketAccessItemsForAccessId(id);
      }
    } else {
      throw new InvalidArgumentException(
          String.format(
              "AccessId '%s' is not a user. " + "You can remove all access items for users only.",
              id));
    }
    return new RemoveWorkbasketAccessItemsPayload(
        "Successfully removed all WorkbasketAccessItems for Workbasket", id);
  }
}
