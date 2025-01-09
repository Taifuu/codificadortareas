package modeloEstimacion;

public abstract class QueryTexto {
	public static final String IN_PARAMS = "[IN_PARAMS]";
	public static final String NOMBRE_PARAMETRO_ENTRADA = "[NOMBRE_PARAMETRO]";
	public static final String QUERY_COMPLETA = "[QUERY_COMPLETA]";
	public static final String NOMBRE_INTERFACE = "[NOMBRE_INTERFACE]";
	public static final String COMA_ESPACIO = ", \"";
	public static final String COMILLAS = "\"";

	/*Este es el nombre de la interface*/
	public static final String NOMBRE_QUERY = "";
	
	/*Acá se pone la query que queres transformar en una interface*/
	public static final String QUERY = "";
	
	public static final String TEMPLATE_INTERFACE = "interface "+ NOMBRE_QUERY +" {\n" + 
			"\tString QUERY =  "+ QUERY_COMPLETA +";\n" + 
			"\n" + 
			IN_PARAMS +
			"}";
	
	public static final String TEMPLATE_PARAMETRO_DE_ENTRADA = "\tList<String> inParams = List.of(\""+ NOMBRE_PARAMETRO_ENTRADA +");";
}
