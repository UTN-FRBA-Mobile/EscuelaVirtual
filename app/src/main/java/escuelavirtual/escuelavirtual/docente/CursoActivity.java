package escuelavirtual.escuelavirtual.docente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.ModelAdapterEjercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.data.EjercicioPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoActivity extends AppCompatActivity {

    final List<Ejercicio> ejercicios = new ArrayList<>();
    private static Curso cursoSeleccionado;

    public static Curso getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public static void setCursoSeleccionado(Curso aCursoSeleccionado) {
        cursoSeleccionado = aCursoSeleccionado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.main_title_id)).setText("Curso: " + cursoSeleccionado.getCodigo() + " - Ejercicios");

        cargarEjercicios();
    }

    private void cargarEjercicios() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvEjercicios);

        ApiUtils.getAPIService().getEjercicios(FirebaseAuth.getInstance().getCurrentUser().getUid(),cursoSeleccionado.getCodigo())
                .enqueue(new Callback<List<EjercicioPersistible>>() {
                    @Override
                    public void onResponse(Call<List<EjercicioPersistible>> call, Response<List<EjercicioPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<EjercicioPersistible> lista = response.body();
                            for (EjercicioPersistible ejercicio : lista) {
                                ejercicios.add(new Ejercicio(ejercicio.getCodigoEjercicio(), ejercicio.getImagenBase64()));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(CursoActivity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(new ModelAdapterEjercicio(ejercicios));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EjercicioPersistible>> call, Throwable t) {
                        Toast.makeText(CursoActivity.this, "Ha ocurrido un error. Intente nuevamente.", Toast.LENGTH_SHORT).show();
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
        EjercicioActivity.setEjercicioSeleccionado(this.findExerciseSelected(button));
        EjercicioActivity.setCursoSeleccionado(cursoSeleccionado);
        Intent intent = new Intent(button.getContext(), EjercicioActivity.class);
        startActivity(intent);
    }

    public void addEjercicio() {
        EjercicioAddActivity.restartForm();
        EjercicioAddActivity.setEjercicioSeleccionado(null);
        EjercicioAddActivity.setDesdePantallaDelEjercicio(false);
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
        EjercicioAddActivity.setEjercicioSeleccionado(this.findExerciseSelected(view));
        Intent intent = new Intent(this, EjercicioAddActivity.class);
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

    private String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}