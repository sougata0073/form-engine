import {FileType} from '../type/file-type';

export class FileUploadConstant {

  static FILE_TYPES: FileType[] = [
    {
      category: 'Document',
      mimeTypes: [
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'text/plain',
      ]
    },
    {
      category: 'Spreadsheet',
      mimeTypes: [
        'application/vnd.ms-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'text/csv',
      ]
    },
    {
      category: 'Presentation',
      mimeTypes: [
        'application/vnd.ms-powerpoint',
        'application/vnd.openxmlformats-officedocument.presentationml.presentation',
      ]
    },
    {
      category: 'Pdf',
      mimeTypes: ['application/pdf']
    },
    {
      category: 'Image',
      mimeTypes: [
        'image/jpeg',
        'image/png',
        'image/gif',
        'image/webp',
        'image/svg+xml',
      ]
    },
    {
      category: 'Video',
      mimeTypes: [
        'video/mp4',
        'video/webm',
        'video/ogg',
      ]
    },
    {
      category: 'Audio',
      mimeTypes: [
        'audio/mpeg',
        'audio/wav',
        'audio/ogg',
        'audio/mp4',
      ]
    },
    {
      category: 'Drawing',
      mimeTypes: [
        'image/svg+xml',
        'application/vnd.oasis.opendocument.graphics',
      ]
    },
  ];

  static ALLOWED_FILE_UPLOAD_SIZES = [1, 5, 10, 15, 20, 30, 50, 80, 100]
}
