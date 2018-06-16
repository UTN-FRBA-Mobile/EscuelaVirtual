package escuelavirtual.escuelavirtual;

public class Curso {

    private String name;
    private String descripcion;

    public Curso(String name, String descripcion) {
        this.name = name;
        this.descripcion = descripcion;
    }

    public String getName() { return name; }

    public String getDescripcion() { return descripcion; }
}