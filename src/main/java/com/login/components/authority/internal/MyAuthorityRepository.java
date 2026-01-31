package com.login.components.authority.internal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** authority の永続化。 */
/* ===== [public/protected] START ===== */
interface MyAuthorityRepository extends JpaRepository<MyAuthorityEntity, String> {

	@Query("SELECT a.name FROM MyAuthorityEntity a WHERE a.id = :id")
	Optional<String> getNameById(@Param("id") String id);

	@Query("SELECT a.version FROM MyAuthorityEntity a WHERE a.id = :id")
	Optional<Long> getVersionById(@Param("id") String id);

	boolean existsByName(String name);

	@Override
	boolean existsById(String id);

	Optional<MyAuthorityEntity> findByName(String name);

	Optional<MyAuthorityEntity> findBySystemId(String systemId);

	@Query("""
		SELECT a FROM MyAuthorityEntity a
		WHERE (:kw = '' OR a.name LIKE CONCAT('%', :kw, '%'))
		""")
	Page<MyAuthorityEntity> searchByNameLike(@Param("kw") String keyword, Pageable pageable);

	@Query("SELECT a FROM MyAuthorityEntity a ORDER BY a.name ASC")
	List<MyAuthorityEntity> findAllOrderByNameAsc();

	@Modifying
	@Query("UPDATE MyAuthorityEntity a SET a.name = :name WHERE a.id = :id")
	int updateNameById(@Param("id") String id, @Param("name") String newName);

	/* === 追加：ID 一括取得（IN 句） === */
	@Query("SELECT a FROM MyAuthorityEntity a WHERE a.id IN :ids")
	List<MyAuthorityEntity> findAllByIdIn(@Param("ids") Collection<String> ids);

	/* === 追加：ID 一括取得（IN 句） === */
	@Query("SELECT a FROM MyAuthorityEntity a WHERE a.systemId IN :systemIds")
	List<MyAuthorityEntity> findAllBySystemIdIn(@Param("systemIds") Collection<String> systemIds);
}
/* ===== [public/protected] END ===== */
