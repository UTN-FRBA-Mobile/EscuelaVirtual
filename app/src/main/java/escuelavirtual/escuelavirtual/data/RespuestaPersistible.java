package escuelavirtual.escuelavirtual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaPersistible {
    @SerializedName("codCurso")
    @Expose
    private String codigoCurso;
    @SerializedName("codEjercicio")
    @Expose
    private String codigoEjercicio;
    @SerializedName("codAlumno")
    @Expose
    private String codigoAlumno;
    @SerializedName("nombreAlumno")
    @Expose
    private String nombreAlumno;
    @SerializedName("imagenBase64")
    @Expose
    private String imagenBase64;
    @SerializedName("docente")
    @Expose
    private String docente;

    public RespuestaPersistible(String codigoCurso, String codigoEjercicio, String codigoAlumno, String nombreAlumno, String imagenBase64, String docente) {
        this.codigoCurso = codigoCurso;
        this.codigoEjercicio = codigoEjercicio;
        this.codigoAlumno = codigoAlumno;
        this.nombreAlumno = nombreAlumno;
        this.imagenBase64 = imagenBase64;
        this.docente = docente;
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

    public String getCodigoAlumno() {
        return codigoAlumno;
    }

    public void setCodigoAlumno(String codigoAlumno) {
        this.codigoAlumno = codigoAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }
}
