package com.halfgallon.withcon.global.storage.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.INTERVAL_SERVER_ERROR;

import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.storage.service.StorageService;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsS3Service implements StorageService {

  private final S3Template s3Template;

  @Value("${aws.s3.bucketName}")
  private String bucketName;

  @Override
  public String uploadFile(MultipartFile file) {
    String originalFileName = file.getOriginalFilename();
    String extension = StringUtils.getFilenameExtension(originalFileName);
    String fileName = UUID.randomUUID() + "." + extension;

    ObjectMetadata objectMetadata = ObjectMetadata.builder()
        .contentType(file.getContentType())
        .contentLength(file.getSize())
        .build();

    try (InputStream inputStream = file.getInputStream()) {
      return s3Template.upload(bucketName, fileName, inputStream, objectMetadata).getURL()
          .toString();
    } catch (IOException e) {
      throw new CustomException(INTERVAL_SERVER_ERROR);
    }
  }
}
