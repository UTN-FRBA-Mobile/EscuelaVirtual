package escuelavirtual.escuelavirtual.common;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DialogTitle;
import android.widget.ProgressBar;

import escuelavirtual.escuelavirtual.R;

public class Loading {

     public static void ejecutar(final ProgressDialog progress){

         //progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50,192,192,192)));
         progress.setIcon(R.mipmap.ic_escuelavirtual);
        progress.setMax(100);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         progress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progress.getProgress() <= progress
                            .getMax()) {
                        Thread.sleep(50);
                        if (progress.getProgress() == 60) {
                            progress.dismiss();
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
        progress.setProgress(85);

    }

}


