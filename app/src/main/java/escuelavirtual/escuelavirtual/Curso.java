package escuelavirtual.escuelavirtual;

import java.util.List;

public class Curso {

    private String name;
    private List<Ejercicio> ejecicioList;

    public Curso(String name, List<Ejercicio> ejecicioList) {
        this.name = name;
        this.ejecicioList = ejecicioList;
    }

    public String getName() { return name; }

    public List<Ejercicio> getEjercicioList() { return ejecicioList; }
}