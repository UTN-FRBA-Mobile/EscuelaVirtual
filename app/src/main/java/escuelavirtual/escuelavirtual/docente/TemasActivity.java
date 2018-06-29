package escuelavirtual.escuelavirtual.docente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.ModelAdapterTema;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.alumno.CursoActivity;
import escuelavirtual.escuelavirtual.data.TemaPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class TemasActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModelAdapterTema mAdapter;
    View mEditTema;
    ImageButton mSaveTema;

    EditText mEditTemaText;
    List<String> temas;
    private static List<String> temasDisponibles;
    private static Boolean temasObtenidos = false;

    String selectedTema;

    public static List<String> getTemasDisponibles() {
        return temasDisponibles;
    }

    public static Boolean getTemasObtenidos() {
        return temasObtenidos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        temas = new ArrayList<String>();
        selectedTema = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.main_title_id)).setText("Mis temas");

        mEditTema = findViewById(R.id.edit_tema_id);
        mEditTemaText = (EditText) findViewById(R.id.edit_tema_text);
        mSaveTema = (ImageButton) findViewById(R.id.ibtn_save_tema_id);
        mSaveTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temaActualizado = mEditTemaText.getText().toString();
                if (selectedTema.isEmpty()){
                    add_tema(temaActualizado );
                }else{
                    update_tema(selectedTema,temaActualizado);
                }
            }
        });

        findViewById(R.id.ibtn_close_tema_id).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeTema();
                }
        });

        getTemas();

        recyclerView = (RecyclerView) findViewById(R.id.rvTemas);
        recyclerView.setLayoutManager(new LinearLayoutManager(TemasActivity.this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new ModelAdapterTema(temas,new ModelAdapterTema.ClickListener() {
            @Override public void onPositionClicked(View v,int position) {
                if(v.getId() == R.id.ibtn_edit_tema_id){
                    openTema(temas.get(position));
                }
                if(v.getId() == R.id.ibtn_delete_tema_id) {
                    delete_tema(temas.get(position));
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
        getMenuInflater().inflate(R.menu.add_tema, menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
            {confirm_logout(this); return false;}
            case R.id.menu_add_tema_id:
            {openTema(""); return false;}
            default:
                return false;
        }
    }


    /**
     * Pasamanos, obtiene temas desde el servicio
     * @return Lista de temas (Strings)
     */
    private void getTemas() {
        getTemasFromService(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    /**
     * Llama a servicio agregar tema y actualiza lista
     * @param tema nuevo tema
     */
    private void add_tema(String tema) {
        persistirAddTema(FirebaseAuth.getInstance().getCurrentUser().getUid(),tema);
    }

    /**
     * Llama a servicio modificar tema y actualiza lista
     * @param viejo nombre del viejo tema
     * @param nuevo nombre del nuevo tema
     */
    private void update_tema(String viejo, String nuevo){
        persistirUpdateTema(FirebaseAuth.getInstance().getCurrentUser().getUid(),viejo,nuevo);
    }

    /**
     * Llama a servicio para eliminar tema y actualiza lista
     * @param tema tema a eliminar
     */
    private void delete_tema(final String tema){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea eliminar este tema?%n%n" +
                        "Los alumnos ya no podrán filtrar por este tema"));
        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        persistirDeleteTema(FirebaseAuth.getInstance().getCurrentUser().getUid(),tema);
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

    /**
     * Actualiza lista de temas. Generica para todas las acciones que involucren servicios
     */
    private void updateListaTemas() {
        mAdapter.notifyDataSetChanged();
        closeTema();
    }

    /**
     * FUNCIONES PARA MANEJAR EDITOR DE TEMA
     */

    /**
     * Muestra Editor de tema
     * @param t Si es vacío se trata de un new, si contiene valor se trata de un update
     */
    private void openTema(String t) {
        selectedTema = t;
        mEditTemaText.setText(selectedTema);
        mEditTema.setVisibility(VISIBLE);
        mEditTemaText.requestFocus();
    }

    /**
     * Oculta el editor de tema
     */
    public void closeTema() {
        mEditTema.setVisibility(GONE);
        mEditTemaText.setText("");
    }


    /**
     * FUNCIONES DE PERSISTENCIA (SERVICIO)
     */

    private void persistirDeleteTema(String uid,final String tema) {

        ApiUtils.getAPIService().deleteTema(new TemaPersistible(tema,uid))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            //Toast.makeText(TemasActivity.this, "El tema ha sido eliminado.",Toast.LENGTH_SHORT).show();
                            Toast.makeText(TemasActivity.this, "Tema eliminado: " + tema, Toast.LENGTH_SHORT).show();
                            temas.remove(tema);
                            updateListaTemas();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void persistirUpdateTema(String uid,final String viejo,final String nuevo) {
        ApiUtils.getAPIService().updateTemas(viejo,nuevo,uid)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(TemasActivity.this, "Tema actualizado: " + nuevo, Toast.LENGTH_SHORT).show();
                            Integer temaEditado = temas.indexOf(viejo);
                            temas.set(temaEditado, nuevo);
                            updateListaTemas();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void persistirAddTema(String uid,final String tema) {
        ApiUtils.getAPIService().guardarTemas(tema,uid)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            //Toast.makeText(TemasActivity.this, "Se ha guardado el tema exitosamente.",Toast.LENGTH_SHORT).show();
                            Toast.makeText(TemasActivity.this, "Tema agregado: " + tema, Toast.LENGTH_SHORT).show();
                            temas.add(tema);
                            updateListaTemas();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static void getTemasAvailable(final CursoActivity cursoActivity, final EjercicioAddActivity ejercicioAddActivity){
        temasDisponibles = new ArrayList<>();
        temasObtenidos = false;
        String docente;
        if(cursoActivity != null /*es alumno*/){
            docente = CursoActivity.getCursoSeleccionado().getDocente();
        }else{
            docente = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        ApiUtils.getAPIService().getTema(docente)
                .enqueue(new Callback<List<TemaPersistible>>() {
                    @Override
                    public void onResponse(Call<List<TemaPersistible>> call, Response<List<TemaPersistible>> response) {
                        if(response.isSuccessful()) {
                            temasObtenidos = true;
                            List<TemaPersistible> lista = response.body();
                            for (TemaPersistible tema : lista) {
                                temasDisponibles.add(tema.getTema());
                            }
                            if(lista.size() == 0){
                                if(cursoActivity != null){
                                    cursoActivity.disableTema();
                                }else{
                                    ejercicioAddActivity.disableTema();
                                }
                            }else{
                                if(cursoActivity != null){
                                    cursoActivity.enableTema();
                                }else{
                                    ejercicioAddActivity.enableTema();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TemaPersistible>> call, Throwable t) {
                        Toast.makeText(cursoActivity != null ? cursoActivity : ejercicioAddActivity, "Ha ocurrido un error buscando los temas. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                        temasObtenidos = false;
                    }
                });
    }

    private void getTemasFromService(String uid) {
        ApiUtils.getAPIService().getTema(uid)
                .enqueue(new Callback<List<TemaPersistible>>() {
                    @Override
                    public void onResponse(Call<List<TemaPersistible>> call, Response<List<TemaPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<TemaPersistible> lista = response.body();
                            for (TemaPersistible tema : lista) {
                                temas.add(tema.getTema());
                            }

                            updateListaTemas();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TemaPersistible>> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();

                    }
                });
    }







}
