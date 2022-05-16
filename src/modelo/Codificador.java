package modelo;

public abstract class Codificador {
	
	private Codificador() {}
	
	public static final String PATH_CODIGO_SUBTAREA_INICIO = "<li id=\"subtask_";
	public static final int CANTIDAD_CARACTERES_PATH_CODIGO_SUBTAREA_INICIO = 16;
	public static final String PATH_CODIGO_SUBTAREA_FIN = "\" class=\"subtask-item\">";
	public static final String PATH_NOMBRE_SUBTAREA_INICIO = "<p>";
	public static final int CANTIDAD_CARACTERES_PATH_NOMBRE_SUBTAREA_INICIO = 3;
	public static final String PATH_NOMBRE_SUBTAREA_FIN = "</p>";
	public static final int CANTIDAD_CARACTERES_PATH_NOMBRE_SUBTAREA_FIN = 4;
	
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder(MuchoTexto.DEMASIADO_TEXTO);
		boolean tengoQueLeerTexto = true;
		while(tengoQueLeerTexto) {
			int posicionInicioCodigoSubTarea = builder.indexOf(PATH_CODIGO_SUBTAREA_INICIO) + CANTIDAD_CARACTERES_PATH_CODIGO_SUBTAREA_INICIO;
			int posicionFinCodigoSubTarea = builder.indexOf(PATH_CODIGO_SUBTAREA_FIN);
			if(posicionFinCodigoSubTarea != -1 && posicionInicioCodigoSubTarea != -1) {
				String codigoSubtarea = builder.substring(posicionInicioCodigoSubTarea, posicionFinCodigoSubTarea);
				int posicionInicioNombreSubtarea = builder.indexOf(PATH_NOMBRE_SUBTAREA_INICIO) + CANTIDAD_CARACTERES_PATH_NOMBRE_SUBTAREA_INICIO;
				int posicionFinNombreSubtarea = builder.indexOf(PATH_NOMBRE_SUBTAREA_FIN);
				if(posicionInicioNombreSubtarea != -1 && posicionFinNombreSubtarea != -1) {
					String nombreSubtarea = builder.substring(posicionInicioNombreSubtarea, posicionFinNombreSubtarea);
					builder.delete(0, posicionFinNombreSubtarea + CANTIDAD_CARACTERES_PATH_NOMBRE_SUBTAREA_FIN);
					if(!"&nbsp;".equalsIgnoreCase(nombreSubtarea)) {
						nombreSubtarea = nombreSubtarea.replace("&nbsp;", " ");
						System.out.println(codigoSubtarea + " " + nombreSubtarea.replace("-&gt;", "->"));
					}
				} else {
					tengoQueLeerTexto = false;
				}
			} else {
				tengoQueLeerTexto = false;
			}
		}
	}
}
