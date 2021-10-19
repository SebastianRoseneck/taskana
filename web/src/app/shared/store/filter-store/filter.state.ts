import { Action, NgxsOnInit, State, StateContext } from '@ngxs/store';
import { Observable, of } from 'rxjs';
import { WorkbasketQueryFilterParameter } from '../../models/workbasket-query-filter-parameter';
import { ClearTaskFilter, ClearWorkbasketFilter, SetTaskFilter, SetWorkbasketFilter } from './filter.actions';
import { TaskQueryFilterParameter } from '../../models/task-query-filter-parameter';

const emptyWorkbasketFilter: WorkbasketQueryFilterParameter = {
  'descriptionLike': [],
  'keyLike': [],
  'nameLike': [],
  'ownerLike': [],
  type: []
};

const emptyTaskFilter: TaskQueryFilterParameter = {
  'nameLike': [],
  'ownerLike': [],
  state: [],
  priority: [],
  'porValue': [],
  'wildcardSearchFields': [],
  'wildcardSearchValue': []
};

@State<FilterStateModel>({ name: 'FilterState' })
export class FilterState implements NgxsOnInit {
  @Action(SetWorkbasketFilter)
  setWorkbasketFilter(ctx: StateContext<FilterStateModel>, action: SetWorkbasketFilter): Observable<null> {
    const currentState = ctx.getState()[action.component];
    const param = action.parameters;
    const filter: WorkbasketQueryFilterParameter = {
      'descriptionLike': param['descriptionLike'] ? [...param['descriptionLike']] : currentState['descriptionLike'],
      'keyLike': param['keyLike'] ? [...param['keyLike']] : currentState['keyLike'],
      'nameLike': param['nameLike'] ? [...param['nameLike']] : currentState['nameLike'],
      'ownerLike': param['ownerLike'] ? [...param['ownerLike']] : currentState['ownerLike'],
      type: param['type'] ? [...param['type']] : currentState['type']
    };

    ctx.setState({
      ...ctx.getState(),
      [action.component]: filter
    });

    return of(null);
  }

  @Action(ClearWorkbasketFilter)
  clearWorkbasketFilter(ctx: StateContext<FilterStateModel>, action: ClearWorkbasketFilter): Observable<null> {
    ctx.setState({
      ...ctx.getState(),
      [action.component]: { ...emptyWorkbasketFilter }
    });

    return of(null);
  }

  @Action(SetTaskFilter)
  setTaskFilter(ctx: StateContext<FilterStateModel>, action: SetTaskFilter): Observable<null> {
    const param = action.parameters;
    let filter = { ...ctx.getState().tasks };

    Object.keys(param).forEach((key) => {
      filter[key] = [...param[key]];
    });

    const isWildcardSearch = filter['wildcardSearchValue'].length !== 0 && filter['wildcardSearchValue'] !== [''];
    filter['wildcardSearchFields'] = isWildcardSearch ? this.initWildcardFields() : [];

    // Delete wildcard search field 'NAME' if 'name-like' exists
    if (filter['nameLike'].length > 0 && filter['nameLike'][0] !== '') {
      filter['wildcardSearchFields'].shift();
    }

    ctx.setState({
      ...ctx.getState(),
      tasks: filter
    });

    return of(null);
  }

  @Action(ClearTaskFilter)
  clearTaskFilter(ctx: StateContext<FilterStateModel>): Observable<null> {
    ctx.setState({
      ...ctx.getState(),
      tasks: { ...emptyTaskFilter }
    });

    return of(null);
  }

  initWildcardFields() {
    let wildcardSearchFields = ['NAME', 'DESCRIPTION'];
    [...Array(16).keys()].map((number) => {
      wildcardSearchFields.push(`CUSTOM_${number + 1}`);
    });
    return wildcardSearchFields;
  }

  ngxsOnInit(ctx: StateContext<FilterStateModel>): void {
    ctx.setState({
      ...ctx.getState(),
      availableDistributionTargets: emptyWorkbasketFilter,
      selectedDistributionTargets: emptyWorkbasketFilter,
      workbasketList: emptyWorkbasketFilter,
      tasks: emptyTaskFilter
    });
  }
}

export interface FilterStateModel {
  availableDistributionTargets: WorkbasketQueryFilterParameter;
  selectedDistributionTargets: WorkbasketQueryFilterParameter;
  workbasketList: WorkbasketQueryFilterParameter;
  tasks: TaskQueryFilterParameter;
}
