package escuelavirtual.escuelavirtual;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import escuelavirtual.escuelavirtual.data.Tag;

public class TagView extends View {
    private Tag tag;

    @SuppressLint("ClickableViewAccessibility")
    public TagView(final Context context){
        super(context);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View tag, MotionEvent event) {
                selectThisTag();
                return true;
            }
        });
    }

    public void selectThisTag(){
        // Tag next to comment box
        ViewsController.getNumberOverTag().setText("" + this.tag.getNumberOfTag());
        ViewsController.getTagAndNumberLayout().setVisibility(VISIBLE);

        // Comment box
        ViewsController.getCommentSection().setVisibility(VISIBLE);
        ViewsController.getCommentBox().setEnabled(false);
        ViewsController.getCommentBox().setText(this.tag.getComment());
        ViewsController.getCommentBox().setVisibility(VISIBLE);
        ViewsController.getCommentBox().setTextColor(Color.BLACK);

        ViewsController.getAddCommentButton().setVisibility(VISIBLE);
        ViewsController.getEditCommentButton().setVisibility(VISIBLE);
        ViewsController.getDeleteCommentButton().setVisibility(VISIBLE);


        ViewsController.getAddCommentButton().setEnabled(true);
        ViewsController.getEditCommentButton().setEnabled(true);
        ViewsController.getDeleteCommentButton().setEnabled(true);
    }

    public TagView(final Context context, Tag tag){
        this(context);
        this.tag = tag;
    }

    // Este método es el que dibuja dentro del canvas
    protected void onDraw(Canvas canvas){
        Paint brush = new Paint();

        // Black circle: used to draw the black contour of tag
        brush.setColor(Color.BLACK);
        brush.setStrokeWidth(8);
        brush.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.tag.getCentralPositionOfTag(), this.tag.getCentralPositionOfTag(), 35, brush);

        // Red circle
        brush.setColor(Color.RED);
        brush.setStrokeWidth(8);
        brush.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.tag.getCentralPositionOfTag(), this.tag.getCentralPositionOfTag(), 33, brush);

        // Number inside the tag
        brush.setColor(Color.WHITE);
        brush.setStrokeWidth(10);
        brush.setTextSize(50);
        brush.setTypeface(Typeface.SANS_SERIF);

        // Adjust center coordinates to numbers with two digits
        if(this.tag.getNumberOfTag() <= 9){
            canvas.drawText(""+this.tag.getNumberOfTag(), this.tag.getCentralPositionOfTag() -13, this.tag.getCentralPositionOfTag() +20, brush);
        }else{
            canvas.drawText(""+this.tag.getNumberOfTag(), this.tag.getCentralPositionOfTag() -30, this.tag.getCentralPositionOfTag() +20, brush);
        }
    }

    public int getCentralPositionOfTag() {
        return this.tag.getCentralPositionOfTag();
    }

    public String getComment() {
        return this.tag.getComment();
    }

    public int getNumberOfTag() {
        return this.tag.getNumberOfTag();
    }

    public String getFoto() {
        return this.tag.getFoto();
    }

    public int getLeftMargin() {
        return this.tag.getLeftMargin();
    }

    public int getTopMargin() {
        return this.tag.getTopMargin();
    }

    public void setComment(String comment) {
        this.tag.setComment(comment);
    }

    public void setNumberOfTag(Integer numberOfTag) {
        this.tag.setNumberOfTag(numberOfTag);
    }

    public Tag getTag() {
        return this.tag;
    }
}
