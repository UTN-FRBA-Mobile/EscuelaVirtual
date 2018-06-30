package escuelavirtual.escuelavirtual.alumno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.Respuesta;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.common.LogoutableActivity;
import escuelavirtual.escuelavirtual.data.RespuestaPersistible;
import escuelavirtual.escuelavirtual.data.UsuarioPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespuestaAddActivity extends LogoutableActivity {

    private static ImageView rtaPhoto;
    private static Bitmap rtaPhotoBitmap;
    private static Respuesta respuesta;
    private static EditText mDescripcionRespuesta;

    public static void setRespuesta(Respuesta rta) {
        RespuestaAddActivity.respuesta = rta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.main_title_id)).setText("Subir nueva respuesta");

        mDescripcionRespuesta = (EditText) findViewById(R.id.descripcion_respuesta_id);
        rtaPhoto = (ImageView) findViewById(R.id.rta_photo_id);
        rtaPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        mDescripcionRespuesta.setOnEditorActionListener(new EventoTeclado());
    }

    class EventoTeclado implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(mDescripcionRespuesta.getWindowToken(), 0);
                mDescripcionRespuesta.clearFocus();
                confirmarFotoRespuesta(null);
            }
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            rtaPhoto.setBackgroundColor(Color.WHITE);
            rtaPhoto.setImageBitmap(bitmap);
            rtaPhotoBitmap = bitmap;
        } catch (Exception e) {
            // No hacer ningún cambio porque no se sacó la foto
        }
    }

    public void confirmarFotoRespuesta(View view) {
        if(rtaPhotoBitmap == null) {
            Toast toast = Toast.makeText(this, "Cargue una imágen", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(String.format("¿Confirma esta nueva respuesta?"));

            alertDialogBuilder.setPositiveButton("Sí",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            agregarRespuesta();
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
    }

    private void agregarRespuesta() {
        final ProgressDialog progress = new ProgressDialog(RespuestaAddActivity.this);
        progress.setMessage("Guardando....");
        progress.setCanceledOnTouchOutside(false);
        progress.setTitle("Guardando la respuesta");
        Loading.ejecutar(progress);
        respuesta.setDescripcionRespuesta(RespuestaAddActivity.mDescripcionRespuesta.getText().toString());
        respuesta.setImagenRespuestaBase64(bitmapToBase64(RespuestaAddActivity.rtaPhotoBitmap));
        String alumnoId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RespuestaPersistible respuestaPersistible = new RespuestaPersistible(
                respuesta.getCodigoCurso(),
                respuesta.getCodigoEjercicio(),
                respuesta.getCodigoRespuesta(),
                alumnoId, "",
                respuesta.getImagenRespuestaBase64(),
                respuesta.getDescripcionRespuesta());

        servicioGetUserNameAndSA(progress, alumnoId, respuestaPersistible);
    }

    private void servicioGetUserNameAndSA(final ProgressDialog progress, final String alumnoId, final RespuestaPersistible respuestaP) {

        ApiUtils.getAPIService().getUsuario(alumnoId)
                .enqueue(new Callback<UsuarioPersistible>() {
                    @Override
                    public void onResponse(Call<UsuarioPersistible> call, Response<UsuarioPersistible> response) {
                        if(response.isSuccessful()) {
                            UsuarioPersistible usuario = response.body();
                            respuestaP.setNombreAlumno(usuario.getNombre());
                            servicioGuardarRespuesta(progress,respuestaP);
                        }
                    }

                    @Override
                    public void onFailure(Call<UsuarioPersistible> call, Throwable t) {
                        Toast.makeText(RespuestaAddActivity.this, "Ha ocurrido un error. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                        Loading.terminar(progress);
                    }
                });

    }


    private void servicioGuardarRespuesta(final ProgressDialog progress, RespuestaPersistible respuestaP){
        ApiUtils.getAPIService().postRespuesta(respuestaP)
                .enqueue(new Callback<RespuestaPersistible>() {
                    @Override
                    public void onResponse(Call<RespuestaPersistible> call, Response<RespuestaPersistible> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(RespuestaAddActivity.this, "La respuesta se guardó con éxito",Toast.LENGTH_SHORT).show();
                            EjercicioActivity.agregarRespuesta(respuesta);
                            Intent intent = new Intent(RespuestaAddActivity.this, EjercicioActivity.class);
                            startActivity(intent);
                        }
                        Loading.terminar(progress);
                    }

                    @Override
                    public void onFailure(Call<RespuestaPersistible> call, Throwable t) {
                        Toast.makeText(RespuestaAddActivity.this, "Ha ocurrido un error. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                        Loading.terminar(progress);
                    }
                });
    }

    private String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void cancelarFotoRespuesta(View view) {
        finish();
    }
}
