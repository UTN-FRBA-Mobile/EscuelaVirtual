package escuelavirtual.escuelavirtual.alumno;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.R;

import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class EjercicioActivity extends escuelavirtual.escuelavirtual.docente.EjercicioActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setCursoSeleccionado(new Curso("name","description"));
        this.setEjercicioSeleccionado(new Ejercicio("e1","123"));
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
                {agregarRespuesta(this);}
            default:
                return false;
        }
    }

    private void agregarRespuesta(EjercicioActivity ejercicioActivity) {
        Intent intent = new Intent(this, RespuestaAddActivity.class);
        startActivity(intent);
    }

}
