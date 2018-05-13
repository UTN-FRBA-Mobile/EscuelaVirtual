package escuelavirtual.escuelavirtual;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewsController{
    private static EditText commentBox;
    private static RelativeLayout tagAndNumberLayout;
    private static TextView numberOverTag;
    private static ImageView baseImage;
    private static Button addCommentButton;
    private static Button editCommentButton;
    private static Button deleteCommentButton;
    private static InputMethodManager keyboard;
    private static RelativeLayout baseImageLayout;
    private static LinearLayout commentSection;

    public static LinearLayout getCommentSection() {
        return commentSection;
    }

    public static void setCommentSection(LinearLayout commentSection) {
        ViewsController.commentSection = commentSection;
    }

    public static Button getEditCommentButton() {
        return editCommentButton;
    }

    public static void setEditCommentButton(Button editCommentButton) {
        ViewsController.editCommentButton = editCommentButton;
    }

    public static Button getDeleteCommentButton() {
        return deleteCommentButton;
    }

    public static void setDeleteCommentButton(Button deleteCommentButton) {
        ViewsController.deleteCommentButton = deleteCommentButton;
    }

    public static RelativeLayout getBaseImageLayout() {
        return baseImageLayout;
    }

    public static void setBaseImageLayout(RelativeLayout baseImageLayout) {
        ViewsController.baseImageLayout = baseImageLayout;
    }

    public static EditText getCommentBox() {
        return commentBox;
    }

    public static void setCommentBox(EditText commentBox) {
        ViewsController.commentBox = commentBox;
    }

    public static RelativeLayout getTagAndNumberLayout() {
        return tagAndNumberLayout;
    }

    public static void setTagAndNumberLayout(RelativeLayout tagAndNumberLayout) {
        ViewsController.tagAndNumberLayout = tagAndNumberLayout;
    }

    public static TextView getNumberOverTag() {
        return numberOverTag;
    }

    public static Integer getNumberOverTagAsInteger() {
        return Integer.parseInt(numberOverTag.getText().toString());
    }

    public static void setNumberOverTag(TextView numberOverTag) {
        ViewsController.numberOverTag = numberOverTag;
    }

    public static ImageView getBaseImage() {
        return baseImage;
    }

    public static void setBaseImage(ImageView baseImage) {
        ViewsController.baseImage = baseImage;
    }

    public static Button getAddCommentButton() {
        return addCommentButton;
    }

    public static void setAddCommentButton(Button addCommentButton) {
        ViewsController.addCommentButton = addCommentButton;
    }

    public static InputMethodManager getKeyboard() {
        return keyboard;
    }

    public static void setKeyboard(InputMethodManager keyboard) {
        ViewsController.keyboard = keyboard;
    }

    public static void turnOffCommentBox(){
        try{
            getAddCommentButton().setEnabled(false);
            getAddCommentButton().setVisibility(View.INVISIBLE);

            getEditCommentButton().setEnabled(false);
            getEditCommentButton().setVisibility(View.INVISIBLE);

            getDeleteCommentButton().setEnabled(false);
            getDeleteCommentButton().setVisibility(View.INVISIBLE);

            getTagAndNumberLayout().setVisibility(View.INVISIBLE);

            getCommentBox().setText("");
            getCommentBox().setEnabled(false);
            getCommentBox().setVisibility(View.INVISIBLE);

            getKeyboard().hideSoftInputFromWindow(ViewsController.getCommentBox().getWindowToken(), 0);
        }catch (Exception e){
            // Do nothing, because doesn't exist views to turn off
        }
    }
}
