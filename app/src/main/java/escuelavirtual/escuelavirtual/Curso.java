package escuelavirtual.escuelavirtual;

import java.util.List;

public class Curso {

    private String codigo;
    private String descripcion;
    private String docente;
    private List<Ejercicio> ejercicios;

    public Curso(String name, String descripcion) {
        this.codigo = name;
        this.descripcion = descripcion;
    }

    public Curso(String name, String descripcion, String docente, List<Ejercicio> ejercicios) {
        this.codigo = name;
        this.descripcion = descripcion;
        this.docente = docente;
        this.ejercicios = ejercicios;
    }

    public String getCodigo() { return codigo; }

    public String getDescripcion() { return descripcion; }

    public String getDocente() {
        return docente;
    }
}