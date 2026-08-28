import {ResponseIndividual} from './response-individual';

export interface FileUploadResponseIndividual extends ResponseIndividual {
  fileName: string,
  fileUrl: string,
  fileMimeType: string
}
