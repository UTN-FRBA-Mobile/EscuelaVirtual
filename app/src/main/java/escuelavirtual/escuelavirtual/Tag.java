package escuelavirtual.escuelavirtual;

public class Tag{
    private int centralPositionOfTag;
    private int leftMargin;
    private int topMargin;
    private int numberOfTag;
    private String comment;

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
}
