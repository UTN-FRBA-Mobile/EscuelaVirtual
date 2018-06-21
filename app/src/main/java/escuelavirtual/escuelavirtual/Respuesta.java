package escuelavirtual.escuelavirtual;

public class Respuesta {

    private String codigoCurso;
    private String codigoEjercicio;
    private String codigoAlumno;
    private String nombreAlumno;

    public Respuesta(String codigoCurso, String codigoEjercicio, String codigoAlumno, String nombreAlumno) {
        this.codigoCurso = codigoCurso;
        this.codigoEjercicio = codigoEjercicio;
        this.codigoAlumno = codigoAlumno;
        this.nombreAlumno = nombreAlumno;
    }

    public String getCodigoAlumno() {
        return codigoAlumno;
    }
}