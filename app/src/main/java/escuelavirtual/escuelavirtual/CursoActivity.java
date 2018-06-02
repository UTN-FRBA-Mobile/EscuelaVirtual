package escuelavirtual.escuelavirtual;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CursoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//     /TODO: Carga de items - Reemplazar con datos persistidos
        List<Ejercicio> ejercicios = new ArrayList<>();
        ejercicios.add(new Ejercicio("Ejercicio 1"));
        ejercicios.add(new Ejercicio("Ejercicio 2"));
        ejercicios.add(new Ejercicio("Ejercicio 3"));
        ejercicios.add(new Ejercicio("Ejercicio 4"));
        ejercicios.add(new Ejercicio("Ejercicio 5"));
        ejercicios.add(new Ejercicio("Ejercicio 6"));
        ejercicios.add(new Ejercicio("Ejercicio 7"));
        ejercicios.add(new Ejercicio("Ejercicio 8"));
        ejercicios.add(new Ejercicio("Ejercicio 9"));
//     \End

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterEjercicio(ejercicios));
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

    public void gotoEjercicio(View button) {
        Intent intent = new Intent(button.getContext(), CommentsOnPhotoActivity.class);
        startActivity(intent);
    }
}