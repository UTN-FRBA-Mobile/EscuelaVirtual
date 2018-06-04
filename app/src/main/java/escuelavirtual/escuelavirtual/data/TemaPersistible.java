package escuelavirtual.escuelavirtual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TemaPersistible {
    @SerializedName("tema")
    @Expose
    private String tema;
    @SerializedName("docente")
    @Expose
    private String docente;


    public TemaPersistible(String tema, String docente) {
        this.tema = tema;
        this.docente = docente;
    }

    public String getDocente() {
        return docente;
    }

    public String getTema() {
        return tema;
    }
}
