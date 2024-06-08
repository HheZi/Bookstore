package com.bookstore.entity.audit;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAudit {
	
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;
	
	@LastModifiedDate
	@Column(name = "update_at")
	private Instant updateAt;
}
