package az.orient.bankdemo.service;

import az.orient.bankdemo.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(Long fileId) throws Exception;
}
