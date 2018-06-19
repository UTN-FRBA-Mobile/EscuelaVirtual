package escuelavirtual.escuelavirtual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import escuelavirtual.escuelavirtual.Ejercicio;

public class EjercicioPersistible {
    @SerializedName("curso")
    @Expose
    private String curso;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("ejecicioList")
    @Expose
    private List<Ejercicio> ejecicioList;
    @SerializedName("docente")
    @Expose
    private String docente;

    public EjercicioPersistible(String codigo, String descripcion, List<Ejercicio> ejecicioList, String docente) {
        this.curso = codigo;
        this.ejecicioList = ejecicioList;
        this.descripcion = descripcion;
        this.docente = docente;
    }


    public String getCurso() {
        return curso;
    }

    public List<Ejercicio> getEjecicioList() {
        return ejecicioList;
    }

    public String getDocente() {
        return docente;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
