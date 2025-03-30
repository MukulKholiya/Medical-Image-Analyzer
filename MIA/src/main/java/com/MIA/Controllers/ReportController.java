package com.MIA.Controllers;

import com.MIA.Entity.Report;
import com.MIA.Service.CloudinaryService;
import com.MIA.Service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {
    public Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    public ReportService reportService;
    @Autowired
    public CloudinaryService cloudinaryService;

    @GetMapping("/health-check")
    public String health(){
        return "Health-Check !!";
    }
    @Async
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<?>> saveReport(
            @RequestPart("report") String reportJson,
            @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            // Convert JSON string to Report object
            ObjectMapper objectMapper = new ObjectMapper();
            Report report = objectMapper.readValue(reportJson, Report.class);

            if (!imageFile.isEmpty()) {
                String url = cloudinaryService.uploadImage(imageFile, report.getReportId());
                logger.info(url);
                report.setReportUrl(url);

                Report savedReport = reportService.saveReport(report);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CREATED).body(savedReport));
            }
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please add an image file"));

        } catch (Exception e) {
            throw new RuntimeException("Error uploading report", e);
        }
    }

}
