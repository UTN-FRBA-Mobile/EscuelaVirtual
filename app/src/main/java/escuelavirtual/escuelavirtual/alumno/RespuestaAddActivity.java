package escuelavirtual.escuelavirtual.alumno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Random;

import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.Respuesta;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import escuelavirtual.escuelavirtual.docente.*;
import escuelavirtual.escuelavirtual.docente.CursoActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespuestaAddActivity extends AppCompatActivity {

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

        mDescripcionRespuesta = (EditText) findViewById(R.id.descripcion_respuesta_id);
        rtaPhoto = (ImageView) findViewById(R.id.rta_photo_id);
        rtaPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

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
        progress.setTitle("Guardando la respuesta");
        Loading.ejecutar(progress);
        respuesta.setDescripcionRespuesta(RespuestaAddActivity.mDescripcionRespuesta.getText().toString());
        respuesta.setImagenRespuestaBase64(bitmapToBase64(RespuestaAddActivity.rtaPhotoBitmap));
        String alumnoId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String alumnoNombre = servicioGetUserName(alumnoId);
        servicioGuardarRespuesta(
                progress,
                //TODO: Actualizar con codigo de curso
                respuesta.getCodigoCurso(),
                respuesta.getCodigoEjercicio(),
                respuesta.getCodigoRespuesta(),
                alumnoId,
                alumnoNombre,
                respuesta.getImagenRespuestaBase64(),
                respuesta.getDescripcionRespuesta()
        );
    }

    private String servicioGetUserName(String alumnoId) {
        //TODO: La idea es que devuelva el nombre de un usuario segun su id con el objetivo de
        // guardar el nombre del user en la respuesta y no tener que buscarlo cada vez que se trae
        // la lista de respuestas
        return "Thor";
    }

    private void servicioGuardarRespuesta(
            ProgressDialog progress,
            String codigoCurso,
            String codigoEjercicio,
            String codigoRespuesta,
            String alumnoId,
            String alumnoNombre,
            String imagenRespuestaBase64,
            String descripcionRespuesta) {

                        Toast.makeText(RespuestaAddActivity.this, "La respuesta se guardó con éxito",Toast.LENGTH_SHORT).show();
                        //Una vez que guarde el servicio, llamar a cargarRespuestas() para refrescar respuestas
                        EjercicioActivity.agregarRespuesta(respuesta);
                        Intent intent = new Intent(RespuestaAddActivity.this, EjercicioActivity.class);
                        startActivity(intent);
                        Loading.terminar(progress);
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
