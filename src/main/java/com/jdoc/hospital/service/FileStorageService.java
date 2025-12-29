package com.jdoc.hospital.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

  private final Path root;

  public FileStorageService(@Value("${app.uploadsDir}") String uploadsDir) {
    this.root = Path.of(uploadsDir).toAbsolutePath().normalize();
  }

  public String store(MultipartFile file) {
    if (file == null || file.isEmpty()) return null;
    try {
      Files.createDirectories(root);
      String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
      String safeName = UUID.randomUUID() + "_" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
      Path dest = root.resolve(safeName);
      Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
      return "/uploads/" + safeName;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to store file", e);
    }
  }
}
