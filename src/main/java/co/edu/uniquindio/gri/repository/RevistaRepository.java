package co.edu.uniquindio.gri.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.gri.model.Revista;

/**
 * Interface GrupoRepository.
 */
@Repository
public interface RevistaRepository extends JpaRepository<Revista, Long>{

	@Query("FROM co.edu.uniquindio.gri.model.Revista where id<>0")
	List<Revista> findAll();
		
	
	@Query(value = "select * from gri.revista us where us.id=(select max(id) from gri.revista)", nativeQuery = true)
	Revista findLastRegister();

}
