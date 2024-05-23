package co.edu.uniquindio.gri.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Revista.
 */
@Entity(name = "REVISTA")
@Table(name = "REVISTA", schema = "gri")
public class Revista implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@Column(name = "ID")
	private long id;

	/** The identificador. issn */
	@Column(name = "IDENTIFICADOR", length = 400)
	private String identificador;

	/** The Name. */
	@Column(name = "NOMBRE", length = 400)
	private String nombre;

	/** The Participacion. */
	@Column(name = "PARTICIPACION", length = 400)
	private String participacion;

	/** The Tipo. */
	@Column(name = "TIPO", length = 400)
	private String tipo;

	/** The institucion. */
	@Column(name = "INSTITUCION", length = 400)
	private String institucion;

	/** The categoria. */
	@Column(name = "CATEGORIA", length = 400)
	private String categoria;

	/** The produccionBibliograficaG. */
	@OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<ProduccionBGrupo> produccionBibliograficaG = new ArrayList<ProduccionBGrupo>();

	/**
	 * Instantiates a new Revista.
	 */
	public Revista() {
	}

	/**
	 * Instantiates a nes Revista
	 * 
	 * @param id
	 * @param identificador
	 * @param nombre
	 * @param participacion
	 * @param tipo
	 * @param institucion
	 * @param grupo
	 */
	public Revista(long id, String identificador, String nombre, String participacion, String tipo, String institucion,
			String categoria, List<ProduccionBGrupo> produccionBibliograficaG) {
		super();
		this.id = id;
		this.identificador = identificador;
		this.nombre = nombre;
		this.participacion = participacion;
		this.tipo = tipo;
		this.institucion = institucion;
		this.produccionBibliograficaG = produccionBibliograficaG;
		this.categoria = categoria;
	}

	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * 
	 * @param identificador
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return
	 */
	public String getParticipacion() {
		return participacion;
	}

	/**
	 * 
	 * @param participacion
	 */
	public void setParticipacion(String participacion) {
		this.participacion = participacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * 
	 * @return
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * 
	 * @param institucion
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<ProduccionBGrupo> getProduccionBibliograficaG() {
		return produccionBibliograficaG;
	}

	public void setProduccionBibliograficaG(List<ProduccionBGrupo> produccionBibliograficaG) {
		this.produccionBibliograficaG = produccionBibliograficaG;
	}

	@Override
	public String toString() {
		return "Revista [id=" + id + ", identificador=" + identificador + ", nombre=" + nombre + ", participacion="
				+ participacion + ", tipo=" + tipo + ", institucion=" + institucion + ", categoria=" + categoria
				+ ", produccionBibliograficaG=" + produccionBibliograficaG + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Revista other = (Revista) obj;
		return id == other.id;
	}

}
