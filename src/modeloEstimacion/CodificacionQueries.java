package modeloEstimacion;

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
	
	public static String getParametrosSalida(String query) {
		StringBuilder template = new StringBuilder();
		int posSelect = query.indexOf(CLAVE_SELECT);
		if(posSelect != -1) {
			posSelect += CLAVE_SELECT.length();
		}
		int posFrom = query.indexOf(CLAVE_FROM);
		String parametrosSalidaString = query.substring(posSelect, posFrom);
		parametrosSalidaString = recortarPalabrasEntreParentesis(parametrosSalidaString);
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
			temp = QueryTexto.TEMPLATE_PARAMETRO_DE_SALIDA.replace(QueryTexto.NOMBRE_PARAMETRO_SALIDA, temp.toUpperCase());
			if(i+1 < limite) {
				template.append(temp+"\n");
			} else {
				template.append(temp);
			}
		}
		
		return template.toString();
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
				if(palabra.contains(PIPE_PARENTESIS_DERECHO_STRING)) {
					palabra = palabra.replace(PIPE_PARENTESIS_DERECHO_STRING, "");
				}
				if(palabra.contains(PIPE_PUNTO_COMA)) {
					palabra = palabra.replace(PIPE_PUNTO_COMA, "");
				}
				if(template.indexOf(palabra) < 0) {
					template.append(QueryTexto.TEMPLATE_PARAMETRO_DE_ENTRADA.replace(QueryTexto.NOMBRE_PARAMETRO_ENTRADA, palabra.toUpperCase()));
				} else {
					continue;
				}
			}
			if(i+1 < parametrosSpliteados.length) {
				template.append("\n");
			}
		}
		return template.toString();
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
				queryCompleta.append(" \\n\" +\n");
			} else {
				queryCompleta.append("\"");
			}
		}
		
		output = output.replace(QueryTexto.QUERY_COMPLETA, queryCompleta.toString());
		
		return output;
	}
	
	private static String recortarPalabrasEntreParentesis(String parametrosSalidaString) {
		StringBuilder output = new StringBuilder(parametrosSalidaString);
		boolean parentesis = parametrosSalidaString.contains(PIPE_PARENTESIS_IZQUIERDO_STRING);
		do {
			int contParIzq = 0;
			int posPrimerParIzq = 0;
			int contParDer = 0;
			//Si tengo parentesis izquierdo
			if(parentesis) {
				for (int i = 0; i < output.length(); i++) {
					char c = output.charAt(i);
					//Reviso cuantos tengo y los cuento
					if(PIPE_PARENTESIS_IZQUIERDO == c) {
						if(contParIzq == 0) {
							posPrimerParIzq = i;
						}
						contParIzq += 1;
					}
					//Cuando encuentro el primer parentesis derecho
					if(PIPE_PARENTESIS_DERECHO == c) {
						for (int j = 0; j < output.length(); j++) {
							char z = output.charAt(j);
							//Reviso cuantos hay y los cuento
							if(PIPE_PARENTESIS_DERECHO == z) {
								contParDer += 1;
							}
							//Cuando la cantidad sea la misma
							if(contParIzq <= contParDer) {
								//Recorto el string para eliminar toda esa parte que no sirve
								output.delete(posPrimerParIzq, j+2);
								break;
							}
						}
					}
				}
				//Reviso si hay m�s partes innecesarias para cortar
				parentesis = output.toString().contains(PIPE_PARENTESIS_IZQUIERDO_STRING);
			}
			/*if(parentesis) {
				for (int i = 0; i < parametrosSalidaString.length(); i++) {
					char c = parametrosSalidaString.charAt(i);
					//Reviso cuantos tengo y los cuento
					if(PIPE_PARENTESIS_IZQUIERDO == c) {
						contParIzq += 1;
					}
					//Cuando encuentro el primer parentesis derecho
					if(PIPE_PARENTESIS_DERECHO == c) {
						for (int j = 0; j < parametrosSalidaString.length(); j++) {
							char z = parametrosSalidaString.charAt(j);
							//Reviso cuantos hay y los cuento
							if(PIPE_PARENTESIS_DERECHO == z) {
								contParDer += 1;
							}
							//Cuando la cantidad sea la misma
							if(contParIzq <= contParDer) {
								//Recorto el string para eliminar toda esa parte que no sirve
								parametrosSalidaString = parametrosSalidaString.substring(j+2);
								//Y reviso si hay m�s parametros de salida
								if(parametrosSalidaString.contains(PIPE_COMA)) {
									//Si los hay, los guardo en una variable temporal
									String par = parametrosSalidaString.substring(0, parametrosSalidaString.indexOf(PIPE_COMA)+1);
									if(par.contains(PIPE_PARENTESIS_IZQUIERDO_STRING)) {
										par = recortarPalabrasEntreParentesis(par);
									}
									output.append(par);
									parametrosSalidaString = parametrosSalidaString.substring(parametrosSalidaString.indexOf(PIPE_COMA)+1);
									break;
								} else {
									//Si es el �ltimo o no hay m�s, tambi�n los guardo en la variable
									output.append(parametrosSalidaString);
									break;
								}
							}
						}
					}
				}
				//Reviso si hay m�s partes innecesarias para cortar
				parentesis = parametrosSalidaString.contains(PIPE_PARENTESIS_IZQUIERDO_STRING);
			}*/
		} while (parentesis);
		//Si en principio no hab�a parentesis entonces devuelvo lo que entro, sino devuelvo lo que tengo en output
		if(output.length() > 0) {
			return output.toString();
		} else {
			return parametrosSalidaString;
		}
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
