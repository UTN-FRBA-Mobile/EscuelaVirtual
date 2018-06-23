package escuelavirtual.escuelavirtual.common;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DialogTitle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import escuelavirtual.escuelavirtual.R;

public class Loading {

     public static void ejecutar(final ProgressDialog progress){

         progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50,192,192,192)));
        /* WindowManager.LayoutParams wlmp = progress.getWindow().getAttributes();
         wlmp.gravity = Gravity.NO_GRAVITY;
         progress.getWindow().setAttributes(wlmp);
         LinearLayout layout = new LinearLayout(progress.getContext());
         layout.setOrientation(LinearLayout.VERTICAL);
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
         ImageView iv = new ImageView(progress.getContext());
         iv.setImageResource(R.mipmap.ic_escuelavirtual);
         layout.addView(iv, params);
         progress.addContentView(layout, params);*/


        progress.setIcon(R.mipmap.ic_escuelavirtual);
        progress.setMax(100);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         //progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         progress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progress.getProgress() <= progress
                            .getMax()) {
                        Thread.sleep(50);
                        if (progress.getProgress() == 55) {

                        }else {
                            progress.incrementProgressBy(1);
                            if (progress.getProgress() == progress
                                    .getMax()) {
                                progress.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void terminar(final ProgressDialog progress){
        //progress.setProgress(95);
        progress.dismiss();
    }

}


