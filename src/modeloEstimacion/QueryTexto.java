package modeloEstimacion;

public abstract class QueryTexto {
	public static final String IN_PARAMS = "[IN_PARAMS]";
	public static final String OUT_PARAMS = "[OUT_PARAMS]";
	public static final String NOMBRE_PARAMETRO_SALIDA = "[NOMBRE_PARAMETRO_SALIDA]";
	public static final String NOMBRE_PARAMETRO_ENTRADA = "[NOMBRE_PARAMETRO]";
	public static final String QUERY_COMPLETA = "[QUERY_COMPLETA]";
	public static final String NOMBRE_INTERFACE = "[NOMBRE_INTERFACE]";

	/*Este es el nombre de la interface*/
	public static final String NOMBRE_QUERY = "";
	
	/*Acá se pone la query que queres transformar en una interface*/
	public static final String QUERY = "";
	
	public static final String TEMPLATE_INTERFACE = "interface "+ NOMBRE_QUERY +" {\n" + 
			"\tString QUERY = "+ QUERY_COMPLETA +";\n" + 
			"\n" + 
			IN_PARAMS +"\n" + 
			OUT_PARAMS +"\n" + 
			"}";
	
	public static final String TEMPLATE_PARAMETRO_DE_ENTRADA = "\tString IN_"+ NOMBRE_PARAMETRO_ENTRADA +" = \""+ NOMBRE_PARAMETRO_ENTRADA +"\";";
	
	public static final String TEMPLATE_PARAMETRO_DE_SALIDA = "\tString OUT_"+ NOMBRE_PARAMETRO_SALIDA +" = \""+ NOMBRE_PARAMETRO_SALIDA +"\";";
}
