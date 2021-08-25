package pro.taskana.workbasket.graphql.resolvers;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.taskana.common.api.exceptions.ConcurrencyException;
import pro.taskana.common.api.exceptions.DomainNotFoundException;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.rest.QueryPagingParameter;
import pro.taskana.workbasket.api.WorkbasketQuery;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.exceptions.WorkbasketAccessItemAlreadyExistException;
import pro.taskana.workbasket.api.exceptions.WorkbasketAlreadyExistException;
import pro.taskana.workbasket.api.exceptions.WorkbasketInUseException;
import pro.taskana.workbasket.api.exceptions.WorkbasketNotFoundException;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.api.models.WorkbasketAccessItem;
import pro.taskana.workbasket.api.models.WorkbasketSummary;
import pro.taskana.workbasket.common.assembler.WorkbasketAccessItemRepresentationModelAssembler;
import pro.taskana.workbasket.common.assembler.WorkbasketRepresentationModelAssembler;
import pro.taskana.workbasket.common.assembler.WorkbasketSummaryRepresentationModelAssembler;
import pro.taskana.workbasket.common.filter.WorkbasketQueryFilterParameter;
import pro.taskana.workbasket.common.models.DistributionTargetsCollectionRepresentationModel;
import pro.taskana.workbasket.common.models.WorkbasketAccessItemCollectionRepresentationModel;
import pro.taskana.workbasket.common.models.WorkbasketRepresentationModel;
import pro.taskana.workbasket.common.models.WorkbasketSummaryPagedRepresentationModel;
import pro.taskana.workbasket.graphql.payloads.CreateWorkbasketPayload;
import pro.taskana.workbasket.graphql.payloads.DeleteWorkbasketPayload;
import pro.taskana.workbasket.graphql.payloads.RemoveDistributionTargetPayload;
import pro.taskana.workbasket.graphql.payloads.SetDistributionTargetsPayload;
import pro.taskana.workbasket.graphql.payloads.SetWorkbasketAccessItemsPayload;
import pro.taskana.workbasket.graphql.payloads.UpdateWorkbasketPayload;
import pro.taskana.workbasket.rest.WorkbasketController.WorkbasketQuerySortParameter;

/** Resolver for all {@linkplain Workbasket} related Queries. */
@GraphQLApi
@Service
public class WorkbasketResolver {

  private final WorkbasketService workbasketService;
  private final WorkbasketSummaryRepresentationModelAssembler workbasketSummaryModelAssembler;
  private final WorkbasketRepresentationModelAssembler workbasketModelAssembler;
  private final WorkbasketAccessItemRepresentationModelAssembler workbasketAccessItemModelAssembler;

  @Autowired
  public WorkbasketResolver(
      WorkbasketService workbasketService,
      WorkbasketRepresentationModelAssembler workbasketModelAssembler,
      WorkbasketAccessItemRepresentationModelAssembler workbasketAccessItemModelAssembler,
      WorkbasketSummaryRepresentationModelAssembler workbasketSummaryModelAssembler) {
    this.workbasketService = workbasketService;
    this.workbasketModelAssembler = workbasketModelAssembler;
    this.workbasketAccessItemModelAssembler = workbasketAccessItemModelAssembler;
    this.workbasketSummaryModelAssembler = workbasketSummaryModelAssembler;
  }

