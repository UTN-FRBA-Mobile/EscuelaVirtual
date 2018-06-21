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
    @SerializedName("docente")
    @Expose
    private String docente;

    public RespuestaPersistible(String codigoCurso, String codigoEjercicio, String codigoAlumno, String nombreAlumno, String docente) {
        this.codigoCurso = codigoCurso;
        this.codigoEjercicio = codigoEjercicio;
        this.codigoAlumno = codigoAlumno;
        this.nombreAlumno = nombreAlumno;
        this.docente = docente;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public String getCodigoEjercicio() {
        return codigoEjercicio;
    }

    public String getCodigoAlumno() {
        return codigoAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public String getDocente() {
        return docente;
    }
}
