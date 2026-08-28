import {ValidationId} from './validation-id';
import {ValueLabelPair} from './value-label-pair';

export interface ValidationIdMeta<AVI> {
  validationId: ValidationId,
  input: ValueLabelPair
  validation: ValueLabelPair
  activeValidationInputId: AVI
}
