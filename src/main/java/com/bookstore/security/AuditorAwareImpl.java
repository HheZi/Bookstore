package com.bookstore.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.bookstore.model.entity.UserEntity;

@Component
public class AuditorAwareImpl implements AuditorAware<UserEntity>{

	@Override
	public Optional<UserEntity> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.filter(Authentication::isAuthenticated)
				.filter(t -> t.getPrincipal() instanceof SecurityUserDetails)
				.map(Authentication::getPrincipal)
				.map(SecurityUserDetails.class::cast)
				.map(t -> SecurityUserDetails.getAuthUser());
	}

}
