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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.ModelAdapterCurso;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class MainActivity extends AppCompatActivity {

    final List<Curso> cursos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar();

        ((TextView)findViewById(R.id.main_title_id)).setText("Mis Cursos");

        cargarCursos();
    }

    private void cargarCursos() {
        ApiUtils.getAPIService().getCurso(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<List<CursoPersistible>>() {
                    @Override
                    public void onResponse(Call<List<CursoPersistible>> call, Response<List<CursoPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<CursoPersistible> lista = response.body();
                            for (CursoPersistible cursoP : lista) {
                                cursos.add(new Curso(cursoP.getCurso(),cursoP.getDescripcion(), cursoP.getDocente(), cursoP.getEjecicioList()));
                            }

                            updateCursos();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_temas).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                {confirm_logout(this); return false;}
            case R.id.menu_add_curso_id:
                {gotoAddCurso(); return false;}
            case R.id.action_temas:
                {gotoTemas(); return false;}
            default:
                return false;
        }
    }

    public void gotoCurso(View view){
        Intent intent = new Intent(this, CursoActivity.class);
        CursoActivity.setCursoSeleccionado(this.findCourseSelected(view));
        startActivity(intent);
    }

    public void gotoAddCurso(){
        Intent intent = new Intent(this, CursoAddActivity.class);
        startActivity(intent);
    }

    public void trytoDeleteCurso (View deleteButton){
        final Curso courseToDelete = this.findCourseSelected(deleteButton);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea cerrar este curso?%n%n" +
                "Los subscriptos a este canal seran notificados automaticamente del cierre del mismo"));

        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        eliminarCurso(new CursoPersistible(courseToDelete.getCodigo(),courseToDelete.getDescripcion(),null,FirebaseAuth.getInstance().getCurrentUser().getUid()));
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

    private Curso findCourseSelected(View view){
        for(int i = 0; i < this.cursos.size(); i++){
            if(this.cursos.get(i).getCodigo().equals(view.getTag())){
                return this.cursos.get(i);
            }
            try{
                if(((TextView)view).getText().toString().contains(cursos.get(i).getCodigo())){
                    return this.cursos.get(i);
                }
            }catch (Exception e){
                
            }
        }
        return null;
    }

    private void eliminarCurso(final CursoPersistible cursoPersistible) {
        ApiUtils.getAPIService().deleteCurso(cursoPersistible)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "El curso ha sido eliminado.",Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            //startActivity(intent);
                            int posicion = ubicarCursoEliminado(cursoPersistible);
                            cursos.remove(posicion);
                            updateCursos();
                        }
                    }


                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCursos() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvCursos);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterCurso(cursos, MainActivity.this));
    }

    private int ubicarCursoEliminado(CursoPersistible cursoPersistible) {
        for (Curso curso : cursos){
            if(curso.getCodigo() == cursoPersistible.getCurso()) return cursos.indexOf(curso);
        }

        return -1;
    }

    public void trytoEditCurso (View button){
        CursoEditActivity.setCourse(this.findCourseSelected(button));
        Intent intent = new Intent(this, CursoEditActivity.class);
        startActivity(intent);
    }

    private void gotoTemas() {
        Intent intent = new Intent(this, TemasActivity.class);
        startActivity(intent);
    }
}
