(function($) {
	$(document).ready(function() {




		// Ampliar/Reducir la barra lateral
		$("#sidebarToggle").on('click', function(e) {
			e.preventDefault();
			$("body").toggleClass("sidebar-toggled");
			$(".sidebar").toggleClass("toggled");
		});

		// Prevent the content wrapper from scrolling when the fixed side
		// navigation hovered over
		$('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
			if ($(window).width() > 768) {
				var e0 = e.originalEvent,
					delta = e0.wheelDelta || -e0.detail;
				this.scrollTop += (delta < 0 ? 1 : -1) * 30;
				e.preventDefault();
			}
		});

		$("#btn-buscar").on('click', function() {
			var cadena = document.getElementById('input-busqueda').value;
			var path = cadena.replace(/\s/g, '+');
			path = path.normalize('NFD').replace(/[\u0300-\u036f]/g, "");
			if (document.getElementById("gruplac").checked) {
				window.location.href = "busqueda?type=g&search=" + path;
			} else {
				window.location.href = "busqueda?type=i&search=" + path;

			}
			return false;
		});

		$("#input-busqueda").keyup(function(event) {
			if (event.keyCode === 13) {
				$("#btn-buscar").click();
			}
		});

		// Configuración de búsqueda en Datatables.
		$.fn.DataTable.ext.type.search['string'] = function(data) {
			return !data ?
				'' :
				typeof data === 'string' ?
					data
						.replace(/έ/g, 'ε')
						.replace(/ύ/g, 'υ')
						.replace(/ό/g, 'ο')
						.replace(/ώ/g, 'ω')
						.replace(/ά/g, 'α')
						.replace(/ί/g, 'ι')
						.replace(/ή/g, 'η')
						.replace(/\n/g, ' ')
						.replace(/[áÁ]/g, 'a')
						.replace(/[éÉ]/g, 'e')
						.replace(/[íÍ]/g, 'i')
						.replace(/[óÓ]/g, 'o')
						.replace(/[úÚ]/g, 'u')
						.replace(/ê/g, 'e')
						.replace(/î/g, 'i')
						.replace(/ô/g, 'o')
						.replace(/è/g, 'e')
						.replace(/ï/g, 'i')
						.replace(/ü/g, 'u')
						.replace(/ã/g, 'a')
						.replace(/õ/g, 'o')
						.replace(/ç/g, 'c')
						.replace(/ì/g, 'i') :
					data;
		};


		// Configuración por defecto de botones e idioma de DataTables.
		if (document.getElementById('table')) {
			$.extend($.fn.dataTable.defaults, {
				responsive: true,
				dom: 'Bfrtip',
				buttons: [
					{
						extend: 'copy',
						text: 'Copiar',
						className: 'copyButton'
					},
					{
						extend: 'excel',
						text: 'Excel',
						className: 'excelButton',
						// Encabezado del documento excel
						customize: function(xlsx) {
							var sheet = xlsx.xl.worksheets['sheet1.xml'];
							if (document.getElementById('tabla_integrantes')) {
								$('c[r=A1] t', sheet).text('INTEGRANTES - ' + document.getElementById('titulo').textContent);
							}
							else if (document.getElementById('tabla_reporte')) {
								$('c[r=A1] t', sheet).text(document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent);
								var table = document.getElementById('tabla_reporte');
							}
							else if (document.getElementById('tabla_reporte_consolidado')) {
								$('c[r=A1] t', sheet).text(document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent);
								var table = document.getElementById('tabla_reporte_consolidado');
							}
							else if (document.getElementById('tabla_centros')) {
								$('c[r=A1] t', sheet).text('Centros de Investigación');
							}
							else if (document.getElementById('tabla_investigadores_wrapper')) {
								$('c[r=A1] t', sheet).text('Investigadores');
							}
							else if (document.getElementById('tabla_orcid_wrapper')) {
								$('c[r=A1] t', sheet).text('Investigadores');
							}
							else if (document.getElementById('tabla_inventario_wrapper')) {
								$('c[r=A1] t', sheet).text('INVENTARIO - ' + document.getElementById('titulo').textContent);
							}
							else if (document.getElementById('tabla_programas_wrapper')) {
								$('c[r=A1] t', sheet).text('Programas');
							}
							else if (document.getElementById('tabla_facultades_wrapper')) {
								$('c[r=A1] t', sheet).text('Facultades');
							}
							else if (document.getElementById('tabla_grupos_wrapper')) {
								$('c[r=A1] t', sheet).text('Grupos');
							}
							else if (document.getElementById('tabla_lineas_wrapper')) {
								$('c[r=A1] t', sheet).text('Lineas de Investigación');
							}
							else if (document.getElementById('tabla_reconocimientos_wrapper')) {
								$('c[r=A1] t', sheet).text('Reconocimientos');
							}
							else if (document.getElementById('tabla_recolecciones_wrapper')) {
								$('c[r=A1] t', sheet).text('Recolecciones');
							}
						},
						// Nombre de archivo personalizado
						filename: function() {
							if (document.getElementById('tabla_integrantes')) {
								return 'INTEGRANTES - ' + document.getElementById('titulo').textContent;
							}
							else if (document.getElementById('tabla_reporte')) {
								return document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent;
							}
							else if (document.getElementById('tabla_reporte_consolidado')) {
								return document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent;
							}
							else if (document.getElementById('tabla_centros')) {
								return 'Centros de Investigación';
							}
							else if (document.getElementById('tabla_investigadores_wrapper')) {
								return 'Investigadores';
							}
							else if (document.getElementById('tabla_orcid_wrapper')) {
								$('c[r=A1] t', sheet).text('Investigadores');
							}
							else if (document.getElementById('tabla_inventario_wrapper')) {
								return 'INVENTARIO - ' + document.getElementById('titulo').textContent;
							}
							else if (document.getElementById('tabla_programas_wrapper')) {
								return 'Programas';
							}
							else if (document.getElementById('tabla_facultades_wrapper')) {
								return 'Facultades';
							}
							else if (document.getElementById('tabla_grupos_wrapper')) {
								return 'Grupos';
							}
							else if (document.getElementById('tabla_lineas_wrapper')) {
								return 'Lineas de Investigación';
							}
							else if (document.getElementById('tabla_reconocimientos_wrapper')) {
								return 'Reconocimientos';
							}
							else if (document.getElementById('tabla_recolecciones_wrapper')) {
								return 'Recolecciones';
							}
						}

					},
					{
						extend: 'pdf',
						text: 'PDF',
						className: 'pdfButton',
						// Nombre de archivo personalizado
						filename: function() {
							if (document.getElementById('tabla_integrantes')) {
								return 'INTEGRANTES - ' + document.getElementById('titulo').textContent;
							}
							else if (document.getElementById('tabla_reporte')) {
								return document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent;
							}
							else if (document.getElementById('tabla_reporte_consolidado')) {
								return document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent;
							}
							else if (document.getElementById('tabla_centros')) {
								return 'Centros de Investigación';
							}
							else if (document.getElementById('tabla_investigadores_wrapper')) {
								return 'Investigadores';
							}
							else if (document.getElementById('tabla_orcid_wrapper')) {
								$('c[r=A1] t', sheet).text('Investigadores');
							}
							else if (document.getElementById('tabla_inventario_wrapper')) {
								return 'INVENTARIO - ' + document.getElementById('titulo').textContent;
							}
							else if (document.getElementById('tabla_programas_wrapper')) {
								return 'Programas';
							}
							else if (document.getElementById('tabla_facultades_wrapper')) {
								return 'Facultades';
							}
							else if (document.getElementById('tabla_grupos_wrapper')) {
								return 'Grupos';
							}
							else if (document.getElementById('tabla_lineas_wrapper')) {
								return 'Lineas de Investigación';
							}
							else if (document.getElementById('tabla_reconocimientos_wrapper')) {
								return 'Reconocimientos';
							}
							else if (document.getElementById('tabla_recolecciones_wrapper')) {
								return 'Recolecciones';
							}
						},
						// Encabezado del PDF
						title: function() {
							if (document.getElementById('tabla_integrantes')) {
								return 'INTEGRANTES - ' + document.getElementById('titulo').textContent;
							}
							else if (document.getElementById('tabla_reporte')) {
								return document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent;
							}
							else if (document.getElementById('tabla_reporte_consolidado')) {
								return document.getElementById('titulo').textContent + ' - ' + document.getElementById('subtitulo').textContent;
							}
							else if (document.getElementById('tabla_centros')) {
								return 'Centros de Investigación';
							}
							else if (document.getElementById('tabla_investigadores_wrapper')) {
								return 'Investigadores';
							}
							else if (document.getElementById('tabla_inventario_wrapper')) {
								return 'INVENTARIO - ' + document.getElementById('titulo').textContent;
							}
							else if (document.getElementById('tabla_programas_wrapper')) {
								return 'Programas';
							}
							else if (document.getElementById('tabla_facultades_wrapper')) {
								return 'Facultades';
							}
							else if (document.getElementById('tabla_grupos_wrapper')) {
								return 'Grupos';
							}
							else if (document.getElementById('tabla_lineas_wrapper')) {
								return 'Lineas de Investigación';
							}
							else if (document.getElementById('tabla_reconocimientos_wrapper')) {
								return 'Reconocimientos';
							}
							else if (document.getElementById('tabla_recolecciones_wrapper')) {
								return 'Recolecciones';
							}
						},
						exportOptions: {
							columns: ':visible'
						}
					},
					{
						extend: 'print',
						text: 'Imprimir',
						className: 'printButton',
						exportOptions: {
							columns: ':visible',
							stripHtml: false
						}
					}
				],

				language: {
					processing: "Procesamiento en curso...",
					search: "Buscar: ",
					lengthMenu: "Mostrando _MENU_ elementos",
					info: "Mostrando _START_ a _END_ de _TOTAL_ elementos",
					infoEmpty: "Mostrando 0 a 0 de 0 elementos",
					infoFiltered: "(filtrado de _MAX_ elementos en total)",
					infoPostFix: "",
					loadingRecords: "Cargando resultados...",
					zeroRecords: "No hay información para mostrar",
					emptyTable: "No hay información para mostrar",
					paginate: {
						first: "Primera",
						previous: "Anterior",
						next: "Siguiente",
						last: "última"
					}
				}
			});
		}


		// Definición de las tablas
		var tabla_investigadores = $('#tabla_investigadores').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" },
				{ data: "categoria" },
				{ data: "nivelAcademico" },
				{ data: "pertenencia" },
				{ data: "sexo" }
			]
		});

		var tabla_orcid = $('#tabla_orcid').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id", visible: false },
				{ data: "orcid", visible: false },
				{ data: "nombre y apellidos" },
				{ data: "empleos" },
				{ data: "educacion" },
				{ data: "actividades" },
				{ data: "trabajos" },
			]
		});

		$('#tabla_investigadores_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_investigadores
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		$('#tabla_orcid_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_orcid
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		$('#tabla_investigadores tbody').on('click', 'tr', function() {
			var data = tabla_investigadores.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=i";
		});

		$('#tabla_orcid tbody').on('click', 'tr', function() {
			var data = tabla_orcid.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=i";
		});

		var tabla_programas = $('#tabla_programas').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" },
				{ data: "facultad" }
			]
		});

		$('#tabla_programas_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_programas
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		$('#tabla_programas tbody').on('click', 'tr', function() {
			var data = tabla_programas.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=p";
		});

		var tabla_lineas = $('#tabla_lineas').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" }
			]
		});

		$('#tabla_lineas_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_lineas
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		var tabla_reconocimientos = $('#tabla_reconocimientos').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" },
				{ data: "investigador" },
				{ data: "reconocimiento" },
				{ data: "entidad" }
			]
		});

		$('#tabla_reconocimientos_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_reconocimientos
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		var tabla_recolecciones = $('#tabla_recolecciones').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id" },
				{ data: "nombre" },
				{ data: "estado" },
				{ data: "tipoproduccion" }
			]
		});

		$('#tabla_recolecciones_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_recolecciones
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		var tabla_facultades = $('#tabla_facultades').DataTable({
			responsive: true,
			rowId: 'id',
			"order": [[1, "asc"]],
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" }
			]
		});

		$('#tabla_facultades_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_facultades
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		$('#tabla_facultades tbody').on('click', 'tr', function() {
			var data = tabla_facultades.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=f";
		});


		var tabla_centros = $('#tabla_centros').DataTable({
			responsive: true,
			rowId: 'id',
			columns: [
				{ data: "id", "visible": false },
				{ data: "nombre" },
				{ data: "facultad.nombre" }
			]
		});

		$('#tabla_centros_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_centros
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		$('#tabla_centros tbody').on('click', 'tr', function() {
			var data = tabla_centros.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=c";
		});

		var tabla_grupos = $('#tabla_grupos').DataTable({
			responsive: true,
			rowId: 'id',
			columns: [
				{ data: "id", "visible": false },
				{ data: "strings.substringBefore(cod,'Lí')" },
				{ data: "programas.get(0).facultad.nombre" },
				{ data: "programas.get(0).nombre" },
				{ data: "centro.nombre" },
				{ data: "nombre" },
				{ data: "lider" },
				{ data: "categoria" }
			]
		});

		$('#tabla_grupos input').keyup(function() {
			// Busqueda con tildes
			tabla_grupos
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		$('#tabla_grupos tbody').on('click', 'tr', function() {
			var data = tabla_grupos.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=g";
		});

		$('#tabla_idiomas').DataTable({
			columns: [
				{ data: "idioma", className: "font-weight-bold" },
				{ data: "habla" },
				{ data: "escribe" },
				{ data: "lee" },
				{ data: "entiende" }
			]
		});

		var tabla_integrantes = $('#tabla_integrantes').DataTable({
			rowId: 'id',
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" },
				{ data: "categoria" },
				{ data: "nivelAcademico" },
				{ data: "pertenencia" }
			]
		});

		$('#tabla_integrantes tbody').on('click', 'tr', function() {
			var data = tabla_integrantes.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=i";
		});

		$('#tabla_integrantes_filter input').keyup(function() {
			tabla_integrantes
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		var tabla_pertenecientes = $('#tabla_grupos_pertenecientes').DataTable({
			rowId: 'id',
			columns: [
				{ data: "id", visible: false },
				{ data: "nombre" },
				{ data: "categoria" },
				{ data: "lider" }

			]
		});

		$('#tabla_grupos_pertenecientes tbody').on('click', 'tr', function() {
			var data = tabla_pertenecientes.row(this).data();
			window.location.href = "general?id=" + data.id + "&type=g";
		});

		var table = $('#tabla_reporte').DataTable({
			search: { "bSmart": false },
			rowId: 'id',
			"columnDefs": [{
				"targets": [0],
				"visible": false,
			}]
		});

		$('#tabla_reporte_filter input').keyup(function() {
			// Busqeuda con tildes
			table
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		var table = $('#tabla_reporte_consolidado').DataTable({
			search: { "bSmart": false },
			rowId: 'id',
			"columnDefs": [{
				"targets": [0, 7, 8, 9, 10],
				"visible": false,
			}]
		});

		$('#tabla_reporte_consolidado_filter input').keyup(function() {
			table
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		var tprod = $('#table-prod').DataTable();

		$('#table-prod_filter input').keyup(function() {
			tprod
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});

		// .---------------------------PERTENENCIA------------------------------------
		var tabla_pertenencia = $('#tabla_pertenencia').DataTable({
			responsive: true,
			dom: 'Bfrti',
			scrollY: "50vh",
			scrollCollapse: true,
			paging: false,
			rowId: 'id',
			columns: [
				{ data: "id", visible: false },
				{ data: "pertenencia" },
				{ data: "nombre" },
				{ data: "categoria" },
				{ data: "nivelAcademico" }],
			buttons: [
				{
					text: 'Guardar Cambios',
					className: 'saveButton'
				}
			],
			language: {
				processing: "Procesamiento en curso...",
				search: "Buscar: ",
				lengthMenu: "Mostrando _MENU_ elementos",
				info: "Mostrando _START_ a _END_ de _TOTAL_ elementos",
				infoEmpty: "Mostrando 0 a 0 de 0 elementos",
				infoFiltered: "(filtrado de _MAX_ elementos en total)",
				infoPostFix: "",
				loadingRecords: "Cargando resultados...",
				zeroRecords: "No hay información para mostrar",
				emptyTable: "No hay información para mostrar",
				paginate: {
					first: "Primera",
					previous: "Anterior",
					next: "Siguiente",
					last: "última"
				}
			}
		});

		$('.saveButton').on('click', function() {
			$('#tabla_pertenencia > tbody  > tr').each(function() {
				var elID = $(this).attr('id');
				var e = document.getElementById("menuPertenencias" + elID);
				var data_2 = e.options[e.selectedIndex].value;
				$.ajax({
					type: "POST",
					contentType: "application/json",
					url: '/gri/rest/service/pertenencia/' + elID + '/' + data_2,
					dataType: 'json',
					cache: false
				});
			});
			alert("Cambios guardados");
		});

		$('#tabla_pertenencia_filter input').keyup(function() {
			// Busqueda con tildes
			tabla_investigadores
				.search(
					jQuery.fn.DataTable.ext.type.search.string(this.value)
				)
				.draw();
		});






		// ---------------------------------FIN
		// PERTENENCIA-----------------------------


		// Definición tabla Inventario
		var table = $('#tabla_inventario')
			.DataTable(
				{
					responsive: true,
					dom: 'Bfrtip',
					buttons: [
						{
							extend: 'copy',
							text: 'Copiar',
							className: 'copyButton'
						},
						{
							// Excel que obtiene los datos de la tabla
							extend: 'excel',
							text: 'Reporte de Tabla',
							className: 'excelButton',
							customize: function(xlsx) {
								var sheet = xlsx.xl.worksheets['sheet1.xml'];
								$('c[r=A1] t', sheet).text('INVENTARIO - ' + document.getElementById('titulo').textContent);
								// Interación entre cada dato del EXCEL
								$('row c[r^="A"]', sheet).each(function(i) {
									if ($(this).text() == 1) {
										// Pinta la celda completa de verde
										$(this).attr('s', '15');
										$('c[r=B' + (i + 2) + ']', sheet).attr('s', '15');
										$('c[r=C' + (i + 2) + ']', sheet).attr('s', '15');
										$('c[r=D' + (i + 2) + ']', sheet).attr('s', '15');
										$('c[r=E' + (i + 2) + ']', sheet).attr('s', '15');
										$('c[r=F' + (i + 2) + ']', sheet).attr('s', '15');
									}
								});
								var indexes = table.rows().eq(0).filter(function(rowIdx) {
									return table.cell(rowIdx, 7).data() === '1' ? true : false;
								});
								console.log(indexes);
							},
							exportOptions: {
								columns: [7, ':visible']
							},
							filename: function() {
								return 'Reporte de Tabla - ' + document.getElementById('titulo').textContent;
							}
						},
						{
							extend: 'excel',
							text: 'Pendientes',
							className: 'excelButton',
							exportOptions: {
								rows: ':not(.en-inventario)'
							},
							filename: function() {
								return 'Producciones pendientes - ' + document.getElementById('titulo').textContent;
							}
						},
						{
							extend: 'excel',
							text: 'En custodia',
							className: 'excelButton',
							exportOptions: {
								rows: '.en-inventario'
							},
							filename: function() {
								return 'Producciones en Custodia - ' + document.getElementById('titulo').textContent;
							}
						}
					],
					rowId: function(a) {
						return a[1];
					},
					"order": [[4, "desc"]],
					"createdRow": function(row, data,
						dataIndex) {
						if (data[7] == 1) {
							$(row).addClass('en-inventario');
						} else if (data[7] == 2) {
							$(row).addClass('en-proceso');
						}
					},
					"columnDefs": [
						{ "targets": [1, 6, 7], "visible": false },
						{
							"targets": 0,
							"createdCell": function(td,
								cellData, rowData, row,
								col) {
								if (rowData[7] == 0) {
									$(td)
										.html(
											"<select id='checkboxReporte'><option value=0 selected='selected'>Sin custodia</option><option value=2>En proceso</option><option value=1>En custodia</option></select>");
								} else if (rowData[7] == 1) {
									$(td)
										.html(
											"<select id='checkboxReporte'><option value=0>Sin custodia</option><option value=2>En proceso</option><option value=1 selected='selected'>En custodia</option></select>");
								} else if (rowData[7] == 2) {
									$(td)
										.html(
											"<select id='checkboxReporte'><option value=0>Sin custodia</option><option value=2 selected='selected'>En proceso</option><option value=1>En custodia</option></select>");

								}
							}
						}
					]
				});



		// Evento para el checkbox

		$('#tabla_inventario tbody').on(
			'change',
			'select',
			function() {
				var data = table.row($(this).parents('tr')).data();
				var prodId = data[1];
				var tipo = data[6];
				var estado = data[7];

				console.log(tipo);
				if (tipo == 3) {
					tipo = 'bibliografica';
				} else {
					tipo = 'generica';
				}

				var nuevoEstado = $(this)[0].value;
				var combo = $(this)[0]
				var antiguoEstilo;
				var nuevoEstilo;

				var texto;
				if (estado == 0) {

					if (nuevoEstado == 1) {
						texto = "¿Desea agregar al inventario?";
						nuevoEstilo = "en-inventario";
					} else if (nuevoEstado == 2) {
						texto = "¿Desea iniciar un proceso de recolección para esta evidencia?"
						nuevoEstilo = "en-proceso";
					}

				} else if (estado == 1) {

					antiguoEstilo = "en-inventario";
					if (nuevoEstado == 0) {
						texto = "¿Desea remover del inventario?";
					} else if (nuevoEstado == 2) {
						nuevoEstilo = "en-proceso"
						texto = "¿Desea volver a iniciar un proceso de recolección para esta evidencia?"
					}

				} else if (estado == 2) {
					antiguoEstilo = "en-proceso";
					if (nuevoEstado == 0) {
						texto = "Ya existe un caso para la recolección de esta producción, al usted cambiar el estado de esta producción y dejarla sin custodia el caso se eliminará. ¿Desea continuar?";
					} else if (nuevoEstado == 1) {
						texto = "Ya existe un caso para la recolección de esta producción, al usted cambiar el estado de esta producción el caso finalizará. ¿Desea continuar?"
						nuevoEstilo = "en-inventario"
					}

				}

				console.log("Antiguo estado: " + estado + " Nuevo: " + nuevoEstado);
				console.log("Antiguo estilo: " + antiguoEstilo + " Nuevo: " + nuevoEstilo);
				swal({
					text: texto,
					icon: "warning",
					dangerMode: true,
					className: "",
					buttons: {
						cancel: {
							text: "Cancelar",
							value: null,
							visible: true,
							className: "",
							closeModal: true,
						},
						confirm: {
							text: "Aceptar",
							value: true,
							visible: true,
							className: "",
							closeModal: true
						}
					},
				}).then((value) => {
					if (value) {
						$.ajax({
							type: "PUT",
							contentType: "application/json",
							url: '/gri/rest/service/producciones/actualizarestado/' + prodId + '?estado=' + nuevoEstado + '&tipo=' + tipo,
							dataType: 'json',
							cache: false,
							success: function(res) {
								if (res) {
									$('#tabla_inventario').dataTable().fnUpdate(nuevoEstado,
										$('#tabla_inventario tr#' + prodId),
										7, false);
									if (antiguoEstilo != null) {
										$('#tabla_inventario tr#' + prodId).removeClass(antiguoEstilo);
									}
									if (nuevoEstilo != null) {
										$('#tabla_inventario tr#' + prodId).addClass(nuevoEstilo);
									}
								}
							}
						}).fail(function() {
							swal({
								text: "No fue posible hacer la actualización del estado de esta producción, por favor consulte al soporte técnico",
								icon: "error",
								dangerMode: true,
								className: "",
								buttons: {
									confirm: {
										text: "Aceptar",
										value: true,
										visible: true,
										className: "",
										closeModal: true
									}
								},
							})
							combo.value = estado;
						});
					} else {
						combo.value = estado;
					}
				});
			});

		$('#tabla_inventario_filter input').keyup(
			function() {
				table.search(
					jQuery.fn.DataTable.ext.type.search
						.string(this.value)).draw();
			});

	});
}



)(jQuery); // End of use strict

function estilosBotones() {
	$(".btn").addClass("text-white");
	$(".btn").css("text-shadow", "none")
}


