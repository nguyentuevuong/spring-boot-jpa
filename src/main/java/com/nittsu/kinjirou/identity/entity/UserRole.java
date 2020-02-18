package com.nittsu.kinjirou.identity.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserRole
 */
@Entity
@Table(name = "USER_ROLE")
public class UserRole {
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {
        private static final long serialVersionUID = 1322120000551624359L;
        
        @Column(name = "APP_USER_ID")
        protected Long userId;
        
        @Enumerated(EnumType.STRING)
        @Column(name = "ROLE")
        protected Role role;
    }
    
    @EmbeddedId
    Id id = new Id();
    
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", insertable=false, updatable=false)
    protected Role role;
}
