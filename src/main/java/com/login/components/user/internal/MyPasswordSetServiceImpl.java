// com.login.components.user.internal.MyPasswordSetServiceImpl
package com.login.components.user.internal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.login.components.user.api.dto.MyPasswordSetInputDto;
import com.login.components.user.api.dto.MyPasswordSetViewDto;
import com.login.components.user.api.dto.MyUsersViewDto;
import com.login.components.user.api.exception.MyPasswordSetException;
import com.login.components.user.api.service.MyPasswordSetService;
import com.login.components.user.api.service.MyUsersService;
import com.login.components.user.internal.MyPasswordSetErrorCode.MyPasswordDbgMsg;
import com.util.type.MyType;

import lombok.AllArgsConstructor;

/**
 * パスワード設定ユースケース実装。
 *
 * <ul>
 * <li>ログインユーザーの現在値を読み、確認一致後にエンコードして保存
 * <li>“画面に見せるもの”は DTO のみ。内部ID・署名の検証は {@link IdBridge} を使用
 * </ul>
 */
@Service
@AllArgsConstructor
public class MyPasswordSetServiceImpl implements MyPasswordSetService {

	/* 依存（DI） */
	private final MyUsersService usersService; // ログインユーザー取得
	private final MyUsersRepository repository; // ユーザー永続化
	private final PasswordEncoder passEncoder; // パスワードエンコード
	private final ToMyPasswordSetViewDtoMapper toViewDtoMapper; // 画面DTO変換

	/**
	 * パスワードを確認付きで更新（エンコード保存）。
	 *
	 * <p>
	 * 一致しない／未入力は {@link MyPasswordSetException} を送出。
	 */
	@Transactional
	@Override
	public MyUsersEntity updatePasswordEncode(final MyPasswordSetInputDto inputDto) {
		if (MyType.isBlank(inputDto.password())
			|| MyType.isBlank(inputDto.passwordCheck())
			|| isNotSame(inputDto.password(), inputDto.passwordCheck())) {
			throw new MyPasswordSetException(
				MyPasswordSetErrorCode.MISMATCH,
				MyPasswordDbgMsg.mismatch(inputDto.password(), inputDto.passwordCheck()));
		}

		final MyUsersViewDto loginUserDto = this.usersService.getLoginUser();
		final String entityId = usersService.getEntityIdBySystemId(loginUserDto.systemId());

		final MyUsersEntity beforeEntity = this.repository.findById(entityId).orElseThrow(
			() -> new MyPasswordSetException(
				MyPasswordSetErrorCode.NOT_FOUND,
				MyPasswordDbgMsg.notFound(entityId)));

		final String encoded = this.passEncoder.encode(inputDto.password());
		final MyUsersEntity afterEntity = MyUsersEntity.builder()
			.id(beforeEntity.getId())
			.username(beforeEntity.getUsername())
			.passwordEncode(encoded)
			.authorityId(beforeEntity.getAuthorityId())
			.isFirstLogin(false)
			.resetPassword(false)
			.build();

		return this.repository.save(afterEntity);
	}

	/**
	 * ユーザー表示DTOからパスワード設定画面DTOへ変換。
	 */
	@Override
	public MyPasswordSetViewDto getViewDtoFromUsersDto(final MyUsersViewDto usersViewDto) {
		return this.toViewDtoMapper.fromViewDto(this.usersService.getLoginUser());
	}

	/* ===== [private] START ===== */
	/* 同値の否定判定（Nullセーフ） */
	private static boolean isNotSame(final String a, final String b) {
		return a == null ? b != null : !a.equals(b);
	}
	/* ===== [private] END ===== */
}
