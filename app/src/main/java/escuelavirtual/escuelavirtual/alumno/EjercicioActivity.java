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
import escuelavirtual.escuelavirtual.docente.CommentsOnPhotoActivity_a;

import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class EjercicioActivity extends escuelavirtual.escuelavirtual.docente.EjercicioActivity {

    @Override
    protected void onStart() {
        super.onStart();
        refreshRespuestas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRespuestas();
    }

    public static void agregarRespuesta(Respuesta respueta) {
        respuestas.add(respueta);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshRespuestas();
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
                {crearRespuesta(this); return false;}
            case R.id.action_logout:
            {confirm_logout(this); return false;}
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
        respuestas.removeAll(respuestas);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (RespuestaPersistible respuesta : lista) {
            if(respuesta.getCodigoAlumno().equals(uid) && respuesta.getCodigoEjercicio().equals(ejercicioSeleccionado.getCodigoEjercicio())) {
                respuestas.add(new Respuesta(respuesta.getCodigoCurso(), respuesta.getCodigoEjercicio(), respuesta.getCodigoRespuesta(), respuesta.getCodigoAlumno(), respuesta.getNombreAlumno(), respuesta.getImagenBase64(), respuesta.getDescripcion()));
            }
        }
        refreshRespuestas();
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

    @Override
    public void goToRespuesta(View button) {
        CommentsOnPhotoActivity_a.setRespuestaSeleccionada(this.findRespuestaSelected(button));
        CommentsOnPhotoActivity_a.setEjercicioSeleccionado(getEjercicioSeleccionado());
        CommentsOnPhotoActivity_a.setCursoSeleccionado(getCursoSeleccionado());
        Intent intent = new Intent(this, CommentsOnPhotoActivity_a.class);
        startActivity(intent);
    }
}
