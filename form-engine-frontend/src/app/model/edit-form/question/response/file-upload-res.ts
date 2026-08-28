import {QuestionRes} from './question-res';
import {FileType} from '../../../../type/file-type';

export interface OnlyFileUploadRes {
  allowedFileTypes: FileType[],
  maxFileSize: number
}

export interface FileUploadRes extends OnlyFileUploadRes, QuestionRes {
}
