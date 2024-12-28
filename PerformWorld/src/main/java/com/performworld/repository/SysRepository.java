package com.performworld.repository;

import com.performworld.domain.SystemCode;
import com.performworld.dto.SysCodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysRepository extends JpaRepository<SystemCode, Long> {

    @Query("SELECT new com.performworld.dto.SysCodeDTO" +
            "(s.codeId, s.mainCode, s.subCode, s.code, s.codeCate, s.codeName) " +
            "FROM SystemCode s WHERE s.mainCode = :mainCode ORDER BY s.subCode")
    List<SysCodeDTO> getSysList(String mainCode);
}
