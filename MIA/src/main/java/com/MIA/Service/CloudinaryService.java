package com.MIA.Service;

import com.MIA.Configuration.CloudinaryConfig;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudinaryService {
    String uploadImage(MultipartFile imageFile,String fileName);
    String getUrlFromPublicId(String publicId);
}
