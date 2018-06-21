package escuelavirtual.escuelavirtual;

public class Ejercicio {

    private String codigoEjercicio;
    private String imagenBase64;

    public Ejercicio(String codigoEjercicio, String imagenBase64){
        this.imagenBase64 = imagenBase64;
        this.codigoEjercicio = codigoEjercicio;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public String getCodigoEjercicio() {
        return codigoEjercicio;
    }
}