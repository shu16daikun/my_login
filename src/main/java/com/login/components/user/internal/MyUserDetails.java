package com.login.components.user.internal;

import com.login.components.authority.api.service.MyAuthorityService;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security 用の UserDetails。
 *
 * <p>
 * 目的: 認証後に UsersEntity を保持しつつ、権限は AuthorityService から解決して付与。
 */
/* ===== [public/protected] START ===== */
@Getter
class MyUserDetails implements UserDetails {

	private final MyUsersEntity loginUser;
	private final List<? extends GrantedAuthority> authoritiesForAuth;

	MyUserDetails(final MyUsersEntity loginUser, final MyAuthorityService authorityService) {
		this.loginUser = loginUser;
		final String role = authorityService.getNameById(loginUser.getAuthorityId());
		this.authoritiesForAuth = List.of(new SimpleGrantedAuthority(role));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authoritiesForAuth;
	}

	@Override
	public String getPassword() {
		return loginUser.getPasswordEncode();
	}

	@Override
	public String getUsername() {
		return loginUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
/* ===== [public/protected] END ===== */
