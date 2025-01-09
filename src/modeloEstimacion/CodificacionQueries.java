package modeloEstimacion;

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
	public static final char PIPE_PARENTESIS_DERECHO = ')';
	public static final char PIPE_PARENTESIS_IZQUIERDO = '(';
	public static final String PIPE_PARENTESIS_DERECHO_STRING = ")";
	public static final String PIPE_PARENTESIS_IZQUIERDO_STRING = ")";
	public static final String PIPE_PUNTO_COMA = ";";
	public static final String PATRON_PARAMETRO_IN = "(\\S+)";
	public static final String PIPE_BARRA_N = "\n";
	public static final String PIPE_ESPACIO  = " ";
	
	public static String getParametrosEntrada(String query) {
		StringBuilder template = new StringBuilder();
		String[] parametrosSpliteados = query.split(PIPE_DOBLE_PUNTO);
		Pattern pattern = Pattern.compile(PATRON_PARAMETRO_IN);
		List<String> palabras = new ArrayList<>();
		StringBuilder inParamsAnt = new StringBuilder();
		for (int i = 1; i < parametrosSpliteados.length; i++) {
			Matcher matcher = pattern.matcher(parametrosSpliteados[i]);
			if(matcher.find()) {
				String palabra =  matcher.group(1);
				if(palabra.contains(PIPE_COMA)) {
					palabra = palabra.replace(PIPE_COMA, "");
				}
				if(palabra.contains(PIPE_PARENTESIS_DERECHO_STRING)) {
					palabra = palabra.replace(PIPE_PARENTESIS_DERECHO_STRING, "");
				}
				if(palabra.contains(PIPE_PUNTO_COMA)) {
					palabra = palabra.replace(PIPE_PUNTO_COMA, "");
				}
				if(!palabras.contains(palabra)) {
					palabras.add(palabra);
					inParamsAnt.append(QueryTexto.COMA_ESPACIO).append(palabra.toUpperCase()).append(QueryTexto.COMILLAS);
				} else {
					continue;
				}
			}
		}
		template.append(QueryTexto.TEMPLATE_PARAMETRO_DE_ENTRADA.replace(QueryTexto.NOMBRE_PARAMETRO_ENTRADA, inParamsAnt.toString().replaceFirst(QueryTexto.COMA_ESPACIO, "")));
		template.append("\n");
		return template.toString();
	}
	
	public static String decodeQuery(String query) {
		String output = QueryTexto.TEMPLATE_INTERFACE;
		if(query.contains(PIPE_DOBLE_PUNTO)) {
			output = output.replace(QueryTexto.IN_PARAMS, getParametrosEntrada(query));
		} else {
			output = output.replace(QueryTexto.IN_PARAMS + "\n", "");
		}
		String[] querySpliteada = query.split(PIPE_BARRA_N);
		StringBuilder queryCompleta = new StringBuilder("\"\"\"\n");
		for (int i = 0; i < querySpliteada.length; i++) {
			String unaLinea = querySpliteada[i];
			if(unaLinea.contains("\r")) {
				unaLinea = unaLinea.replace("\r", "");
			}
			if(unaLinea.contains("\n")) {
				unaLinea = unaLinea.replace("\n", "");
			}
			queryCompleta.append("\t\t\t\t\t\t").append(unaLinea).append("\n");
		}
		queryCompleta.append("\t\t\t\t\t\t").append("\"\"\"");
		
		output = output.replace(QueryTexto.QUERY_COMPLETA, queryCompleta.toString());
		
		return output;
	}
	
	public static void main(String[] args) {
		System.out.println(decodeQuery(QueryTexto.QUERY.toUpperCase()));
	}
}
