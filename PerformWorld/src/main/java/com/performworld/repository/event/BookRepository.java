package com.performworld.repository.event;

import com.performworld.domain.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Ticketing, Long>, BookCustomRepo {

}
