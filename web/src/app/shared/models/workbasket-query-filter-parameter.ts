import { WorkbasketType } from './workbasket-type';
import { WorkbasketPermission } from './workbasket-permission';

export interface WorkbasketQueryFilterParameter {
  name?: string[];
  'nameLike'?: string[];
  key?: string[];
  'keyLike'?: string[];
  owner?: string[];
  'ownerLike'?: string[];
  'descriptionLike'?: string[];
  domain?: string[];
  type?: WorkbasketType[];
  'requiredPermission'?: WorkbasketPermission[];
}
