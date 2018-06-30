package escuelavirtual.escuelavirtual;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.iid.FirebaseInstanceId;

import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRepeatPasswordView;
    private EditText mNombreView;
    private RadioGroup mPerfilRadioGroup;
    private TextView mPerfilError;
    private ImageView mPEPicture;

    private String email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRepeatPasswordView = (EditText) findViewById(R.id.repeatPassword);
        mNombreView = (EditText) findViewById(R.id.name);
        mPerfilRadioGroup = (RadioGroup) findViewById(R.id.perfilRadioGroup);
        mPerfilError = (TextView) findViewById(R.id.perfilError);
        mPEPicture = (ImageView) findViewById(R.id.pe_picture);
        mPEPicture.setImageResource(R.drawable.tech_stud_50op);
    }

    public void registrar(View view) {
        if(validarRegistro()){

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Registrando sus datos...");
            progress.setTitle("Por favor, espere...");
            progress.setCanceledOnTouchOutside(false);
            Loading.ejecutar(progress);

            email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            //String uid =
                    mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Loading.terminar(progress);
                            if (task.isSuccessful()) {
                                updateUI(true);
                            } else {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    mPasswordView.setError(getString(R.string.error_weak_password));
                                    mPasswordView.requestFocus();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    mEmailView.setError(getString(R.string.error_user_exists));
                                    mEmailView.requestFocus();
                                } catch(Exception e) {
                                    Toast.makeText(RegistrationActivity.this, getString(R.string.error_aut),
                                            Toast.LENGTH_SHORT).show();
                                }

                                updateUI(false);
                            }
                        }
                    });

        }
    }

    public void onSelectedPerfil(View view){
        if (selectedPrerfil().equals("radioAlumno")) {
            mPEPicture.setImageResource(R.drawable.tech_50op);
        }else{
            mPEPicture.setImageResource(R.drawable.stud_50op);
        }
    }

    private String selectedPrerfil(){
        int checkedPerfilId = mPerfilRadioGroup.getCheckedRadioButtonId();
        View radioButton = mPerfilRadioGroup.findViewById(checkedPerfilId);
        return radioButton.getResources().getResourceEntryName(checkedPerfilId);
    }

    private void guardarUsuario(String uid) {
        String nombre = mNombreView.getText().toString();
        String perfilId = selectedPrerfil();
        //1 alumno, 0 docente
        //Refefrencia al ID del radioButton
        int perfil = (perfilId.equals("radioAlumno"))?1:0;
        ApiUtils.getAPIService().guardarUsuario(nombre, perfil, uid, FirebaseInstanceId.getInstance().getToken())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Usuario Registrado.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RegistrationActivity.this, "Falló la registración del usuario",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean validarRegistro() {

        View focusView = null;
        boolean retorno = true;

        //Limpiar errores
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRepeatPasswordView.setError(null);
        mNombreView.setError(null);
        mPerfilError.setText("");

        if (mPerfilRadioGroup.getCheckedRadioButtonId() == -1) {
            mPerfilError.setText(getString(R.string.error_field_required));
            retorno = false;
        }


        if (mNombreView.getText().toString().length() < 1) {
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            retorno = false;
        }

        if(mPasswordView.getText().toString().isEmpty()){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            retorno = false;
        }else {
            if (!mPasswordView.getText().toString().equals(mRepeatPasswordView.getText().toString())) {
                mPasswordView.setError(getString(R.string.error_distinct_password));
                focusView = mPasswordView;
                retorno = false;
            }
        }
        if (!mEmailView.getText().toString().contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            retorno = false;
        }

        if(focusView != null)focusView.requestFocus();
        return retorno;
    }

    private void updateUI(boolean successfulReg) {
        if(successfulReg){
            guardarUsuario(mAuth.getCurrentUser().getUid());
            Intent returnIntent = new Intent();
            returnIntent.putExtra("email",email);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }
}
