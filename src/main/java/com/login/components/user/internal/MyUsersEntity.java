package com.login.components.user.internal;

import com.login.components.user.internal.MyUsersDB.UsersColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

/** ユーザー情報エンティティ。 - 権限は ID 参照による疎結合 - toString ではパスワード非表示 */
@Entity
@Table(name = MyUsersDB.TABLE)
@DynamicInsert // null列はINSERTに含めず、DB DEFAULTを活かす
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(of = {
	"id", "username"
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
/* ===== [public/protected] START ===== */
public class MyUsersEntity {

	/** 主キー（US0001 形式、6桁）※アプリ側で採番してセットする */
	@Id
	@EqualsAndHashCode.Include
	@Column(name = UsersColumn.ID, nullable = false, unique = true, length = 6)
	private String id;

	@Column(name = UsersColumn.ID, nullable = false, unique = true, updatable = false)
	private String systemId;

	/** ユーザー名（ユニーク） */
	@Column(name = UsersColumn.USERNAME, nullable = false, length = 30)
	private String username;

	/** エンコード済みパスワード（nullならDB DEFAULTを使用できる設計） */
	@Column(name = UsersColumn.PASSWORD_ENCODE, nullable = false, insertable = false, length = 255)
	private String passwordEncode;

	/** 権限ID（6桁固定） */
	@Column(name = UsersColumn.AUTHORITY_ID, nullable = false, length = 6)
	private String authorityId;

	/** 初回ログインフラグ（nullでINSERT→DB DEFAULT:true） */
	@Column(name = UsersColumn.IS_FIRST_LOGIN, nullable = false)
	private Boolean isFirstLogin;

	/** パスワード既定リセット指示（JPQLでtrue→DBトリガが既定化） */
	@Column(name = UsersColumn.RESET_PASSWORD, nullable = false)
	@Builder.Default
	private boolean resetPassword = false;

	/** 楽観ロック用バージョン **/
	@Version
	@Column(name = UsersColumn.VERSION, nullable = false, unique = true)
	private Long version;
}
/* ===== [public/protected] END ===== */
