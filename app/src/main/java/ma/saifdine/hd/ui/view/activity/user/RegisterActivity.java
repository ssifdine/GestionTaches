package ma.saifdine.hd.ui.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ma.saifdine.hd.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private MaterialButton registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Assurez-vous que le bon layout est utilisé

        // Initialiser les vues
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // Gérer le clic sur le bouton d'inscription
        registerButton.setOnClickListener(v -> registerUser());

        // Gérer le lien pour la connexion
        loginLink.setOnClickListener(v -> navigateToLogin());
    }

    private void registerUser() {
        // Récupérer les informations d'inscription
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation des champs
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Vous pouvez ajouter une logique pour afficher un message d'erreur
            return;
        }

        if (!password.equals(confirmPassword)) {
            // Afficher un message d'erreur si les mots de passe ne correspondent pas
            return;
        }

        // Implémentez ici la logique d'inscription avec Firebase ou un autre backend

        // Si l'inscription est réussie, naviguer vers la page de connexion
        // startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    private void navigateToLogin() {
        // Naviguer vers la page de connexion
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
