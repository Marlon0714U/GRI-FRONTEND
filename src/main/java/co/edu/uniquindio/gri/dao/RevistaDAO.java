package co.edu.uniquindio.gri.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.gri.CrudRepository.GrupoCrudRepository;
import co.edu.uniquindio.gri.CrudRepository.RevistaCrudRepository;
import co.edu.uniquindio.gri.model.Facultad;
import co.edu.uniquindio.gri.model.Grupo;
import co.edu.uniquindio.gri.model.Revista;
import co.edu.uniquindio.gri.repository.GrupoRepository;
import co.edu.uniquindio.gri.repository.RevistaRepository;
import co.edu.uniquindio.gri.service.api.GrupoServiceApi;
import co.edu.uniquindio.gri.service.api.RevistaServiceApi;
import co.edu.uniquindio.gri.utilities.GenericServiceImpl;

/**
 * Clase RevistaDAO.
 */
@Service
public class RevistaDAO extends GenericServiceImpl<Revista, Long> implements RevistaServiceApi {

	/** Repository para revistas. */
	@Autowired
	RevistaRepository revistaRepository;

	@Autowired
	RevistaCrudRepository revistaCrudRepository;

	/**
	 * Obtiene todas las revistas.
	 *
	 * @return lista con todas las revistas.
	 */
	public List<Revista> getAllRevistas() {
		return revistaRepository.findAll();
	}


	/**
	 * metodo que permite obtener la ultima revista registrada
	 * 
	 * @return
	 */
	public Revista findLastRegister() {
		return revistaRepository.findLastRegister();
	}

	@Override
	public CrudRepository<Revista, Long> getCrudRepository() {
		return revistaCrudRepository;
	}

}
