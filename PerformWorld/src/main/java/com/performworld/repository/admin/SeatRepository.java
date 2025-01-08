package com.performworld.repository.admin;

import com.performworld.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {
    @Query("SELECT DISTINCT s.section, s.price FROM Seat s")
    List<Object[]> findDistinctSectionsAndPrices();

    @Modifying
    @Query("UPDATE Seat s SET s.price = :price WHERE s.section = :section")
    void updatePriceBySection(@Param("section") String section, @Param("price") Long price);
}