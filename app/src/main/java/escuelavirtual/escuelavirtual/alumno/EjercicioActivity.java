package escuelavirtual.escuelavirtual.alumno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.ModelAdapterRespuesta;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.Respuesta;
import escuelavirtual.escuelavirtual.data.RespuestaPersistible;

public class EjercicioActivity extends escuelavirtual.escuelavirtual.docente.EjercicioActivity {

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static void agregarRespuesta(Respuesta respueta) {
        escuelavirtual.escuelavirtual.docente.EjercicioActivity.respuestas.add(respueta);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.add_respuesta,menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menu_add_respuesta_id:
                {crearRespuesta(this);}
            default:
                return false;
        }
    }

    private void crearRespuesta(EjercicioActivity ejercicioActivity) {
        String codigoRta = getNuevoCodigoRespuesta(respuestas);
        //Autogenero codigo de respuesta para la nueva Respuesta
        RespuestaAddActivity.setRespuesta(new Respuesta(cursoSeleccionado.getCodigo(),ejercicioSeleccionado.getCodigoEjercicio(),codigoRta));
        Intent intent = new Intent(this, RespuestaAddActivity.class);
        startActivity(intent);
    }

    public static void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        EjercicioActivity.ejercicioSeleccionado = ejercicioSeleccionado;
    }

    public static void setCursoSeleccionado(Curso cursoSeleccionado) {
        EjercicioActivity.cursoSeleccionado = cursoSeleccionado;
    }

    private String getNuevoCodigoRespuesta(List<Respuesta> respuestas) {
        //En caso de lista vacía, devuelve 1 como id inicial
        if(respuestas.size() == 0) return "1";

        Integer maxValue = 0;
        for(int i = 0; i < respuestas.size(); i++) {
            if(Integer.parseInt(respuestas.get(i).getCodigoRespuesta()) > maxValue) {
                maxValue = Integer.parseInt(respuestas.get(i).getCodigoRespuesta());
            }
        }

        maxValue += 1;

        return String.valueOf(maxValue);
    }

    @Override
    protected void setAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(new ModelAdapterRespuesta(false,respuestas));
    }

    @Override
    protected void persistiblesToList(List<RespuestaPersistible> lista) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (RespuestaPersistible respuesta : lista) {
            if(respuesta.getCodigoAlumno() == uid) {
                respuestas.add(new Respuesta(respuesta.getCodigoCurso(), respuesta.getCodigoEjercicio(), respuesta.getCodigoRespuesta(), respuesta.getCodigoAlumno(), respuesta.getNombreAlumno(), respuesta.getImagenBase64(), respuesta.getDescripcion()));
            }
        }
    }

    @Override
    protected Respuesta findRespuestaSelected(View view) {
        for(int i = 0; i < this.respuestas.size(); i++){
            //Devuelve la respuesta con el mismo codigo de respuesta
            if(this.respuestas.get(i).getCodigoRespuesta().equals(view.getTag()))
            {
                return this.respuestas.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        CursoActivity.setCursoSeleccionado(getCursoSeleccionado());
        startActivity(new Intent(this, CursoActivity.class));
        return true;
    }
}
