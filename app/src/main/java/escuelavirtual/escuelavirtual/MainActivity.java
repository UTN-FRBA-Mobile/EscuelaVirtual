package escuelavirtual.escuelavirtual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToCommentsPageButton = (Button) findViewById(R.id.go_to_comments_page_id);
        goToCommentsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), CommentsOnPhotoActivity.class);
                startActivity(intent);
            }
        });
    }
}
