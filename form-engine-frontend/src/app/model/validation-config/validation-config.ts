import {ValidationId} from '../../type/validation-id';

export interface ValidationConfig {
  validationId: ValidationId,
  errorText: string | null
}
