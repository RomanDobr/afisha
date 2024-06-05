package org.javaacademy.afisha.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventRepository {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  public void createEvent(String nameEvent, String namePlace, BigDecimal price, int qty) {
    String sql = """
          INSERT INTO application.event (name, event_type_id, event_date, place_id) values (
          ?,
          (SELECT id FROM application.event_type WHERE name = ?),
          ?,
          (SELECT id FROM application.place WHERE name = ?));
        """;
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      int update = jdbcTemplate.update(sql, preparedStatement -> {
        preparedStatement.setString(1, nameEvent);
        preparedStatement.setString(2, nameEvent);
        preparedStatement.setString(3, LocalDateTime.now().toString());
        preparedStatement.setString(4, namePlace);
      });
      if (!nameEvent.equals("museum")) {
        jdbcTemplate.execute(createTickets());
        jdbcTemplate.execute("call application.create_tickets(%s, '%s', %s);"
                .formatted(qty, nameEvent, price));
      }
    });
  }

  public Event getEventById(Integer id) {
    String sql = "SELECT * FROM application.event WHERE id = ?";
    Event eventResult = jdbcTemplate.query(
        sql,
        preparedStatement -> preparedStatement.setInt(1, id),
        this::eventRowMapper)
        .stream()
        .findFirst()
        .orElseThrow();
    return eventResult;
  }

  public List<Event> findAllEvents() {
    String sql = "SELECT * FROM application.event";
    List<Event> events = jdbcTemplate.query(
        sql,
        this::eventRowMapper);
    return events;
  }

  private Event eventRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
    Event event = new Event();
    event.setId(resultSet.getInt("id"));
    event.setName(resultSet.getString("name"));
    event.setEventTypeId(resultSet.getInt("event_type_id"));
    event.setEventDate(LocalDateTime.parse(resultSet.getString("event_date")));
    event.setPlaceId(resultSet.getInt("place_id"));
    return event;
  }

  private String createTickets() {
    String sql = """
        CREATE OR REPLACE PROCEDURE application.create_tickets(count_tickets int, event_type_name varchar(100), price numeric(7,2))
        LANGUAGE plpgsql
        AS $$
        DECLARE i int;
        begin
            FOREACH i IN ARRAY(SELECT ARRAY(SELECT * FROM generate_series(1, count_tickets)))
            loop
            INSERT INTO application.ticket (event_id, price)
            VALUES (
                (SELECT id FROM application.event WHERE name = event_type_name),
                price);
        end loop;
        END;
        $$;
        """;
    return sql;
  }
}
