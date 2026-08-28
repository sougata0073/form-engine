import {isEqual} from 'lodash'

export class ObjectUtil {

  static areMatchingFieldsSame(obj1: any, obj2: any): boolean {
    for (const key of Object.keys(obj1)) {
      if (key in obj2 && !isEqual(obj1[key], obj2[key])) {
        return false
      }
    }
    return true
  }

}
