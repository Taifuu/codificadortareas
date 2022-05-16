package modeloEstimacion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class CodificacionEstimacion {

	private CodificacionEstimacion() {}
	
	public static final String REGEX_TAREA_SIN_HORA = "[.]+[a-zA-Z _.]+";
	public static final String REGEX_TAREA = "[.]+[a-zA-Z _.]+[	]*[0-9]*hs*";
	public static final String REGEX_COMIENZO_DESCRIPCION = "<[\\s\\S]+";
	public static final String REGEX_FIN_DESCRIPCION = "[\\s\\S]+>";
	public static final String REGEX_TITULO = "[a-zA-Z _.]+";
	public static final char PUNTO = '.';
	public static final String TABULACION = "	";
	
	public static List<Tarea> getHijos(ListaDeTareas lista, int nivel){
		if(lista.i > lista.lineas.length) {
			return new ArrayList<Tarea>();
		}
		List<Tarea> output = new ArrayList<Tarea>();
		for(; lista.i < lista.lineas.length; lista.i++) {
			String unString = lista.lineas[lista.i].trim();
			
			if(deboIgnorar(unString, lista)) {
				continue;
			}
			
			int nivelTarea = getNivel(unString);
			boolean esHijo = false;
			
			if(nivelTarea == nivel) {
				Tarea hijo = new Tarea();
				hijo.nivel = nivelTarea;
				hijo.nombreTarea = getNombreBien(unString);
				if(lista.i+1 < lista.lineas.length) {
					String nombreSiguiente = obtenerSiguienteNombre(lista);
					int nivelHijo = getNivel(nombreSiguiente);
					esHijo = nivelHijo > nivel;
				}
				if(esHijo) {
					lista.i++;
					hijo.hijos = getHijos(lista, hijo.nivel+1);
				} else {
					hijo.hijos = new ArrayList<Tarea>();
				}
				output.add(hijo);
			} else if(nivelTarea < nivel) {
				lista.i--;
				return output;
			}
		}
		return output;
	}
	
	public static boolean deboIgnorar(String unString, ListaDeTareas lista) {
		if(unString.isEmpty()) {
			return true;
		}
		
		if(esComentario(unString)) {
			saltearComentarios(lista);
			return true;
		}
		return false;
	}
	
	public static String obtenerSiguienteNombre(ListaDeTareas lista) {
		String output = lista.lineas[lista.i+1].trim();
		if(output.isEmpty()) {
			lista.i++;
			if(lista.i+1 < lista.lineas.length) {
				output = lista.lineas[lista.i+1].trim();
			}
		}
		
		if(esComentario(output)) {
			saltearComentarios(lista);
			if(lista.i+1 < lista.lineas.length) {
				output = lista.lineas[lista.i+1].trim();
			}
		}
		return output;
	}
	
	public static void saltearComentarios(ListaDeTareas lista) {
		boolean esFinDescripcion = false;
		for(;(lista.i < lista.lineas.length) && !esFinDescripcion; lista.i++) {
			String descripcion = lista.lineas[lista.i].trim();
			esFinDescripcion = Pattern.matches(REGEX_FIN_DESCRIPCION, descripcion);
		}
		lista.i--;
	}
	
	public static boolean esComentario(String texto) {
		return Pattern.matches(REGEX_COMIENZO_DESCRIPCION, texto);
	}
	
	public static String getNombreBien(String nombreFeo) {
		int inicio = nombreFeo.indexOf(TABULACION);
		int fin = nombreFeo.lastIndexOf(TABULACION);
		if(inicio < 0 || fin < 0) {
			return nombreFeo.substring(getNivel(nombreFeo), nombreFeo.length());
		}
		String primeraParteBien = nombreFeo.substring(getNivel(nombreFeo), inicio);
		String segundaParteBien = nombreFeo.substring(fin, nombreFeo.length());
		return (primeraParteBien + segundaParteBien);
	}
	
	public static int getNivel(String nombreTarea) {
		for(int i = 0; i < nombreTarea.length(); i++) {
			if(PUNTO != nombreTarea.charAt(i)) {
				return i;
			}
		}
		return 0;
	}
	
	public static float transformarTextoAhoras(String nombreTarea) {
		nombreTarea = nombreTarea.trim();
		int lastIndexEspacioLargo = nombreTarea.lastIndexOf("	");
		int lastIndexHs = nombreTarea.lastIndexOf("hs");
		String horasSueltas = nombreTarea.substring(lastIndexEspacioLargo+1, lastIndexHs);
		return Float.parseFloat(horasSueltas);
	}
	
	public static float getTareasString(Tarea tarea, String nombreAnterior, boolean esHija, float contadorHoras) {
		for (Tarea unaTarea : tarea.hijos) {
			String nombreTareaModif = (esHija? " -> " + unaTarea.nombreTarea: unaTarea.nombreTarea);
			nombreAnterior += nombreTareaModif;
			if(unaTarea.hijos != null && !unaTarea.hijos.isEmpty()) {
				contadorHoras = getTareasString(unaTarea, nombreAnterior, true, contadorHoras);
				nombreAnterior = nombreAnterior.replace(nombreTareaModif, "");
			} else {
				if(!TareaEnTexto.LISTA_TAREA_EN_TEXTO.contains(nombreAnterior)) {
					contadorHoras += transformarTextoAhoras(nombreAnterior);
					System.out.println(nombreAnterior);
				}
				nombreAnterior = nombreAnterior.replace(nombreTareaModif, "");
			}
		}
		return contadorHoras;
	}
	
	public static void main(String[] args) {
		ListaDeTareas lista = new ListaDeTareas();
		lista.lineas = TareaEnTexto.TAREA_EN_TEXTO.split("\n");
		lista.i = 0;
		Tarea padreSupremo = new Tarea();
		if(lista.lineas != null && lista.lineas.length > 0) {
			padreSupremo.nombreTarea = "Lista de Tareas";
			padreSupremo.hijos = getHijos(lista, 0);
		}
		float contadorHoras = getTareasString(padreSupremo, "", false, 0);
		System.out.println("El total de horas son: " + contadorHoras + "hs");
	}
}
