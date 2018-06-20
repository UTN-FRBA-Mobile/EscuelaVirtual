package escuelavirtual.escuelavirtual;

public class Ejercicio {

    private String codigoCurso;
    private String imagenBase64;

    public Ejercicio(String name) {
        this.imagenBase64 = name;
    }

    public Ejercicio(String codigoCurso, String imagenBase64){
        this.imagenBase64 = imagenBase64;
        this.codigoCurso = codigoCurso;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }
}