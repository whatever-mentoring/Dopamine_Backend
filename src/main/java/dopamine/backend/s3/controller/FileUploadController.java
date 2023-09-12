package dopamine.backend.s3.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        // File Path 설정
        String entityName = "level";
        String columnName = "image";
        String uploadFilePath = entityName + "/" + columnName;

        // File 이름 설정
        String currentTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(System.currentTimeMillis());
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("no name");
        String uploadFileName = getUuidFileName(fileName) + currentTime;

        // Object MetaData
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            String keyName = uploadFilePath + "/" + uploadFileName;

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(new PutObjectRequest(bucket, keyName, inputStream, metadata));

            String uploadFileUrl = amazonS3Client.getUrl(bucket, keyName).toString();
            return ResponseEntity.ok(uploadFileUrl);

        } catch(IOException e) {
            e.printStackTrace();
            log.error("Failed to upload file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }
}