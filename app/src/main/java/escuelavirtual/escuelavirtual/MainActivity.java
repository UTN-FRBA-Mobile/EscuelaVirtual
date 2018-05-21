package escuelavirtual.escuelavirtual;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//     /TODO: Carga de items - Reemplazar con datos persistidos
        List<Ejercicio> ejecicios = new ArrayList<>();
        ejecicios.add(new Ejercicio(R.string.ejercicio_1));

        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso(R.string.curso_1, ejecicios));
        cursos.add(new Curso(R.string.curso_2, ejecicios));
        cursos.add(new Curso(R.string.curso_3, ejecicios));
        cursos.add(new Curso(R.string.curso_4, ejecicios));
        cursos.add(new Curso(R.string.curso_5, ejecicios));
        cursos.add(new Curso(R.string.curso_6, ejecicios));
        cursos.add(new Curso(R.string.curso_7, ejecicios));
        cursos.add(new Curso(R.string.curso_8, ejecicios));
        cursos.add(new Curso(R.string.curso_9, ejecicios));
//     \End

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterCurso(cursos));
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

    public void gotoCurso(View view){

        Intent intent = new Intent(this, CursoActivity.class);
        startActivity(intent);
    }

}
