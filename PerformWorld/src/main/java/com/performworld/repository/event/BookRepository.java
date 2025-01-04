package com.performworld.repository.event;

import com.performworld.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Booking, Long>, BookCustomRepo {

}
