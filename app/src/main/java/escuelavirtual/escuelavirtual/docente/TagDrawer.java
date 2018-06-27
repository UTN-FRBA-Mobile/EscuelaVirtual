package escuelavirtual.escuelavirtual.docente;

import android.view.View;
import android.widget.RelativeLayout;

import java.util.Map;

public class TagDrawer{

    public static TagView drawTag(Map<Integer, TagView> tagsAdded, TagView tagView){
        // Put tag into the layout of image
        int size = 2 * tagView.getCentralPositionOfTag();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        params.leftMargin = tagView.getLeftMargin();
        params.topMargin = tagView.getTopMargin();
        ViewsController.getBaseImageLayout().addView(tagView, params);

        Integer maxIndex = 1;
        for(Integer numberOfTag : tagsAdded.keySet()){
            if(numberOfTag >= maxIndex){
                maxIndex = numberOfTag;
            }
        }

        Integer nextNumberToUse = 1;
        Integer nextNumberAvailable = 1;
        while(nextNumberAvailable <= maxIndex){
            nextNumberToUse = nextNumberAvailable;
            if(!tagsAdded.keySet().contains(nextNumberAvailable)){
                break;
            }
            nextNumberAvailable++;
        }

        if(nextNumberAvailable > maxIndex){
            nextNumberToUse++;
        }

        tagsAdded.put(nextNumberToUse, tagView);
        tagView.setNumberOfTag(nextNumberToUse);
        tagView.selectThisTag();

        return tagView;
    }

    public static void seletcNoneTag(){
        ViewsController.getTagAndNumberLayout().setVisibility(View.INVISIBLE);
        // Comment box
        ViewsController.getCommentBox().setEnabled(false);
        ViewsController.getCommentBox().setVisibility(View.INVISIBLE);
    }

    public static void reDrawTags(Map<Integer, TagView> tagsAdded, Boolean fromImageClicking){
        if(!tagsAdded.isEmpty()){
            ViewsController.getBaseImageLayout().removeAllViews();
            ViewsController.getBaseImageLayout().addView(ViewsController.getBaseImage());

            for(TagView tagView : tagsAdded.values()){
                int size = 2 * tagView.getCentralPositionOfTag();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
                params.leftMargin = tagView.getLeftMargin();
                params.topMargin = tagView.getTopMargin();
                ViewsController.getBaseImageLayout().addView(tagView, params);
            }
        }else{
            if(ViewsController.getBaseImageLayout() != null && !fromImageClicking){
                ViewsController.getBaseImageLayout().removeAllViews();
                ViewsController.getBaseImageLayout().addView(ViewsController.getBaseImage());
            }
        }
    }
}
