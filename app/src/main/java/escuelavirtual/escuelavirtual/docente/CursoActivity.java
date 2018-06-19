package escuelavirtual.escuelavirtual.docente;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.ModelAdapterCurso;
import escuelavirtual.escuelavirtual.ModelAdapterEjercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.EjercicioPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoActivity extends AppCompatActivity {

    final List<Ejercicio> ejercicios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cargarEjercicios();
    }

    private void cargarEjercicios() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvEjercicios);
        ApiUtils.getAPIService().getEjercicio(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<List<EjercicioPersistible>>() {
                    @Override
                    public void onResponse(Call<List<EjercicioPersistible>> call, Response<List<EjercicioPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<EjercicioPersistible> lista = response.body();
                            for (EjercicioPersistible ejercicio : lista) {
                                ejercicios.add(new Ejercicio(""));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(CursoActivity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(new ModelAdapterEjercicio(ejercicios));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EjercicioPersistible>> call, Throwable t) {
                        Toast.makeText(CursoActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_ejercicio, menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
            {confirm_logout(); return false;}
            case R.id.menu_add_ejercicio_id:
            {addEjercicio(); return false;}
            default:
                return false;
        }
    }

    private void confirm_logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("¿Cerrar Sesión?");
        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        logout();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    public void gotoEjercicio(View button) {
        Intent intent = new Intent(button.getContext(), EjercicioActivity.class);
        startActivity(intent);
    }

    public void addEjercicio() {
        Intent intent = new Intent(this, EjercicioAddActivity.class);
        startActivity(intent);
    }

    public void trytoDeleteEjercicio (View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea eliminar este ejercicio?%n%n" +
                "Los usuarios con respuestas en este ejercicio seran notificados automaticamente de la eliminación del mismo"));
        //TODO: Eliminar el ejercicio de la lista
        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void trytoEditEjercicio (View view){
        //TODO: Agregar actividad EjercicioEdit y pasar el nombre del ejercicio al EditText
/*        Intent intent = new Intent(this, XXX);
        startActivity(intent);*/
    }
}