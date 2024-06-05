package org.javaacademy.afisha.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class TicketRepository {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  private int createTicketToMuseum(String eventName,
                                   String email,
                                   BigDecimal price,
                                   boolean isSelled) {
    AtomicInteger salesTicket = new AtomicInteger(0);
    String sql =
        """
          INSERT INTO application.ticket (event_id, client_email, price, is_selled) values (
          (SELECT id FROM application.event WHERE id =
          (select id FROM application.event WHERE name = ? ORDER BY ID DESC LIMIT 1)),
          ?,
          ?,
          ?);
        """;
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      salesTicket.set(jdbcTemplate.update(sql, preparedStatement -> {
        preparedStatement.setString(1, eventName);
        preparedStatement.setString(2, email);
        preparedStatement.setBigDecimal(3, price);
        preparedStatement.setBoolean(4, isSelled);
      }));
    });
    int result = salesTicket.get();
    return result;
  }

  public Ticket getTicketById(Integer id) {
    String sql = "SELECT * FROM application.ticket WHERE id = ?";
    Ticket ticketResult = jdbcTemplate.query(
        sql,
        preparedStatement -> preparedStatement.setInt(1, id),
        this::ticketRowMapper)
        .stream()
        .findFirst()
        .orElseThrow();
    return ticketResult;
  }

  public List<Ticket> fundAllTicket() {
    String sql = "SELECT * FROM application.ticket WHERE is_selled = true";
    List<Ticket> tickets = jdbcTemplate.query(
        sql,
        this::ticketRowMapper);
    return tickets;
  }

  public int salesByTicket(String email, String nameEvent, int qty, BigDecimal price) {
    if (nameEvent.equals("museum")) {
      return createTicketToMuseum(nameEvent, email, price, true);
    }
    return salesByTicketToCinemaAndTheatre(email, nameEvent, qty);
  }

  private Ticket ticketRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
    Ticket ticket = new Ticket();
    ticket.setId(resultSet.getInt("id"));
    ticket.setEventId(resultSet.getInt("event_id"));
    ticket.setClientEmail(resultSet.getString("client_email"));
    ticket.setIsSelled(resultSet.getBoolean("is_selled"));
    return ticket;
  }

  private int salesByTicketToCinemaAndTheatre(String email, String nameEvent, int qty) {
    AtomicInteger salesTicket = new AtomicInteger(0);
    String sql = """
            Update application.ticket set is_selled = true, client_email = ?
            where id in (select id
            from application.ticket
            where event_id in (select id from application.event_type where name = ? 
            AND is_selled = false)
            ORDER BY id desc limit ?);
            """;
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      salesTicket.set(jdbcTemplate.update(sql, ps -> {
        ps.setString(1, email);
        ps.setString(2, nameEvent);
        ps.setInt(3, qty);
      }));
    });
    int result = salesTicket.get();
    salesTicket.set(0);
    return result;
  }
}
