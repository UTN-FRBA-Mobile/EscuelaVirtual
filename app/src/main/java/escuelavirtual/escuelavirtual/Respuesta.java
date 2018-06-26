package escuelavirtual.escuelavirtual;

public class Respuesta {

    private String codigoCurso;
    private String codigoEjercicio;
    private String codigoRespuesta; //Marca el orden de las respuestas del mismo ej y usuario. Autoincremental desde agregarRespuesta
    private String idAlumno;
    private String nombreAlumno;
    private String imagenRespuestaBase64;
    private String descripcionRespuesta;

    public Respuesta(String codigoCurso, String codigoEjercicio, String codigoRespuesta, String idAlumno, String nombreAlumno, String imagenRespuestaBase64, String descripcionRespuesta) {
        this.codigoCurso = codigoCurso;
        this.codigoEjercicio = codigoEjercicio;
        this.codigoRespuesta = codigoRespuesta;
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
        this.imagenRespuestaBase64 = imagenRespuestaBase64;
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public Respuesta(String codigoCurso, String codigoEjercicio, String codigoRta) {
        this.codigoCurso = codigoCurso;
        this.codigoEjercicio = codigoEjercicio;
        this.codigoRespuesta = codigoRta;
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

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public String getIdAlumno(){
        return idAlumno;
    }
}