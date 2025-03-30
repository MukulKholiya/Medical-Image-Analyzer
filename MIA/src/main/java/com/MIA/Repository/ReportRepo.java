package com.MIA.Repository;

import com.MIA.Entity.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepo extends MongoRepository<Report,String> {
    Report findReportByReportId(String reportId);
}
