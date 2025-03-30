package com.MIA.Implementation;

import com.MIA.Entity.Report;
import com.MIA.Repository.ReportRepo;
import com.MIA.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ReportImpl implements ReportService {
    @Autowired
    public ReportRepo reportRepo;
    @Override
    public Report saveReport(Report report) {
        report.setReportId(UUID.randomUUID().toString());
        report.setReportDate(LocalDateTime.now());
        return reportRepo.save(report);
    }

    @Override
    public Report getReportByReportId(String reportId) {
        return reportRepo.findReportByReportId(reportId);
    }
}
