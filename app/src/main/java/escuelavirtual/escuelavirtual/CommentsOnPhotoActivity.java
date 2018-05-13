package escuelavirtual.escuelavirtual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class CommentsOnPhotoActivity extends AppCompatActivity {

    public int centralPositionOfTag = 35;
    public Map<Integer, Tag> tagsAdded;

    /** Called when the activity is first created. */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_on_photo);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagsAdded = new HashMap<>();

        ViewsController.setCommentBox((EditText) findViewById(R.id.comment_box_id));
        ViewsController.getCommentBox().setVisibility(View.INVISIBLE);
        ViewsController.getCommentBox().setEnabled(false);

        ViewsController.setTagAndNumberLayout((RelativeLayout)findViewById(R.id.tag_and_number_id));
        ViewsController.getTagAndNumberLayout().setVisibility(View.INVISIBLE);

        ViewsController.setNumberOverTag((TextView)findViewById(R.id.tag_number_id));

        // Button to add comment of comment box
        ViewsController.setAddCommentButton((Button) findViewById(R.id.apply_comment_id));
        ViewsController.getAddCommentButton().setVisibility(View.INVISIBLE);
        ViewsController.getAddCommentButton().setEnabled(false);

        // Button to edit comment of comment box
        ViewsController.setEditCommentButton((Button) findViewById(R.id.edit_comment_id));
        ViewsController.getEditCommentButton().setVisibility(View.INVISIBLE);
        ViewsController.getEditCommentButton().setEnabled(false);

        // Button to delete comment of comment box
        ViewsController.setDeleteCommentButton((Button) findViewById(R.id.delete_comment_id));
        ViewsController.getDeleteCommentButton().setVisibility(View.INVISIBLE);
        ViewsController.getDeleteCommentButton().setEnabled(false);

        // Comment section
        ViewsController.setCommentSection((LinearLayout)findViewById(R.id.comment_section_id));

        ViewsController.setBaseImage((ImageView)findViewById(R.id.base_image_id));
        ViewsController.getBaseImage().setOnTouchListener(new View.OnTouchListener() {

            final Handler handler = new Handler();
            int touchX;
            int touchY;

            // This runs when a tag is added over image
            Runnable mLongPressed = new Runnable() {
                public void run() {
                    // Keyboard
                    ViewsController.setKeyboard((InputMethodManager) getSystemService(ViewsController.getCommentBox().getContext().INPUT_METHOD_SERVICE));
                    ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);

                    // Create a tag
                    final Tag tag = new Tag(ViewsController.getBaseImage().getContext(), centralPositionOfTag, touchX, touchY);

                    // Draw a tag
                    RelativeLayout baseImageLayout = (RelativeLayout) findViewById(R.id.tags_layout_id);
                    ViewsController.setBaseImageLayout(baseImageLayout);
                    TagDrawer.drawTag(tagsAdded, tag);

                    // Set pipe/focus into comment box
                    ViewsController.getCommentBox().setEnabled(true);
                    ViewsController.getCommentBox().setVisibility(View.VISIBLE);
                    ViewsController.getCommentBox().requestFocus();

                    // Activate button to send comments when first tag is added
                    ViewsController.getAddCommentButton().setEnabled(true);
                    ViewsController.getEditCommentButton().setEnabled(false);
                    ViewsController.getDeleteCommentButton().setEnabled(true);

                    ViewsController.getAddCommentButton().setVisibility(View.VISIBLE);
                    ViewsController.getEditCommentButton().setVisibility(View.INVISIBLE);
                    ViewsController.getDeleteCommentButton().setVisibility(View.VISIBLE);

                    ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);

                    ViewsController.getAddCommentButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            Tag tagToUpdate = tagsAdded.get(ViewsController.getNumberOverTagAsInteger());
                            tagToUpdate.setComment(ViewsController.getCommentBox().getText().toString());
                            ViewsController.turnOffCommentBox();
                        }
                    });

                    ViewsController.getEditCommentButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            ViewsController.getCommentBox().setEnabled(true);
                            ViewsController.getCommentBox().setSelection(ViewsController.getCommentBox().getText().length());
                            ViewsController.getCommentBox().requestFocus();
                            ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);
                        }
                    });

                    ViewsController.getDeleteCommentButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            ViewsController.turnOffCommentBox();
                            tagsAdded.remove(ViewsController.getNumberOverTagAsInteger());
                            TagDrawer.reDrawTags(tagsAdded);
                        }
                    });
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do this to allow clearing comment section if image is touched
                        ViewsController.turnOffCommentBox();
                        TagDrawer.reDrawTags(tagsAdded);

                        touchX = (int) event.getX();
                        touchY = (int) event.getY();
                        handler.postDelayed(mLongPressed, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                        handler.removeCallbacks(mLongPressed);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }
}
