package com.bookstore.entity.audit;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bookstore.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BookAudit extends BaseAudit{
	
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY, optional = false, 
	cascade = {CascadeType.MERGE})
	@JoinColumn(name = "created_by")
	private UserEntity createdBy;
	
}
