package com.performworld.repository;

import com.performworld.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long>, TestSearch {

}
