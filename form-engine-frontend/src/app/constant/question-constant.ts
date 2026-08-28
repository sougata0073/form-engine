import {ValidationIdMeta} from '../type/validation-id-meta';
import {ValueLabelPair} from '../type/value-label-pair';
import {ValidationId} from '../type/validation-id';

export abstract class QuestionConstant<AVI> {

  VALIDATION_ID_METAS: ReadonlyArray<ValidationIdMeta<AVI>> = []

  get DEFAULT_VALIDATION_ID_META() {
    return this.VALIDATION_ID_METAS[0]
  }

  getInputs(): ReadonlyArray<ValueLabelPair> {
    const map = new Map<string, ValueLabelPair>()
    this.VALIDATION_ID_METAS.forEach(val => {
      map.set(val.input.value, val.input)
    })
    return Array.from(map.values())
  }

  getValidationTypes(inputValue?: string): ReadonlyArray<ValueLabelPair> {
    return this.VALIDATION_ID_METAS
      .filter(val => inputValue ? val.input.value === inputValue : true)
      .map(val => val.validation)
  }

  getByValidationId(validationId: ValidationId): ValidationIdMeta<AVI> {
    return this.VALIDATION_ID_METAS.find(val => val.validationId === validationId)!
  }
}
