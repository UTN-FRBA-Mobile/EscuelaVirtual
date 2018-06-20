package escuelavirtual.escuelavirtual.docente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.ModelAdapterRespuesta;
import escuelavirtual.escuelavirtual.R;

import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class EjercicioActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModelAdapterRespuesta mAdapter;
    List<String> respuestas;
    private static Ejercicio ejercicioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getRespuestas();

        recyclerView = (RecyclerView) findViewById(R.id.rvRespuestas);
        recyclerView.setLayoutManager(new LinearLayoutManager(EjercicioActivity.this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new ModelAdapterRespuesta(respuestas,new ModelAdapterRespuesta.ClickListener() {
            @Override public void onPositionClicked(View v, int position) {
                if(v.getId() == R.id.tv_respuesta_id) {
                    openRta(position);
                }
            }

            @Override public void onLongClicked(View v, int position) {
                return;
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_respuesta, menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
            {confirm_logout(this); return false;}
            case R.id.menu_editar_respuesta_id:
            {
                //editarEjercicio();
                Toast.makeText(EjercicioActivity.this, "Edit",Toast.LENGTH_SHORT).show();
            }
            default:
                return false;
        }
    }

    private void openRta(int position) {
        Intent intent = new Intent(this, CommentsOnPhotoActivity.class);
        //TODO: Obtener el id de la respuesta en esta POSITION
        //TODO: Pasar el id al CommentsOnPhotoActivity
        startActivity(intent);
    }

    private void getRespuestas() {
        respuestas = new ArrayList<String>();
        respuestas.add("rta1");
        respuestas.add("rta2");
        respuestas.add("rta3");
        respuestas.add("rta4");
        respuestas.add("rta5");



    }
}
