package com.nittsu.kinjirou.identity.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APP_USER")
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 35, nullable = false)
    private String username;

    @Column(name = "DISPLAY_NAME", length = 35, nullable = false)
    private String displayName;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany
    @JoinColumn(name = "APP_USER_ID", referencedColumnName = "ID")
    private List<UserRole> roles;
}
