package escuelavirtual.escuelavirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

//     /Testeo de RecyclerView - Carga de items
        List<Ejercicio> ejecicios = new ArrayList<>();
        ejecicios.add(new Ejercicio(R.string.ejercicio_1));
        ejecicios.add(new Ejercicio(R.string.ejercicio_2));
        ejecicios.add(new Ejercicio(R.string.ejercicio_3));
        ejecicios.add(new Ejercicio(R.string.ejercicio_4));
        ejecicios.add(new Ejercicio(R.string.ejercicio_5));
        ejecicios.add(new Ejercicio(R.string.ejercicio_6));
        ejecicios.add(new Ejercicio(R.string.ejercicio_7));
        ejecicios.add(new Ejercicio(R.string.ejercicio_8));
        ejecicios.add(new Ejercicio(R.string.ejercicio_9));
//     \End

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterEjercicio(ejecicios));
    }

    public void gotoEjercicio(View button) {
        Intent intent = new Intent(button.getContext(), CommentsOnPhotoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }
}