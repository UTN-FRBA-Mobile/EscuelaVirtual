package escuelavirtual.escuelavirtual.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;

import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.R;

/**
 * Funciones comunes a la aplicación completa
 */

public class FirebaseCommon {
    public static void confirm_logout(final AppCompatActivity context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("¿Cerrar Sesión?");
        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        logout(context);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private static void logout(final AppCompatActivity context) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

}
