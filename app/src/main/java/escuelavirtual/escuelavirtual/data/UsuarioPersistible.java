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
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("token")
    @Expose
    private String token;

    public UsuarioPersistible(String nombre, int perfil, String uid, String token) {
        this.nombre = nombre;
        this.perfil = perfil;
        this.uid = uid;
        this.token = token;
    }


    public String getNombre() {
        return nombre;
    }

    public int getPerfil() {
        return perfil;
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }
}