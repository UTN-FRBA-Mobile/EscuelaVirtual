package escuelavirtual.escuelavirtual;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.TemaPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class TemasActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModelAdapterTema mAdapter;
    View mEditTema;
    ImageButton mSaveTema;

    EditText mEditTemaText;
    List<String> temas;

    String selectedTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        selectedTema = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        temas = getTemas();

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
    private List<String> getTemas() {
        return getTemasFromService(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        Toast.makeText(this, "Tema actualizado: " + nuevo, Toast.LENGTH_SHORT).show();
        updateListaTemas();
    }

    /**
     * Llama a servicio para eliminar tema y actualiza lista
     * @param tema tema a eliminar
     */
    private void delete_tema(String tema){
        persistirDeleteTema(FirebaseAuth.getInstance().getCurrentUser().getUid(),tema);
    }

    /**
     * Actualiza lista de temas. Generica para todas las acciones que involucren servicios
     */
    private void updateListaTemas() {
        temas = getTemas();
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
                            updateListaTemas();
                            //MOCK
                            //temas.remove(tema);

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void persistirUpdateTema(String uid, String viejo, String nuevo) {
        //TODO: Persistir Actualizacion de tema

        //MOCK
        Integer temaEditado = temas.indexOf(viejo);
        temas.set(temaEditado, nuevo);
    }

    private void persistirAddTema(String uid,final String tema) {

        ApiUtils.getAPIService().guardarTemas(tema,uid)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            //Toast.makeText(TemasActivity.this, "Se ha guardado el tema exitosamente.",Toast.LENGTH_SHORT).show();
                            Toast.makeText(TemasActivity.this, "Tema agregado: " + tema, Toast.LENGTH_SHORT).show();
                            updateListaTemas();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private List<String> getTemasFromService(String uid) {
        ApiUtils.getAPIService().getTema(uid)
                .enqueue(new Callback<List<TemaPersistible>>() {
                    @Override
                    public void onResponse(Call<List<TemaPersistible>> call, Response<List<TemaPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<TemaPersistible> lista = response.body();
                            for (TemaPersistible tema : lista) {

                                temas.add(tema.getTema());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<TemaPersistible>> call, Throwable t) {
                        Toast.makeText(TemasActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();

                    }
                });
        return temas;
    }







}
