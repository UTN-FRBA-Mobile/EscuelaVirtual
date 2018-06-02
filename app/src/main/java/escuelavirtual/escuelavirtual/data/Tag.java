package escuelavirtual.escuelavirtual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag{
    @SerializedName("centralPositionOfTag")
    @Expose
    private int centralPositionOfTag;
    @SerializedName("leftMargin")
    @Expose
    private int leftMargin;
    @SerializedName("topMargin")
    @Expose
    private int topMargin;
    @SerializedName("numberOfTag")
    @Expose
    private int numberOfTag;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("foto")
    @Expose
    private String foto;

    public Tag(int centralPositionOfTag, int leftMargin, int topMargin, int numberOfTag, String comment) {
        this.centralPositionOfTag = centralPositionOfTag;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.numberOfTag = numberOfTag;
        this.comment = comment;
    }

    public Tag(int centralPositionOfTag, int leftMargin, int topMargin) {
        this(centralPositionOfTag, leftMargin, topMargin, 0, "");
    }

    public void setNumberOfTag(int numberOfTag) {
        this.numberOfTag = numberOfTag;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCentralPositionOfTag() {
        return centralPositionOfTag;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getNumberOfTag() {
        return numberOfTag;
    }

    public String getComment() {
        return comment;
    }

    public String getFoto() {
        return foto;
    }

}
