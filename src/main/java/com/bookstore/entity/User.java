package com.bookstore.entity;

import org.springframework.security.core.GrantedAuthority;

import com.bookstore.entity.audit.BaseAudit;
import com.bookstore.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseAudit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private Integer id;

	@Column(length = 40)
	private String email;
	
	@Column(length = 40)
	private String username;

	@Column(length = 60)
	private String password;
	
	@Column(length = 64)
	private String avatar;
	
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;

}
