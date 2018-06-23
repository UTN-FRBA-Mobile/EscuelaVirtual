package escuelavirtual.escuelavirtual.docente;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.ModelAdapterRespuesta;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.Respuesta;
import escuelavirtual.escuelavirtual.data.RespuestaPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static escuelavirtual.escuelavirtual.common.FirebaseCommon.confirm_logout;

public class EjercicioActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModelAdapterRespuesta mAdapter;
    List<Respuesta> respuestas = new ArrayList<>();
    private static Curso cursoSeleccionado;
    private static Ejercicio ejercicioSeleccionado;

    public static void setCursoSeleccionado(Curso cursoSeleccionado) {
        EjercicioActivity.cursoSeleccionado = cursoSeleccionado;
    }

    public static void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        EjercicioActivity.ejercicioSeleccionado = ejercicioSeleccionado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.main_title_id)).setText("Ejercicio: " + ejercicioSeleccionado.getCodigoEjercicio());
        ((ImageView)findViewById(R.id.image_id)).setImageBitmap(base64ToBitMap(ejercicioSeleccionado.getImagenBase64()));

        cargarRespuestas();
    }

    private void cargarRespuestas(){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvRespuestas);

        // TODO: este código es para probar mientras no se tenga la persistencia
        ImageView imagenRespuesta = new ImageView(this);
        imagenRespuesta.setImageResource(R.drawable.ejercicio_ejemplo);
        Bitmap bitmap = ((BitmapDrawable) imagenRespuesta.getDrawable()).getBitmap();
        respuestas.add(new Respuesta(cursoSeleccionado.getCodigo(), ejercicioSeleccionado.getCodigoEjercicio(),"1", "Deadpool", bitmapToBase64(bitmap)));
        respuestas.add(new Respuesta(cursoSeleccionado.getCodigo(), ejercicioSeleccionado.getCodigoEjercicio(),"2", "Batman", bitmapToBase64(bitmap)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterRespuesta(respuestas));

        // TODO: este código comentado va a funcar cuando esté la persistencia hecha
        ApiUtils.getAPIService().getRespuestas(cursoSeleccionado.getCodigo(), ejercicioSeleccionado.getCodigoEjercicio(), FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<List<RespuestaPersistible>>() {
                    @Override
                    public void onResponse(Call<List<RespuestaPersistible>> call, Response<List<RespuestaPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<RespuestaPersistible> lista = response.body();
                            for (RespuestaPersistible respuesta : lista) {
                                respuestas.add(new Respuesta(respuesta.getCodigoCurso(), respuesta.getCodigoEjercicio(), respuesta.getCodigoAlumno(), respuesta.getNombreAlumno(), respuesta.getImagenBase64()));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(EjercicioActivity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(new ModelAdapterRespuesta(respuestas));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RespuestaPersistible>> call, Throwable t) {
                        Toast.makeText(EjercicioActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goToRespuesta(View button) {
        CommentsOnPhotoActivity.setRespuestaSeleccionada(this.findRespuestaSelected(button));
        CommentsOnPhotoActivity.setEjercicioSeleccionado(ejercicioSeleccionado);
        CommentsOnPhotoActivity.setCursoSeleccionado(cursoSeleccionado);
        Intent intent = new Intent(button.getContext(), CommentsOnPhotoActivity.class);
        startActivity(intent);
    }

    private Respuesta findRespuestaSelected(View view){
        for(int i = 0; i < this.respuestas.size(); i++){
            if(this.respuestas.get(i).getCodigoEjercicio().equals(view.getTag())){
                return this.respuestas.get(i);
            }
            try{
                if(((TextView)view).getText().toString().contains(respuestas.get(i).getCodigoEjercicio())){
                    return this.respuestas.get(i);
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

    private Bitmap base64ToBitMap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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
            case R.id.menu_editar_respuesta_id: {
                editarEjercicio();
            }
            default:
                return false;
        }
    }

    private void editarEjercicio(){
        EjercicioAddActivity.setEjercicioSeleccionado(ejercicioSeleccionado);
        EjercicioAddActivity.setDesdePantallaDelEjercicio(true);
        Intent intent = new Intent(this, EjercicioAddActivity.class);
        startActivity(intent);
    }

    private void openRta(int position) {
        Intent intent = new Intent(this, CommentsOnPhotoActivity.class);
        //TODO: Obtener el id de la respuesta en esta POSITION
        //TODO: Pasar el id al CommentsOnPhotoActivity
        startActivity(intent);
    }
}
