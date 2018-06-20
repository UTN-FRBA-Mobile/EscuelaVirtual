package escuelavirtual.escuelavirtual;

public class Ejercicio {

    private String codigoCurso;
    private String imagenBase64;

    public Ejercicio(String codigoCurso, String imagenBase64){
        this.imagenBase64 = imagenBase64;
        this.codigoCurso = codigoCurso;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }
}