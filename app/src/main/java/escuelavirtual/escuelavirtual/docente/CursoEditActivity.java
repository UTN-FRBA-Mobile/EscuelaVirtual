package escuelavirtual.escuelavirtual.docente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoEditActivity extends AppCompatActivity{

    private EditText etCursoCode;
    private EditText etCursoDescripcion;

    private static Curso course;

    public static Curso getCourse() {
        return course;
    }

    public static void setCourse(Curso course) {
        CursoEditActivity.course = course;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_edit);

        etCursoCode = (EditText) findViewById(R.id.et_curso_code_id);
        etCursoDescripcion = (EditText) findViewById(R.id.et_curso_descripcion_id);

        etCursoCode.setText(getCourse().getCodigo());
        etCursoDescripcion.setText(getCourse().getDescripcion());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                confirm_logout();
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

    public void confirm_EditCurso(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Confirma estas modificaciónes?"));

        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        editCursoConfirm();
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

    private void editCursoConfirm() {
        final ProgressDialog progress = new ProgressDialog(CursoEditActivity.this);
        progress.setMessage("Guardando....");
        progress.setTitle("Actualizando el curso:  " + course.getDescripcion());
        Loading.ejecutar(progress);
        ApiUtils.getAPIService().updateCursos(course.getCodigo(),etCursoCode.getText().toString(),course.getDescripcion(), etCursoDescripcion.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(CursoEditActivity.this, "El curso ha sido editado.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CursoEditActivity.this, MainActivity.class);
                            startActivity(intent);
                            Loading.terminar(progress);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(CursoEditActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                        Loading.terminar(progress);
                    }
                });
    }

    public void cancel_EditCurso(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea cancelar la edición del curso?"));

        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        editCursoCanceled();
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

    private void editCursoCanceled() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
