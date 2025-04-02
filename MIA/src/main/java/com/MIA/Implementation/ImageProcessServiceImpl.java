package com.MIA.Implementation;

import com.MIA.Service.ImageProcessingService;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class ImageProcessServiceImpl implements ImageProcessingService {

    @Override
    public Mat loadImage(String filePath) {
        try {
            if (filePath.startsWith("http")) {
                Path tempFilePath = Paths.get("temp_image.jpg");
                try (InputStream in = new URL(filePath).openStream()) {
                    Files.copy(in, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                }
                Mat image = imread(tempFilePath.toString());
                Files.deleteIfExists(tempFilePath); // Delete after loading
                return image;
            }
            return imread(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Mat();
        }
    }

    @Override
    public Mat convertToGray(Mat image) {
        Mat grayImg = new Mat();
        cvtColor(image, grayImg, COLOR_BGR2GRAY);
        return grayImg;
    }

    public Mat applyHistogramEqualization(Mat image) {
        Mat equalized = new Mat();
        equalizeHist(image, equalized);
        return equalized;
    }

    @Override
    public Mat applyGaussianBlur(Mat image) {
        Mat blurredImage = new Mat();
        GaussianBlur(image, blurredImage, new Size(5, 5), 0);
        return blurredImage;
    }

    public Mat applyMedianBlur(Mat image) {
        Mat medianBlurred = new Mat();
        medianBlur(image, medianBlurred, 5);
        return medianBlurred;
    }

    @Override
    public Mat applyThreshold(Mat image) {
        Mat thresholded = new Mat();
        threshold(image, thresholded, 100, 255, THRESH_BINARY);
        return thresholded;
    }

    public Mat applyCannyEdgeDetection(Mat image) {
        Mat edges = new Mat();
        Canny(image, edges, 100, 200);
        return edges;
    }

    @Override
    public Mat applyErosion(Mat image) {
        Mat eroded = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(3, 3));
        erode(image, eroded, kernel);
        return eroded;
    }

    @Override
    public Mat applyDilation(Mat image) {
        Mat dilated = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(3, 3));
        dilate(image, dilated, kernel);
        return dilated;
    }

    @Override
    public Mat applyOpening(Mat image) {
        Mat opened = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(3, 3));
        morphologyEx(image, opened, MORPH_OPEN, kernel);
        return opened;
    }

    @Override
    public Mat applyClosing(Mat image) {
        Mat closed = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(3, 3));
        morphologyEx(image, closed, MORPH_CLOSE, kernel);
        return closed;
    }

    public MatVector findContours(Mat image) {
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        opencv_imgproc.findContours(image, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public Mat extractROI(Mat image, MatVector contours) {
        Mat roi = image.clone();
        drawContours(roi, contours, -1, new Scalar(255, 0, 0, 255));
        return roi;
    }

    @Override
    public String saveImage(Mat image, String outputPath) {
        boolean result = imwrite(outputPath, image);
        return result ? outputPath : null;
    }
    @Override
    public Mat processImagePipeline(String filePath, String imageType) {
        Mat image = loadImage(filePath);
        image = convertToGray(image);

        switch (imageType.toLowerCase()) {
            case "ct":
                image = applyGaussianBlur(image);
                image = applyHistogramEqualization(image);
                break;

            case "xray":
                image = applyHistogramEqualization(image);
//                image = applyThreshold(image);
//                image = applyCannyEdgeDetection(image);
                break;

            case "mri":
                image = applyGaussianBlur(image);
                image = applyOpening(image);
                image = applyClosing(image);
                break;
        }

        return image;
    }

}
