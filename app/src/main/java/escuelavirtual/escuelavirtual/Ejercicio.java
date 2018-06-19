package escuelavirtual.escuelavirtual;

public class Ejercicio {

    private String codigoCurso;
    private String name;

    public Ejercicio(String name) {
        this.name = name;
    }

    public Ejercicio(String codigoCurso, String name){
        this.name = name;
        this.codigoCurso = codigoCurso;
    }

    public String getName() {
        return name;
    }
}