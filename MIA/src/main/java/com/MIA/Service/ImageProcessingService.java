package com.MIA.Service;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageProcessingService {
    Mat loadImage(String filePath);
    Mat convertToGray(Mat image);
    Mat applyGaussianBlur(Mat image);
    Mat applyThreshold(Mat image);
    Mat applyHistogramEqualization(Mat image);
    Mat applyErosion(Mat image);
    Mat applyDilation(Mat image);
    Mat applyOpening(Mat image);
    Mat applyClosing(Mat image);
    Mat applyCannyEdgeDetection(Mat image);
    MatVector findContours(Mat image);
    Mat extractROI(Mat image, MatVector contours);
    Mat processImagePipeline(String filePath,String imageType);

    String saveImage(Mat image, String outputPath);


}
