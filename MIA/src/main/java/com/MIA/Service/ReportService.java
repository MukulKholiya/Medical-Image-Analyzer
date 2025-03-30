package com.MIA.Service;

import com.MIA.Entity.Report;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportService {
    Report saveReport(Report report);
    Report getReportByReportId(String reportId);
//    List<String> getResultByReportId(String reportId);
}
