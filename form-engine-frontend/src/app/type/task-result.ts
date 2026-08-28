export class TaskResult<T> {
  constructor(
    public data: T,
    public isSuccessful: boolean,
    public message: string
  ) {
  }

  static success<T>(data: T, message?: string) {
    return new TaskResult(data, true, message ?? 'Task successful')
  }

  static failed(message?: string) {
    return new TaskResult(null, false, message ?? 'Task failed')
  }
}
