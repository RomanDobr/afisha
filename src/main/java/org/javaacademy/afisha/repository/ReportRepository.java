package org.javaacademy.afisha.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Report;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportRepository {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  public List<Report> findAllSelledTicketsToReport() {
    String sql = """
        WITH fullticket AS (
            SELECT name, event_type_id, count(*)
            FROM application.ticket ti JOIN application.event ev on ti.event_id = ev.id
            GROUP BY name, event_type_id
            ), selledticket AS (
            SELECT name, event_type_id, count(*) AS counts, sum(price) AS sum
            FROM application.ticket ti JOIN application.event ev ON ti.event_id = ev.id
            WHERE ti.is_selled = true
            GROUP BY name, event_type_id)
        SELECT fl.name, fl.event_type_id, coalesce(st.counts, 0) AS counts, coalesce(st.sum, 0) AS sum
        FROM fullticket fl LEFT JOIN selledticket st on fl.event_type_id = st.event_type_id;
        """;
    return jdbcTemplate.query(
            sql,
            this::reportRowMapper);
  }

  private Report reportRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
    Report report = new Report();
    report.setNameEvent(resultSet.getString("name"));
    report.setEventTypeId(resultSet.getInt("event_type_id"));
    report.setQty(resultSet.getInt("counts"));
    report.setSumSelledTickets(resultSet.getBigDecimal("sum"));
    return report;
  }
}
