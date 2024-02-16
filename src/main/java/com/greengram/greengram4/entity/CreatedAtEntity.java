package com.greengram.greengram4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)//이 entity를 적용하기 전이나 후에 AuditingEntityListener(감시하다) 호출해 공통적으로 처리하는 기능?
public class CreatedAtEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
