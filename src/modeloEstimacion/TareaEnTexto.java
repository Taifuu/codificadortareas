package modeloEstimacion;

public abstract class TareaEnTexto {

	/*Reglas de Formato:
	 * 1. Se debe agregar el nombre "Padre" que es el nombre de lo que se vaya a modificar (Ejemplo: BASE DE DATOS, SGT, WS MDP TR069, etc...)
	 * 2. Bajo el nombre "Padre" se debe empezar a enumerar las tareas hijas con puntos, las tareas de nivel 1 tienen un punto
	 * 		las de nivel 2 tienen dos puntos y asi suscesivamente hasta completar el numero de tareas hijas deseadas
	 * 3. Los nombres de las tareas no pueden superar el renglon, si el nombre de tu tarea es demasiado largo eso indica que no es un buen nombre
	 * 		y deberias cambiarlo a algo mas sencillo agregandole una descripcion abajo
	 * 4. Las descripciones tienen que estar contenidas dentro de "<>" para poder tomarse como tal
	 * 5. Para las horas utilizar formato hs, es decir si tu tarea lleva 30m seria 0.5hs
	 * 6. Solo las tareas hijas finales deben llevar horas
	 * Estas reglas pueden ir mutando dependiendo el codigo, favor de cambiarlas si se toco algo que modifique el comportamiento del mismo
	 comentario de prueba Nacho*/
	
	public static final String TAREA_EN_TEXTO = "DB\r\n" + 
			"	.Crear tabla MAT_GAMER.																									0.5hs\r\n" + 
			"		<Fecha tiene que tener un DEFAULT SYSDATE para que se cargue solo>\r\n" + 
			"	.Agregar columna GAMER a LOG_MIGRACION_IPV6. 																			0.5hs\r\n" + 
			"	.Crear tabla MAT_GAMER_LOTE.																							0.5hs\r\n" + 
			"		<(ID_MAT_GAMER_LOTE, MAX_SERVICIOS, PROCESADOS_OK, PROCESADOS_ERROR, RESULTADO, ID_LOG_MIGRACION_IPV6_LOTE)>\r\n" + 
			"	\r\n" + 
			"wsIpFija\r\n" + 
			"	.Crear MigracionVlanController.																							1hs\r\n" + 
			"	.Crear modelo ServicioAMigrar.																							1hs\r\n" + 
			"		<String idOrdenServicio, String idOrdenServicioInternet, String numeroEnlaceCrm, String claveProductoCrm, \r\n" + 
			"		String idEquipo, String idPuertoOlt, boolean esIpv4Privada, boolean esIpv4Fija, boolean equipoSoportaMigracion,\r\n" + 
			"		boolean flagBridge, boolean flagDmz>\r\n" + 
			"	.Crear modelo LogMigracionVlan.																							1hs\r\n" + 
			"		<String idLote, Date fecha, String maxServicios, String procesadosOk, String procesadosError>\r\n" + 
			"	.Crear enum EstadoMigracion.																							0.5hs\r\n" + 
			"		<En Curso, Completa, No existe la petición>\r\n" + 
			"	.Crear modelo RespuestaVerificarEstadoMigracionVlan.																	1hs\r\n" + 
			"		<String resultado, EstadoMigracion mensaje, LogMigracionVlan logMigracionVlan>\r\n" + 
			"	.Crear modelo RespuestaConsultarHistorialMigracionVlan.																	1hs\r\n" + 
			"		<String resultado, String mensaje, List<LogMigracionVlan> logsMigracionVlan>\r\n" + 
			"	.Crear modelo RequestComenzarMigracionVlan.																				0.5hs\r\n" + 
			"		<String maxServicios>\r\n" + 
			"	.Crear modelo RequestVerificarEstadoMigracionVlan.																		0.5hs\r\n" + 
			"		<String idLote>\r\n" + 
			"	.Crear modelo RequestConsultarHistorialMigracionVlan.																	0.5hs\r\n" + 
			"		<String fechaInicial, String fechaFinal>\r\n" + 
			"	.Crear endpoind comenzarMigracionVlan.																					\r\n" + 
			"		..Crear hilo aparte para ejecutar el procesamiento.																	1hs\r\n" + 
			"		..Responder OK si se recibio el request.																			0.5hs\r\n" + 
			"		..Recuperar Servicios a migrar.																						1.5hs\r\n" + 
			"		..Validar el servicio.\r\n" + 
			"			...Valido que el producto sea un SCI no BAFE.																	0.5hs\r\n" + 
			"			...Valido que el equipo soporte la migracion (FLAG_MIGRA_VLAN = 1, MODELO_EQUIPO).								0.5hs\r\n" + 
			"			...Valido si el Servicio esta activo (Revisar que no tenga una provision de baja ok).							1hs\r\n" + 
			"			...Valido que no este ya en ipv4 privada.																		0.5hs\r\n" + 
			"			...Valido que no tenga ip fija (INTERNET_SCI, FLAG_IPV4_FIJA = 1).												0.5hs\r\n" + 
			"			...Valido que no tenga flag_dmz ni flag_bridge activo en EQUIPO.												0.5hs\r\n" + 
			"			...Valido que no se este migrando el puerto de la olt (MIGRACION_SERVICIOS_GPON).								1hs\r\n" + 
			"			...Si el FLAG_VALIDAR_CONDICION_IP esta activo.																	1hs\r\n" + 
			"				<Reviso que la ip no sea publica>\r\n" + 
			"		..Cargar tabla MIGRACION_SERVICIOS_IPV6 para que no se migren dos veces el mismo servicio.							1hs\r\n" + 
			"		..Actualizo la tabla internet_sci IPV4_PRIVADA = 1, FLAG_IPV4_GAMING = 0.											1hs\r\n" + 
			"		..Si debo migrar a Gaming actualizo FLAG_IPV4_GAMING = 1.															1hs\r\n" + 
			"		..Borro registro en tabla MIGRACION_SERVICIOS_IPV6 porque termine la migracion.										0.5hs\r\n" + 
			"		..Impacto servicio.\r\n" + 
			"			...Si es huawei impacto MigracionVlan.																			0.5hs\r\n" + 
			"			...Si es nokia hago Baja Alta.																					0.5hs\r\n" + 
			"			...Si el servicio estaba suspendido, se realiza un impacto de suspensión.										0.5hs\r\n" + 
			"		..Loggear servicio migrado.																							1hs\r\n" + 
			"		..Finalizar Lote.																									1hs\r\n" + 
			"	.Crear endpoint verificarEstadoMigracionVlan.\r\n" + 
			"		..Validar datos de entrada.																							0.5hs\r\n" + 
			"		..Recuperar migracion.																								2hs\r\n" + 
			"		..Armar json con la informacion.																					0.5hs\r\n" + 
			"	.Crear endpoint consultarHistorialMigracionVlan\r\n" + 
			"		..Validar datos de entrada.																							0.5hs\r\n" + 
			"		..Recuperar migraciones.																							2hs\r\n" + 
			"		..Armar json con la informacion.																					0.5hs\r\n" + 
			"	\r\n" + 
			"	.Crear Junits																											20hs\r\n" + 
			"	\r\n" + 
			"Pruebas																														30hs";
	
	/*Esta variable sirve para que si tu a tu estimacion se le tuvo que agregar algo entonces podes poner la anterior
	 * lista de tareas aca y cuando se corra el main solo te va a devolver las nuevas tareas*/
	
	public static final String LISTA_TAREA_EN_TEXTO = "";
}
