export class ArrayUtil {

  static includesAll<T>(sourceArray: T[], ...searchElements: T[]): boolean {
    const sourceSet = new Set(sourceArray)
    for (let element of searchElements) {
      if (!sourceSet.has(element)) {
        return false
      }
    }
    return true
  }

  static toArray<T>(iterator: MapIterator<T>) {
    return [...iterator];
  }

  static fillByNumbers(from: number, to: number): number[] {
    const res: number[] = []
    for (let i = from; i <= to; i++) {
      res.push(i)
    }
    return res
  }

}
