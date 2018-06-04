package escuelavirtual.escuelavirtual;

import java.util.List;

public class Curso {

    private String name;
    private String descripcion;
    private List<Ejercicio> ejecicioList;

    public Curso(String name, String descripcion, List<Ejercicio> ejecicioList) {
        this.name = name;
        this.descripcion = descripcion;
        this.ejecicioList = ejecicioList;
    }

    public String getName() { return name; }

    public String getDescripcion() { return descripcion; }

    public List<Ejercicio> getEjercicioList() { return ejecicioList; }
}