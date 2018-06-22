package escuelavirtual.escuelavirtual;

public class Respuesta {

    private String codigoCurso;
    private String codigoEjercicio;
    private String codigoRespuesta;
    private String nombreAlumno;
    private String imagenRespuestaBase64;

    public Respuesta(String codigoCurso, String codigoEjercicio, String codigoRespuesta, String nombreAlumno, String imagenRespuestaBase64) {
        this.codigoCurso = codigoCurso;
        this.codigoEjercicio = codigoEjercicio;
        this.codigoRespuesta = codigoRespuesta;
        this.nombreAlumno = nombreAlumno;
        this.imagenRespuestaBase64 = imagenRespuestaBase64;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getCodigoEjercicio() {
        return codigoEjercicio;
    }

    public void setCodigoEjercicio(String codigoEjercicio) {
        this.codigoEjercicio = codigoEjercicio;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getImagenRespuestaBase64() {
        return imagenRespuestaBase64;
    }

    public void setImagenRespuestaBase64(String imagenRespuestaBase64) {
        this.imagenRespuestaBase64 = imagenRespuestaBase64;
    }
}