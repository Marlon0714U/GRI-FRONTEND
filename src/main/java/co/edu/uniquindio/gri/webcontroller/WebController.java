package co.edu.uniquindio.gri.webcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.gri.dao.CasoRevisionProduccionDAO;
import co.edu.uniquindio.gri.dao.CentroDAO;
import co.edu.uniquindio.gri.dao.FacultadDAO;
import co.edu.uniquindio.gri.dao.GrupoDAO;
import co.edu.uniquindio.gri.dao.GruposInvesDAO;
import co.edu.uniquindio.gri.dao.InvestigadorDAO;
import co.edu.uniquindio.gri.dao.LineasInvestigacionDAO;
import co.edu.uniquindio.gri.dao.PertenenciaDAO;
import co.edu.uniquindio.gri.dao.ProduccionDAO;
import co.edu.uniquindio.gri.dao.ProgramaDAO;
import co.edu.uniquindio.gri.dao.ReconocimientosDAO;
import co.edu.uniquindio.gri.dao.RevistaDAO;
import co.edu.uniquindio.gri.dao.UserDAO;
import co.edu.uniquindio.gri.model.CasoRevisionProduccion;
import co.edu.uniquindio.gri.model.Centro;
import co.edu.uniquindio.gri.model.CompositeKey;
import co.edu.uniquindio.gri.model.Facultad;
import co.edu.uniquindio.gri.model.Grupo;
import co.edu.uniquindio.gri.model.GruposInves;
import co.edu.uniquindio.gri.model.Investigador;
import co.edu.uniquindio.gri.model.ProduccionBGrupo;
import co.edu.uniquindio.gri.model.ProduccionGrupo;
import co.edu.uniquindio.gri.model.Programa;
import co.edu.uniquindio.gri.model.Revista;
import co.edu.uniquindio.gri.model.User;
import co.edu.uniquindio.gri.utilities.GRIConstantes;
import co.edu.uniquindio.gri.utilities.Respuesta;
import co.edu.uniquindio.gri.utilities.Util;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Controller
public class WebController {

	@Autowired
	GrupoDAO grupoDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	InvestigadorDAO investigadorDAO;

	@Autowired
	PertenenciaDAO pertenenciaDAO;

	@Autowired
	CentroDAO centroDAO;

	@Autowired
	ProgramaDAO programaDAO;

	@Autowired
	RevistaDAO revistaDAO;

	@Autowired
	FacultadDAO facultadDAO;

	@Autowired
	ProduccionDAO produccionDAO;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	LineasInvestigacionDAO lineasInvestigacionDAO;

	@Autowired
	ReconocimientosDAO reconocimientosDAO;

	@Autowired
	CasoRevisionProduccionDAO casosRevisionProduccionDAO;

	@Autowired
	Util utilidades = new Util();

	@Autowired
	GruposInvesDAO gruposInvesDAO;

	@GetMapping(value = { "/", "inicio" })
	public String main(Model model) {
		List<BigInteger> stats = facultadDAO.getStats();

		model.addAttribute(Util.CANTIDAD_FACULTADES, stats.get(Util.CANTIDAD_FACULTADES_ID));
		model.addAttribute(Util.CANTIDAD_CENTROS, stats.get(Util.CANTIDAD_CENTROS_ID));
		model.addAttribute(Util.CANTIDAD_PROGRAMAS, stats.get(Util.CANTIDAD_PROGRAMAS_ID));
		model.addAttribute(Util.CANTIDAD_GRUPOS, stats.get(Util.CANTIDAD_GRUPOS_ID));
		model.addAttribute(Util.CANTIDAD_INVESTIGADORES, stats.get(Util.CANTIDAD_INVESTIGADORES_ID));
		model.addAttribute(Util.ESTATICA_ESTADISTICAS, "");

		return "index";

	}

