package escuelavirtual.escuelavirtual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import escuelavirtual.escuelavirtual.Ejercicio;

public class EjercicioPersistible {
    @SerializedName("codCurso")
    @Expose
    private String curso;
    @SerializedName("codEjercicio")
    @Expose
    private String codigoEjercicio;
    @SerializedName("imagenBase64")
    @Expose
    private String imagenBase64;
    @SerializedName("docente")
    @Expose
    private String docente;

    public EjercicioPersistible(String curso, String codigoEjercicio, String imagenBase64, String docente) {
        this.curso = curso;
        this.codigoEjercicio = codigoEjercicio;
        this.imagenBase64 = imagenBase64;
        this.docente = docente;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCodigoEjercicio() {
        return codigoEjercicio;
    }

    public void setCodigoEjercicio(String codigoEjercicio) {
        this.codigoEjercicio = codigoEjercicio;
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
