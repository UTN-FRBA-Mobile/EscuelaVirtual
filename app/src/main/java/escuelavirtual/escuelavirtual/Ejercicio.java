package escuelavirtual.escuelavirtual;

public class Ejercicio {

    private String codigoEjercicio;
    private String imagenBase64;

    public Ejercicio(String codigoCurso, String imagenBase64){
        this.imagenBase64 = imagenBase64;
        this.codigoEjercicio = codigoCurso;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public String getCodigoEjercicio() {
        return codigoEjercicio;
    }
}