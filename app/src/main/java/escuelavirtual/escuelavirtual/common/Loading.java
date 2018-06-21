package escuelavirtual.escuelavirtual.common;

import android.app.ProgressDialog;

public class Loading {

     public static void ejecutar(final ProgressDialog progress){
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
                        progress.incrementProgressBy(1);
                        if (progress.getProgress() == progress
                                .getMax()) {
                            progress.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void terminar(final ProgressDialog progress){
        progress.incrementProgressBy(100);

    }

}


