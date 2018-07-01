package escuelavirtual.escuelavirtual.docente;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import escuelavirtual.escuelavirtual.Respuesta;
import escuelavirtual.escuelavirtual.data.Tag;
import escuelavirtual.escuelavirtual.data.remote.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewsController{
    private static EditText commentBox;
    private static RelativeLayout tagAndNumberLayout;
    private static TextView numberOverTag;
    private static ImageView baseImage;
    private static Button addCommentButton;
    private static Button editCommentButton;
    private static Button deleteCommentButton;
    private static Button saveButton;
    private static Button infoDetailButton;
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

    public static Button getSaveButton() {
        return saveButton;
    }

    public static void setSaveButton(Button saveButton) {
        ViewsController.saveButton = saveButton;
    }

    public static Button getInfoDetailButton() {
        return infoDetailButton;
    }

    public static void setInfoDetailButton(Button infoDetailButton) {
        ViewsController.infoDetailButton = infoDetailButton;
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

    public static void disableAndHideButton(String action){
        Button button = getButtonFrom(action);
        button.setVisibility(View.INVISIBLE);
        button.setEnabled(false);
    }

    public static void enableAndShowButton(String action){
        Button button = getButtonFrom(action);
        button.setVisibility(View.VISIBLE);
        button.setEnabled(true);
    }

    private static Button getButtonFrom(String action){
        Button button;
        switch (action){
            case "add":
                button = getAddCommentButton();
                break;
            case "edit":
                button = getEditCommentButton();
                break;
            case "delete":
                button = getDeleteCommentButton();
                break;
            default:
                button = getSaveButton();
                break;
        }
        return button;
    }

    public static void setAddButtonClickListener(final Map<Integer, TagView> tagsAdded){
        getAddCommentButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                TagView tagViewToUpdate = tagsAdded.get(ViewsController.getNumberOverTagAsInteger());
                tagViewToUpdate.setComment(ViewsController.getCommentBox().getText().toString());
                tagViewToUpdate.setComment(ViewsController.getCommentBox().getText().toString());
                ViewsController.turnOffCommentBox();
            }
        });
    }

    public static void setEditButtonClickListener(final Map<Integer, TagView> tagsAdded){
        getEditCommentButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                ViewsController.getCommentBox().setEnabled(true);
                ViewsController.getCommentBox().setSelection(ViewsController.getCommentBox().getText().length());
                ViewsController.getCommentBox().requestFocus();
                ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public static void setDeleteButtonClickListener(final Map<Integer, TagView> tagsAdded, final APIService apiService){
        getDeleteCommentButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                ViewsController.turnOffCommentBox();
                TagView tagRemovido = tagsAdded.remove(ViewsController.getNumberOverTagAsInteger());
                Respuesta respuestaSeleccionada = CommentsOnPhotoActivity.getRespuestaSeleccionada();
                String ruta = respuestaSeleccionada.getCodigoCurso() + respuestaSeleccionada.getCodigoEjercicio() + respuestaSeleccionada.getCodigoRespuesta() + respuestaSeleccionada.getNombreAlumno();
                apiService.deleteTag(ruta, tagRemovido.getTag())
                        .enqueue(new Callback<Tag>() {
                            @Override
                            public void onResponse(Call<Tag> call, Response<Tag> response) {
                                if(response.isSuccessful()) {
                                 //Toast.makeText(CommentsOnPhotoActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Tag> call, Throwable t) {
                                //Toast.makeText(CommentsOnPhotoActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                TagDrawer.reDrawTags(tagsAdded, false);
            }
        });
    }
}
