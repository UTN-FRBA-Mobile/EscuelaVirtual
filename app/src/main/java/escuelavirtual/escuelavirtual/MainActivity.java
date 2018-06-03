package escuelavirtual.escuelavirtual;

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

import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    final List<Curso> cursos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar();


        //TODO: Carga de items - Reemplazar con datos persistidos
        cargarCursos();

    }

    private void cargarCursos() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvCursos);
        ApiUtils.getAPIService().getCurso(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<List<CursoPersistible>>() {
                    @Override
                    public void onResponse(Call<List<CursoPersistible>> call, Response<List<CursoPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<CursoPersistible> lista = response.body();
                            for (CursoPersistible cursoP : lista) {

                                //TODO ejercicios habria que reemplazarlo por cursoP.getEjercicios()
                                final List<Ejercicio> ejercicios = new ArrayList<>();
                                ejercicios.add(new Ejercicio("Ejercicio 1"));
                                cursos.add(new Curso(cursoP.getCurso(),cursoP.getDescripcion(), ejercicios));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(new ModelAdapterCurso(cursos));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CursoPersistible>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_curso, menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                {confirm_logout(); return false;}
            case R.id.menu_add_curso_id:
                {gotoAddCurso(); return false;}
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

    public void gotoCurso(View view){
        //TODO: Pasar el nombre del ejercicio para el encabezado en el menu
        Intent intent = new Intent(this, CursoActivity.class);
        startActivity(intent);
    }

    public void gotoAddCurso(){
        Intent intent = new Intent(this, CursoAddActivity.class);
        startActivity(intent);
    }

    public void trytoDeleteCurso (View deleteButton){
        final Curso courseToDelete = this.findCourseToDelete(deleteButton);
        // TODO: borrar este Toast
        Toast.makeText(MainActivity.this, "Curso: "+courseToDelete.getName()+" descripción: "+courseToDelete.getDescripcion(),Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea cerrar este curso?%n%n" +
                "Los subscriptos a este canal seran notificados automaticamente del cierre del mismo"));

        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        eliminarCurso(new CursoPersistible("ERIC","ERIC2",null,FirebaseAuth.getInstance().getCurrentUser().getUid()));
                        //TODO Refrescar pantalla para que desaparezca el curso eliminado
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

    private Curso findCourseToDelete(View view){
        for(int i = 0; i < this.cursos.size(); i++){
            if(this.cursos.get(i).getName() == view.getTag()){
                return this.cursos.get(i);
            }
        }
        return null;
    }

    private void eliminarCurso(CursoPersistible cursoPersistible) {
        ApiUtils.getAPIService().deleteCurso(cursoPersistible)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "El curso ha sido eliminado.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void trytoEditCurso (View view){
        //TODO: Pasar el nombre del curso al EditText (y codigo)

        Intent intent = new Intent(this, CursoEditActivity.class);
        startActivity(intent);
    }
}
