package org.javaacademy.afisha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.dto.ReportDto;
import org.javaacademy.afisha.entity.Report;
import org.javaacademy.afisha.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@Tag(name = "Сервис по работе с отчетами по мероприятиям",
        description = "Методы по работе с отчетами")
public class ReportAfishaController {
  private final ReportService reportService;

  @GetMapping()
  @Operation(summary = "Поиск всех отчетов")
  @ApiResponse(responseCode = "200",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ReportDto.class)))
  public ResponseEntity<List<ReportDto>> getAllReports() {
    return ResponseEntity.status(200).body(getTicketDtoRs(reportService.findAllReports()));
  }

  private ReportDto reportToDtoConvert(Report report) {
    return new ReportDto(
            report.getNameEvent(),
            report.getEventTypeId(),
            report.getQty(),
            report.getSumSelledTickets());
  }

  private List<ReportDto> getTicketDtoRs(List<Report> reports) {
    return reports.stream().map(this::reportToDtoConvert).toList();
  }
}