  @GraphQLQuery(
      name = "getWorkbaskets",
      description =
          "This endpoint retrieves a list of existing Workbaskets. Filters can be applied.")
  public WorkbasketSummaryPagedRepresentationModel getWorkbaskets(
      @GraphQLArgument(name = "filterParameter") WorkbasketQueryFilterParameter filterParameter,
      @GraphQLArgument(name = "sortParameter") WorkbasketQuerySortParameter sortParameter,
      @GraphQLArgument(name = "pagingParameter")
          QueryPagingParameter<WorkbasketSummary, WorkbasketQuery> pagingParameter) {
    WorkbasketQuery query = workbasketService.createWorkbasketQuery();
    if (filterParameter != null) {
      filterParameter.apply(query);
    }
    if (sortParameter != null) {
      sortParameter.apply(query);
    }

    if (pagingParameter != null) {
      List<WorkbasketSummary> workbasketSummaries = pagingParameter.apply(query);
      return workbasketSummaryModelAssembler.toPagedModel(
          workbasketSummaries, pagingParameter.getPageMetadata());

    } else {
      List<WorkbasketSummary> workbasketSummaries = query.list();
      return workbasketSummaryModelAssembler.toPagedModel(workbasketSummaries, null);
    }
  }

  @GraphQLQuery(
      name = "getWorkbasket",
      description = "This endpoint retrieves a single Workbasket.")
  public WorkbasketRepresentationModel getWorkbasket(
      @GraphQLArgument(name = "workbasketId") String id)
      throws WorkbasketNotFoundException, NotAuthorizedException {
    Workbasket workbasket = workbasketService.getWorkbasket(id);
    return workbasketModelAssembler.toModelWithoutLinks(workbasket);
  }

  @GraphQLQuery(
      name = "getWorkbasketAccessItemsByWorkbasket",
      description = "This endpoint retrieves all Workbasket Access Items for a given Workbasket.")
  public WorkbasketAccessItemCollectionRepresentationModel getWorkbasketAccessItemsByWorkbasket(
      @GraphQLArgument(name = "workbasketId") String id)
      throws NotAuthorizedException, WorkbasketNotFoundException {
    List<WorkbasketAccessItem> accessItems = workbasketService.getWorkbasketAccessItems(id);
    return workbasketAccessItemModelAssembler.toTaskanaCollectionModelForSingleWorkbasket(
        id, accessItems);
  }

  @GraphQLQuery(
      name = "getDistributionTargetsByWorkbasket",
      description = "This endpoint retrieves all Distribution Targets for a requested Workbasket.")
  public DistributionTargetsCollectionRepresentationModel getDistributionTargets(
      @GraphQLArgument(name = "workbaskteId") String id)
      throws WorkbasketNotFoundException, NotAuthorizedException {
    List<WorkbasketSummary> distributionTargets = workbasketService.getDistributionTargets(id);
    return workbasketSummaryModelAssembler.toTaskanaCollectionModel(distributionTargets);
  }

  @GraphQLMutation(
      name = "deleteWorkbasket",
      description = "This endpoint deletes an existing Workbasket.")
  public DeleteWorkbasketPayload deleteWorkbasket(@GraphQLArgument(name = "workbasketId") String id)
      throws WorkbasketInUseException, InvalidArgumentException, WorkbasketNotFoundException,
          NotAuthorizedException {

    boolean workbasketDeleted = workbasketService.deleteWorkbasket(id);
    String customMessage;
    if (workbasketDeleted) {
      customMessage = "Workbasket successfully deleted.";
    } else {
      customMessage =
          "Workbasket was only marked for deletion and will be physically deleted later on.";
    }
    return new DeleteWorkbasketPayload(customMessage);
  }

  @GraphQLMutation(
      name = "createWorkbasket",
      description = "This endpoint creates a persistent Workbasket.")
  public CreateWorkbasketPayload createWorkbasket(
      @GraphQLArgument(name = "workbasket") WorkbasketRepresentationModel workbasketRepModel)
      throws InvalidArgumentException, WorkbasketAlreadyExistException, DomainNotFoundException,
          NotAuthorizedException {
    Workbasket workbasket = workbasketModelAssembler.toEntityModel(workbasketRepModel);
    workbasket = workbasketService.createWorkbasket(workbasket);
    return new CreateWorkbasketPayload(
        "Workbasket successfully created",
        workbasketModelAssembler.toModelWithoutLinks(workbasket));
  }

