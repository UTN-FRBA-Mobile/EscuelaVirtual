package escuelavirtual.escuelavirtual;

import android.widget.RelativeLayout;

import java.util.Map;

public class TagDrawer{

    public static Tag drawTag(Map<Integer, Tag> tagsAdded, Tag tag){
        // Put tag into the layout of image
        int size = 2 * tag.getCentralPositionOfTag();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        params.leftMargin = tag.getLeftMargin();
        params.topMargin = tag.getTopMargin();
        ViewsController.getBaseImageLayout().addView(tag, params);

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

        tagsAdded.put(nextNumberToUse, tag);
        tag.setNumberOfTag(nextNumberToUse);
        tag.selectThisTag();

        return tag;
    }

    public static void reDrawTags(Map<Integer, Tag> tagsAdded){
        if(!tagsAdded.isEmpty()){
            ViewsController.getBaseImageLayout().removeAllViews();
            ViewsController.getBaseImageLayout().addView(ViewsController.getBaseImage());

            for(Tag tag : tagsAdded.values()){
                int size = 2 * tag.getCentralPositionOfTag();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
                params.leftMargin = tag.getLeftMargin();
                params.topMargin = tag.getTopMargin();
                ViewsController.getBaseImageLayout().addView(tag, params);
            }
        }else{
            if(ViewsController.getBaseImageLayout() != null){
                ViewsController.getBaseImageLayout().removeAllViews();
                ViewsController.getBaseImageLayout().addView(ViewsController.getBaseImage());
            }
        }
    }
}