	@GetMapping("/updateDB")
	public ResponseEntity<Respuesta> updateDB(String user, String password) {
		Respuesta respuesta = new Respuesta();
		User userDB = userDAO.findOne(user);

		if (userDB != null) {
			System.out.println("sisas?: " + userDB.getPassword().equals(password));

			try {
				if (password.equals(userDB.getPassword())) {
					respuesta.setCodigoRespuesta(200);
					respuesta.setMensajeRespuesta("Base de datos actualizada");
					// Process proc = Runtime.getRuntime()
					// .exec("java -jar D:/Escritorio/TB/GRI-BACKEND/target/HelloWorld.jar 2 >
					// log.txt");
					return new ResponseEntity<Respuesta>(respuesta, org.springframework.http.HttpStatus.OK);
				}

			} catch (NullPointerException e) {
				respuesta.setCodigoRespuesta(401);
				respuesta.setMensajeRespuesta("Ingrese las credenciales");
				return new ResponseEntity<Respuesta>(respuesta, org.springframework.http.HttpStatus.UNAUTHORIZED);
			}
			/*
			 * catch (IOException e) { respuesta.setCodigoRespuesta(500); respuesta.
			 * setMensajeRespuesta("La actualizacion no pudo ser generada, intente nuevamente"
			 * ); return new ResponseEntity<Respuesta>(respuesta,
			 * org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR); }
			 */
		}

		respuesta.setCodigoRespuesta(401);
		respuesta.setMensajeRespuesta("No cuenta con las credenciales necesiarias");
		return new ResponseEntity<Respuesta>(respuesta, org.springframework.http.HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/login")
	public String getLogin(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			return main(model);

		}

		return "login";

	}

	@GetMapping("/AdmGruposInves")
	public String getAllGruposInves(Model model) {
		model.addAttribute("id", 0);
		model.addAttribute("ListaGruposInves", gruposInvesDAO.findAll());

		model.addAttribute("listaInvestigadores", investigadorDAO.findAll());
		model.addAttribute("listaGrupos", grupoDAO.findAll());

		List<Grupo> grupo = new ArrayList<>();
		List<Investigador> investigador = new ArrayList<>();

		model.addAttribute("grupo", grupo);
		model.addAttribute("investigador", investigador);

		return "admin/gruposInves/gruposInves";
	}

	@GetMapping("/investigadoresP")
	public String getInvestigadoresPertenencia(
			@RequestParam(name = "type", required = false, defaultValue = "u") String type,
			@RequestParam(name = "subType", required = false, defaultValue = "pa") String subType,
			@RequestParam(name = "id", required = false, defaultValue = "0") String id, Model model) {

		model.addAttribute("type", type);
		model.addAttribute("id", id);

		System.out.println("GET INVESTIGADORES PERTENENCIA");

		List<Investigador> investigadores = new ArrayList<>();
		if (Long.parseLong(id) != 0) {
			switch (type) {
			case "f":
			case "u":
				switch (subType) {
				case "adm":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ADMINISTRATIVO);
					break;
				case "dp":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_PLANTA);
					break;
				case "dc":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_CATEDRATICO);
					break;
				case "do":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_OCASIONAL);
					break;
				case "ie":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_EXTERNO);
					break;
				case "ei":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ESTUDIANTE);
					break;
				case "ind":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_INDEFINIDO);
					break;
				default:
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					utilidades.agregarPertenenciaInves(investigadores);
					break;
				}

				break;
			case "g":
				switch (subType) {
				case "adm":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ADMINISTRATIVO);
					break;
				case "dp":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_PLANTA);
					break;
				case "dc":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_CATEDRATICO);
					break;
				case "do":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_OCASIONAL);
					break;
				case "ie":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_EXTERNO);
					break;
				case "ei":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ESTUDIANTE);
					break;
				case "ind":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_INDEFINIDO);
					break;
				default:
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					utilidades.agregarPertenenciaInves(investigadores);
					break;
				}
				break;
			case "c":
				switch (subType) {
				case "adm":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ADMINISTRATIVO);
					break;
				case "dp":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_PLANTA);
					break;
				case "dc":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_CATEDRATICO);
					break;
				case "do":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_OCASIONAL);
					break;
				case "ie":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_EXTERNO);
					break;
				case "ei":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ESTUDIANTE);
					break;
				case "ind":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_INDEFINIDO);
					break;
				default:
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					utilidades.agregarPertenenciaInves(investigadores);
					break;
				}
				break;
			case "p":
				switch (subType) {
				case "adm":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ADMINISTRATIVO);
					break;
				case "dp":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_PLANTA);
					break;
				case "dc":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_CATEDRATICO);
					break;
				case "do":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_DOCENTE_OCASIONAL);
					break;
				case "ie":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_EXTERNO);
					break;
				case "ei":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_ESTUDIANTE);
					break;
				case "ind":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					investigadores = utilidades.agregarPertenenciaInves(investigadores);
					investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
							Util.PERTENENCIA_INDEFINIDO);
					break;
				default:
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					utilidades.agregarPertenenciaInves(investigadores);
					break;
				}
				break;
			}
		} else {
			switch (subType) {
			case "adm":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_ADMINISTRATIVO);
				break;
			case "dp":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_DOCENTE_PLANTA);
				break;
			case "dc":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_DOCENTE_CATEDRATICO);
				break;
			case "do":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_DOCENTE_OCASIONAL);
				break;
			case "ie":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_EXTERNO);
				break;
			case "ei":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_ESTUDIANTE);
				break;
			case "ind":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				investigadores = utilidades.agregarPertenenciaInves(investigadores);
				investigadores = utilidades.seleccionarInvestigadoresPertenencia(investigadores,
						Util.PERTENENCIA_INDEFINIDO);
				break;
			default:
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				utilidades.agregarPertenenciaInves(investigadores);
				break;
			}
		}

		model.addAttribute("listaInvestigadores", investigadores);

		Map<String, Integer> generoInvestigadores = new HashMap<>();

		int[] cantidades = utilidades.obtenerCantidadGenerosInvesgitadores(investigadores);

		generoInvestigadores.put("Masculino", cantidades[0]);
		generoInvestigadores.put("Femenino", cantidades[1]);
		generoInvestigadores.put("Indefinido", cantidades[2]);

		model.addAttribute("dataGeneroInvestigadores", generoInvestigadores.values());
		model.addAttribute("clavesGeneroInvestigadores", generoInvestigadores.keySet());

		return "investigadores";
	}

	@GetMapping("/investigadores")
	public String getInvestigadores(@RequestParam(name = "type", required = false, defaultValue = "u") String type,
			@RequestParam(name = "subType", required = false, defaultValue = "pa") String subType,
			@RequestParam(name = "id", required = false, defaultValue = "0") String id, Model model) {
		model.addAttribute("type", type);
		model.addAttribute("id", id);

		model.addAttribute("generoInvestigadores", id);

		List<Investigador> investigadores = null;
		if (Long.parseLong(id) != 0) {
			switch (type) {
			case "f":
			case "u":
				switch (subType) {
				case "ie":
					investigadores = investigadorDAO.getInvestigadoresEmeritosFacultad(Long.parseLong(id));
					break;
				case "is":
					investigadores = investigadorDAO.getInvestigadoresSeniorFacultad(Long.parseLong(id));
					break;
				case "ia":
					investigadores = investigadorDAO.getInvestigadoresAsociadosFacultad(Long.parseLong(id));
					break;
				case "ij":
					investigadores = investigadorDAO.getInvestigadoresJuniorFacultad(Long.parseLong(id));
					break;
				case "isc":
					investigadores = investigadorDAO.getInvestigadoresSinCategoriaFacultad(Long.parseLong(id));
					break;
				case "fd":
					investigadores = investigadorDAO.getInvestigadoresInternosDoctoresFacultad(Long.parseLong(id));
					break;
				case "fm":
					investigadores = investigadorDAO.getInvestigadoresInternosMagisterFacultad(Long.parseLong(id));
					break;
				case "fe":
					investigadores = investigadorDAO.getInvestigadoresInternosEspecialistasFacultad(Long.parseLong(id));
					break;
				case "fp":
					investigadores = investigadorDAO.getInvestigadoresInternosPregradoFacultad(Long.parseLong(id));
					break;
				case "d":
					investigadores = investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id));
					break;
				default:
					investigadores = investigadorDAO.getInvestigadoresFacultad(Long.parseLong(id));
					break;
				}
				break;
			case "g":
				switch (subType) {
				case "ie":
					investigadorDAO.getInvestigadoresEmeritosGrupo(Long.parseLong(id));
					break;
				case "is":
					investigadores = investigadorDAO.getInvestigadoresSeniorGrupo(Long.parseLong(id));
					break;
				case "ia":
					investigadores = investigadorDAO.getInvestigadoresAsociadosGrupo(Long.parseLong(id));
					break;
				case "ij":
					investigadores = investigadorDAO.getInvestigadoresJuniorGrupo(Long.parseLong(id));
					break;
				case "isc":
					investigadores = investigadorDAO.getInvestigadoresSinCategoriaGrupo(Long.parseLong(id));
					break;
				case "fd":
					investigadores = investigadorDAO.getInvestigadoresInternosDoctoresGrupo(Long.parseLong(id));
					break;
				case "fm":
					investigadores = investigadorDAO.getInvestigadoresInternosMagisterGrupo(Long.parseLong(id));
					break;
				case "fe":
					investigadores = investigadorDAO.getInvestigadoresInternosEspecialistasGrupo(Long.parseLong(id));
					break;
				case "fp":
					investigadores = investigadorDAO.getInvestigadoresInternosPregradoGrupo(Long.parseLong(id));
					break;
				case "d":
					investigadores = investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id));
					break;
				default:
					investigadores = investigadorDAO.getInvestigadoresGrupo(Long.parseLong(id));
					break;
				}
				break;
			case "c":
				switch (subType) {
				case "ie":
					investigadores = investigadorDAO.getInvestigadoresEmeritosCentro(Long.parseLong(id));
					break;
				case "is":
					investigadores = investigadorDAO.getInvestigadoresSeniorCentro(Long.parseLong(id));
					break;
				case "ia":
					investigadores = investigadorDAO.getInvestigadoresAsociadosCentro(Long.parseLong(id));
					break;
				case "ij":
					investigadores = investigadorDAO.getInvestigadoresJuniorCentro(Long.parseLong(id));
					break;
				case "isc":
					investigadores = investigadorDAO.getInvestigadoresSinCategoriaCentro(Long.parseLong(id));
					break;
				case "fd":
					investigadores = investigadorDAO.getInvestigadoresInternosDoctoresCentro(Long.parseLong(id));
					break;
				case "fm":
					investigadores = investigadorDAO.getInvestigadoresInternosMagisterCentro(Long.parseLong(id));
					break;
				case "fe":
					investigadores = investigadorDAO.getInvestigadoresInternosEspecialistasCentro(Long.parseLong(id));
					break;
				case "fp":
					investigadores = investigadorDAO.getInvestigadoresInternosPregradoCentro(Long.parseLong(id));
					break;
				case "d":
					investigadores = investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id));
					break;
				default:
					investigadores = investigadorDAO.getInvestigadoresCentro(Long.parseLong(id));
					break;
				}
				break;
			case "p":
				switch (subType) {
				case "ie":
					investigadores = investigadorDAO.getInvestigadoresEmeritosPrograma(Long.parseLong(id));
					break;
				case "is":
					investigadores = investigadorDAO.getInvestigadoresSeniorPrograma(Long.parseLong(id));
					break;
				case "ia":
					investigadores = investigadorDAO.getInvestigadoresAsociadosPrograma(Long.parseLong(id));
					break;
				case "ij":
					investigadores = investigadorDAO.getInvestigadoresJuniorPrograma(Long.parseLong(id));
					break;
				case "isc":
					investigadores = investigadorDAO.getInvestigadoresSinCategoriaPrograma(Long.parseLong(id));
					break;
				case "fd":
					investigadores = investigadorDAO.getInvestigadoresInternosDoctoresPrograma(Long.parseLong(id));
					break;
				case "fm":
					investigadores = investigadorDAO.getInvestigadoresInternosMagisterPrograma(Long.parseLong(id));
					break;
				case "fe":
					investigadores = investigadorDAO.getInvestigadoresInternosEspecialistasPrograma(Long.parseLong(id));
					break;
				case "fp":
					investigadores = investigadorDAO.getInvestigadoresInternosPregradoPrograma(Long.parseLong(id));
					break;
				case "d":
					investigadores = investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id));
					break;
				default:
					investigadores = investigadorDAO.getInvestigadoresPrograma(Long.parseLong(id));
					break;
				}
				break;
			}
		} else {
			switch (subType) {
			case "ie":
				investigadores = investigadorDAO.getAllInvestigadoresEmeritos();
				break;
			case "is":
				investigadores = investigadorDAO.getAllInvestigadoresSenior();
				break;
			case "ia":
				investigadores = investigadorDAO.getAllInvestigadoresAsociado();
				break;
			case "ij":
				investigadores = investigadorDAO.getAllInvestigadoresJunior();
				break;
			case "isc":
				investigadores = investigadorDAO.getAllInvestigadoresSinCategoria();
				break;
			case "fd":
				investigadores = investigadorDAO.getAllInvestigadoresInternosDoctores();
				break;
			case "fm":
				investigadores = investigadorDAO.getAllInvestigadoresInternosMagister();
				break;
			case "fe":
				investigadores = investigadorDAO.getAllInvestigadoresEspecialistas();
				break;
			case "fp":
				investigadores = investigadorDAO.getAllInvestigadoresPregrado();
				break;
			case "d":
				investigadores = investigadorDAO.getAllInvestigadoresInternos();
				break;
			default:
				investigadores = investigadorDAO.findAll();

				break;
			}
		}

		assert investigadores != null;
		List<Investigador> investigadores_pertenencia = utilidades.agregarPertenenciaInves(investigadores);

		for (Investigador investigador : investigadores_pertenencia) {
			if (investigador.getProducciones().size() > 1 || investigador.getProduccionesBibliograficas().size() > 1) {
				System.out.println(investigador);
			}
		}

		model.addAttribute("listaInvestigadores", investigadores_pertenencia);

		Map<String, Integer> generoInvestigadores = new HashMap<>();

		int[] cantidades = utilidades.obtenerCantidadGenerosInvesgitadores(investigadores);

		generoInvestigadores.put("Masculino", cantidades[0]);
		generoInvestigadores.put("Femenino", cantidades[1]);
		generoInvestigadores.put("Indefinido", cantidades[2]);

		model.addAttribute("dataGeneroInvestigadores", generoInvestigadores.values());
		model.addAttribute("clavesGeneroInvestigadores", generoInvestigadores.keySet());
		model.addAttribute("investigadoresOrcid",
				Util.getListaOrcidData(investigadorDAO.getAllInvestigadoresConOrcid()));

		return "investigadores";
	}

	@GetMapping("/programas")
	public String getProgramas(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {
		model.addAttribute(Util.PARAM_TYPE, type);
		model.addAttribute(Util.PARAM_ID, id);
		if (Long.parseLong(id) != 0) {
			switch (type) {
			case Util.UNDERGRADUATE_PROGRAM_PARAM_ID:
				model.addAttribute("listaProgramas", programaDAO.getProgramasAcademicosFacultad(Long.parseLong(id)));
				break;
			case "pd":
				model.addAttribute("listaProgramas", programaDAO.getProgramasDoctoradoFacultad(Long.parseLong(id)));
				break;
			case "pm":
				model.addAttribute("listaProgramas", programaDAO.getProgramasMaestriaFacultad(Long.parseLong(id)));
				break;
			case "pe":
				model.addAttribute("listaProgramas",
						programaDAO.getProgramasEspecializacionFacultad(Long.parseLong(id)));
				break;
			default:
				model.addAttribute("listaProgramas", programaDAO.getAllProgramas());
				break;
			}
		} else {
			switch (type) {
			case Util.UNDERGRADUATE_PROGRAM_PARAM_ID:
				model.addAttribute("listaProgramas", programaDAO.getProgramasAcademicos());
				break;
			case "pd":
				model.addAttribute("listaProgramas", programaDAO.getProgramasDoctorado());
				break;
			case "pm":
				model.addAttribute("listaProgramas", programaDAO.getProgramasMaestria());
				break;
			case "pe":
				model.addAttribute("listaProgramas", programaDAO.getProgramasEspecializacion());
				break;
			default:
				model.addAttribute("listaProgramas", programaDAO.getAllProgramas());
				break;
			}
		}
		return "programas";
	}

	@GetMapping("/revistas")
	public String getRevistas(Model model) {
		model.addAttribute("listaRevistas", revistaDAO.getAllRevistas());
		if (produccionDAO.getCantidadProduccionConRevista() == 0) {
			List<Revista> revistas = revistaDAO.getAllRevistas();
			for (Revista revista : revistas) {
				saveRevistas(revista);
			}
		}
		return "admin/revistas/revistas";
	}

	@PostMapping("/revistas/save")
	public String saveRevista(@ModelAttribute(name = "revista") Revista revista) {
		saveRevistas(revista);
		return "redirect:/revistas";
	}

	private void saveRevistas(Revista revista) {
		if (revista.getId() < 0) {
			Revista ultimo = revistaDAO.findLastRegister();
			if (ultimo == null) {
				revista.setId(1);
			} else {
				long id = ultimo.getId() + 1;
				revista.setId(id);
			}
			revistaDAO.save(revista);
		}

		Revista revistaOld = revistaDAO.get(revista.getId());
		List<ProduccionBGrupo> lista = produccionDAO.getProduccionRevista(revistaOld.getIdentificador());
		for (ProduccionBGrupo produccion : lista) {
			produccion.setRevista(null);
			produccionDAO.save(produccion);
		}
		revistaDAO.save(revista);
		lista = produccionDAO.getProduccionRevista(revista.getIdentificador());
		for (ProduccionBGrupo produccion : lista) {
			produccion.setRevista(revista);
			produccionDAO.save(produccion);
		}
	}

	@GetMapping("/facultades")
	public String getFacultades(Model model) {
		model.addAttribute("listaFacultades", facultadDAO.getAllFacultades());
		return "facultades";
	}

	@GetMapping("/centros")
	public String getCentros(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNDERGRADUATE_PROGRAM_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {
		model.addAttribute(Util.PARAM_TYPE, type);
		model.addAttribute(Util.PARAM_ID, id);
		if (Long.parseLong(id) != 0) {
			model.addAttribute("listaCentros", centroDAO.getAllCentrosFacultad(Long.parseLong(id)));
		} else {
			model.addAttribute("listaCentros", centroDAO.getAllCentros());
		}
		return "centros";
	}


	/*administracion de investigadores*/
	@GetMapping("/AdmInvestigadores")
	public String getAllInvestigadores(Model model) {

		// model.addAttribute("titulo", "USUARIOS");
		model.addAttribute("id", 0);

		model.addAttribute("listaInvestigadores", investigadorDAO.getAll());

		return "admin/investigadores/investigadores";

	}

	@PostMapping("/investigadores/save")
	public @ResponseBody Respuesta saveInvestigador(@ModelAttribute Investigador investigador) {
		Respuesta respuesta = new Respuesta();
		System.out.println("el investigador es: " + investigador);

		if (investigador != null) {
			Investigador consulta = investigadorDAO.findOne(investigador.getId());
		//	System.out.println("la consulta es: " + consulta);

			Investigador peticion = new Investigador();
			peticion.setId(investigador.getId());
			peticion.setNombre(investigador.getNombre());
			peticion.setNivelAcademico(investigador.getNivelAcademico());
			peticion.setCategoria(investigador.getCategoria());
			peticion.setPertenencia(investigador.getPertenencia());
			peticion.setSexo(investigador.getSexo());

			if (consulta == null) {
				// Guardar
				investigadorDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_INVESTIGADOR_CORRECTO);
			} else {
				// Actualizar
				investigadorDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_INVESTIGADOR_CORRECTO);
			}
		}
		return respuesta;
	}


	/*administracion de grupos de ivnestigacion*/
	@GetMapping("/Admgrupos")
	public String getAllGrupos(Model model) {

		// model.addAttribute("titulo", "USUARIOS");
		model.addAttribute("id", 0);

		model.addAttribute("listaGrupos", grupoDAO.getAll());
		model.addAttribute("centros", centroDAO.getAll());
		model.addAttribute("programas", programaDAO.getAll());
		//model.addAttribute("facultades", facultadDAO.getAll());

		return "admin/grupos/grupos";

	}

	@PostMapping("grupos/save/{idCentro}/{idPrograma}")
	public @ResponseBody Respuesta saveGrupo(Grupo grupo, @PathVariable("idCentro") Long idCentro,
											 @PathVariable("idPrograma") Long idPrograma) {

		Respuesta respuesta = new Respuesta();

		if (grupo != null) {

			Grupo consulta = grupoDAO.findOne(grupo.getId());
			Centro centroAsociada = centroDAO.findOne(idCentro);
			List<Programa> programaAsociado = new ArrayList<Programa>();
			programaAsociado.add(programaDAO.findOne(idPrograma));

			Grupo peticion = new Grupo();
			peticion.setId(grupo.getId());
			peticion.setNombre(grupo.getNombre());
			peticion.setLider(grupo.getLider());
			peticion.setCategoria(grupo.getCategoria());
			peticion.setContacto(grupo.getContacto());
			peticion.setAreaConocimiento(grupo.getAreaConocimiento());
			peticion.setInformacionGeneral(grupo.getInformacionGeneral());
			peticion.setCentro(centroAsociada);
			peticion.setProgramas(programaAsociado);


			if (consulta == null) {

				// guardar
				Util util = new Util();
				peticion.setAnioFundacion(util.obtenerFechaSistma());
				grupoDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_GRUPO_CORRECTO);
			} else {

				// actualizar
				peticion.setAnioFundacion(grupo.getAnioFundacion());
				grupoDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_GRUPO_CORRECTO);
			}
		}
		return respuesta;
	}

	@GetMapping("grupos/delete/{id}")
	public String deleteGrupo(@PathVariable("id") Long id, Model model) {

		grupoDAO.delete(id);

		return "redirect:/Admgrupos";
	}

	@GetMapping("gruposInves/active/{idGrupo}/{idInvestigador}")
	public String activeGruposInves(@PathVariable("idGrupo") Long idGrupo,
			@PathVariable("idInvestigador") Long idInvestigador) {

		CompositeKey id = new CompositeKey(idGrupo, idInvestigador);

		GruposInves gruposInves = gruposInvesDAO.findOne(id);
		gruposInves.setEstado(gruposInves.getEstado().equals("ACTUAL") ? "NO ACTUAL" : "ACTUAL");
		gruposInvesDAO.save(gruposInves);

		return "redirect:/AdmGruposInves";
	}

	@PostMapping("gruposInves/save/{idGrupo}/{idInvestigador}")
	public @ResponseBody Respuesta crearGruposInves(@PathVariable("idGrupo") Long idGrupo,
			@PathVariable("idInvestigador") Long idInvestigador) {

		CompositeKey id = new CompositeKey(idGrupo, idInvestigador);

		Respuesta respuesta = new Respuesta();
		GruposInves consulta = gruposInvesDAO.findOne(id);

		if (consulta == null) {
			Investigador investigador = investigadorDAO.findOne(idInvestigador);
			Grupo grupo = grupoDAO.findOne(idGrupo);
			GruposInves gruposInves = new GruposInves(grupo, investigador, "ACTUAL");
			gruposInvesDAO.save(gruposInves);
			respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
			respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_GRUPO_INVESTIGADOR_CORRECTO);
		} else {
			respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_ERROR);
			respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_GRUPO_INVESTIGADOR_ERROR);
		}

		return respuesta;
	}

	@GetMapping("grupos/active/{id}")
	public String activeGrupo(@PathVariable("id") Long id) {

		Grupo grupo = grupoDAO.findOne(id);
		grupoDAO.save(grupo);

		return "redirect:/Admgrupos";
	}

	@GetMapping("Investigadores/active/{id}")
	public String activeInvestigador(@PathVariable("id") Long id) {

		Investigador investigador = investigadorDAO.findOne(id);
		investigadorDAO.save(investigador);

		return "redirect:/Admgrupos";
	}

	@GetMapping("/grupos")
	public String getGrupos(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_SUBTYPE, required = false, defaultValue = Util.UNDERGRADUATE_PROGRAM_PARAM_ID) String subType,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {
		model.addAttribute(Util.PARAM_TYPE, type);
		model.addAttribute(Util.PARAM_ID, id);
		if (Long.parseLong(id) != 0) {
			switch (type) {
			case Util.FACULTY_PARAM_ID:
			case Util.UNIVERSITY_PARAM_ID:
				switch (subType) {
				case "ca1":
					model.addAttribute("listaGrupos", grupoDAO.getGruposA1Facultad(Long.parseLong(id)));
					break;
				case "ca":
					model.addAttribute("listaGrupos", grupoDAO.getGruposAFacultad(Long.parseLong(id)));
					break;
				case "cb":
					model.addAttribute("listaGrupos", grupoDAO.getGruposBFacultad(Long.parseLong(id)));
					break;
				case "cc":
					model.addAttribute("listaGrupos", grupoDAO.getGruposCFacultad(Long.parseLong(id)));
					break;
				case "cr":
					model.addAttribute("listaGrupos", grupoDAO.getGruposReconocidosFacultad(Long.parseLong(id)));
					break;
				case "cnr":
					model.addAttribute("listaGrupos", grupoDAO.getGruposNoReconocidosFacultad(Long.parseLong(id)));
					break;
				default:
					model.addAttribute("listaGrupos", grupoDAO.getAllGruposFacultad(Long.parseLong(id)));
					break;
				}
				break;
			case "c":
				switch (subType) {
				case "ca1":
					model.addAttribute("listaGrupos", grupoDAO.getGruposA1Centro(Long.parseLong(id)));
					break;
				case "ca":
					model.addAttribute("listaGrupos", grupoDAO.getGruposACentro(Long.parseLong(id)));
					break;
				case "cb":
					model.addAttribute("listaGrupos", grupoDAO.getGruposBCentro(Long.parseLong(id)));
					break;
				case "cc":
					model.addAttribute("listaGrupos", grupoDAO.getGruposCCentro(Long.parseLong(id)));
					break;
				case "cr":
					model.addAttribute("listaGrupos", grupoDAO.getGruposReconocidosCentro(Long.parseLong(id)));
					break;
				case "cnr":
					model.addAttribute("listaGrupos", grupoDAO.getGruposNoReconocidosCentro(Long.parseLong(id)));
					break;
				default:
					model.addAttribute("listaGrupos", grupoDAO.getAllGruposCentro_0(Long.parseLong(id)));
					break;
				}
				break;
			case "p":
				switch (subType) {
				case "ca1":
					model.addAttribute("listaGrupos", grupoDAO.getGruposA1Programa(Long.parseLong(id)));
					break;
				case "ca":
					model.addAttribute("listaGrupos", grupoDAO.getGruposAPrograma(Long.parseLong(id)));
					break;
				case "cb":
					model.addAttribute("listaGrupos", grupoDAO.getGruposBPrograma(Long.parseLong(id)));
					break;
				case "cc":
					model.addAttribute("listaGrupos", grupoDAO.getGruposCPrograma(Long.parseLong(id)));
					break;
				case "cr":
					model.addAttribute("listaGrupos", grupoDAO.getGruposReconocidosPrograma(Long.parseLong(id)));
					break;
				case "cnr":
					model.addAttribute("listaGrupos", grupoDAO.getGruposNoReconocidosPrograma(Long.parseLong(id)));
					break;
				default:
					model.addAttribute("listaGrupos", grupoDAO.getAllGruposPrograma(Long.parseLong(id)));
					break;
				}
				break;
			}
		} else {
			switch (subType) {
			case "ca1":
				model.addAttribute("listaGrupos", grupoDAO.getAllGruposA1());
				break;
			case "ca":
				model.addAttribute("listaGrupos", grupoDAO.getAllGruposA());
				break;
			case "cb":
				model.addAttribute("listaGrupos", grupoDAO.getAllGruposB());
				break;
			case "cc":
				model.addAttribute("listaGrupos", grupoDAO.getAllGruposC());
				break;
			case "cr":
				model.addAttribute("listaGrupos", grupoDAO.getAllGruposReconocidos());
				break;
			case "cnr":
				model.addAttribute("listaGrupos", grupoDAO.getAllGruposNoReconocidos());
				break;
			default:
				model.addAttribute("listaGrupos", grupoDAO.findAll());
				break;
			}
		}

		List<Grupo> grupos = new ArrayList<Grupo>();

		grupos = grupoDAO.findAll();

		Map<String, Integer> categoriaGrupos = new HashMap<>();

		int[] cantidades = utilidades.obtenerCantidadCategoriasGrupos(grupos);

		categoriaGrupos.put("A1", cantidades[0]);
		categoriaGrupos.put("A", cantidades[1]);
		categoriaGrupos.put("B", cantidades[2]);
		categoriaGrupos.put("C", cantidades[3]);
		categoriaGrupos.put("RECONOCIDO", cantidades[4]);
		categoriaGrupos.put("NO RECONOCIDO", cantidades[5]);

		model.addAttribute("dataCategoriaGrupos", categoriaGrupos.values());
		model.addAttribute("clavesCategoriaGrupos", categoriaGrupos.keySet());

		return "grupos";
	}

	@GetMapping("/lineas")
	public String getLineasInvestigacion(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {
		model.addAttribute(Util.PARAM_TYPE, type);
		model.addAttribute(Util.PARAM_ID, id);
		if (Long.parseLong(id) != 0) {
			switch (type) {
			case Util.FACULTY_PARAM_ID:
			case Util.UNIVERSITY_PARAM_ID:
				model.addAttribute("listaLineas", lineasInvestigacionDAO.getLineasFacultad(Long.parseLong(id)));
				break;
			case "c":
				model.addAttribute("listaLineas", lineasInvestigacionDAO.getLineasCentro(Long.parseLong(id)));
				break;
			case "p":
				model.addAttribute("listaLineas", lineasInvestigacionDAO.getLineasPrograma(Long.parseLong(id)));
				break;
			case "g":
			case "i":
				model.addAttribute("listaLineas", lineasInvestigacionDAO.getLineasGrupo(Long.parseLong(id)));
				break;
			default:
				model.addAttribute("listaLineas", lineasInvestigacionDAO.findAll());
				break;
			}
		} else {
			model.addAttribute("listaLineas", lineasInvestigacionDAO.findAll());
		}
		return "lineas";
	}

	@GetMapping("/reconocimientos")
	public String getReconocimientos(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {
		model.addAttribute(Util.PARAM_TYPE, type);
		model.addAttribute(Util.PARAM_ID, id);
		model.addAttribute("nombre", "Reconocimientos");
		model.addAttribute("tipo", "Reporte");

		if (Long.parseLong(id) != 0) {
			if (type.equals(Util.RESEARCHER_PARAM_ID) || type.equals(Util.GROUP_PARAM_ID)
					|| type.equals(Util.CENTER_PARAM_ID) || type.equals(Util.PROGRAM_PARAM_ID)
					|| type.equals(Util.FACULTY_PARAM_ID) || type.equals(Util.UNIVERSITY_PARAM_ID)) {
				model.addAttribute("reconocimientos", reconocimientosDAO.getReconocimientos(Long.parseLong(id), type));
			} else {
				model.addAttribute("reconocimientos", reconocimientosDAO.findAll());
			}
		} else {
			model.addAttribute("reconocimientos", reconocimientosDAO.findAll());
		}

		long facultadId = 0;
		long longId = Long.parseLong(id);
		switch (type) {

		case "g":
			facultadId = grupoDAO.findOne(longId).getProgramas().get(0).getFacultad().getId();
			System.err.println("-------------------- " + facultadId);
			break;
		case "p":
			facultadId = programaDAO.getProgramaById(longId).getFacultad().getId();
			break;
		case "c":
			facultadId = centroDAO.getCentroById(longId).getFacultad().getId();
			break;
		case "f":
			facultadId = longId;
			break;

		}

		model.addAttribute("facultadId", facultadId);
		model.addAttribute("color", "card-" + facultadId);

		return "reconocimientos";
	}

	@GetMapping("/recolecciones")
	public String getRecoleccion(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {
		model.addAttribute(Util.PARAM_TYPE, type);
		model.addAttribute(Util.PARAM_ID, id);
		model.addAttribute("nombre", "Recolecciones");
		model.addAttribute("tipo", "Reporte");

		ArrayList<String> nombres = new ArrayList<String>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		List<ProduccionBGrupo> produccionesb = utilidades.obtenerBibliograficas(type, Long.parseLong(id));
		List<ProduccionGrupo> producciones = utilidades.obtenerGenericas(type, Long.parseLong(id));
		List<CasoRevisionProduccion> casos = casosRevisionProduccionDAO.getRecolecciones();

		List<CasoRevisionProduccion> casosResult = utilidades.obtenerNombresNumerosCasosPorListas(casos, produccionesb,
				producciones, indices, nombres);

		model.addAttribute("nombres", nombres);
		model.addAttribute("indices", indices);
		model.addAttribute("recolecciones", casosResult);

		long facultadId = 0;
		long longId = Long.parseLong(id);
		switch (type) {

		case "g":
			facultadId = grupoDAO.findOne(longId).getProgramas().get(0).getFacultad().getId();
			break;
		case "p":
			facultadId = programaDAO.getProgramaById(longId).getFacultad().getId();
			break;
		case "c":
			facultadId = centroDAO.getCentroById(longId).getFacultad().getId();
			break;
		case "f":
			facultadId = longId;
			break;

		}

		model.addAttribute("facultadId", facultadId);
		model.addAttribute("color", "card-" + facultadId);

		return "recolecciones";
	}

	@GetMapping("/general")
	public String getTipologias(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {

		Investigador investigador = investigadorDAO.findOne(Long.parseLong(id));
		model.addAttribute("investigador", investigador);
		model.addAttribute(Util.PARAM_ID, id);
		model.addAttribute("tipo", type);

		switch (type) {
		case Util.UNIVERSITY_PARAM_ID:
			List<Facultad> facultades = facultadDAO.getAllFacultades();

			model.addAttribute("nombre", "Tipología De Productos Para La Universidad Del Quindío");
			model.addAttribute("lista", facultades);
			model.addAttribute("subtipo", Util.FACULTY_PARAM_ID);
			model.addAttribute("color", "card-0");
			model.addAttribute("tamanio", "ci-" + calcularTamanio(facultades.size()));

			break;
		case Util.FACULTY_PARAM_ID:
			Facultad f = facultadDAO.getFacultadById(Long.parseLong(id));
			List<Programa> programas = programaDAO.getProgramasFacultad(Long.parseLong(id));

			model.addAttribute("nombre", "Tipología de Productos Para la Facultad de " + f.getNombre());
			model.addAttribute("lista", programas);
			model.addAttribute("subtipo", "p");
			model.addAttribute("color", "card-" + f.getId());
			model.addAttribute("tamanio", "ci-" + calcularTamanio(programas.size()));

			break;
		case "p": {
			Programa p = programaDAO.getProgramaById(Long.parseLong(id));
			List<Grupo> grupos = grupoDAO.getGruposPrograma(Long.parseLong(id));

			model.addAttribute("nombre", p.getNombre());
			model.addAttribute("lista", grupos);
			model.addAttribute("subtipo", "g");
			model.addAttribute("color", "card-" + p.getFacultad().getId());
			model.addAttribute("tamanio", "ci-" + calcularTamanio(grupos.size()));

			break;
		}
		case "c": {
			Centro c = centroDAO.getCentroById(Long.parseLong(id));
			List<Grupo> grupos = grupoDAO.getGruposCentro(Long.parseLong(id));

			model.addAttribute("nombre", c.getNombre());
			model.addAttribute("lista", grupos);
			model.addAttribute("subtipo", "g");
			model.addAttribute("color", "card-" + c.getFacultad().getId());
			model.addAttribute("tamanio", "ci-" + calcularTamanio(grupos.size()));

			break;
		}
		case "g":
			Grupo g = grupoDAO.findOne(Long.parseLong(id));

			model.addAttribute("nombre", g.getNombre());
			model.addAttribute("color", "card-" + g.getProgramas().get(0).getFacultad().getId());

			break;
		case "i":
			Investigador i = investigadorDAO.findOne(Long.parseLong(id));

			model.addAttribute("nombre",
					"Tipología de Productos de " + utilidades.convertToTitleCaseIteratingChars(i.getNombre()));
			model.addAttribute("color", "card-0");
			break;
		}
		return "general";
	}

	@GetMapping("/inventario")
	public String getInventario(
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String id,
			Model model) {

		if (id.equals(Util.UNIVERSITY_PARAM_ID)) {
			model.addAttribute("nombre", "Producciones en Custodia");
			model.addAttribute("lista", facultadDAO.getAllFacultades());
			model.addAttribute("tamanio", "ci-4");
			model.addAttribute("color", "card-0");
			model.addAttribute("subtipo", Util.FACULTY_PARAM_ID);
		} else {
			Facultad f = facultadDAO.getFacultadById(Long.parseLong(id));
			List<Grupo> listaGrupos = grupoDAO.getGruposPertenecientes(Long.parseLong(id), Util.FACULTY_PARAM_ID);
			model.addAttribute("nombre", f.getNombre());
			model.addAttribute("lista", listaGrupos);
			model.addAttribute("color", "card-" + f.getId());
			model.addAttribute(Util.PARAM_ID, "" + f.getId());
			model.addAttribute("tamanio", "ci-" + calcularTamanio(listaGrupos.size()));
		}
		return "inventario/inventario";
	}

	@GetMapping("/reporteinventario")
	public String getReporteInventario(@RequestParam(name = Util.PARAM_ID) String id, Model model) {

		Grupo g = grupoDAO.findOne(Long.parseLong(id));

		model.addAttribute("nombre", g.getNombre());
		model.addAttribute("color", "card-" + g.getProgramas().get(0).getFacultad().getId());
		model.addAttribute(Util.PARAM_ID, "" + g.getProgramas().get(0).getFacultad().getId());
		model.addAttribute("producciones", produccionDAO.getAllProducciones(Long.parseLong(id)));

		return "inventario/reporteinventario";
	}

	@GetMapping("/reportepertenencia")
	public String getReportePertenencia(@RequestParam(name = Util.PARAM_ID) String id, Model model) {

		Grupo g = grupoDAO.findOne(Long.parseLong(id));

		List<Investigador> integrantes;

		integrantes = investigadorDAO.getInvestigadoresGrupo(Long.parseLong(id));

		List<String> pertenencias = new ArrayList<>();

		utilidades.agregarPertenenciaInves(integrantes);

		pertenencias.add(Util.PERTENENCIA_INDEFINIDO);
		pertenencias.add(Util.PERTENENCIA_ADMINISTRATIVO);
		pertenencias.add(Util.PERTENENCIA_DOCENTE_PLANTA);
		pertenencias.add(Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		pertenencias.add(Util.PERTENENCIA_DOCENTE_OCASIONAL);
		pertenencias.add(Util.PERTENENCIA_EXTERNO);
		pertenencias.add(Util.PERTENENCIA_ESTUDIANTE);

		model.addAttribute("pertenencias", pertenencias);
		model.addAttribute("nombre", g.getNombre());
		model.addAttribute("color", "card-" + g.getProgramas().get(0).getFacultad().getId());
		model.addAttribute("integrantes", integrantes);
		model.addAttribute(Util.PARAM_ID, "" + g.getProgramas().get(0).getFacultad().getId());

		return "pertenencia_investigadores/reportepertenencia";

	}

	@GetMapping("/pertenencia")
	public String getPertenencia(
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String id,
			Model model) {

		if (id.equals(Util.UNIVERSITY_PARAM_ID)) {

			model.addAttribute("nombre", "Pertenencia de Investigadores");
			model.addAttribute("lista", facultadDAO.getAllFacultades());
			model.addAttribute("tamanio", "ci-4");
			model.addAttribute("color", "card-0");
			model.addAttribute("subtipo", Util.FACULTY_PARAM_ID);
		} else {

			Facultad f = facultadDAO.getFacultadById(Long.parseLong(id));
			List<Grupo> listaGrupos = grupoDAO.getGruposPertenecientes(Long.parseLong(id), Util.FACULTY_PARAM_ID);
			model.addAttribute("nombre", f.getNombre());
			model.addAttribute("lista", listaGrupos);
			model.addAttribute("color", "card-" + f.getId());
			model.addAttribute(Util.PARAM_ID, "" + f.getId());
			model.addAttribute("tamanio", "ci-" + calcularTamanio(listaGrupos.size()));

		}

		return "pertenencia_investigadores/pertenencia";

	}

	@GetMapping("/admin")
	public String getAdmin(Model model) {

		model.addAttribute("tamanio", "2");

		return "admin";

	}

	@GetMapping("/usuarios")
	public String getAllUsuarios(Model model) {

		model.addAttribute("titulo", "USUARIOS");
		model.addAttribute("id", 0);

		model.addAttribute("usuarios", userDAO.getAll());

		return "admin/users/usuarios";

	}

	@PostMapping("usuarios/save")
	public @ResponseBody Respuesta saveUsuario(User user) {

		Respuesta respuesta = new Respuesta();

		if (user != null) {

			User consulta = userDAO.findOne(user.getUsername());

			User ultimo = userDAO.findLastRegisterUser();

			if (user.getId() == -1) {
				if (consulta == null) {

					User peticion = new User();
					peticion.setRol(user.getRol());
					peticion.setUsername(user.getUsername());
					peticion.setPassword(utilidades.encodePassword(user.getPassword()));
					peticion.setId(ultimo.getId() + 1);

					userDAO.save(peticion);
					respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
					respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_USUARIO_CORRECTO);

				} else {

					respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_ERROR);
					respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_USUARIO_ERROR_YA_EXISTE);

				}
			} else {

				if (consulta == null) {

					respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_ERROR);
					respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_USUARIO_ERROR_NO_EXISTE);

				} else {

					consulta.setRol(user.getRol());
					consulta.setUsername(user.getUsername());
					consulta.setPassword(utilidades.encodePassword(user.getPassword()));
					userDAO.save(consulta);
					respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
					respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_USUARIO_CORRECTO);

				}

			}
		}

		return respuesta;
	}

	@GetMapping("usuario/active/{username}")
	public String activepFacultad(@PathVariable("username") String username) {

		User usuario = userDAO.findOne(username);
		userDAO.save(usuario);

		return "redirect:/usuarios";
	}

	@GetMapping("usuarios/delete/{id}")
	public String deleteUsuario(@PathVariable("id") Long id, Model model) {

		userDAO.delete(id);

		return "redirect:/usuarios";
	}

	@GetMapping("/Admcentros")
	public String getAllCentros(Model model) {

		// model.addAttribute("titulo", "USUARIOS");
		model.addAttribute("id", 0);

		model.addAttribute("centros", centroDAO.getAll());
		model.addAttribute("facultades", facultadDAO.getAll());

		return "admin/centros/centros";

	}

	@PostMapping("centros/save/{idFacultad}")
	public @ResponseBody Respuesta saveCentro(Centro centro, @PathVariable("idFacultad") Long idFacultad) {

		Respuesta respuesta = new Respuesta();

		if (centro != null) {

			Centro consulta = centroDAO.findOne(centro.getId());

			Centro ultimo = centroDAO.findLastRegister();

			if (consulta == null) {

				// guardar

				Facultad facultadAsociada = facultadDAO.findOne(idFacultad);

				Centro peticion = new Centro();
				peticion.setNombre(centro.getNombre());
				peticion.setInformaciongeneral(centro.getInformaciongeneral());
				peticion.setContacto(centro.getContacto());
				peticion.setFacultad(facultadAsociada);
				peticion.setId(ultimo.getId() + 1);

				centroDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_CENTRO_CORRECTO);
			} else {

				// actualizar

				Facultad facultadAsociada = facultadDAO.findOne(idFacultad);

				consulta.setNombre(centro.getNombre());
				consulta.setInformaciongeneral(centro.getInformaciongeneral());
				consulta.setContacto(centro.getContacto());
				consulta.setFacultad(facultadAsociada);

				centroDAO.save(consulta);

				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_CENTRO_CORRECTO);
			}
		}

		return respuesta;
	}

	@GetMapping("centro/active/{id}")
	public String activeCentro(@PathVariable("id") Long id) {

		Centro centro = centroDAO.findOne(id);
		centroDAO.save(centro);

		return "redirect:/Admcentros";
	}

	@GetMapping("centros/delete/{id}")
	public String deleteCentro(@PathVariable("id") Long id, Model model) {

		centroDAO.delete(id);

		// esto en realidad no hace nada, la actualizacion de la pagina se esta dando
		// con location.reaload() -> javascript
		return "redirect:/Admcentros";
	}

	@GetMapping("/Admfacultades")
	public String getAllFacultades(Model model) {

		// model.addAttribute("titulo", "USUARIOS");
		model.addAttribute("id", 0);

		model.addAttribute("facultades", facultadDAO.getAll());

		return "admin/facultades/facultades";

	}

	@PostMapping("facultades/save")
	public @ResponseBody Respuesta saveFacultad(Facultad facultad) {

		Respuesta respuesta = new Respuesta();

		if (facultad != null) {

			Facultad consulta = facultadDAO.findOne(facultad.getId());

			Facultad ultimo = facultadDAO.findLastRegister();

			if (consulta == null) {

				// guardar

				Facultad peticion = new Facultad();
				peticion.setNombre(facultad.getNombre());
				peticion.setMision(facultad.getMision());
				peticion.setVision(facultad.getVision());
				peticion.setContacto(facultad.getContacto());
				peticion.setId(ultimo.getId() + 1);

				facultadDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_FACULTAD_CORRECTO);
			} else {

				// actualizar

				consulta.setNombre(facultad.getNombre());
				consulta.setMision(facultad.getMision());
				consulta.setVision(facultad.getVision());
				consulta.setContacto(facultad.getContacto());

				facultadDAO.save(consulta);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_FACULTAD_CORRECTO);
			}
		}

		return respuesta;
	}

	@GetMapping("facultad/active/{id}")
	public String activeFacultad(@PathVariable("id") Long id) {

		Facultad facultad = facultadDAO.findOne(id);
		facultadDAO.save(facultad);

		return "redirect:/Admfacultades";
	}

	@GetMapping("facultades/delete/{id}")
	public String deleteFacultad(@PathVariable("id") Long id, Model model) {

		facultadDAO.delete(id);

		// esto en realidad no hace nada, la actualizacion de la pagina se esta dando
		// con location.reaload() -> javascript
		return "redirect:/Admfacultades";
	}

	@GetMapping("/Admprogramas")
	public String getAllProgramas(Model model) {

		// model.addAttribute("titulo", "USUARIOS");
		model.addAttribute("id", 0);

		model.addAttribute("programas", programaDAO.getAll());

		model.addAttribute("facultades", facultadDAO.getAll());

		return "admin/programas/programas";

	}

	@PostMapping("programas/save/{idFacultad}")
	public @ResponseBody Respuesta savePrograma(Programa programa, @PathVariable("idFacultad") Long idFacultad) {

		Respuesta respuesta = new Respuesta();

		if (programa != null) {

			Programa consulta = programaDAO.findOne(programa.getId());

			Programa ultimo = programaDAO.findLastRegister();

			if (consulta == null) {

				// guardar

				Facultad facultadAsociada = facultadDAO.findOne(idFacultad);

				Programa peticion = new Programa();
				peticion.setNombre(programa.getNombre());
				peticion.setInformaciongeneral(programa.getInformaciongeneral());
				peticion.setMision(programa.getMision());
				peticion.setVision(programa.getVision());
				peticion.setContacto(programa.getContacto());
				peticion.setFacultad(facultadAsociada);
				peticion.setId(ultimo.getId() + 1);

				programaDAO.save(peticion);
				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_CREAR_PROGRAMA_CORRECTO);
			} else {

				// actualizar

				Facultad facultadAsociada = facultadDAO.findOne(idFacultad);

				consulta.setNombre(programa.getNombre());
				consulta.setInformaciongeneral(programa.getInformaciongeneral());
				consulta.setMision(programa.getMision());
				consulta.setVision(programa.getVision());
				consulta.setContacto(programa.getContacto());
				consulta.setFacultad(facultadAsociada);

				programaDAO.save(consulta);

				respuesta.setCodigoRespuesta(GRIConstantes.CODIGO_RESPUESTA_EXITOSO);
				respuesta.setMensajeRespuesta(GRIConstantes.RESPUESTA_MODIFICAR_PROGRAMA_CORRECTO);
			}
		}

		return respuesta;
	}

	@GetMapping("programa/active/{id}")
	public String activePrograma(@PathVariable("id") Long id) {

		Programa programa = programaDAO.findOne(id);
		programaDAO.save(programa);

		return "redirect:/Admprograma";
	}

	@GetMapping("programas/delete/{id}")
	public String deletePrograma(@PathVariable("id") Long id, Model model) {

		programaDAO.delete(id);

		// esto en realidad no hace nada, la actualizacion de la pagina se esta dando
		// con location.reaload() -> javascript
		return "redirect:/Admprogramas";
	}

	/**
	 * permite obtener el reporte estadistico solicitado en formato pdf
	 *
	 * @param type
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/imprimir-reporte-estadistico")
	public void imprimirReporteEstadistico(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model, HttpServletResponse response) throws SQLException, IOException, JRException {

		Connection conexion = jdbcTemplate.getDataSource().getConnection();

		List<JasperPrint> jasperPrintList = new ArrayList<>();


try{
		configurarReportes(jasperPrintList, type, id, conexion);

		if (jasperPrintList.isEmpty()) {
			throw new JRException("No se encontraron reportes para el tipo y ID proporcionados");
		}

		imprimirReporte(response, jasperPrintList);
	} catch (JRException e) {
		// Manejo de la excepción
		e.printStackTrace();
	}

		conexion.close();

	}

	/**
	 * @param response        respuesta
	 * @param jasperPrintList lista de impresion
	 * @throws IOException excepcion de flujo
	 * @throws JRException excepcion de entorno
	 */
	private void imprimirReporte(HttpServletResponse response, List<JasperPrint> jasperPrintList)
			throws IOException, JRException {


		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=reporte de investigaciÃ³n.pdf");

		final OutputStream outStream = response.getOutputStream();

		JRPdfExporter exporter = new JRPdfExporter();

		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));

		SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outStream);

		exporter.setExporterOutput(exporterOutput);

		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

		exporter.setConfiguration(configuration);

		exporter.exportReport();

	}

	/**
	 * permite obtener el reporte estadistico solicitado en formato pdf
	 *
	 * @param type
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/descargar-reporte-estadistico")
	public void descargarReporteEstadistico(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model, HttpServletResponse response) throws SQLException, IOException, JRException {

		Connection conexion = jdbcTemplate.getDataSource().getConnection();

		List<JasperPrint> jasperPrintList = new ArrayList<>();

		configurarReportes(jasperPrintList, type, id, conexion);



		descargarReportePDF(response, jasperPrintList);

		conexion.close();

	}

	/**
	 * @param response        respuesta
	 * @param jasperPrintList lista de impresion
	 * @throws IOException excepcion de flujo
	 * @throws JRException excepcion de entorno
	 */
	private void descargarReportePDF(HttpServletResponse response, List<JasperPrint> jasperPrintList)
			throws IOException, JRException {
		response.setContentType("application/download");

		response.setHeader("Content-disposition", "inline; filename=reporte de investigaciÃ³n.pdf");

		final OutputStream outStream = response.getOutputStream();

		JRPdfExporter exporter = new JRPdfExporter();

		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));

		SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outStream);

		exporter.setExporterOutput(exporterOutput);

		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

		exporter.setConfiguration(configuration);

		exporter.exportReport();

	}

	/**
	 * Permite generar un listado de archivos .jasper, que seran exportados en un
	 * formato posteriormente, como pdf.
	 *
	 * @param jasperPrintList lista de archivos .jasper que serÃ¡ generada
	 * @param type            el tipo
	 * @param id              el id
	 * @param conexion        la conexiÃ³n
	 */
	private void configurarReportes(List<JasperPrint> jasperPrintList, String type, String id, Connection conexion)
			throws JRException {

		System.out.println("Configurando reportes con type: " + type + " y id: " + id);
		System.out.println("Cantidad inicial de jasperPrintList : "+ jasperPrintList.size() );


		BigInteger cantidad_grupos = new BigInteger(Util.PARAM_UNIVERSITY_LEVEL_ID);
		BigInteger cantidad_investigadores = new BigInteger(Util.PARAM_UNIVERSITY_LEVEL_ID);
		BigInteger cantidad_producciones = new BigInteger(Util.PARAM_UNIVERSITY_LEVEL_ID);

		// PARAMETROS FACULTAD//
		String title_facultad = "";
		String mision_facultad = "";
		String vision_facultad = "";
		String contacto_facultad = "";
		Long id_facultad = null;
		////////////

		// PARAMETROS PROGRAMA//
		String title_programa = "";
		String mision_programa = "";
		String vision_programa = "";
		String contacto_programa = "";
		Long id_programa = null;
		////////////

		// PARAMETROS CENTRO//
		String title_centro = "";
		String info_general = "";
		String contacto_centro = "";
		Long id_centro = null;
		////////////

		// PARAMETROS GRUPO//
		String title_grupo = "";
		String contacto_grupo = "";
		Long id_grupo = null;
		////////////

		// PARAMETROS INVESTIGADOR//
		String nombre_investigador = "";
		Long id_investigador = null;
		////////////

		boolean facultad = false;
		boolean programa = false;
		boolean centro = false;
		boolean grupo = false;
		boolean universidad = false;
		boolean investigador = false;

		switch (type) {
		case Util.FACULTY_PARAM_ID: {
			System.out.println("buscando en la facultad ");
			Facultad f = facultadDAO.getFacultadById(Long.parseLong(id));
			facultad = true;
			title_facultad = f.getNombre();

			if (f.getMision() != null) {

				mision_facultad = f.getMision().replaceAll("\n", " ");

			}
			if (f.getVision() != null) {

				vision_facultad = f.getVision().replaceAll("\n", " ");

			}

			contacto_facultad = f.getContacto();
			id_facultad = f.getId();

			cantidad_grupos = BigInteger.valueOf(grupoDAO.getAllGruposFacultad(id_facultad).size());
			cantidad_investigadores = BigInteger.valueOf(investigadorDAO.getInvestigadoresFacultad(id_facultad).size());

			int suma = 0;

			for (int j = 0; j < 7; j++) {
				suma += produccionDAO.getCantidadProduccionesFacultadPorTipo(String.valueOf(id_facultad), j + "")
						.intValue();

			}

			suma += produccionDAO.getCantidadProduccionesFacultadPorSubTipo(String.valueOf(id_facultad), "32")
					.intValue();
			suma += produccionDAO.getCantidadProduccionesFacultadPorSubTipo(String.valueOf(id_facultad), "33")
					.intValue();

			cantidad_producciones = new BigInteger(String.valueOf(suma));

			break;
		}
		case "p": {
			System.out.println("buscando en programa");
			Programa p = programaDAO.getProgramaById(Long.parseLong(id));
			programa = true;
			title_programa = p.getNombre();

			if (p.getMision() != null) {

				mision_programa = p.getMision().replaceAll("\n", " ");

			}
			if (p.getVision() != null) {

				vision_programa = p.getVision().replaceAll("\n", " ");

			}

			contacto_programa = p.getContacto();
			id_programa = p.getId();
			id_facultad = p.getFacultad().getId();

			cantidad_grupos = BigInteger.valueOf(grupoDAO.getAllGruposPrograma(id_programa).size());
			cantidad_investigadores = BigInteger.valueOf(investigadorDAO.getInvestigadoresPrograma(id_programa).size());

			int suma = 0;

			for (int j = 0; j < 7; j++) {
				if (j != 5) {
					suma += produccionDAO.getCantidadProduccionesProgramaPorTipo(String.valueOf(id_programa), j + "")
							.intValue();
				} else {
					suma += produccionDAO.getCantidadProduccionesProgramaPorTipo(String.valueOf(id_programa), j + "")
							.intValue();
				}

			}

			suma += produccionDAO.getCantidadProduccionesProgramaPorSubTipo(String.valueOf(id_programa), "32")
					.intValue();
			suma += produccionDAO.getCantidadProduccionesProgramaPorSubTipo(String.valueOf(id_programa), "33")
					.intValue();

			cantidad_producciones = new BigInteger(String.valueOf(suma));

			break;
		}
		case "c": {
			Centro c = centroDAO.getCentroById(Long.parseLong(id));
			centro = true;
			title_centro = c.getNombre();

			info_general = c.getInformaciongeneral().replaceAll("\n", " ");

			contacto_centro = utilidades.validateStringOrNull(c.getContacto());
			id_centro = c.getId();
			id_facultad = c.getFacultad().getId();
			cantidad_grupos = BigInteger.valueOf(grupoDAO.getAllGruposCentro(id_centro).size());
			cantidad_investigadores = BigInteger.valueOf(investigadorDAO.getInvestigadoresCentro(id_centro).size());

			int suma = 0;

			for (int j = 0; j < 7; j++) {
				suma += produccionDAO.getCantidadProduccionesCentroPorTipo(String.valueOf(id_centro), j + "")
						.intValue();

			}

			suma += produccionDAO.getCantidadProduccionesCentroPorSubTipo(String.valueOf(id_centro), "32").intValue();
			suma += produccionDAO.getCantidadProduccionesCentroPorSubTipo(String.valueOf(id_centro), "33").intValue();

			cantidad_producciones = new BigInteger(String.valueOf(suma));

			break;
		}
		case "g": {
			Grupo g = grupoDAO.findOne(Long.parseLong(id));
			grupo = true;
			title_grupo = g.getNombre();

			if (g.getInformacionGeneral() != null) {

				info_general = g.getInformacionGeneral().replaceAll("\n", " ");

			}

			contacto_grupo = utilidades.validateStringOrNull(g.getContacto());
			id_grupo = g.getId();
			id_facultad = g.getProgramas().get(0).getFacultad().getId();

			cantidad_investigadores = BigInteger.valueOf(investigadorDAO.getInvestigadoresGrupo(id_grupo).size());
			int suma = 0;

			for (int j = 0; j < 7; j++) {
				if (j != 5) {
					suma += produccionDAO.getCantidadProduccionesGrupoPorTipo(String.valueOf(id_grupo), j + "")
							.intValue();
				} else {
					suma += produccionDAO.getCantidadProduccionesGrupoPorTipo(String.valueOf(id_grupo), j + "")
							.intValue();
				}

			}

			suma += produccionDAO.getCantidadProduccionesGrupoPorSubTipo(String.valueOf(id_grupo), "32").intValue();
			suma += produccionDAO.getCantidadProduccionesGrupoPorSubTipo(String.valueOf(id_grupo), "33").intValue();

			cantidad_producciones = new BigInteger(String.valueOf(suma));

			break;
		}
		case "i": {
			Investigador i = investigadorDAO.findOne(Long.parseLong(id));
			investigador = true;
			nombre_investigador = utilidades.convertToTitleCaseIteratingChars(i.getNombre());
			id_investigador = i.getId();

			int suma = 0;

			for (int j = 0; j < 7; j++) {
				if (j != 5) {
					suma += produccionDAO
							.getCantidadProduccionesInvestigadorPorTipo(String.valueOf(id_investigador), j + "")
							.intValue();
				} else {
					suma += produccionDAO
							.getCantidadProduccionesInvestigadorPorTipo(String.valueOf(id_investigador), j + "")
							.intValue();
				}

			}

			suma += produccionDAO.getCantidadProduccionesInvestigadorPorSubTipo(String.valueOf(id_investigador), "32")
					.intValue();
			suma += produccionDAO.getCantidadProduccionesInvestigadorPorSubTipo(String.valueOf(id_investigador), "33")
					.intValue();

			cantidad_producciones = new BigInteger(String.valueOf(suma));

			break;
		}
		default:
			universidad = true;

			break;
		}

		int aux = 1;
		InputStream input = null;

		while (true) {
			Map<String, Object> parametros = new HashMap<>();

			if (universidad) {

				if (type.equals(Util.UNIVERSITY_PARAM_ID)) {
					System.out.println("type u equals to u UNIVERSITY_PARAM_ID");

					input = this.getClass().getResourceAsStream("/reportes/" + type + "_" + id + "_" + aux + ".jasper");
					if (input == null) {
						System.out.println("Archivo jasper no encontrado: " +"/reportes/" + type + "_" + id + "_" + aux + ".jasper" );
						break;
					} else {
						System.out.println("Archivo jasper encontrado: " + "/reportes/" + type + "_" + id + "_" + aux + ".jasper");
					}

				}
			} else {
				if (facultad) {
					if (aux == 4 && cantidad_grupos.intValue() == 0) {
						aux++;
						continue;
					}
					if ((aux == 6 || aux == 7) && cantidad_investigadores.intValue() == 0) {
						aux++;
						continue;
					}
					if (aux == 9 && cantidad_producciones.intValue() == 0) {
						break;
					}
					parametros.put("title_facultad", title_facultad);
					parametros.put("mision_facultad", mision_facultad);
					parametros.put("vision_facultad", vision_facultad);
					parametros.put("contacto_facultad", contacto_facultad);
					parametros.put("id_facultad", id_facultad);

					input = this.getClass().getResourceAsStream("/reportes/" + type + "_" + aux + ".jasper");

				} else if (programa) {

					if (aux == 4 && cantidad_grupos.intValue() == 0) {
						aux++;
						continue;
					}
					if ((aux == 5 || aux == 6) && cantidad_investigadores.intValue() == 0) {
						aux++;
						continue;
					}
					if (aux == 8 && cantidad_producciones.intValue() == 0) {
						break;
					}
					parametros.put("title_programa", title_programa);
					parametros.put("mision_programa", mision_programa);
					parametros.put("vision_programa", vision_programa);
					parametros.put("contacto_programa", contacto_programa);
					parametros.put("id_facultad", id_facultad);
					parametros.put("id_programa", id_programa);

					input = this.getClass().getResourceAsStream("/reportes/" + type + "_" + aux + ".jasper");

				} else if (centro) {
					if (aux == 4 && cantidad_grupos.intValue() == 0) {
						aux++;
						continue;
					}
					if ((aux == 5 || aux == 6) && cantidad_investigadores.intValue() == 0) {
						aux++;
						continue;
					}
					if (aux == 8 && cantidad_producciones.intValue() == 0) {
						break;
					}
					parametros.put("title_centro", title_centro);
					parametros.put("info_general", info_general);
					parametros.put("contacto_centro", contacto_centro);
					parametros.put("id_facultad", id_facultad);
					parametros.put("id_centro", id_centro);

					input = this.getClass().getResourceAsStream("/reportes/" + type + "_" + aux + ".jasper");

				} else if (grupo) {

					if ((aux == 4 || aux == 5) && cantidad_investigadores.intValue() == 0) {
						aux++;
						continue;
					}
					if (aux == 7 && cantidad_producciones.intValue() == 0) {
						break;
					}
					parametros.put("title_grupo", title_grupo);
					parametros.put("info_general", info_general);
					parametros.put("contacto_grupo", contacto_grupo);
					parametros.put("id_facultad", id_facultad);
					parametros.put("id_grupo", id_grupo);

					input = this.getClass().getResourceAsStream("/reportes/" + type + "_" + aux + ".jasper");

				} else if (investigador) {
					if (aux == 3 && cantidad_producciones.intValue() == 0) {
						break;
					}
					parametros.put("nombre_investigador", nombre_investigador);
					parametros.put("id_investigador", id_investigador);

					input = this.getClass().getResourceAsStream("/reportes/" + type + "_" + aux + ".jasper");

				}
			}

			if (input == null) {
				break;
			} else {
				aux++;
			}

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(input);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);
			jasperPrintList.add(jasperPrint);

			System.out.println("Cantidad de reportes configurados: " + jasperPrintList.size());


		}

	}

	@GetMapping("/estadisticas")
	public String getEstadisticas(
			@RequestParam(name = Util.PARAM_TYPE, required = false, defaultValue = Util.UNIVERSITY_PARAM_ID) String type,
			@RequestParam(name = Util.PARAM_ID, required = false, defaultValue = Util.PARAM_UNIVERSITY_LEVEL_ID) String id,
			Model model) {

		String[] datos = getDatosEstadisticas(id, type);
		model.addAttribute(Util.PARAM_ID, id);
		model.addAttribute("tipo", type);
		model.addAttribute("nombre", datos[0]);
		model.addAttribute("color", datos[1]);
		model.addAttribute("colorTituloBoton", datos[2]);
		model.addAttribute("colorTotalBoton", datos[3]);

		List<String> stringinfo = formatoCadena(datos[4]);
		List<String> stringcontacto = formatoCadena(datos[5]);
		model.addAttribute("infogeneral", stringinfo);
		model.addAttribute("contacto", stringcontacto);

		switch (type) {
		case Util.FACULTY_PARAM_ID:

			model.addAttribute("mision", datos[4]);
			model.addAttribute("vision", datos[6]);
			return getEstadisticasFacultad(id, model);

		case "p":
			List<String> mision = formatoCadena(datos[6]);
			List<String> vision = formatoCadena(datos[7]);
			model.addAttribute("mision", mision);
			model.addAttribute("vision", vision);

			return getEstadisticasProgramas(id, model);

		case "c":

			return getEstadisticasCentros(id, model);

		case "g":
			model.addAttribute("infogeneral", datos[4]);
			return getEstadisticasGrupo(id, model);

		case "i":
			return getEstadisticasInvestigador(id, model);

		default:
			return getEstadisticasUniquindio(model);
		}

	}

	/**
	 * @param id   id obtenido
	 * @param type tipo obtenido
	 * @return datos estadisticos
	 */
	public String[] getDatosEstadisticas(String id, String type) {

		String[] datos = new String[8];

		switch (type) {
		case Util.FACULTY_PARAM_ID:
			Facultad f = facultadDAO.getFacultadById(Long.parseLong(id));

			datos[0] = "Estadísticas Generales de la Facultad de " + f.getNombre();
			datos[1] = "card-" + f.getId();
			datos[2] = "btn-title-grid-" + f.getId();
			datos[3] = "btn-total-grid-" + f.getId();
			datos[4] = f.getMision();
			datos[5] = f.getContacto();
			datos[6] = f.getVision();
			break;
		case "p":
			Programa p = programaDAO.getProgramaById(Long.parseLong(id));

			datos[0] = p.getNombre();
			datos[1] = "card-" + p.getFacultad().getId();
			datos[2] = "btn-title-grid-" + p.getFacultad().getId();
			datos[3] = "btn-total-grid-" + p.getFacultad().getId();
			datos[4] = p.getInformaciongeneral();
			datos[5] = p.getContacto();
			datos[6] = p.getMision();
			datos[7] = p.getVision();
			break;
		case "c":
			Centro c = centroDAO.getCentroById(Long.parseLong(id));

			datos[0] = c.getNombre();
			datos[1] = "card-" + c.getFacultad().getId();
			datos[2] = "btn-title-grid-" + c.getFacultad().getId();
			datos[3] = "btn-total-grid-" + c.getFacultad().getId();
			datos[4] = c.getInformaciongeneral();
			datos[5] = c.getContacto();

			break;
		case "g":
			Grupo g = grupoDAO.findOne(Long.parseLong(id));

			datos[0] = g.getNombre();
			datos[1] = "card-" + g.getProgramas().get(0).getFacultad().getId();
			datos[2] = "btn-title-grid-" + g.getProgramas().get(0).getFacultad().getId();
			datos[3] = "btn-total-grid-" + g.getProgramas().get(0).getFacultad().getId();
			datos[4] = g.getInformacionGeneral();
			datos[5] = g.getContacto();

			break;
		case "i":
			Investigador i = investigadorDAO.findOne(Long.parseLong(id));

			datos[0] = i.getNombre();
			datos[1] = "card-0";
			datos[2] = "btn-title-grid-0";
			datos[3] = "btn-total-grid-0";
			datos[4] = "";
			datos[5] = "";
			break;
		default:
			datos[4] = "";
			datos[5] = "";

			break;
		}

		return datos;

	}

	public int calcularTamanio(int tamanio) {
		for (int i = 5; i > 1; i--) {
			if ((tamanio % i) == 0) {
				return i;
			}
		}
		return calcularTamanio(tamanio + 1);
	}

	/**
	 * metodo que permite dar formato a una cadena quitando - y separandola por \n
	 *
	 * @param cadena cadena a formatear
	 * @return lista con posiciones por \n
	 */
	public List<String> formatoCadena(String cadena) {

		List<String> resultado = new ArrayList<>();

		if (cadena != null && !cadena.equals("")) {
			String[] splitcadena = cadena.split("\n");

			for (String string : splitcadena) {

				if (!string.equals("") && !string.contains("N/A")) {

					resultado.add(string);

				}
			}
		}
		return resultado;

	}

	public String getEstadisticasFacultad(String id, Model model) {
		// ------Llamado a las consultas en la base de datos para las
		// ------facultades-----------------------------------------------------------------------
		List<BigInteger> resumen = facultadDAO.getResumenGeneral(Long.parseLong(id));

		// ------Llamado a las consultas en la base de datos para
		// producciones-----------------------------------------------------------------------
		model.addAttribute("cantidadActividadesDeFormacion",
				produccionDAO.getCantidadProduccionesFacultadPorTipo(id, Util.PARAM_UNIVERSITY_LEVEL_ID));
		model.addAttribute("cantidadActividadesEvaluador",
				produccionDAO.getCantidadProduccionesFacultadPorTipo(id, "1"));
		model.addAttribute("cantidadApropiacionSocial", produccionDAO.getCantidadProduccionesFacultadPorTipo(id, "2"));
		model.addAttribute("cantidadProduccionesBibliograficas",
				produccionDAO.getCantidadProduccionesBFacultadPorTipo(id, "3"));
		model.addAttribute("cantidadTecnicasTecnologicas",
				produccionDAO.getCantidadProduccionesFacultadPorTipo(id, "4"));
		model.addAttribute("cantidadProduccionesArte",
				String.valueOf(produccionDAO.getCantidadProduccionesFacultadPorTipo(id, "6")));
		model.addAttribute("cantidadProduccionesDemasTrabajos",
				produccionDAO.getCantidadProduccionesFacultadPorSubTipo(id, "32"));
		model.addAttribute("cantidadProduccionesProyectos",
				produccionDAO.getCantidadProduccionesFacultadPorSubTipo(id, "33"));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// basicas-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicos", resumen.get(0));
		model.addAttribute("cantidadProgramasDoctorado", resumen.get(1));
		model.addAttribute("cantidadProgramasMaestria", resumen.get(2));
		model.addAttribute("cantidadProgramasEspecializacion", resumen.get(3));
		model.addAttribute("cantidadCentrosInvestigacion", resumen.get(4));
		model.addAttribute("cantidadGruposInvestigacion", resumen.get(5));
		model.addAttribute("cantidadLineasInvestigacion", resumen.get(6));
		model.addAttribute("cantidadInvestigadores", resumen.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1", resumen.get(8));
		model.addAttribute("cantidadGruposInvestigacionA", resumen.get(9));
		model.addAttribute("cantidadGruposInvestigacionB", resumen.get(10));
		model.addAttribute("cantidadGruposInvestigacionC", resumen.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidos", resumen.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocido", resumen.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritos", resumen.get(14));
		model.addAttribute("cantidadInvestigadoresSenior", resumen.get(15));
		model.addAttribute("cantidadInvestigadoresAsociados", resumen.get(16));
		model.addAttribute("cantidadInvestigadoresJunior", resumen.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoria", resumen.get(18));

		model.addAttribute("cantidadDocentesDoctores", resumen.get(19));
		model.addAttribute("cantidadDocentesMagister", resumen.get(20));
		model.addAttribute("cantidadDocentesEspecialistas", resumen.get(21));
		model.addAttribute("cantidadDocentesPregrado", resumen.get(22));

		model.addAttribute("cantidadProgramasTotal",
				resumen.get(0).add(resumen.get(1).add(resumen.get(2).add(resumen.get(3)))));

		model.addAttribute("cantidadGruposTotal", resumen.get(8).add(
				resumen.get(9).add(resumen.get(10).add(resumen.get(11).add(resumen.get(12)).add(resumen.get(13))))));

		model.addAttribute("cantidadInvestigadoresTotal",
				resumen.get(14).add(resumen.get(15).add(resumen.get(16).add(resumen.get(17).add(resumen.get(18))))));

		model.addAttribute("cantidadDocentesTotal",
				resumen.get(19).add(resumen.get(20).add(resumen.get(21).add(resumen.get(22)))));

		List<Investigador> investigadores_facultad = utilidades
				.agregarPertenenciaInves(investigadorDAO.getAllInvestigadoresInternosFacultad(Long.parseLong(id)));

		List<Investigador> investigadores_facultad_Adm = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_ADMINISTRATIVO);
		List<Investigador> investigadores_facultad_DP = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_DOCENTE_PLANTA);
		List<Investigador> investigadores_facultad_DC = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		List<Investigador> investigadores_facultad_DO = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		List<Investigador> investigadores_facultad_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_EXTERNO);
		List<Investigador> investigadores_facultad_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_ESTUDIANTE);
		List<Investigador> investigadores_facultad_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_facultad, Util.PERTENENCIA_INDEFINIDO);

		model.addAttribute("num_inves_Adm", investigadores_facultad_Adm.size());
		model.addAttribute("num_inves_DP", investigadores_facultad_DP.size());
		model.addAttribute("num_inves_DC", investigadores_facultad_DC.size());
		model.addAttribute("num_inves_DO", investigadores_facultad_DO.size());
		model.addAttribute("num_inves_IE", investigadores_facultad_IE.size());
		model.addAttribute("num_inves_EI", investigadores_facultad_EI.size());
		model.addAttribute("num_inves_IND", investigadores_facultad_IND.size());

		model.addAttribute("peAdm", "adm");
		model.addAttribute("peDp", "dp");
		model.addAttribute("peDc", "dc");
		model.addAttribute("peDo", "do");
		model.addAttribute("peIe", "ie");
		model.addAttribute("peEi", "ei");
		model.addAttribute("peInd", "ind");

		model.addAttribute("programaAcademico", Util.UNDERGRADUATE_PROGRAM_PARAM_ID);
		model.addAttribute("programaDoctorado", "pd");
		model.addAttribute("programaMagister", "pm");
		model.addAttribute("programaEspecializacion", "pe");
		model.addAttribute("centrosInvestigacion", "c");
		model.addAttribute("gruposInvestigacion", "g");
		model.addAttribute("lineasInvestigacion", "l");
		model.addAttribute("investigadores", "i");

		model.addAttribute("categoriaA1", "ca1");
		model.addAttribute("categoriaA", "ca");
		model.addAttribute("categoriaB", "cb");
		model.addAttribute("categoriaC", "cc");
		model.addAttribute("categoriaReconocido", "cr");
		model.addAttribute("categoriaNoReconocido", "cnr");

		model.addAttribute("investigadorEmerito", "ie");
		model.addAttribute("investigadorSenior", "is");
		model.addAttribute("investigadorAsociado", "ia");
		model.addAttribute("investigadorJunior", "ij");
		model.addAttribute("investigadorSinCategoria", "isc");

		model.addAttribute("formacionDoctor", "fd");
		model.addAttribute("formacionMagister", "fm");
		model.addAttribute("formacionEspecialista", "fe");
		model.addAttribute("formacionPregrado", "fp");
		model.addAttribute("docentes", "d");

		model.addAttribute(Util.PARAM_UNIVERSITY_LEVEL, Util.PARAM_UNIVERSITY_LEVEL_ID);
		model.addAttribute("idFacultad", id);

		return "estadisticas/facultades";
	}

	public String getEstadisticasProgramas(String id, Model model) {
		// ------Llamado a las consultas en la base de datos para las
		// ------facultades-----------------------------------------------------------------------
		List<BigInteger> resumen = programaDAO.getResumenGeneralPrograma(Long.parseLong(id));

		// ------Llamado a las consultas en la base de datos para
		// producciones-----------------------------------------------------------------------
		model.addAttribute("cantidadActividadesDeFormacion",
				produccionDAO.getCantidadProduccionesProgramaPorTipo(id, Util.PARAM_UNIVERSITY_LEVEL_ID));
		model.addAttribute("cantidadActividadesEvaluador",
				produccionDAO.getCantidadProduccionesProgramaPorTipo(id, "1"));
		model.addAttribute("cantidadApropiacionSocial", produccionDAO.getCantidadProduccionesProgramaPorTipo(id, "2"));
		model.addAttribute("cantidadProduccionesBibliograficas",
				produccionDAO.getCantidadProduccionesBProgramaPorTipo(id, "3"));
		model.addAttribute("cantidadTecnicasTecnologicas",
				produccionDAO.getCantidadProduccionesProgramaPorTipo(id, "4"));
		model.addAttribute("cantidadProduccionesArte",
				String.valueOf(produccionDAO.getCantidadProduccionesProgramaPorTipo(id, "6")));
		model.addAttribute("cantidadProduccionesDemasTrabajos",
				produccionDAO.getCantidadProduccionesProgramaPorSubTipo(id, "32"));
		model.addAttribute("cantidadProduccionesProyectos",
				produccionDAO.getCantidadProduccionesProgramaPorSubTipo(id, "33"));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// basicas-----------------------------------------------------------------------
		model.addAttribute("cantidadGruposInvestigacion", resumen.get(0));
		model.addAttribute("cantidadLineasInvestigacion", resumen.get(1));
		model.addAttribute("cantidadInvestigadores", resumen.get(2));
		model.addAttribute("cantidadGruposInvestigacionA1", resumen.get(3));
		model.addAttribute("cantidadGruposInvestigacionA", resumen.get(4));
		model.addAttribute("cantidadGruposInvestigacionB", resumen.get(5));
		model.addAttribute("cantidadGruposInvestigacionC", resumen.get(6));
		model.addAttribute("cantidadGruposInvestigacionReconocidos", resumen.get(7));
		model.addAttribute("cantidadGruposInvestigacionNoReconocido", resumen.get(8));
		model.addAttribute("cantidadInvestigadoresEmeritos", resumen.get(9));
		model.addAttribute("cantidadInvestigadoresSenior", resumen.get(10));
		model.addAttribute("cantidadInvestigadoresAsociados", resumen.get(11));
		model.addAttribute("cantidadInvestigadoresJunior", resumen.get(12));
		model.addAttribute("cantidadInvestigadoresSinCategoria", resumen.get(13));
		model.addAttribute("cantidadDocentesDoctores", resumen.get(14));
		model.addAttribute("cantidadDocentesMagister", resumen.get(15));
		model.addAttribute("cantidadDocentesEspecialistas", resumen.get(16));
		model.addAttribute("cantidadDocentesPregrado", resumen.get(17));

		model.addAttribute("cantidadGruposTotal", resumen.get(3)
				.add(resumen.get(4).add(resumen.get(5).add(resumen.get(6).add(resumen.get(7)).add(resumen.get(8))))));

		model.addAttribute("cantidadInvestigadoresTotal",
				resumen.get(9).add(resumen.get(10).add(resumen.get(11).add(resumen.get(12).add(resumen.get(13))))));

		model.addAttribute("cantidadDocentesTotal",
				resumen.get(14).add(resumen.get(15).add(resumen.get(16).add(resumen.get(17)))));

		List<Investigador> investigadores_programa = utilidades
				.agregarPertenenciaInves(investigadorDAO.getAllInvestigadoresInternosPrograma(Long.parseLong(id)));

		List<Investigador> investigadores_programa_Adm = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_ADMINISTRATIVO);
		List<Investigador> investigadores_programa_DP = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_DOCENTE_PLANTA);
		List<Investigador> investigadores_programa_DC = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		List<Investigador> investigadores_programa_DO = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		List<Investigador> investigadores_programa_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_EXTERNO);
		List<Investigador> investigadores_programa_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_ESTUDIANTE);
		List<Investigador> investigadores_programa_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_programa, Util.PERTENENCIA_INDEFINIDO);

		model.addAttribute("num_inves_Adm", investigadores_programa_Adm.size());
		model.addAttribute("num_inves_DP", investigadores_programa_DP.size());
		model.addAttribute("num_inves_DC", investigadores_programa_DC.size());
		model.addAttribute("num_inves_DO", investigadores_programa_DO.size());
		model.addAttribute("num_inves_IE", investigadores_programa_IE.size());
		model.addAttribute("num_inves_EI", investigadores_programa_EI.size());
		model.addAttribute("num_inves_IND", investigadores_programa_IND.size());

		model.addAttribute("peAdm", "adm");
		model.addAttribute("peDp", "dp");
		model.addAttribute("peDc", "dc");
		model.addAttribute("peDo", "do");
		model.addAttribute("peIe", "ie");
		model.addAttribute("peEi", "ei");
		model.addAttribute("peInd", "ind");

		model.addAttribute("gruposInvestigacion", "g");
		model.addAttribute("lineasInvestigacion", "l");
		model.addAttribute("investigadores", "i");

		model.addAttribute("categoriaA1", "ca1");
		model.addAttribute("categoriaA", "ca");
		model.addAttribute("categoriaB", "cb");
		model.addAttribute("categoriaC", "cc");
		model.addAttribute("categoriaReconocido", "cr");
		model.addAttribute("categoriaNoReconocido", "cnr");

		model.addAttribute("investigadorEmerito", "ie");
		model.addAttribute("investigadorSenior", "is");
		model.addAttribute("investigadorAsociado", "ia");
		model.addAttribute("investigadorJunior", "ij");
		model.addAttribute("investigadorSinCategoria", "isc");

		model.addAttribute("formacionDoctor", "fd");
		model.addAttribute("formacionMagister", "fm");
		model.addAttribute("formacionEspecialista", "fe");
		model.addAttribute("formacionPregrado", "fp");
		model.addAttribute("docentes", "d");

		model.addAttribute(Util.PARAM_UNIVERSITY_LEVEL, Util.PARAM_UNIVERSITY_LEVEL_ID);
		model.addAttribute("idPrograma", id);

		return "estadisticas/programas";
	}

	public String getEstadisticasCentros(String id, Model model) {
		// ------Llamado a las consultas en la base de datos para las
		// ------facultades-----------------------------------------------------------------------
		List<BigInteger> resumen = centroDAO.getResumenGeneralCentros(Long.parseLong(id));

		// ------Llamado a las consultas en la base de datos para
		// producciones-----------------------------------------------------------------------
		model.addAttribute("cantidadActividadesDeFormacion",
				produccionDAO.getCantidadProduccionesCentroPorTipo(id, Util.PARAM_UNIVERSITY_LEVEL_ID));
		model.addAttribute("cantidadActividadesEvaluador", produccionDAO.getCantidadProduccionesCentroPorTipo(id, "1"));
		model.addAttribute("cantidadApropiacionSocial", produccionDAO.getCantidadProduccionesCentroPorTipo(id, "2"));
		model.addAttribute("cantidadProduccionesBibliograficas",
				produccionDAO.getCantidadProduccionesBCentroPorTipo(id, "3"));
		model.addAttribute("cantidadTecnicasTecnologicas", produccionDAO.getCantidadProduccionesCentroPorTipo(id, "4"));
		model.addAttribute("cantidadProduccionesArte",
				String.valueOf(produccionDAO.getCantidadProduccionesCentroPorTipo(id, "6")));
		model.addAttribute("cantidadProduccionesDemasTrabajos",
				produccionDAO.getCantidadProduccionesCentroPorSubTipo(id, "32"));
		model.addAttribute("cantidadProduccionesProyectos",
				produccionDAO.getCantidadProduccionesCentroPorSubTipo(id, "33"));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// basicas-----------------------------------------------------------------------
		model.addAttribute("cantidadGruposInvestigacion", resumen.get(0));
		model.addAttribute("cantidadLineasInvestigacion", resumen.get(1));
		model.addAttribute("cantidadInvestigadores", resumen.get(2));
		model.addAttribute("cantidadGruposInvestigacionA1", resumen.get(3));
		model.addAttribute("cantidadGruposInvestigacionA", resumen.get(4));
		model.addAttribute("cantidadGruposInvestigacionB", resumen.get(5));
		model.addAttribute("cantidadGruposInvestigacionC", resumen.get(6));
		model.addAttribute("cantidadGruposInvestigacionReconocidos", resumen.get(7));
		model.addAttribute("cantidadGruposInvestigacionNoReconocido", resumen.get(8));
		model.addAttribute("cantidadInvestigadoresEmeritos", resumen.get(9));
		model.addAttribute("cantidadInvestigadoresSenior", resumen.get(10));
		model.addAttribute("cantidadInvestigadoresAsociados", resumen.get(11));
		model.addAttribute("cantidadInvestigadoresJunior", resumen.get(12));
		model.addAttribute("cantidadInvestigadoresSinCategoria", resumen.get(13));
		model.addAttribute("cantidadDocentesDoctores", resumen.get(14));
		model.addAttribute("cantidadDocentesMagister", resumen.get(15));
		model.addAttribute("cantidadDocentesEspecialistas", resumen.get(16));
		model.addAttribute("cantidadDocentesPregrado", resumen.get(17));

		model.addAttribute("cantidadGruposTotal", resumen.get(3)
				.add(resumen.get(4).add(resumen.get(5).add(resumen.get(6).add(resumen.get(7)).add(resumen.get(8))))));

		model.addAttribute("cantidadInvestigadoresTotal",
				resumen.get(9).add(resumen.get(10).add(resumen.get(11).add(resumen.get(12).add(resumen.get(13))))));

		model.addAttribute("cantidadDocentesTotal",
				resumen.get(14).add(resumen.get(15).add(resumen.get(16).add(resumen.get(17)))));

		model.addAttribute("gruposInvestigacion", "g");
		model.addAttribute("lineasInvestigacion", "l");
		model.addAttribute("investigadores", "i");

		model.addAttribute("categoriaA1", "ca1");
		model.addAttribute("categoriaA", "ca");
		model.addAttribute("categoriaB", "cb");
		model.addAttribute("categoriaC", "cc");
		model.addAttribute("categoriaReconocido", "cr");
		model.addAttribute("categoriaNoReconocido", "cnr");

		model.addAttribute("investigadorEmerito", "ie");
		model.addAttribute("investigadorSenior", "is");
		model.addAttribute("investigadorAsociado", "ia");
		model.addAttribute("investigadorJunior", "ij");
		model.addAttribute("investigadorSinCategoria", "isc");

		model.addAttribute("formacionDoctor", "fd");
		model.addAttribute("formacionMagister", "fm");
		model.addAttribute("formacionEspecialista", "fe");
		model.addAttribute("formacionPregrado", "fp");
		model.addAttribute("docentes", "d");

		model.addAttribute(Util.PARAM_UNIVERSITY_LEVEL, Util.PARAM_UNIVERSITY_LEVEL_ID);
		model.addAttribute("idCentro", id);

		List<Investigador> investigadores_centro = utilidades
				.agregarPertenenciaInves(investigadorDAO.getAllInvestigadoresInternosCentro(Long.parseLong(id)));

		List<Investigador> investigadores_centro_Adm = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_ADMINISTRATIVO);
		List<Investigador> investigadores_centro_DP = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_DOCENTE_PLANTA);
		List<Investigador> investigadores_centro_DC = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		List<Investigador> investigadores_centro_DO = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		List<Investigador> investigadores_centro_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_EXTERNO);
		List<Investigador> investigadores_centro_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_ESTUDIANTE);
		List<Investigador> investigadores_centro_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_centro, Util.PERTENENCIA_INDEFINIDO);

		model.addAttribute("num_inves_Adm", investigadores_centro_Adm.size());
		model.addAttribute("num_inves_DP", investigadores_centro_DP.size());
		model.addAttribute("num_inves_DC", investigadores_centro_DC.size());
		model.addAttribute("num_inves_DO", investigadores_centro_DO.size());
		model.addAttribute("num_inves_IE", investigadores_centro_IE.size());
		model.addAttribute("num_inves_EI", investigadores_centro_EI.size());
		model.addAttribute("num_inves_IND", investigadores_centro_IND.size());

		model.addAttribute("peAdm", "adm");
		model.addAttribute("peDp", "dp");
		model.addAttribute("peDc", "dc");
		model.addAttribute("peDo", "do");
		model.addAttribute("peIe", "ie");
		model.addAttribute("peEi", "ei");
		model.addAttribute("peInd", "ind");

		return "estadisticas/centros";
	}

	public String getEstadisticasGrupo(String id, Model model) {
		// ------Llamado a las consultas en la base de datos para las
		// ------facultades-----------------------------------------------------------------------
		List<BigInteger> resumen = grupoDAO.getResumenGeneralGrupo(Long.parseLong(id));

		// ------Llamado a las consultas en la base de datos para
		// producciones-----------------------------------------------------------------------
		model.addAttribute("cantidadActividadesDeFormacion",
				produccionDAO.getCantidadProduccionesGrupoPorTipo(id, Util.PARAM_UNIVERSITY_LEVEL_ID));
		model.addAttribute("cantidadActividadesEvaluador", produccionDAO.getCantidadProduccionesGrupoPorTipo(id, "1"));
		model.addAttribute("cantidadApropiacionSocial", produccionDAO.getCantidadProduccionesGrupoPorTipo(id, "2"));
		model.addAttribute("cantidadProduccionesBibliograficas",
				produccionDAO.getCantidadProduccionesBGrupoPorTipo(id, "3"));
		model.addAttribute("cantidadTecnicasTecnologicas", produccionDAO.getCantidadProduccionesGrupoPorTipo(id, "4"));
		model.addAttribute("cantidadProduccionesArte",
				String.valueOf(produccionDAO.getCantidadProduccionesGrupoPorTipo(id, "6")));
		model.addAttribute("cantidadProduccionesDemasTrabajos",
				produccionDAO.getCantidadProduccionesGrupoPorSubTipo(id, "32"));
		model.addAttribute("cantidadProduccionesProyectos",
				produccionDAO.getCantidadProduccionesGrupoPorSubTipo(id, "33"));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// basicas-----------------------------------------------------------------------
		model.addAttribute("cantidadLineasInvestigacion", resumen.get(0));
		model.addAttribute("cantidadInvestigadores", resumen.get(1));
		model.addAttribute("cantidadInvestigadoresEmeritos", resumen.get(2));
		model.addAttribute("cantidadInvestigadoresSenior", resumen.get(3));
		model.addAttribute("cantidadInvestigadoresAsociados", resumen.get(4));
		model.addAttribute("cantidadInvestigadoresJunior", resumen.get(5));
		model.addAttribute("cantidadInvestigadoresSinCategoria", resumen.get(6));
		model.addAttribute("cantidadDocentesDoctores", resumen.get(7));
		model.addAttribute("cantidadDocentesMagister", resumen.get(8));
		model.addAttribute("cantidadDocentesEspecialistas", resumen.get(9));
		model.addAttribute("cantidadDocentesPregrado", resumen.get(10));

		model.addAttribute("cantidadInvestigadoresTotal",
				resumen.get(2).add(resumen.get(3).add(resumen.get(4).add(resumen.get(5).add(resumen.get(6))))));

		model.addAttribute("cantidadDocentesTotal",
				resumen.get(7).add(resumen.get(8).add(resumen.get(9).add(resumen.get(10)))));

		model.addAttribute("gruposInvestigacion", "g");
		model.addAttribute("lineasInvestigacion", "l");
		model.addAttribute("investigadores", "i");

		model.addAttribute("investigadorEmerito", "ie");
		model.addAttribute("investigadorSenior", "is");
		model.addAttribute("investigadorAsociado", "ia");
		model.addAttribute("investigadorJunior", "ij");
		model.addAttribute("investigadorSinCategoria", "isc");

		model.addAttribute("formacionDoctor", "fd");
		model.addAttribute("formacionMagister", "fm");
		model.addAttribute("formacionEspecialista", "fe");
		model.addAttribute("formacionPregrado", "fp");
		model.addAttribute("docentes", "d");

		model.addAttribute(Util.PARAM_UNIVERSITY_LEVEL, Util.PARAM_UNIVERSITY_LEVEL_ID);
		model.addAttribute("idGrupo", id);

		List<Investigador> investigadores_grupo = utilidades
				.agregarPertenenciaInves(investigadorDAO.getAllInvestigadoresInternosGrupo(Long.parseLong(id)));

		List<Investigador> investigadores_grupo_Adm = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_ADMINISTRATIVO);
		List<Investigador> investigadores_grupo_DP = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_DOCENTE_PLANTA);
		List<Investigador> investigadores_grupo_DC = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		List<Investigador> investigadores_grupo_DO = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		List<Investigador> investigadores_grupo_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_EXTERNO);
		List<Investigador> investigadores_grupo_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_ESTUDIANTE);
		List<Investigador> investigadores_grupo_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_grupo, Util.PERTENENCIA_INDEFINIDO);

		model.addAttribute("num_inves_Adm", investigadores_grupo_Adm.size());
		model.addAttribute("num_inves_DP", investigadores_grupo_DP.size());
		model.addAttribute("num_inves_DC", investigadores_grupo_DC.size());
		model.addAttribute("num_inves_DO", investigadores_grupo_DO.size());
		model.addAttribute("num_inves_IE", investigadores_grupo_IE.size());
		model.addAttribute("num_inves_EI", investigadores_grupo_EI.size());
		model.addAttribute("num_inves_IND", investigadores_grupo_IND.size());

		model.addAttribute("peAdm", "adm");
		model.addAttribute("peDp", "dp");
		model.addAttribute("peDc", "dc");
		model.addAttribute("peDo", "do");
		model.addAttribute("peIe", "ie");
		model.addAttribute("peEi", "ei");
		model.addAttribute("peInd", "ind");

		return "estadisticas/grupos";
	}

	public String getEstadisticasInvestigador(String id, Model model) {
		// ------Llamado a las consultas en la base de datos para
		// producciones-----------------------------------------------------------------------
		model.addAttribute("cantidadActividadesDeFormacion",
				produccionDAO.getCantidadProduccionesInvestigadorPorTipo(id, Util.PARAM_UNIVERSITY_LEVEL_ID));
		model.addAttribute("cantidadActividadesEvaluador",
				produccionDAO.getCantidadProduccionesInvestigadorPorTipo(id, "1"));
		model.addAttribute("cantidadApropiacionSocial",
				produccionDAO.getCantidadProduccionesInvestigadorPorTipo(id, "2"));
		model.addAttribute("cantidadProduccionesBibliograficas",
				produccionDAO.getCantidadProduccionesBInvestigadorPorTipo(id, "3"));
		model.addAttribute("cantidadTecnicasTecnologicas",
				produccionDAO.getCantidadProduccionesInvestigadorPorTipo(id, "4"));
		model.addAttribute("cantidadProduccionesArte",
				String.valueOf(produccionDAO.getCantidadProduccionesInvestigadorPorTipo(id, "6")));
		model.addAttribute("cantidadProduccionesDemasTrabajos",
				produccionDAO.getCantidadProduccionesInvestigadorPorSubTipo(id, "32"));
		model.addAttribute("cantidadProduccionesProyectos",
				produccionDAO.getCantidadProduccionesInvestigadorPorSubTipo(id, "33"));

		return "estadisticas/investigadores";
	}

	public String getEstadisticasUniquindio(Model model) {

		model.addAttribute("color", "card-0");
		System.out.println("Estadisticas Uniquindio");
		// ------Llamado a las consultas en la base de datos para
		// producciones-----------------------------------------------------------------------
		model.addAttribute("cantidadActividadesDeFormacion", produccionDAO.getCantidadActividadesFormacion());
		model.addAttribute("cantidadActividadesEvaluador", produccionDAO.getCantidadActividadesEvaluador());
		model.addAttribute("cantidadApropiacionSocial", produccionDAO.getCantidadApropiacionSocial());
		model.addAttribute("cantidadProduccionesBibliograficas", produccionDAO.getCantidadProduccionesBibliograficas());
		model.addAttribute("cantidadTecnicasTecnologicas", produccionDAO.getCantidadTecnicasTecnologicas());
		model.addAttribute("cantidadProduccionesArte", String.valueOf(produccionDAO.getCantidadProduccionesArte()));
		model.addAttribute("cantidadProduccionesDemasTrabajos", produccionDAO.getCantidadProduccionesDemasTrabajos());
		model.addAttribute("cantidadProduccionesProyectos", produccionDAO.getCantidadProduccionesProyectos());

		// ------Llamado a las consultas en la base de datos para las
		// ------facultades-----------------------------------------------------------------------
		List<BigInteger> resumenCienciasBasicas = facultadDAO.getResumenGeneral(Long.parseLong("1"));
		List<BigInteger> resumenEducacion = facultadDAO.getResumenGeneral(Long.parseLong("2"));
		List<BigInteger> resumenCienciasDeLaSalud = facultadDAO.getResumenGeneral(Long.parseLong("3"));
		List<BigInteger> resumenIngenieria = facultadDAO.getResumenGeneral(Long.parseLong("4"));
		List<BigInteger> resumenCienciasHumanas = facultadDAO.getResumenGeneral(Long.parseLong("5"));
		List<BigInteger> resumenAgroindustria = facultadDAO.getResumenGeneral(Long.parseLong("6"));
		List<BigInteger> resumenCienciasEconomicas = facultadDAO.getResumenGeneral(Long.parseLong("7"));

		// Este nÃºmero es usado para indicar la cantidad de investigadores total debido
		// a que con una suma aritmetica normal repetirÃ­a los investigadores
		BigInteger cantidadTotalInvestigadores = facultadDAO.getStats().get(4);

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// ingenieria-----------------------------------------------------------------------

		model.addAttribute("cantidadProgramasAcademicosIngenieria", resumenIngenieria.get(0));
		System.out.println("la cantidad de programas academicos en ingenieria es: " + resumenIngenieria.get(0));
		model.addAttribute("cantidadProgramasDoctoradoIngenieria", resumenIngenieria.get(1));
		System.out.println("la cantidad de programas doctorado en ingenieria es: " + resumenIngenieria.get(1));
		model.addAttribute("cantidadProgramasMaestriaIngenieria", resumenIngenieria.get(2));
		model.addAttribute("cantidadProgramasEspecializacionIngenieria", resumenIngenieria.get(3));
		model.addAttribute("cantidadCentrosInvestigacionIngenieria", resumenIngenieria.get(4));
		model.addAttribute("cantidadGruposInvestigacionIngenieria", resumenIngenieria.get(5));
		model.addAttribute("cantidadLineasInvestigacionIngenieria", resumenIngenieria.get(6));
		model.addAttribute("cantidadInvestigadoresIngenieria", resumenIngenieria.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1Ingenieria", resumenIngenieria.get(8));
		model.addAttribute("cantidadGruposInvestigacionAIngenieria", resumenIngenieria.get(9));
		model.addAttribute("cantidadGruposInvestigacionBIngenieria", resumenIngenieria.get(10));
		model.addAttribute("cantidadGruposInvestigacionCIngenieria", resumenIngenieria.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosIngenieria", resumenIngenieria.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoIngenieria", resumenIngenieria.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosIngenieria", resumenIngenieria.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorIngenieria", resumenIngenieria.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosIngenieria", resumenIngenieria.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorIngenieria", resumenIngenieria.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaIngenieria", resumenIngenieria.get(18));
		model.addAttribute("cantidadDocentesDoctoresIngenieria", resumenIngenieria.get(19));
		model.addAttribute("cantidadDocentesMagisterIngenieria", resumenIngenieria.get(20));
		model.addAttribute("cantidadDocentesEspecialistasIngenieria", resumenIngenieria.get(21));
		model.addAttribute("cantidadDocentesPregradoIngenieria", resumenIngenieria.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// basicas-----------------------------------------------------------------------

		model.addAttribute("cantidadProgramasAcademicosCienciasBasicas", resumenCienciasBasicas.get(0));
		model.addAttribute("cantidadProgramasDoctoradoCienciasBasicas", resumenCienciasBasicas.get(1));
		model.addAttribute("cantidadProgramasMaestriaCienciasBasicas", resumenCienciasBasicas.get(2));
		model.addAttribute("cantidadProgramasEspecializacionCienciasBasicas", resumenCienciasBasicas.get(3));
		model.addAttribute("cantidadCentrosInvestigacionCienciasBasicas", resumenCienciasBasicas.get(4));
		model.addAttribute("cantidadGruposInvestigacionCienciasBasicas", resumenCienciasBasicas.get(5));
		model.addAttribute("cantidadLineasInvestigacionCienciasBasicas", resumenCienciasBasicas.get(6));
		model.addAttribute("cantidadInvestigadoresCienciasBasicas", resumenCienciasBasicas.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1CienciasBasicas", resumenCienciasBasicas.get(8));
		model.addAttribute("cantidadGruposInvestigacionACienciasBasicas", resumenCienciasBasicas.get(9));
		model.addAttribute("cantidadGruposInvestigacionBCienciasBasicas", resumenCienciasBasicas.get(10));
		model.addAttribute("cantidadGruposInvestigacionCCienciasBasicas", resumenCienciasBasicas.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosCienciasBasicas", resumenCienciasBasicas.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoCienciasBasicas", resumenCienciasBasicas.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosCienciasBasicas", resumenCienciasBasicas.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorCienciasBasicas", resumenCienciasBasicas.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosCienciasBasicas", resumenCienciasBasicas.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorCienciasBasicas", resumenCienciasBasicas.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaCienciasBasicas", resumenCienciasBasicas.get(18));
		model.addAttribute("cantidadDocentesDoctoresCienciasBasicas", resumenCienciasBasicas.get(19));
		model.addAttribute("cantidadDocentesMagisterCienciasBasicas", resumenCienciasBasicas.get(20));
		model.addAttribute("cantidadDocentesEspecialistasCienciasBasicas", resumenCienciasBasicas.get(21));
		model.addAttribute("cantidadDocentesPregradoCienciasBasicas", resumenCienciasBasicas.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// educacion-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicosEducacion", resumenEducacion.get(0));
		model.addAttribute("cantidadProgramasDoctoradoEducacion", resumenEducacion.get(1));
		model.addAttribute("cantidadProgramasMaestriaEducacion", resumenEducacion.get(2));
		model.addAttribute("cantidadProgramasEspecializacionEducacion", resumenEducacion.get(3));
		model.addAttribute("cantidadCentrosInvestigacionEducacion", resumenEducacion.get(4));
		model.addAttribute("cantidadGruposInvestigacionEducacion", resumenEducacion.get(5));
		model.addAttribute("cantidadLineasInvestigacionEducacion", resumenEducacion.get(6));
		model.addAttribute("cantidadInvestigadoresEducacion", resumenEducacion.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1Educacion", resumenEducacion.get(8));
		model.addAttribute("cantidadGruposInvestigacionAEducacion", resumenEducacion.get(9));
		model.addAttribute("cantidadGruposInvestigacionBEducacion", resumenEducacion.get(10));
		model.addAttribute("cantidadGruposInvestigacionCEducacion", resumenEducacion.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosEducacion", resumenEducacion.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoEducacion", resumenEducacion.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosEducacion", resumenEducacion.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorEducacion", resumenEducacion.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosEducacion", resumenEducacion.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorEducacion", resumenEducacion.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaEducacion", resumenEducacion.get(18));
		model.addAttribute("cantidadDocentesDoctoresEducacion", resumenEducacion.get(19));
		model.addAttribute("cantidadDocentesMagisterEducacion", resumenEducacion.get(20));
		model.addAttribute("cantidadDocentesEspecialistasEducacion", resumenEducacion.get(21));
		model.addAttribute("cantidadDocentesPregradoEducacion", resumenEducacion.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// salud-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicosCienciasDeLaSalud", resumenCienciasDeLaSalud.get(0));
		model.addAttribute("cantidadProgramasDoctoradoCienciasDeLaSalud", resumenCienciasDeLaSalud.get(1));
		model.addAttribute("cantidadProgramasMaestriaCienciasDeLaSalud", resumenCienciasDeLaSalud.get(2));
		model.addAttribute("cantidadProgramasEspecializacionCienciasDeLaSalud", resumenCienciasDeLaSalud.get(3));
		model.addAttribute("cantidaCentrosInvestigacionCienciasDeLaSalud", resumenCienciasDeLaSalud.get(4));
		model.addAttribute("cantidadGruposInvestigacionCienciasDeLaSalud", resumenCienciasDeLaSalud.get(5));
		model.addAttribute("cantidadLineasInvestigacionCienciasDeLaSalud", resumenCienciasDeLaSalud.get(6));
		model.addAttribute("cantidadInvestigadoresCienciasDeLaSalud", resumenCienciasDeLaSalud.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1CienciasDeLaSalud", resumenCienciasDeLaSalud.get(8));
		model.addAttribute("cantidadGruposInvestigacionACienciasDeLaSalud", resumenCienciasDeLaSalud.get(9));
		model.addAttribute("cantidadGruposInvestigacionBCienciasDeLaSalud", resumenCienciasDeLaSalud.get(10));
		model.addAttribute("cantidadGruposInvestigacionCCienciasDeLaSalud", resumenCienciasDeLaSalud.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosCienciasDeLaSalud", resumenCienciasDeLaSalud.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoCienciasDeLaSalud",
				resumenCienciasDeLaSalud.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosCienciasDeLaSalud", resumenCienciasDeLaSalud.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorCienciasDeLaSalud", resumenCienciasDeLaSalud.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosCienciasDeLaSalud", resumenCienciasDeLaSalud.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorCienciasDeLaSalud", resumenCienciasDeLaSalud.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaCienciasDeLaSalud", resumenCienciasDeLaSalud.get(18));
		model.addAttribute("cantidadDocentesDoctoresCienciasDeLaSalud", resumenCienciasDeLaSalud.get(19));
		model.addAttribute("cantidadDocentesMagisterCienciasDeLaSalud", resumenCienciasDeLaSalud.get(20));
		model.addAttribute("cantidadDocentesEspecialistasCienciasDeLaSalud", resumenCienciasDeLaSalud.get(21));
		model.addAttribute("cantidadDocentesPregradoCienciasDeLaSalud", resumenCienciasDeLaSalud.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// humanas-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicosCienciasHumanas", resumenCienciasHumanas.get(0));
		model.addAttribute("cantidadProgramasDoctoradoCienciasHumanas", resumenCienciasHumanas.get(1));
		model.addAttribute("cantidadProgramasMaestriaCienciasHumanas", resumenCienciasHumanas.get(2));
		model.addAttribute("cantidadProgramasEspecializacionCienciasHumanas", resumenCienciasHumanas.get(3));
		model.addAttribute("cantidadCentrosInvestigacionCienciasHumanas", resumenCienciasHumanas.get(4));
		model.addAttribute("cantidadGruposInvestigacionCienciasHumanas", resumenCienciasHumanas.get(5));
		model.addAttribute("cantidadLineasInvestigacionCienciasHumanas", resumenCienciasHumanas.get(6));
		model.addAttribute("cantidadInvestigadoresCienciasHumanas", resumenCienciasHumanas.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1CienciasHumanas", resumenCienciasHumanas.get(8));
		model.addAttribute("cantidadGruposInvestigacionACienciasHumanas", resumenCienciasHumanas.get(9));
		model.addAttribute("cantidadGruposInvestigacionBCienciasHumanas", resumenCienciasHumanas.get(10));
		model.addAttribute("cantidadGruposInvestigacionCCienciasHumanas", resumenCienciasHumanas.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosCienciasHumanas", resumenCienciasHumanas.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoCienciasHumanas", resumenCienciasHumanas.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosCienciasHumanas", resumenCienciasHumanas.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorCienciasHumanas", resumenCienciasHumanas.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosCienciasHumanas", resumenCienciasHumanas.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorCienciasHumanas", resumenCienciasHumanas.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaCienciasHumanas", resumenCienciasHumanas.get(18));
		model.addAttribute("cantidadDocentesDoctoresCienciasHumanas", resumenCienciasHumanas.get(19));
		model.addAttribute("cantidadDocentesMagisterCienciasHumanas", resumenCienciasHumanas.get(20));
		model.addAttribute("cantidadDocentesEspecialistasCienciasHumanas", resumenCienciasHumanas.get(21));
		model.addAttribute("cantidadDocentesPregradoCienciasHumanas", resumenCienciasHumanas.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// agroindustria-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicosAgroindustria", resumenAgroindustria.get(0));
		model.addAttribute("cantidadProgramasDoctoradoAgroindustria", resumenAgroindustria.get(1));
		model.addAttribute("cantidadProgramasMaestriaAgroindustria", resumenAgroindustria.get(2));
		model.addAttribute("cantidadProgramasEspecializacionAgroindustria", resumenAgroindustria.get(3));
		model.addAttribute("cantidadCentrosInvestigacionAgroindustria", resumenAgroindustria.get(4));
		model.addAttribute("cantidadGruposInvestigacionAgroindustria", resumenAgroindustria.get(5));
		model.addAttribute("cantidadLineasInvestigacionAgroindustria", resumenAgroindustria.get(6));
		model.addAttribute("cantidadInvestigadoresAgroindustria", resumenAgroindustria.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1Agroindustria", resumenAgroindustria.get(8));
		model.addAttribute("cantidadGruposInvestigacionAAgroindustria", resumenAgroindustria.get(9));
		model.addAttribute("cantidadGruposInvestigacionBAgroindustria", resumenAgroindustria.get(10));
		model.addAttribute("cantidadGruposInvestigacionCAgroindustria", resumenAgroindustria.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosAgroindustria", resumenAgroindustria.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoAgroindustria", resumenAgroindustria.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosAgroindustria", resumenAgroindustria.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorAgroindustria", resumenAgroindustria.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosAgroindustria", resumenAgroindustria.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorAgroindustria", resumenAgroindustria.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaAgroindustria", resumenAgroindustria.get(18));
		model.addAttribute("cantidadDocentesDoctoresAgroindustria", resumenAgroindustria.get(19));
		model.addAttribute("cantidadDocentesMagisterAgroindustria", resumenAgroindustria.get(20));
		model.addAttribute("cantidadDocentesEspecialistasAgroindustria", resumenAgroindustria.get(21));
		model.addAttribute("cantidadDocentesPregradoAgroindustria", resumenAgroindustria.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// economicas-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicosCienciasEconomicas", resumenCienciasEconomicas.get(0));
		model.addAttribute("cantidadProgramasDoctoradoCienciasEconomicas", resumenCienciasEconomicas.get(1));
		model.addAttribute("cantidadProgramasMaestriaCienciasEconomicas", resumenCienciasEconomicas.get(2));
		model.addAttribute("cantidadProgramasEspecializacionCienciasEconomicas", resumenCienciasEconomicas.get(3));
		model.addAttribute("cantidadCentrosInvestigacionCienciasEconomicas", resumenCienciasEconomicas.get(4));
		model.addAttribute("cantidadGruposInvestigacionCienciasEconomicas", resumenCienciasEconomicas.get(5));
		model.addAttribute("cantidadLineasInvestigacionCienciasEconomicas", resumenCienciasEconomicas.get(6));
		model.addAttribute("cantidadInvestigadoresCienciasEconomicas", resumenCienciasEconomicas.get(7));
		model.addAttribute("cantidadGruposInvestigacionA1CienciasEconomicas", resumenCienciasEconomicas.get(8));
		model.addAttribute("cantidadGruposInvestigacionACienciasEconomicas", resumenCienciasEconomicas.get(9));
		model.addAttribute("cantidadGruposInvestigacionBCienciasEconomicas", resumenCienciasEconomicas.get(10));
		model.addAttribute("cantidadGruposInvestigacionCCienciasEconomicas", resumenCienciasEconomicas.get(11));
		model.addAttribute("cantidadGruposInvestigacionReconocidosCienciasEconomicas",
				resumenCienciasEconomicas.get(12));
		model.addAttribute("cantidadGruposInvestigacionNoReconocidoCienciasEconomicas",
				resumenCienciasEconomicas.get(13));
		model.addAttribute("cantidadInvestigadoresEmeritosCienciasEconomicas", resumenCienciasEconomicas.get(14));
		model.addAttribute("cantidadInvestigadoresSeniorCienciasEconomicas", resumenCienciasEconomicas.get(15));
		model.addAttribute("cantidadInvestigadoresAsociadosCienciasEconomicas", resumenCienciasEconomicas.get(16));
		model.addAttribute("cantidadInvestigadoresJuniorCienciasEconomicas", resumenCienciasEconomicas.get(17));
		model.addAttribute("cantidadInvestigadoresSinCategoriaCienciasEconomicas", resumenCienciasEconomicas.get(18));
		model.addAttribute("cantidadDocentesDoctoresCienciasEconomicas", resumenCienciasEconomicas.get(19));
		model.addAttribute("cantidadDocentesMagisterCienciasEconomicas", resumenCienciasEconomicas.get(20));
		model.addAttribute("cantidadDocentesEspecialistasCienciasEconomicas", resumenCienciasEconomicas.get(21));
		model.addAttribute("cantidadDocentesPregradoCienciasEconomicas", resumenCienciasEconomicas.get(22));

		// ------AdiciÃ³n de atributos al modelo con informacion de
		// totales-----------------------------------------------------------------------
		model.addAttribute("cantidadProgramasAcademicosTotal", resumenIngenieria.get(0)
				.add(resumenCienciasBasicas.get(0)
						.add(resumenEducacion.get(0).add(resumenCienciasDeLaSalud.get(0).add(resumenCienciasHumanas
								.get(0).add(resumenAgroindustria.get(0).add(resumenCienciasEconomicas.get(0))))))));

		model.addAttribute("cantidadProgramasDoctoradoTotal", resumenIngenieria.get(1)
				.add(resumenCienciasBasicas.get(1)
						.add(resumenEducacion.get(1).add(resumenCienciasDeLaSalud.get(1).add(resumenCienciasHumanas
								.get(1).add(resumenAgroindustria.get(1).add(resumenCienciasEconomicas.get(1))))))));

		model.addAttribute("cantidadProgramasMaestriaTotal", resumenIngenieria.get(2)
				.add(resumenCienciasBasicas.get(2)
						.add(resumenEducacion.get(2).add(resumenCienciasDeLaSalud.get(2).add(resumenCienciasHumanas
								.get(2).add(resumenAgroindustria.get(2).add(resumenCienciasEconomicas.get(2))))))));

		model.addAttribute("cantidadProgramasEspecializacionTotal", resumenIngenieria.get(3)
				.add(resumenCienciasBasicas.get(3)
						.add(resumenEducacion.get(3).add(resumenCienciasDeLaSalud.get(3).add(resumenCienciasHumanas
								.get(3).add(resumenAgroindustria.get(3).add(resumenCienciasEconomicas.get(3))))))));

		model.addAttribute("cantidadCentrosInvestigacionTotal", resumenIngenieria.get(4)
				.add(resumenCienciasBasicas.get(4)
						.add(resumenEducacion.get(4).add(resumenCienciasDeLaSalud.get(4).add(resumenCienciasHumanas
								.get(4).add(resumenAgroindustria.get(4).add(resumenCienciasEconomicas.get(4))))))));

		model.addAttribute("cantidadGruposInvestigacionTotal", resumenIngenieria.get(5)
				.add(resumenCienciasBasicas.get(5)
						.add(resumenEducacion.get(5).add(resumenCienciasDeLaSalud.get(5).add(resumenCienciasHumanas
								.get(5).add(resumenAgroindustria.get(5).add(resumenCienciasEconomicas.get(5))))))));

		model.addAttribute("cantidadLineasInvestigacionTotal", resumenIngenieria.get(6)
				.add(resumenCienciasBasicas.get(6)
						.add(resumenEducacion.get(6).add(resumenCienciasDeLaSalud.get(6).add(resumenCienciasHumanas
								.get(6).add(resumenAgroindustria.get(6).add(resumenCienciasEconomicas.get(6))))))));

		model.addAttribute("cantidadInvestigadoresTotal", cantidadTotalInvestigadores);

		// ------Segunda
		// tabla----------------------------------------------------------------------------------------------------
		model.addAttribute("cantidadGruposA1Total", resumenIngenieria.get(8)
				.add(resumenCienciasBasicas.get(8)
						.add(resumenEducacion.get(8).add(resumenCienciasDeLaSalud.get(8).add(resumenCienciasHumanas
								.get(8).add(resumenAgroindustria.get(8).add(resumenCienciasEconomicas.get(8))))))));

		model.addAttribute("cantidadGruposATotal", resumenIngenieria.get(9)
				.add(resumenCienciasBasicas.get(9)
						.add(resumenEducacion.get(9).add(resumenCienciasDeLaSalud.get(9).add(resumenCienciasHumanas
								.get(9).add(resumenAgroindustria.get(9).add(resumenCienciasEconomicas.get(9))))))));

		model.addAttribute("cantidadGruposBTotal", resumenIngenieria.get(10)
				.add(resumenCienciasBasicas.get(10)
						.add(resumenEducacion.get(10).add(resumenCienciasDeLaSalud.get(10).add(resumenCienciasHumanas
								.get(10).add(resumenAgroindustria.get(10).add(resumenCienciasEconomicas.get(10))))))));

		model.addAttribute("cantidadGruposCTotal", resumenIngenieria.get(11)
				.add(resumenCienciasBasicas.get(11)
						.add(resumenEducacion.get(11).add(resumenCienciasDeLaSalud.get(11).add(resumenCienciasHumanas
								.get(11).add(resumenAgroindustria.get(11).add(resumenCienciasEconomicas.get(11))))))));

		model.addAttribute("cantidadGruposReconocidosTotal", resumenIngenieria.get(12)
				.add(resumenCienciasBasicas.get(12)
						.add(resumenEducacion.get(12).add(resumenCienciasDeLaSalud.get(12).add(resumenCienciasHumanas
								.get(12).add(resumenAgroindustria.get(12).add(resumenCienciasEconomicas.get(12))))))));

		model.addAttribute("cantidadGruposNoReconocidosTotal", resumenIngenieria.get(13)
				.add(resumenCienciasBasicas.get(13)
						.add(resumenEducacion.get(13).add(resumenCienciasDeLaSalud.get(13).add(resumenCienciasHumanas
								.get(13).add(resumenAgroindustria.get(13).add(resumenCienciasEconomicas.get(13))))))));

		// ------Totales tabla 2
		// --------------------------------------------------------------------------------------------------
		model.addAttribute("cantidadGruposIngenieriaTotal",
				resumenIngenieria.get(8).add(resumenIngenieria.get(9).add(resumenIngenieria.get(10).add(
						resumenIngenieria.get(11).add(resumenIngenieria.get(12).add(resumenIngenieria.get(13)))))));

		model.addAttribute("cantidadGruposCienciasBasicasTotal",
				resumenCienciasBasicas.get(8)
						.add(resumenCienciasBasicas.get(9).add(resumenCienciasBasicas.get(10).add(resumenCienciasBasicas
								.get(11).add(resumenCienciasBasicas.get(12).add(resumenCienciasBasicas.get(13)))))));

		model.addAttribute("cantidadGruposEducacionTotal",
				resumenEducacion.get(8).add(resumenEducacion.get(9).add(resumenEducacion.get(10)
						.add(resumenEducacion.get(11).add(resumenEducacion.get(12).add(resumenEducacion.get(13)))))));

		model.addAttribute("cantidadGruposCienciasDeLaSaludTotal",
				resumenCienciasDeLaSalud.get(8).add(resumenCienciasDeLaSalud.get(9)
						.add(resumenCienciasDeLaSalud.get(10).add(resumenCienciasDeLaSalud.get(11)
								.add(resumenCienciasDeLaSalud.get(12).add(resumenCienciasDeLaSalud.get(13)))))));

		model.addAttribute("cantidadGruposCienciasHumanasTotal",
				resumenCienciasHumanas.get(8)
						.add(resumenCienciasHumanas.get(9).add(resumenCienciasHumanas.get(10).add(resumenCienciasHumanas
								.get(11).add(resumenCienciasHumanas.get(12).add(resumenCienciasHumanas.get(13)))))));

		model.addAttribute("cantidadGruposAgroindustriaTotal",
				resumenAgroindustria.get(8)
						.add(resumenAgroindustria.get(9).add(resumenAgroindustria.get(10).add(resumenAgroindustria
								.get(11).add(resumenAgroindustria.get(12).add(resumenAgroindustria.get(13)))))));

		model.addAttribute("cantidadGruposCienciasEconomicasTotal",
				resumenCienciasEconomicas.get(8).add(resumenCienciasEconomicas.get(9)
						.add(resumenCienciasEconomicas.get(10).add(resumenCienciasEconomicas.get(11)
								.add(resumenCienciasEconomicas.get(12).add(resumenCienciasEconomicas.get(13)))))));

		// ------Tercera
		// tabla----------------------------------------------------------------------------------------------------
		model.addAttribute("cantidadInvestigadoresEmeritosTotal", resumenIngenieria.get(14)
				.add(resumenCienciasBasicas.get(14)
						.add(resumenEducacion.get(14).add(resumenCienciasDeLaSalud.get(14).add(resumenCienciasHumanas
								.get(14).add(resumenAgroindustria.get(14).add(resumenCienciasEconomicas.get(14))))))));

		model.addAttribute("cantidadInvestigadoresSeniorTotal", resumenIngenieria.get(15)
				.add(resumenCienciasBasicas.get(15)
						.add(resumenEducacion.get(15).add(resumenCienciasDeLaSalud.get(15).add(resumenCienciasHumanas
								.get(15).add(resumenAgroindustria.get(15).add(resumenCienciasEconomicas.get(15))))))));

		model.addAttribute("cantidadInvestigadoresAsociadosTotal", resumenIngenieria.get(16)
				.add(resumenCienciasBasicas.get(16)
						.add(resumenEducacion.get(16).add(resumenCienciasDeLaSalud.get(16).add(resumenCienciasHumanas
								.get(16).add(resumenAgroindustria.get(16).add(resumenCienciasEconomicas.get(16))))))));

		model.addAttribute("cantidadInvestigadoresJuniorTotal", resumenIngenieria.get(17)
				.add(resumenCienciasBasicas.get(17)
						.add(resumenEducacion.get(17).add(resumenCienciasDeLaSalud.get(17).add(resumenCienciasHumanas
								.get(17).add(resumenAgroindustria.get(17).add(resumenCienciasEconomicas.get(17))))))));

		model.addAttribute("cantidadInvestigadoresSinCategoriaTotal", resumenIngenieria.get(18)
				.add(resumenCienciasBasicas.get(18)
						.add(resumenEducacion.get(18).add(resumenCienciasDeLaSalud.get(18).add(resumenCienciasHumanas
								.get(18).add(resumenAgroindustria.get(18).add(resumenCienciasEconomicas.get(18))))))));

		// ------Totales tabla 3
		// --------------------------------------------------------------------------------------------------
		model.addAttribute("cantidadInvestigadoresIngenieriaTotal", resumenIngenieria.get(14).add(resumenIngenieria
				.get(15).add(resumenIngenieria.get(16).add(resumenIngenieria.get(17).add(resumenIngenieria.get(18))))));

		model.addAttribute("cantidadInvestigadoresCienciasBasicasTotal",
				resumenCienciasBasicas.get(14).add(resumenCienciasBasicas.get(15).add(resumenCienciasBasicas.get(16)
						.add(resumenCienciasBasicas.get(17).add(resumenCienciasBasicas.get(18))))));

		model.addAttribute("cantidadInvestigadoresEducacionTotal", resumenEducacion.get(14).add(resumenEducacion.get(15)
				.add(resumenEducacion.get(16).add(resumenEducacion.get(17).add(resumenEducacion.get(18))))));

		model.addAttribute("cantidadInvestigadoresCienciasDeLaSaludTotal",
				resumenCienciasDeLaSalud.get(14).add(resumenCienciasDeLaSalud.get(15).add(resumenCienciasDeLaSalud
						.get(16).add(resumenCienciasDeLaSalud.get(17).add(resumenCienciasDeLaSalud.get(18))))));

		model.addAttribute("cantidadInvestigadoresCienciasHumanasTotal",
				resumenCienciasHumanas.get(14).add(resumenCienciasHumanas.get(15).add(resumenCienciasHumanas.get(16)
						.add(resumenCienciasHumanas.get(17).add(resumenCienciasHumanas.get(18))))));

		model.addAttribute("cantidadInvestigadoresAgroindustriaTotal",
				resumenAgroindustria.get(14).add(resumenAgroindustria.get(15).add(resumenAgroindustria.get(16)
						.add(resumenAgroindustria.get(17).add(resumenAgroindustria.get(18))))));

		model.addAttribute("cantidadInvestigadoresCienciasEconomicasTotal",
				resumenCienciasEconomicas.get(14).add(resumenCienciasEconomicas.get(15).add(resumenCienciasEconomicas
						.get(16).add(resumenCienciasEconomicas.get(17).add(resumenCienciasEconomicas.get(18))))));

		// ------Cuarta
		// tabla----------------------------------------------------------------------------------------------------
		model.addAttribute("cantidadDocentesDoctoresTotal", resumenIngenieria.get(19)
				.add(resumenCienciasBasicas.get(19)
						.add(resumenEducacion.get(19).add(resumenCienciasDeLaSalud.get(19).add(resumenCienciasHumanas
								.get(19).add(resumenAgroindustria.get(19).add(resumenCienciasEconomicas.get(19))))))));

		model.addAttribute("cantidadDocentesMagisterTotal", resumenIngenieria.get(20)
				.add(resumenCienciasBasicas.get(20)
						.add(resumenEducacion.get(20).add(resumenCienciasDeLaSalud.get(20).add(resumenCienciasHumanas
								.get(20).add(resumenAgroindustria.get(20).add(resumenCienciasEconomicas.get(20))))))));

		model.addAttribute("cantidadDocentesEspecialistasTotal", resumenIngenieria.get(21)
				.add(resumenCienciasBasicas.get(21)
						.add(resumenEducacion.get(21).add(resumenCienciasDeLaSalud.get(21).add(resumenCienciasHumanas
								.get(21).add(resumenAgroindustria.get(21).add(resumenCienciasEconomicas.get(21))))))));

		model.addAttribute("cantidadDocentesPregradoTotal", resumenIngenieria.get(22)
				.add(resumenCienciasBasicas.get(22)
						.add(resumenEducacion.get(22).add(resumenCienciasDeLaSalud.get(22).add(resumenCienciasHumanas
								.get(22).add(resumenAgroindustria.get(22).add(resumenCienciasEconomicas.get(22))))))));

		// ------Totales tabla 4
		// --------------------------------------------------------------------------------------------------
		model.addAttribute("cantidadDocentesIngenieriaTotal", resumenIngenieria.get(19)
				.add(resumenIngenieria.get(20).add(resumenIngenieria.get(21).add(resumenIngenieria.get(22)))));

		model.addAttribute("cantidadDocentesCienciasBasicasTotal",
				resumenCienciasBasicas.get(19).add(resumenCienciasBasicas.get(20)
						.add(resumenCienciasBasicas.get(21).add(resumenCienciasBasicas.get(22)))));

		model.addAttribute("cantidadDocentesEducacionTotal", resumenEducacion.get(19)
				.add(resumenEducacion.get(20).add(resumenEducacion.get(21).add(resumenEducacion.get(22)))));

		model.addAttribute("cantidadDocentesCienciasDeLaSaludTotal",
				resumenCienciasDeLaSalud.get(19).add(resumenCienciasDeLaSalud.get(20)
						.add(resumenCienciasDeLaSalud.get(21).add(resumenCienciasDeLaSalud.get(22)))));

		model.addAttribute("cantidadDocentesCienciasHumanasTotal",
				resumenCienciasHumanas.get(19).add(resumenCienciasHumanas.get(20)
						.add(resumenCienciasHumanas.get(21).add(resumenCienciasHumanas.get(22)))));

		model.addAttribute("cantidadDocentesAgroindustriaTotal", resumenAgroindustria.get(19)
				.add(resumenAgroindustria.get(20).add(resumenAgroindustria.get(21).add(resumenAgroindustria.get(22)))));

		model.addAttribute("cantidadDocentesCienciasEconomicasTotal",
				resumenCienciasEconomicas.get(19).add(resumenCienciasEconomicas.get(20)
						.add(resumenCienciasEconomicas.get(21).add(resumenCienciasEconomicas.get(22)))));

		model.addAttribute("cantidadDocentesTotal", resumenIngenieria.get(19).add(resumenIngenieria.get(20)
				.add(resumenIngenieria.get(21).add(resumenIngenieria.get(22).add(resumenCienciasBasicas.get(19).add(
						resumenCienciasBasicas.get(20).add(resumenCienciasBasicas.get(21).add(resumenCienciasBasicas
								.get(22)
								.add(resumenEducacion.get(19).add(resumenEducacion.get(20).add(resumenEducacion.get(21)
										.add(resumenEducacion.get(22).add(resumenCienciasDeLaSalud.get(19)
												.add(resumenCienciasDeLaSalud.get(20).add(resumenCienciasDeLaSalud
														.get(21)
														.add(resumenCienciasDeLaSalud.get(22).add(resumenCienciasHumanas
																.get(19)
																.add(resumenCienciasHumanas.get(20)
																		.add(resumenCienciasHumanas.get(21)
																				.add(resumenCienciasHumanas.get(22)
																						.add(resumenAgroindustria
																								.get(19)
																								.add(resumenAgroindustria
																										.get(20)
																										.add(resumenAgroindustria
																												.get(21)
																												.add(resumenAgroindustria
																														.get(22)
																														.add(resumenCienciasEconomicas
																																.get(19)
																																.add(resumenCienciasEconomicas
																																		.get(20)
																																		.add(resumenCienciasEconomicas
																																				.get(21)
																																				.add(resumenCienciasEconomicas
																																						.get(22)))))))))))))))))))))))))))));
		/////////////////// Tabla Pertenencia////////////////////////////////////

		List<Investigador> investigadores_Basicas = investigadorDAO.getAllInvestigadoresInternosFacultad(1L);
		List<Investigador> investigadores_pertenencia_Basicas = utilidades
				.agregarPertenenciaInves(investigadores_Basicas);

		List<Investigador> investigadores_Educacion = investigadorDAO.getAllInvestigadoresInternosFacultad(2L);
		List<Investigador> investigadores_pertenencia_Educacion = utilidades
				.agregarPertenenciaInves(investigadores_Educacion);

		List<Investigador> investigadores_Salud = investigadorDAO.getAllInvestigadoresInternosFacultad(3L);
		List<Investigador> investigadores_pertenencia_Salud = utilidades.agregarPertenenciaInves(investigadores_Salud);

		List<Investigador> investigadores_Ingenieria = investigadorDAO.getAllInvestigadoresInternosFacultad(4L);
		List<Investigador> investigadores_pertenencia_Ingenieria = utilidades
				.agregarPertenenciaInves(investigadores_Ingenieria);

		List<Investigador> investigadores_Humanas = investigadorDAO.getAllInvestigadoresInternosFacultad(5L);
		List<Investigador> investigadores_pertenencia_Humanas = utilidades
				.agregarPertenenciaInves(investigadores_Humanas);

		List<Investigador> investigadores_Agro = investigadorDAO.getAllInvestigadoresInternosFacultad(6L);
		List<Investigador> investigadores_pertenencia_Agro = utilidades.agregarPertenenciaInves(investigadores_Agro);

		List<Investigador> investigadores_Economicas = investigadorDAO.getAllInvestigadoresInternosFacultad(7L);
		List<Investigador> investigadores_pertenencia_Economicas = utilidades
				.agregarPertenenciaInves(investigadores_Economicas);

		// ---------------BASICAS-----------------------------

		List<Investigador> lista_investigadores_Basicas_Adm = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Basicas, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_basicas_Admin", lista_investigadores_Basicas_Adm.size());

		List<Investigador> lista_investigadores_Basicas_DP = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Basicas, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_basicas_dp", lista_investigadores_Basicas_DP.size());

		List<Investigador> lista_investigadores_Basicas_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Basicas, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_basicas_dc", lista_investigadores_Basicas_DC.size());

		List<Investigador> lista_investigadores_Basicas_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Basicas, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_basicas_do", lista_investigadores_Basicas_DO.size());

		List<Investigador> lista_investigadores_Basicas_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Basicas, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_basicas_ie", lista_investigadores_Basicas_IE.size());

		List<Investigador> lista_investigadores_Basicas_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Basicas, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_basicas_ei", lista_investigadores_Basicas_EI.size());

		List<Investigador> lista_investigadores_Basicas_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Basicas, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_basicas_ind", lista_investigadores_Basicas_IND.size());

		// --------------------EDUCACION----------------------

		List<Investigador> lista_investigadores_Educacion_Adm = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Educacion, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_educacion_Admin", lista_investigadores_Educacion_Adm.size());

		List<Investigador> lista_investigadores_Educacion_DP = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Educacion, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_educacion_dp", lista_investigadores_Educacion_DP.size());

		List<Investigador> lista_investigadores_Educacion_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Educacion, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_educacion_dc", lista_investigadores_Educacion_DC.size());

		List<Investigador> lista_investigadores_Educacion_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Educacion, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_educacion_do", lista_investigadores_Educacion_DO.size());
		List<Investigador> lista_investigadores_Educacion_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Educacion, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_educacion_ie", lista_investigadores_Educacion_IE.size());

		List<Investigador> lista_investigadores_Educacion_EI = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Educacion, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_educacion_ei", lista_investigadores_Educacion_EI.size());

		List<Investigador> lista_investigadores_Educacion_IND = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Educacion, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_educacion_ind", lista_investigadores_Educacion_IND.size());

		// -----------------------SALUD----------------------------

		List<Investigador> lista_investigadores_Salud_Adm = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Salud, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_salud_Admin", lista_investigadores_Salud_Adm.size());

		List<Investigador> lista_investigadores_Salud_DP = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Salud, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_salud_dp", lista_investigadores_Salud_DP.size());

		List<Investigador> lista_investigadores_Salud_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Salud, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_salud_dc", lista_investigadores_Salud_DC.size());

		List<Investigador> lista_investigadores_Salud_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Salud, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_salud_do", lista_investigadores_Salud_DO.size());
		List<Investigador> lista_investigadores_Salud_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Salud, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_salud_ie", lista_investigadores_Salud_IE.size());

		List<Investigador> lista_investigadores_Salud_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Salud, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_salud_ei", lista_investigadores_Salud_EI.size());

		List<Investigador> lista_investigadores_Salud_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Salud, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_salud_ind", lista_investigadores_Salud_IND.size());

		// -----------------INGENIERIA-------------------------

		List<Investigador> lista_investigadores_Ingenieria_Adm = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_ingenieria_Admin", lista_investigadores_Ingenieria_Adm.size());

		List<Investigador> lista_investigadores_Ingenieria_DP = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_ingenieria_dp", lista_investigadores_Ingenieria_DP.size());

		List<Investigador> lista_investigadores_Ingenieria_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_ingenieria_dc", lista_investigadores_Ingenieria_DC.size());

		List<Investigador> lista_investigadores_Ingenieria_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_ingenieria_do", lista_investigadores_Ingenieria_DO.size());
		List<Investigador> lista_investigadores_Ingenieria_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_ingenieria_ie", lista_investigadores_Ingenieria_IE.size());

		List<Investigador> lista_investigadores_Ingenieria_EI = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_ingenieria_ei", lista_investigadores_Ingenieria_EI.size());

		List<Investigador> lista_investigadores_Ingenieria_IND = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Ingenieria, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_ingenieria_ind", lista_investigadores_Ingenieria_IND.size());

		// -----------------------------HUMANAS---------------------------

		List<Investigador> lista_investigadores_Humanas_Adm = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Humanas, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_humanas_Admin", lista_investigadores_Humanas_Adm.size());

		List<Investigador> lista_investigadores_Humanas_DP = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Humanas, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_humanas_dp", lista_investigadores_Humanas_DP.size());

		List<Investigador> lista_investigadores_Humanas_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Humanas, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_humanas_dc", lista_investigadores_Humanas_DC.size());

		List<Investigador> lista_investigadores_Humanas_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Humanas, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_humanas_do", lista_investigadores_Humanas_DO.size());
		List<Investigador> lista_investigadores_Humanas_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Humanas, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_humanas_ie", lista_investigadores_Humanas_IE.size());

		List<Investigador> lista_investigadores_Humanas_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Humanas, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_humanas_ei", lista_investigadores_Humanas_EI.size());

		List<Investigador> lista_investigadores_Humanas_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Humanas, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_humanas_ind", lista_investigadores_Humanas_IND.size());

		// --------------------AGRO-------------------------------

		List<Investigador> lista_investigadores_Agro_Adm = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Agro, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_agro_Admin", lista_investigadores_Agro_Adm.size());

		List<Investigador> lista_investigadores_Agro_DP = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Agro, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_agro_dp", lista_investigadores_Agro_DP.size());

		List<Investigador> lista_investigadores_Agro_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Agro, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_agro_dc", lista_investigadores_Agro_DC.size());

		List<Investigador> lista_investigadores_Agro_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Agro, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_agro_do", lista_investigadores_Agro_DO.size());
		List<Investigador> lista_investigadores_Agro_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Agro, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_agro_ie", lista_investigadores_Agro_IE.size());

		List<Investigador> lista_investigadores_Agro_EI = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Agro, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_agro_ei", lista_investigadores_Agro_EI.size());

		List<Investigador> lista_investigadores_Agro_IND = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Agro, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_agro_ind", lista_investigadores_Agro_IND.size());

		// -------------------Economicas-------------------------------
		List<Investigador> lista_investigadores_Economicas_Adm = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Economicas, Util.PERTENENCIA_ADMINISTRATIVO);
		model.addAttribute("Num_inves_economicas_Admin", lista_investigadores_Economicas_Adm.size());

		List<Investigador> lista_investigadores_Economicas_DP = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Economicas, Util.PERTENENCIA_DOCENTE_PLANTA);
		model.addAttribute("Num_inves_economicas_dp", lista_investigadores_Economicas_DP.size());

		List<Investigador> lista_investigadores_Economicas_DC = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Economicas, Util.PERTENENCIA_DOCENTE_CATEDRATICO);
		model.addAttribute("Num_inves_economicas_dc", lista_investigadores_Economicas_DC.size());

		List<Investigador> lista_investigadores_Economicas_DO = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Economicas, Util.PERTENENCIA_DOCENTE_OCASIONAL);
		model.addAttribute("Num_inves_economicas_do", lista_investigadores_Economicas_DO.size());
		List<Investigador> lista_investigadores_Economicas_IE = utilidades
				.seleccionarInvestigadoresPertenencia(investigadores_pertenencia_Economicas, Util.PERTENENCIA_EXTERNO);
		model.addAttribute("Num_inves_economicas_ie", lista_investigadores_Economicas_IE.size());

		List<Investigador> lista_investigadores_Economicas_EI = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Economicas, Util.PERTENENCIA_ESTUDIANTE);
		model.addAttribute("Num_inves_economicas_ei", lista_investigadores_Economicas_EI.size());

		List<Investigador> lista_investigadores_Economicas_IND = utilidades.seleccionarInvestigadoresPertenencia(
				investigadores_pertenencia_Economicas, Util.PERTENENCIA_INDEFINIDO);
		model.addAttribute("Num_inves_economicas_ind", lista_investigadores_Economicas_IND.size());

		// -------------------------------TOTAL-------------------------------------------

		int total_investigadores_Adm = lista_investigadores_Basicas_Adm.size()
				+ lista_investigadores_Educacion_Adm.size() + lista_investigadores_Salud_Adm.size()
				+ lista_investigadores_Ingenieria_Adm.size() + lista_investigadores_Humanas_Adm.size()
				+ lista_investigadores_Agro_Adm.size() + lista_investigadores_Economicas_Adm.size();
		int total_investigadores_DP = lista_investigadores_Basicas_DP.size() + lista_investigadores_Educacion_DP.size()
				+ lista_investigadores_Salud_DP.size() + lista_investigadores_Ingenieria_DP.size()
				+ lista_investigadores_Humanas_DP.size() + lista_investigadores_Agro_DP.size()
				+ lista_investigadores_Economicas_DP.size();
		int total_investigadores_DC = lista_investigadores_Basicas_DC.size() + lista_investigadores_Educacion_DC.size()
				+ lista_investigadores_Salud_DC.size() + lista_investigadores_Ingenieria_DC.size()
				+ lista_investigadores_Humanas_DC.size() + lista_investigadores_Agro_DC.size()
				+ lista_investigadores_Economicas_DC.size();
		int total_investigadores_DO = lista_investigadores_Basicas_DO.size() + lista_investigadores_Educacion_DO.size()
				+ lista_investigadores_Salud_DO.size() + lista_investigadores_Ingenieria_DO.size()
				+ lista_investigadores_Humanas_DO.size() + lista_investigadores_Agro_DO.size()
				+ lista_investigadores_Economicas_DO.size();
		int total_investigadores_IE = lista_investigadores_Basicas_IE.size() + lista_investigadores_Educacion_IE.size()
				+ lista_investigadores_Salud_IE.size() + lista_investigadores_Ingenieria_IE.size()
				+ lista_investigadores_Humanas_IE.size() + lista_investigadores_Agro_IE.size()
				+ lista_investigadores_Economicas_IE.size();
		int total_investigadores_EI = lista_investigadores_Basicas_EI.size() + lista_investigadores_Educacion_EI.size()
				+ lista_investigadores_Salud_EI.size() + lista_investigadores_Ingenieria_EI.size()
				+ lista_investigadores_Humanas_EI.size() + lista_investigadores_Agro_EI.size()
				+ lista_investigadores_Economicas_EI.size();

		int total_investigadores_IND = lista_investigadores_Basicas_IND.size()
				+ lista_investigadores_Educacion_IND.size() + lista_investigadores_Salud_IND.size()
				+ lista_investigadores_Ingenieria_IND.size() + lista_investigadores_Humanas_IND.size()
				+ lista_investigadores_Agro_IND.size() + lista_investigadores_Economicas_IND.size();

		model.addAttribute("total_adm", total_investigadores_Adm);
		model.addAttribute("total_DP", total_investigadores_DP);
		model.addAttribute("total_DC", total_investigadores_DC);
		model.addAttribute("total_DO", total_investigadores_DO);
		model.addAttribute("total_IE", total_investigadores_IE);
		model.addAttribute("total_EI", total_investigadores_EI);
		model.addAttribute("total_IND", total_investigadores_IND);

		model.addAttribute("peAdm", "adm");
		model.addAttribute("peDp", "dp");
		model.addAttribute("peDc", "dc");
		model.addAttribute("peDo", "do");
		model.addAttribute("peIe", "ie");
		model.addAttribute("peEi", "ei");
		model.addAttribute("peInd", "ind");

		// ------AdiciÃ³n de atributos al modelo para referenciar a pÃ¡ginas
		// especificas-----------------------------------------------------------------------

		model.addAttribute("programaAcademico", Util.UNDERGRADUATE_PROGRAM_PARAM_ID);
		model.addAttribute("programaDoctorado", "pd");
		model.addAttribute("programaMagister", "pm");
		model.addAttribute("programaEspecializacion", "pe");
		model.addAttribute("centrosInvestigacion", "c");
		model.addAttribute("gruposInvestigacion", "g");
		model.addAttribute("lineasInvestigacion", "l");
		model.addAttribute("investigadores", "i");

		model.addAttribute("categoriaA1", "ca1");
		model.addAttribute("categoriaA", "ca");
		model.addAttribute("categoriaB", "cb");
		model.addAttribute("categoriaC", "cc");
		model.addAttribute("categoriaReconocido", "cr");
		model.addAttribute("categoriaNoReconocido", "cnr");

		model.addAttribute("investigadorEmerito", "ie");
		model.addAttribute("investigadorSenior", "is");
		model.addAttribute("investigadorAsociado", "ia");
		model.addAttribute("investigadorJunior", "ij");
		model.addAttribute("investigadorSinCategoria", "isc");

		model.addAttribute("formacionDoctor", "fd");
		model.addAttribute("formacionMagister", "fm");
		model.addAttribute("formacionEspecialista", "fe");
		model.addAttribute("formacionPregrado", "fp");
		model.addAttribute("docentes", "d");

		model.addAttribute(Util.PARAM_UNIVERSITY_LEVEL, Util.PARAM_UNIVERSITY_LEVEL_ID);
		model.addAttribute("idFacultadCienciasBasicas", "1");
		model.addAttribute("idFacultadEducacion", "2");
		model.addAttribute("idFacultadCienciasDeLaSalud", "3");
		model.addAttribute("idFacultadIngenieria", "4");
		model.addAttribute("idFacultadCienciasHumanas", "5");
		model.addAttribute("idFacultadAgroindustria", "6");
		model.addAttribute("idFacultadCienciasEconomicas", "7");

		return "estadisticas/uniquindio";

	}

}