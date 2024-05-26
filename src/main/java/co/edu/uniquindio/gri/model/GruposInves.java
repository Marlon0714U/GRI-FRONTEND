package co.edu.uniquindio.gri.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "GRUPOS_INVES")
@Table(name = "GRUPOS_INVES", schema = "gri")
public class GruposInves implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private CompositeKey id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("grupos")
	private Grupo grupos;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("investigadores")
	private Investigador investigadores;

	@Column(name = "ESTADO", length = 50)
	private String estado;

	public GruposInves() {
	}

	public GruposInves(Grupo grupos, Investigador investigadores, String estado) {
		this.id = new CompositeKey(grupos.getId(), investigadores.getId());
		this.grupos = grupos;
		this.investigadores = investigadores;
		this.estado = estado;
	}

	public CompositeKey getId() {
		return id;
	}

	public void setId(CompositeKey id) {
		this.id = id;
	}

	public Grupo getGrupo() {
		return grupos;
	}

	public void setGrupo(Grupo grupos) {
		this.grupos = grupos;
	}

	@JsonIgnore
	public Investigador getInvestigador() {
		return investigadores;
	}
	
	@JsonIgnore
	public void setInvestigador(Investigador investigadores) {
		this.investigadores = investigadores;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "GruposInves [id=" + id + ", grupos=" + grupos + ", investigadores=" + investigadores + ", estado="
				+ estado + "]\n";
	}
	
	

}
