package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "system_code")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemCode extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long codeId;

    @Column(name = "main_code", nullable = false, length = 50)
    private String mainCode;

    @Column(name = "sub_code", nullable = false, length = 50)
    private String subCode;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "code_cate", nullable = false, length = 50)
    private String codeCate;

    @Column(name = "code_name", nullable = false, length = 100)
    private String codeName;
}
