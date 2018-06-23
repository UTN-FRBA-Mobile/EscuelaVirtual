package escuelavirtual.escuelavirtual.docente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EjercicioAddActivity extends AppCompatActivity {

    private static ImageView photo;
    private static Bitmap photoBitmap;
    private static EditText codigoEjercicio;
    private static Ejercicio ejercicioSeleccionado;
    private static Boolean desdePantallaDelEjercicio = false;

    public static void setDesdePantallaDelEjercicio(Boolean desdePantallaDelEjercicio) {
        EjercicioAddActivity.desdePantallaDelEjercicio = desdePantallaDelEjercicio;
    }

    public static void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        EjercicioAddActivity.ejercicioSeleccionado = ejercicioSeleccionado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio_add);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = (ImageView) findViewById(R.id.photo_id);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        codigoEjercicio = (EditText) findViewById(R.id.codigo_ejercicio_id);

        if(ejercicioSeleccionado != null){
            ((TextView)findViewById(R.id.main_title_id)).setText("Editar ejercicio");
            Bitmap bitmap = base64ToBitMap(ejercicioSeleccionado.getImagenBase64());
            photo.setImageBitmap(bitmap);
            photo.setBackgroundColor(Color.WHITE);
            photoBitmap = bitmap;
            codigoEjercicio.setText(ejercicioSeleccionado.getCodigoEjercicio());
        }else{
            ((TextView)findViewById(R.id.main_title_id)).setText("Subir nuevo ejercicio");
            restartForm();
        }

        EventoTeclado keyboard = new EventoTeclado();
        codigoEjercicio.setOnEditorActionListener(keyboard);
    }

    class EventoTeclado implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                confirmarFotoEjercicio(null);
            }
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(desdePantallaDelEjercicio){
            EjercicioActivity.setEjercicioSeleccionado(ejercicioSeleccionado);
            startActivity(new Intent(this, EjercicioActivity.class));
            return true;
        }

        desdePantallaDelEjercicio = false;
        startActivity(new Intent(this, CursoActivity.class));
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try{
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            photo.setBackgroundColor(Color.WHITE);
            photo.setImageBitmap(bitmap);
            photoBitmap = bitmap;
        }catch (Exception e){
            // No hacer ningún cambio porque no se sacó la foto
        }
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

    public void confirmarFotoEjercicio(View view) {
        if("".equals(codigoEjercicio.getText().toString()) || photoBitmap == null){
            Toast toast = Toast.makeText(this,"Para cargar el ejercicio, ingresá un código identificatorio y la imagen", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(String.format(
                    "¿Confirma este nuevo ejercicio?"));

            alertDialogBuilder.setPositiveButton("Sí",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            addOrEditExercise();
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

    public void cancelarFotoEjercicio(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea cancelar la edición del curso?"));

        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        restartForm();
                        if(desdePantallaDelEjercicio){
                            EjercicioActivity.setEjercicioSeleccionado(ejercicioSeleccionado);
                            startActivity(new Intent(EjercicioAddActivity.this, EjercicioActivity.class));
                            return;
                        }

                        desdePantallaDelEjercicio = false;
                        startActivity(new Intent(EjercicioAddActivity.this, CursoActivity.class));
                        return;
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

    private void addOrEditExercise(){
        if(ejercicioSeleccionado != null){
            editEjercicioConfirm();
        }else{
            addEjercicioConfirm();
        }
    }

    private void editEjercicioConfirm() {
        ApiUtils.getAPIService().actualizarEjercicio(CursoActivity.getCursoSeleccionado().getCodigo(), ejercicioSeleccionado.getCodigoEjercicio() , codigoEjercicio.getText().toString(), bitmapToBase64(photoBitmap), FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(EjercicioAddActivity.this, "El ejercicio se actualizó con éxito",Toast.LENGTH_SHORT).show();

                            if(desdePantallaDelEjercicio){
                                EjercicioActivity.setEjercicioSeleccionado(new Ejercicio(codigoEjercicio.getText().toString(), bitmapToBase64(photoBitmap)));
                                Intent intent = new Intent(EjercicioAddActivity.this, EjercicioActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(EjercicioAddActivity.this, CursoActivity.class);
                                startActivity(intent);
                            }
                            desdePantallaDelEjercicio = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(EjercicioAddActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addEjercicioConfirm() {
        ApiUtils.getAPIService().crearEjercicio(CursoActivity.getCursoSeleccionado().getCodigo(), codigoEjercicio.getText().toString(), this.bitmapToBase64(photoBitmap), FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(EjercicioAddActivity.this, "El ejercicio se creó con éxito",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EjercicioAddActivity.this, CursoActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(EjercicioAddActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void restartForm(){
        if(codigoEjercicio != null){
            codigoEjercicio.setText("");
        }
        if(photo != null){
            photo.setImageBitmap(null);
            photo.setBackgroundResource(android.R.drawable.ic_menu_camera);
        }
        photoBitmap = null;
    }
}
