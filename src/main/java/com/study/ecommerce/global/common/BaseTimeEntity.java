package com.study.ecommerce.global.common;

<<<<<<< HEAD

=======
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
<<<<<<< HEAD
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // created_at
=======

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
