package escuelavirtual.escuelavirtual;

public class Ejercicio {

    private String codigoEjercicio;
    private String imagenBase64;
    private String tema;

    public Ejercicio(String codigoEjercicio, String imagenBase64, String tema){
        this.imagenBase64 = imagenBase64;
        this.codigoEjercicio = codigoEjercicio;
        this.tema = tema;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public String getCodigoEjercicio() {
        return codigoEjercicio;
    }

    public String getTema() {
        return tema;
    }
}