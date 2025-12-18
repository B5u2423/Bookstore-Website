package dev.vubl.bookstore.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
  private final Cloudinary cloudinary;

  public String uploadImage(MultipartFile multipartFile) throws IOException {
    Map result =
        cloudinary
            .uploader()
            .upload(multipartFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
    return (String) result.get("secure_url");
  }
}
