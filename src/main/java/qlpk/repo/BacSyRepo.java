package qlpk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import qlpk.entity.BacSy;
import qlpk.entity.Benh;
import qlpk.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BacSyRepo extends JpaRepository<BacSy, Integer> {

	// for BacSyController
	BacSy findBacSyByUserAndIsDelete(User user, boolean isDelete);

	Optional<BacSy> findBacSyByIdAndIsDelete(int id, boolean isDelete);

	List<BacSy> findBacSyByIsDelete(boolean isDelete);

	@Query(value = "SELECT COUNT(*) FROM bac_sy WHERE is_delete = 0", nativeQuery = true)
	public List<Long> countByIsDelete();

	@Query(value = "SELECT * FROM bac_sy WHERE is_delete = 0 LIMIT :start, :to ", nativeQuery = true)
	public List<BacSy> findByPage(@RequestParam("start") long start, @RequestParam("to") long to);
}
