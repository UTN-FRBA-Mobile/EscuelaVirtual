package escuelavirtual.escuelavirtual;

import java.util.List;

public class Curso {

    private int textId;
    private List<Ejercicio> ejecicioList;

    public Curso(int textId, List<Ejercicio> ejecicioList) {
        this.textId = textId;
        this.ejecicioList = ejecicioList;
    }

    public int getTextId() {
        return textId;
    }

    public List<Ejercicio> getEjercicioList() {
        return ejecicioList;
    }
}