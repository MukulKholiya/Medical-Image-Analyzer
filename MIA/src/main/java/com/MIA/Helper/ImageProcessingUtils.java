package com.MIA.Helper;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
@Component
public class ImageProcessingUtils {

    // Convert byte array to OpenCV Mat
    public Mat convertToMat(byte[] imageBytes) {
        return imdecode(new Mat(new BytePointer(imageBytes)), IMREAD_COLOR);
    }

    // Convert Mat back to byte array
    public byte[] convertMatToBytes(Mat mat) throws IOException {
        BytePointer buffer = new BytePointer();
        try {
            imencode(".png", mat, buffer);
            byte[] imageBytes = new byte[(int) buffer.limit()];
            buffer.get(imageBytes);
            return imageBytes;
        } finally {
            buffer.deallocate();  // Prevent memory leaks
        }
    }

    public byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }
}
