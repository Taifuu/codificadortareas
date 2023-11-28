package modeloEstimacion;

public abstract class QueryTexto {
	public static final String IN_PARAMS = "[IN_PARAMS]";
	public static final String OUT_PARAMS = "[OUT_PARAMS]";
	public static final String PARAMETRO_DE_SALIDA = "[PARAMETRO_DE_SALIDA]";
	public static final String NOMBRE_PARAMETRO_SALIDA = "[NOMBRE_PARAMETRO_SALIDA]";
	public static final String POSICION_PARAMETRO_SALIDA = "[POSICION_PARAMETRO_SALIDA]";
	public static final String NOMBRE_PARAMETRO_ENTRADA = "[NOMBRE_PARAMETRO]";
	public static final String PARAMETRO_DE_ENTRADA = "[PARAMETRO_DE_ENTRADA]";
	public static final String QUERY_COMPLETA = "[QUERY_COMPLETA]";
	public static final String NOMBRE_INTERFACE = "[NOMBRE_INTERFACE]";

	/*Este es el nombre de la interface*/
	public static final String NOMBRE_QUERY = "";
	
	/*Ac� se pone la query que queres transformar en una interface*/
	public static final String QUERY = "asdfasd";
	
	public static final String TEMPLATE_INTERFACE = "interface "+ NOMBRE_QUERY +" {\n" + 
			"\tString QUERY = "+ QUERY_COMPLETA +";\n" + 
			"\n" + 
			"\t"+ IN_PARAMS +"\n" + 
			"\t"+ OUT_PARAMS +"\n" + 
			"}";
	
	public static final String TEMPLATE_INTERFACE_IN_PARAMS = "interface IN_PARAMS {\n" + 
			""+ PARAMETRO_DE_ENTRADA +"\n" + 
			"\t}";
	
	public static final String TEMPLATE_PARAMETRO_DE_ENTRADA = "\t\tString "+ NOMBRE_PARAMETRO_ENTRADA +" = \""+ NOMBRE_PARAMETRO_ENTRADA +"\";";
	
	public static final String TEMPLATE_INTERFACE_OUT_PARAMS = "interface OUT_PARAMS {\n" + 
			""+ PARAMETRO_DE_SALIDA +"\n" + 
			"\t}";
	
	public static final String TEMPLATE_PARAMETRO_DE_SALIDA = "\t\tint "+ NOMBRE_PARAMETRO_SALIDA +" = "+ POSICION_PARAMETRO_SALIDA +";";
}
