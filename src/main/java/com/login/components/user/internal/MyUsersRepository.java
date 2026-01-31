package com.login.components.user.internal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/* ===== [public/protected] START ===== */
interface MyUsersRepository extends JpaRepository<MyUsersEntity, String> {

	@Query("SELECT u.version FROM MyUsersEntity u WHERE u.id = :id")
	Optional<Long> getVersionById(@Param("id") String id);

	boolean existsByUsername(String username);

	@Override
	boolean existsById(String id);

	boolean existsByUsernameAndIdNot(String username, String id);

	/** 外部キー（権限ID）が Users に存在するか（true=使用中=削除不可） */
	boolean existsByAuthorityId(String authorityId);

	Optional<MyUsersEntity> findByUsername(String username);

	Optional<MyUsersEntity> findBySystemId(String systemId);

	@Query("""
		SELECT u FROM MyUsersEntity u
		WHERE (:kw = '' OR u.username LIKE CONCAT('%', :kw, '%'))
		""")
	Page<MyUsersEntity> searchByUsernameLike(@Param("kw") String keyword, Pageable pageable);

	@Query("SELECT u FROM MyUsersEntity u ORDER BY u.id ASC")
	List<MyUsersEntity> findAllOrderByIdAsc();

	@Modifying
	@Query("UPDATE MyUsersEntity u SET u.username = :username WHERE u.id = :id")
	int updateUsernameById(@Param("id") String id, @Param("username") String username);

	@Modifying
	@Query("UPDATE MyUsersEntity u SET u.passwordEncode = :encoded WHERE u.id = :id")
	int updatePasswordById(@Param("id") String id, @Param("encoded") String encoded);

	@Modifying
	@Query("UPDATE MyUsersEntity u SET u.authorityId = :authId WHERE u.id = :id")
	int updateAuthorityById(@Param("id") String id, @Param("authId") String authorityId);

	@Modifying
	@Query("""
		UPDATE MyUsersEntity u
		SET u.resetPassword = true, u.isFirstLogin = true
		WHERE u.id = :id
		""")
	int requestPasswordReset(@Param("id") String id);

	@Query("SELECT u FROM MyUsersEntity u WHERE u.id IN :ids ORDER BY u.id ASC")
	List<MyUsersEntity> findAllByIdIn(@Param("ids") Collection<String> ids);

	@Query("SELECT u FROM MyUsersEntity u WHERE u.systemId IN :systemIds ORDER BY u.id ASC")
	List<MyUsersEntity> findAllBySystemIdIn(@Param("systemIds") Collection<String> systemIds);

	@Query("SELECT DISTINCT u.authorityId FROM MyUsersEntity u WHERE u.authorityId IN :authIds")
	Set<String> pickUsedAuthorityIds(@Param("authIds") Collection<String> authorityIds);
}
/* ===== [public/protected] END ===== */
