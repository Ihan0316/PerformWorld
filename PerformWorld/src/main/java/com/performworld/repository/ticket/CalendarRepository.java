package com.performworld.repository.ticket;

import com.performworld.dto.ticket.CalendarEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class CalendarRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CalendarEventDTO> findBookedDatesAndTitlesByUserIdAndStatus(String userId, String status) {
        // 수정된 쿼리
        String sql = "SELECT es.event_date, e.title " +
                "FROM event_schedules es " +
                "JOIN bookings b ON es.schedule_id = b.schedule_id " +
                "JOIN events e ON es.event_id = e.event_id " +
                "WHERE b.user_id = ? AND b.status = ?";

        // 쿼리 실행 후 결과를 CalendarEventDTO 객체로 매핑하여 반환
        return jdbcTemplate.query(sql, new Object[]{userId, status}, new RowMapper<CalendarEventDTO>() {
            @Override
            public CalendarEventDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                // event_date를 LocalDateTime으로 변환
                LocalDateTime dateTime = rs.getTimestamp("event_date").toLocalDateTime();
                String title = rs.getString("title");

                // LocalDateTime을 "yyyy-MM-dd HH:mm:ss.SSSSSS" 형식으로 변환
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                String formattedDate = dateTime.format(formatter);

                // CalendarEventDTO 객체로 반환
                return new CalendarEventDTO(formattedDate, title);
            }
        });
    }
}