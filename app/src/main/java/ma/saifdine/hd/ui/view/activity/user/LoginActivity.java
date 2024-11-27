package ma.saifdine.hd.ui.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.User;
import ma.saifdine.hd.ui.view.activity.task.TaskActivity;
import ma.saifdine.hd.ui.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private MaterialButton loginButton;
    private TextView registerLink;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Assurez-vous que le bon layout est utilisé

        // Initialiser les vues
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        authViewModel = new AuthViewModel();

        // Gérer le clic sur le bouton de connexion
        loginButton.setOnClickListener(v -> loginUser());

        // Gérer le lien pour l'inscription
        registerLink.setOnClickListener(v -> navigateToRegister());

        authViewModel.getCurrentUser().observe(this,firebaseUser -> {
            if(firebaseUser != null){
                Intent intent = new Intent(LoginActivity.this, TaskActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Observer pour les messages d'erreur
        authViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser() {
        // Récupérer les informations de connexion
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Essayer de connecter l'utilisateur avec Firebase
        authViewModel.login(new User(email, password));
    }

    private void navigateToRegister() {
        // Naviguer vers la page d'inscription
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
