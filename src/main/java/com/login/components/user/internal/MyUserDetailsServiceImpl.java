package com.login.components.user.internal;

import com.login.components.authority.api.service.MyAuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security の UserDetailsService 実装。
 *
 * <p>
 * ユーザー名から {@link MyUsersEntity} を読み出し、{@link MyUserDetails} を返す。
 */
/* ===== [public/protected] START ===== */
@Service
@AllArgsConstructor
public class MyUserDetailsServiceImpl implements UserDetailsService {

	private final MyUsersServiceImpl usersService;
	private final MyAuthorityService authorityService;

	/**
	 * ユーザー名でユーザー情報を取得し、{@link UserDetails} を返す。
	 *
	 * @param username
	 *            ログインID
	 * @return {@link MyUserDetails}
	 * @throws UsernameNotFoundException
	 *             Spring 要件上の宣言（実装では MyUsersException を送出する場合あり）
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final MyUsersEntity loginUser = usersService.getEntityByUsername(username);
		return new MyUserDetails(loginUser, authorityService);
	}
}
/* ===== [public/protected] END ===== */
