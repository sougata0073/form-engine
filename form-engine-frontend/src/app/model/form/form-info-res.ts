export interface FormInfoRes {
  id: string,
  name: string,
  title: string,
  description: string,
  published: boolean,
  acceptingResponse: boolean,
  notAcceptingResponseMessage: string | null,
  stopAcceptingResponseOn: string,
  stopAcceptingResponseAfterResponse: number,
}
