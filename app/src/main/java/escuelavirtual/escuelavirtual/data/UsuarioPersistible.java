package escuelavirtual.escuelavirtual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsuarioPersistible {
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("perfil")
    @Expose
    private int perfil;


    public UsuarioPersistible(String nombre, int perfil) {
        this.nombre = nombre;
        this.perfil = perfil;
    }


    public String getNombre() {
        return nombre;
    }

    public int getPerfil() {
        return perfil;
    }
}