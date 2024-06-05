package org.javaacademy.afisha.service;

import lombok.AllArgsConstructor;
import org.javaacademy.afisha.entity.Report;
import org.javaacademy.afisha.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
  private final ReportRepository reportRepository;

  public List<Report> findAllReports() {
    return reportRepository.findAllSelledTicketsToReport();
  }
}
