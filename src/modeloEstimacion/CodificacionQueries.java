package modeloEstimacion;

import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodificacionQueries {

	public static final String CLAVE_SELECT = "SELECT";
	public static final String CLAVE_FROM = "FROM";
	public static final String CLAVE_WHERE = "WHERE";
	public static final String PIPE_PUNTO = ".";
	public static final String PIPE_COMA = ",";
	public static final String PIPE_DOBLE_PUNTO = ":";
	public static final String PIPE_PARENTESIS_DERECHO = ")";
	public static final String PATRON_PARAMETRO_IN = "(\\S+)";
	public static final String PIPE_BARRA_N = "\n";
	public static final String PIPE_ESPACIO  = " ";
	
	public static String getParametrosSalida(String query) {
		StringBuilder template = new StringBuilder();
		int posSelect = query.indexOf(CLAVE_SELECT);
		if(posSelect != -1) {
			posSelect += CLAVE_SELECT.length();
		}
		int posFrom = query.indexOf(CLAVE_FROM);
		String parametrosSalidaString = query.substring(posSelect, posFrom);
		String[] parametrosSpliteados = parametrosSalidaString.split(PIPE_COMA);
		int limite = parametrosSpliteados.length;
		for (int i = 0; i < limite; i++) {
			String unParametro = parametrosSpliteados[i].trim();
			String temp = unParametro;
			if(temp.contains(PIPE_PUNTO)) {
				temp = temp.substring(temp.indexOf(PIPE_PUNTO)+1);
			}
			if(temp.contains(PIPE_ESPACIO)) {
				String[] parSplit = temp.split(PIPE_ESPACIO);
				temp = parSplit[parSplit.length-1];
			}
			temp = QueryTexto.TEMPLATE_PARAMETRO_DE_SALIDA.replace(QueryTexto.NOMBRE_PARAMETRO_SALIDA, temp);
			temp = temp.replace(QueryTexto.POSICION_PARAMETRO_SALIDA, ((i+1)+""));
			if(i+1 < limite) {
				template.append(temp+"\n");
			} else {
				template.append(temp);
			}
		}
		
		return QueryTexto.TEMPLATE_INTERFACE_OUT_PARAMS.replace(QueryTexto.PARAMETRO_DE_SALIDA, template.toString());
	}
	
	public static String getParametrosEntrada(String query) {
		StringBuilder template = new StringBuilder();
		String[] parametrosSpliteados = query.split(PIPE_DOBLE_PUNTO);
		Pattern pattern = Pattern.compile(PATRON_PARAMETRO_IN);
		for (int i = 1; i < parametrosSpliteados.length; i++) {
			Matcher matcher = pattern.matcher(parametrosSpliteados[i]);
			if(matcher.find()) {
				String palabra =  matcher.group(1);
				if(palabra.contains(PIPE_COMA)) {
					palabra = palabra.replace(PIPE_COMA, "");
				}
				if(palabra.contains(PIPE_PARENTESIS_DERECHO)) {
					palabra = palabra.replace(PIPE_PARENTESIS_DERECHO, "");
				}
				template.append(QueryTexto.TEMPLATE_PARAMETRO_DE_ENTRADA.replace(QueryTexto.NOMBRE_PARAMETRO_ENTRADA, palabra));
			}
			if(i+1 < parametrosSpliteados.length) {
				template.append("\n");
			}
		}
		return QueryTexto.TEMPLATE_INTERFACE_IN_PARAMS.replace(QueryTexto.PARAMETRO_DE_ENTRADA, template.toString());
	}
	
	public static String decodeQuery(String query) {
		String output = QueryTexto.TEMPLATE_INTERFACE;
		if(query.startsWith(CLAVE_SELECT)) {
			output = output.replace(QueryTexto.OUT_PARAMS, getParametrosSalida(query));
		} else {
			output = output.replace(QueryTexto.OUT_PARAMS, "");
		}
		if(query.contains(PIPE_DOBLE_PUNTO)) {
			output = output.replace(QueryTexto.IN_PARAMS, getParametrosEntrada(query));
		} else {
			output = output.replace(QueryTexto.IN_PARAMS, "");
		}
		String[] querySpliteada = query.split(PIPE_BARRA_N);
		StringBuilder queryCompleta = new StringBuilder();
		for (int i = 0; i < querySpliteada.length; i++) {
			String unaLinea = querySpliteada[i];
			if(unaLinea.contains("\r")) {
				unaLinea = unaLinea.replace("\r", "");
			}
			queryCompleta.append("\"").append(unaLinea);
			if(i+1 < querySpliteada.length) {
				queryCompleta.append("\" +\n");
			} else {
				queryCompleta.append("\"");
			}
		}
		
		output = output.replace(QueryTexto.QUERY_COMPLETA, queryCompleta.toString());
		
		return output;
	}
	
	public static void main(String[] args) {
		/*List<String> o = getParametrosSalida(QueryTexto.QUERY);
		for (String string : o) {
			System.out.println(string);
		}*/
		//System.out.println(getParametrosSalida(QueryTexto.QUERY));
		System.out.println(decodeQuery(QueryTexto.QUERY));
	}
}
