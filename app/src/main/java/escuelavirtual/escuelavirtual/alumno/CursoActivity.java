package escuelavirtual.escuelavirtual.alumno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.ModelAdapterEjercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.EjercicioPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoActivity extends AppCompatActivity {

    static final List<Ejercicio> ejercicios = new ArrayList<>();
    private static Curso cursoSeleccionado;

    public static Curso getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public static void setCursoSeleccionado(Curso c) {
        cursoSeleccionado = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_curso_a);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, EjercicioActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Cargando ejercicios...");
        progress.setTitle("Por favor espere...");
        Loading.ejecutar(progress);
        cargarEjercicios(progress);
    }

    private void cargarEjercicios(final ProgressDialog progress) {
        ApiUtils.getAPIService().getEjercicios(FirebaseAuth.getInstance().getCurrentUser().getUid(),cursoSeleccionado.getCodigo())
                .enqueue(new Callback<List<EjercicioPersistible>>() {
                    @Override
                    public void onResponse(Call<List<EjercicioPersistible>> call, Response<List<EjercicioPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<EjercicioPersistible> lista = response.body();
                            for (EjercicioPersistible ejercicio : lista) {
                                ejercicios.add(new Ejercicio(ejercicio.getCodigoEjercicio(), ejercicio.getImagenBase64()));
                            }
                            updateEjercicios();
                        }
                        Loading.terminar(progress);
                    }

                    @Override
                    public void onFailure(Call<List<EjercicioPersistible>> call, Throwable t) {
                        Toast.makeText(escuelavirtual.escuelavirtual.alumno.CursoActivity.this, "Ha ocurrido un error. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                        Loading.terminar(progress);
                    }
                });
    }

    private void updateEjercicios() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvEjercicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterEjercicio(ejercicios));
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
        EjercicioActivity.setEjercicioSeleccionado(this.findExerciseSelected(button));
        EjercicioActivity.setCursoSeleccionado(cursoSeleccionado);
        Intent intent = new Intent(button.getContext(), EjercicioActivity.class);
        startActivity(intent);
    }

    private Ejercicio findExerciseSelected(View view){
        for(int i = 0; i < this.ejercicios.size(); i++){
            if(this.ejercicios.get(i).getCodigoEjercicio().equals(view.getTag())){
                return this.ejercicios.get(i);
            }
            try{
                if(((TextView)view).getText().toString().contains(ejercicios.get(i).getCodigoEjercicio())){
                    return this.ejercicios.get(i);
                }
            }catch (Exception e){

            }
        }
        return null;
    }
}
