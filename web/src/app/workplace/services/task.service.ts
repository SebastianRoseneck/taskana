import {Task} from 'app/workplace/models/task';
import {Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {TaskResource} from 'app/workplace/models/task-resource';
import {Sorting, TaskQuerySortParameter} from 'app/shared/models/sorting';
import {StartupService} from '../../shared/services/startup/startup.service';
import {TaskQueryFilterParameter} from '../../shared/models/task-query-filter-parameter';
import {QueryPagingParameter} from '../../shared/models/query-paging-parameter';

@Injectable()
export class TaskService {
  private taskChangedSource = new Subject<Task>();
  taskChangedStream = this.taskChangedSource.asObservable();
  private taskSelectedSource = new Subject<Task>();
  taskSelectedStream = this.taskSelectedSource.asObservable();
  private taskDeletedSource = new Subject<Task>();
  taskDeletedStream = this.taskDeletedSource.asObservable();
  private query: string;

  constructor(private httpClient: HttpClient, private startupService: StartupService) {
  }

  get url(): string {
    return this.startupService.getTaskanaRestUrl() + '/graphql';
  }

  publishUpdatedTask(task?: Task) {
    this.taskChangedSource.next(task);
  }

  publishTaskDeletion() {
    this.taskDeletedSource.next(null);
  }

  selectTask(task?: Task) {
    this.taskSelectedSource.next(task);
  }

  getSelectedTask(): Observable<Task> {
    return this.taskSelectedStream;
  }

  findTasksWithWorkbasket(
      filterParameter: TaskQueryFilterParameter,
      sortParameter: Sorting<TaskQuerySortParameter>,
      pagingParameter: QueryPagingParameter
  ): Observable<TaskResource> {
    this.query = `query{getTasks(pagingParameter:{${pagingParameter},filterParameter:{${filterParameter},sortParameter:{${sortParameter}{content {
  attachmentSummaries {
    attachmentId,channel
    classificationSummary {
      applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type
    }
    created,modified,objectReference {
      company,id,system,systemInstance,type,value
    }
    received,taskId
  },
  businessProcessId,claimed,
  classificationSummary {applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type},
  completed,created,creator,custom1,custom10,custom11,custom12,custom13,custom14,custom15,custom16,custom2,custom3,custom4,custom5,custom6,custom7,custom8,custom9,description,due,externalId,modified,name,note,owner,parentBusinessProcessId,planned,
  primaryObjRef {company,id,system,systemInstance,type,value},
  priority,read,received,state,taskId,transferred,
  workbasketSummary {custom1,custom2,custom3,custom4,description,domain,key,markedForDeletion,name,orgLevel1,orgLevel2,orgLevel3,orgLevel4,owner,type,workbasketId}
}}}`
    return this.httpClient.post<TaskResource>(`${this.url}`, this.query);
  }

  getTask(id: string): Observable<Task> {
    this.query = `query{getTask(taskId:${id}){
  attachmentSummaries {
    attachmentId,channel
    classificationSummary {
      applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type
    }
    created,modified,objectReference {
      company,id,system,systemInstance,type,value
    }
    received,taskId
  },
  businessProcessId,claimed,
  classificationSummary {applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type},
  completed,created,creator,custom1,custom10,custom11,custom12,custom13,custom14,custom15,custom16,custom2,custom3,custom4,custom5,custom6,custom7,custom8,custom9,description,due,externalId,modified,name,note,owner,parentBusinessProcessId,planned,
  primaryObjRef {company,id,system,systemInstance,type,value},
  priority,read,received,state,taskId,transferred,
  workbasketSummary {custom1,custom2,custom3,custom4,description,domain,key,markedForDeletion,name,orgLevel1,orgLevel2,orgLevel3,orgLevel4,owner,type,workbasketId}
}}`
    return this.httpClient.post<Task>(this.url, this.query);
  }

  completeTask(id: string): Observable<Task> {
    this.query = `mutation{completeTask(taskId:${id}){message}}`
    this.httpClient.post<Task>(this.url, this.query);
    return this.getTask(id);
  }

  claimTask(id: string): Observable<Task> {
    this.query = `mutation{claimTask(taskId:${id}){message}}`
    this.httpClient.post<Task>(this.url, this.query);
    return this.getTask(id);
  }

  cancelClaimTask(id: string): Observable<Task> {
    this.query = `mutation{cancelClaimTask(taskId:${id}){message}}`
    this.httpClient.post<Task>(this.url, this.query);
    return this.getTask(id);
  }

  transferTask(taskId: string, workbasketId: string): Observable<Task> {
    this.query = `mutation{transferTask(taskId:${taskId},workbasketId:${workbasketId}){message}}`
    this.httpClient.post<Task>(this.url, this.query);
    return this.getTask(taskId);
  }

  updateTask(task: Task): Observable<Task> {
    const taskConv = TaskService.convertTasksDatesToGMT(task);
    this.query = `mutation{updateTask(taskId:${task.taskId},task:{${taskConv}}){message}`
    this.httpClient.post<Task>(this.url, this.query);
    return this.getTask(task.taskId);
  }

  deleteTask(task: Task): Observable<Task> {
    this.query = `mutation{deleteTask(taskId:${task.taskId}){message}}`
    return this.httpClient.post<Task>(this.url, this.query);
  }

  createTask(task: Task): Observable<Task> {
    this.query = `mutation{createTask(task:{${task}}{task {
  attachmentSummaries {attachmentId,channel
    classificationSummary {
      applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type
    },
    created,modified,
    objectReference {company,id,system,systemInstance,type,value},
    received,taskId
  },
  attachments {attachmentId,channel,
    classificationSummary {
      applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type
    },
    created,customAttributes,modified,
    objectReference {
      company,id,system,systemInstance,type,value
    },
    received,taskId
  },
  businessProcessId,
  callbackInfo {
    key,value
  },
  claimed,
  classificationSummary {
    applicationEntryPoint,category,classificationId,custom1,custom2,custom3,custom4,custom5,custom6,custom7,custom8,domain,key,name,parentId,parentKey,priority,serviceLevel,type
  },
  completed,created,creator,custom1,custom10,custom11,custom12,custom13,custom14,custom15,custom16,custom2,custom3,custom4,custom5,custom6,custom7,custom8,custom9,
  customAttributes {key,value},
  description,due,externalId,modified,name,note,owner,parentBusinessProcessId,planned,
  primaryObjRef {company,id,system,systemInstance,type,value},
  priority,read,received,state,taskId,transferred,
  workbasketSummary {custom1,custom2,custom3,custom4,description,domain,key,markedForDeletion,name,orgLevel1,orgLevel2,orgLevel3,orgLevel4,owner,type,workbasketId}
}}}`;

    return this.httpClient.post<Task>(this.url,this.query);
  }

  private static convertTasksDatesToGMT(task: Task): Task {
    const timeAttributes = ['created', 'claimed', 'completed', 'modified', 'planned', 'due'];
    timeAttributes.forEach((attributeName) => {
      if (task[attributeName]) {
        task[attributeName] = new Date(task[attributeName]).toISOString();
      }
    });
    return task;
  }
}
