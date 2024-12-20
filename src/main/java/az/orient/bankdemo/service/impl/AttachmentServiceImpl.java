package az.orient.bankdemo.service.impl;

import az.orient.bankdemo.entity.Attachment;
import az.orient.bankdemo.repository.AttachmentRepository;
import az.orient.bankdemo.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence" + fileName);
            }
            Attachment attachment = new Attachment(fileName, file.getContentType(), file.getBytes());
            return attachmentRepository.save(attachment);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Couldn't save File" + fileName+"Error:"+ex.getMessage());
        }
    }

    @Override
    public Attachment getAttachment(Long fileId) throws Exception {
        return attachmentRepository.findById(fileId).orElseThrow(() -> new Exception("File not found with Id: " + fileId));
    }
}
