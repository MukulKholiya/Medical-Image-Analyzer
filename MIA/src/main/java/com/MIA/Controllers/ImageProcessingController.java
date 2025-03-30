package com.MIA.Controllers;

import com.MIA.Helper.ImageProcessingUtils;
import com.MIA.Service.ImageProcessingService;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/process")
public class ImageProcessingController {
    @Autowired
    public ImageProcessingUtils imageProcessingUtils;

    @Autowired
    public ImageProcessingService imageProcessingService;

    @GetMapping()
    public ResponseEntity<?> performOperations(@RequestParam("url") String url,
                                               @RequestParam("type") String type,
                                               @RequestParam(value = "operation", required = false) String operation) {
        try {
            // Download and convert image
            byte[] imageBytes = imageProcessingUtils.downloadImage(url);
            Mat image = imageProcessingUtils.convertToMat(imageBytes);

            Mat processedImage;
            if (operation == null || operation.isEmpty()) {
                // Full pipeline processing
                processedImage = imageProcessingService.processImagePipeline(url,type);
            } else {
                // Apply specific operation
                switch (operation.toLowerCase()) {
                    case "grayscale":
                        processedImage = imageProcessingService.convertToGray(image);
                        break;
                    case "blur":
                        processedImage = imageProcessingService.applyGaussianBlur(image);
                        break;
                    case "histogram":
                        processedImage = imageProcessingService.applyHistogramEqualization(image);
                        break;
                    case "threshold":
                        processedImage = imageProcessingService.applyThreshold(image);
                        break;
                    case "opening":
                        processedImage = imageProcessingService.applyOpening(image);
                        break;
                    case "closing":
                        processedImage = imageProcessingService.applyClosing(image);
                        break;
                    case "canny":
                        processedImage = imageProcessingService.applyCannyEdgeDetection(image);
                        break;
                    case "find_contours":
                        processedImage = imageProcessingService.extractROI(image, imageProcessingService.findContours(image));
                        break;
                    default:
                        return new ResponseEntity<>("Invalid operation", HttpStatus.BAD_REQUEST);
                }
            }

            // Convert processed image to byte array
            byte[] processedImageBytes = imageProcessingUtils.convertMatToBytes(processedImage);

            // Set headers for image response
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");

            return new ResponseEntity<>(processedImageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error processing image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
