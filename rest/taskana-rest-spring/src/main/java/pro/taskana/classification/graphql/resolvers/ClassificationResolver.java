package pro.taskana.classification.graphql.resolvers;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.taskana.classification.api.ClassificationQuery;
import pro.taskana.classification.api.ClassificationService;
import pro.taskana.classification.api.exceptions.ClassificationAlreadyExistException;
import pro.taskana.classification.api.exceptions.ClassificationInUseException;
import pro.taskana.classification.api.exceptions.ClassificationNotFoundException;
import pro.taskana.classification.api.exceptions.MalformedServiceLevelException;
import pro.taskana.classification.api.models.Classification;
import pro.taskana.classification.api.models.ClassificationSummary;
import pro.taskana.classification.common.assembler.ClassificationRepresentationModelAssembler;
import pro.taskana.classification.common.assembler.ClassificationSummaryRepresentationModelAssembler;
import pro.taskana.classification.common.filter.ClassificationQueryFilterParameter;
import pro.taskana.classification.common.models.ClassificationRepresentationModel;
import pro.taskana.classification.common.models.ClassificationSummaryPagedRepresentationModel;
import pro.taskana.classification.graphql.payloads.CreateClassificationPayload;
import pro.taskana.classification.graphql.payloads.DeleteClassificationPayload;
import pro.taskana.classification.graphql.payloads.UpdateClassificationPayload;
import pro.taskana.classification.rest.ClassificationController.ClassificationQuerySortParameter;
import pro.taskana.common.api.exceptions.ConcurrencyException;
import pro.taskana.common.api.exceptions.DomainNotFoundException;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.rest.QueryPagingParameter;

@GraphQLApi
@Service
public class ClassificationResolver {

  private final ClassificationService classificationService;
  private final ClassificationRepresentationModelAssembler classificationModelAssembler;
  private final ClassificationSummaryRepresentationModelAssembler
      classificationSummaryModelAssembler;

  @Autowired
  public ClassificationResolver(
      ClassificationService classificationService,
      ClassificationRepresentationModelAssembler classificationModelAssembler,
      ClassificationSummaryRepresentationModelAssembler classificationSummaryModelAssembler) {
    this.classificationService = classificationService;
    this.classificationModelAssembler = classificationModelAssembler;
    this.classificationSummaryModelAssembler = classificationSummaryModelAssembler;
  }

  @GraphQLQuery(
      name = "getClassifications",
      description =
          "This endpoint retrieves a list of existing Classifications. Filters can be applied.")
  public ClassificationSummaryPagedRepresentationModel getClassifications(
      @GraphQLArgument(name = "filterParameter") ClassificationQueryFilterParameter filterParameter,
      @GraphQLArgument(name = "sortParameter") ClassificationQuerySortParameter sortParameter,
      @GraphQLArgument(name = "pagingParameter")
          QueryPagingParameter<ClassificationSummary, ClassificationQuery> pagingParameter) {

    final ClassificationQuery query = classificationService.createClassificationQuery();
    if (filterParameter != null) {
      filterParameter.apply(query);
    }
    if (sortParameter != null) {
      sortParameter.apply(query);
    }

    if (pagingParameter != null) {
      List<ClassificationSummary> classificationSummaries = pagingParameter.apply(query);
      return classificationSummaryModelAssembler.toPagedModel(
          classificationSummaries, pagingParameter.getPageMetadata());

    } else {
      List<ClassificationSummary> classificationSummaries = query.list();
      return classificationSummaryModelAssembler.toPagedModel(classificationSummaries, null);
    }
  }

  @GraphQLQuery(
      name = "getClassification",
      description = "This endpoint retrieves a single Classification.")
  public ClassificationRepresentationModel getClassification(
      @GraphQLArgument(name = "classificationId") String id)
      throws ClassificationNotFoundException {
    Classification classification = classificationService.getClassification(id);
    return classificationModelAssembler.toModel(classification);
  }

  @GraphQLMutation(
      name = "createClassification",
      description = "This endpoint creates a new Classification.")
  public CreateClassificationPayload createClassification(
      @GraphQLArgument(name = "classification")
          ClassificationRepresentationModel classificationRepModel)
      throws InvalidArgumentException, ClassificationAlreadyExistException, DomainNotFoundException,
          MalformedServiceLevelException, NotAuthorizedException {
    Classification classification =
        classificationModelAssembler.toEntityModel(classificationRepModel);
    classification = classificationService.createClassification(classification);

    return new CreateClassificationPayload(
        "Classification successfully created",
        classificationModelAssembler.toModel(classification));
  }

  @GraphQLMutation(
      name = "updateClassification",
      description = "This endpoint updates a Classification.")
  public UpdateClassificationPayload updateClassification(
      @GraphQLArgument(name = "classificationId") String id,
      @GraphQLArgument(name = "classification")
          ClassificationRepresentationModel classificationRepModel)
      throws InvalidArgumentException, ConcurrencyException, ClassificationNotFoundException,
          MalformedServiceLevelException, NotAuthorizedException {

    Classification classification =
        classificationModelAssembler.toEntityModel(classificationRepModel);
    if (classification.getId() != null && !id.equals(classification.getId())) {
      throw new InvalidArgumentException(
          String.format(
              "ClassificationId ('%s') of the URI is not identical"
                  + " with the classificationId ('%s') of the object in the payload.",
              id, classification.getId()));
    }

    classification = classificationService.updateClassification(classification);
    return new UpdateClassificationPayload(
        "Classification successfully updated",
        classificationModelAssembler.toModel(classification));
  }

  @GraphQLMutation(
      name = "deleteClassification",
      description = "This endpoint deletes a requested Classification if possible.")
  public DeleteClassificationPayload deleteClassification(
      @GraphQLArgument(name = "classificationId") String id)
      throws ClassificationInUseException, ClassificationNotFoundException, NotAuthorizedException {
    classificationService.deleteClassification(id);
    return new DeleteClassificationPayload("Classification successfully deleted", id);
  }
}
