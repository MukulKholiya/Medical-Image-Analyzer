package com.MIA.Implementation;

import com.MIA.Service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class CloudinaryImpl implements CloudinaryService {

    @Autowired
    public Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile profileImage, String filename) {
        if (profileImage.isEmpty()) {
            throw new RuntimeException("Empty file");
        }
        try {
            byte[] data = profileImage.getBytes();

            // Upload image and get response
            Map uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", filename
            ));

            // Get URL directly from the response
            String url = (String) uploadResult.get("secure_url"); // Always prefer "secure_url"

            // Log and return the URL
            System.out.println("Uploaded Image URL: " + url);
            return url;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url()
                .publicId(publicId)
                .generate();
    }

}
