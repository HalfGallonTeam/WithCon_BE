package com.halfgallon.withcon.global.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String uploadFile(MultipartFile file);

}
