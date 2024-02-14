package com.greengram.greengram4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass //나를 상속받을 시 내가 가지고 있는 매핑자료도 자식에게 상속이 가능
@EntityListeners(AuditingEntityListener.class)//이 entity를 적용하기 전이나 후에 AuditingEntityListener(감시하다) 호출해 공통적으로 처리하는 기능?
//날짜 관련 값들을 체크해 원하는 곳에 값을 넣는 것?
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
