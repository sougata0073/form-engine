export class StringUtil {

  static equalsAny(source: string | null | undefined, targets: string[]): boolean {
    for (const target of targets) {
      if (source === target) {
        return true;
      }
    }
    return false;
  }

}