  @GraphQLMutation(
      name = "updateWorkbasket",
      description = "This endpoint updates a given Workbasket.")
  public UpdateWorkbasketPayload updateWorkbasket(
      @GraphQLArgument(name = "workbasketId") String id,
      @GraphQLArgument(name = "workbasket") WorkbasketRepresentationModel workbasketModel)
      throws InvalidArgumentException, ConcurrencyException, WorkbasketNotFoundException,
          NotAuthorizedException {
    Workbasket workbasket = workbasketModelAssembler.toEntityModel(workbasketModel);
    if (!id.equals(workbasket.getId())) {
      throw new InvalidArgumentException(
          "Target-WB-ID('"
              + id
              + "') is not identical with the WB-ID of to object which should be updated. ID=('"
              + workbasket.getId()
              + "')");
    }
    workbasket = workbasketService.updateWorkbasket(workbasket);
    return new UpdateWorkbasketPayload(
        "Workbasket successfully updated",
        workbasketModelAssembler.toModelWithoutLinks(workbasket));
  }

  @GraphQLMutation(
      name = "setWorkbasketAccessItems",
      description =
          "This endpoint replaces all Workbasket Access Items for a given Workbasket with the"
              + " provided ones.")
  public SetWorkbasketAccessItemsPayload setWorkbasketAccessItems(
      @GraphQLArgument(name = "workbasketId") String id,
      @GraphQLArgument(name = "workbasketAccessItems")
          WorkbasketAccessItemCollectionRepresentationModel workbasketAccessItemsRepModel)
      throws InvalidArgumentException, WorkbasketAccessItemAlreadyExistException,
          WorkbasketNotFoundException, NotAuthorizedException {
    if (workbasketAccessItemsRepModel == null) {
      throw new InvalidArgumentException("Can't create something with NULL body-value.");
    }

    List<WorkbasketAccessItem> workbasketAccessItems =
        workbasketAccessItemsRepModel.getContent().stream()
            .map(workbasketAccessItemModelAssembler::toEntityModel)
            .collect(Collectors.toList());
    workbasketService.setWorkbasketAccessItems(id, workbasketAccessItems);
    // return workbasketService.getWorkbasketAccessItems(id);
    return new SetWorkbasketAccessItemsPayload(
        "Successfully set WorkbasketAccessItems for Workbasket", id, workbasketAccessItemsRepModel);
  }

  @GraphQLMutation(
      name = "setDistributionTargets",
      description =
          "This endpoint replaces all Distribution Targets for a given Workbasket with the provided"
              + " ones.")
  public SetDistributionTargetsPayload setDistriubtionTargets(
      @NotNull @GraphQLArgument(name = "sourceWorkbasketId") String sourceWorkbasketId,
      @NotNull @GraphQLArgument(name = "targetWorkbasketIds") List<String> targetWorkbasketIds)
      throws WorkbasketNotFoundException, NotAuthorizedException {
    workbasketService.setDistributionTargets(sourceWorkbasketId, targetWorkbasketIds);
    return new SetDistributionTargetsPayload(
        "Successfully set Target-Workbaskets for Workbasket",
        sourceWorkbasketId,
        targetWorkbasketIds);
  }

  @GraphQLMutation(
      name = "removeDistributionTarget",
      description =
          "This endpoint removes all Distribution Target references for a provided Workbasket.")
  public RemoveDistributionTargetPayload removeDistributionTarget(
      @GraphQLArgument(name = "workbasketId") String targetWorkbasketId)
      throws WorkbasketNotFoundException, NotAuthorizedException {
    List<WorkbasketSummary> sourceWorkbaskets =
        workbasketService.getDistributionSources(targetWorkbasketId);
    for (WorkbasketSummary source : sourceWorkbaskets) {
      workbasketService.removeDistributionTarget(source.getId(), targetWorkbasketId);
    }
    return new RemoveDistributionTargetPayload(
        "Successfully removed all DistributionTarget references for a Workbasket",
        targetWorkbasketId);
  }
}
