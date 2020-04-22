package com.nittsu.kinjirou.update.entity.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_INSTALL_LOG")
public class InstallLog {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Column(name = "TIME", nullable = false)
    private Date time;

    @Column(name = "TYPE", nullable = false)
    private int type;

    @Column(name = "PATH", nullable = true)
    private String path;

    @Column(name = "CONTENT", nullable = true)
    private String content;
}