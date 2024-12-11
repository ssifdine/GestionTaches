package ma.saifdine.hd.ui.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.User;
import ma.saifdine.hd.ui.view.activity.drawer.DrawerActivity;
import ma.saifdine.hd.ui.viewmodel.user.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    // Déclaration des vues
    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private MaterialButton loginButton;
    private TextView registerLink;

    // Déclaration des objets Firebase et ViewModel
    private AuthViewModel authViewModel;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des instances FirebaseAuth et AuthViewModel
        firebaseAuth = FirebaseAuth.getInstance();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Vérification de la connexion de l'utilisateur
        if (firebaseAuth.getCurrentUser() != null) {
            navigateToTaskActivity();  // Utilisateur déjà connecté
            return;
        }

        // Initialisation des vues
        initializeViews();
        setupObservers();
    }

    // Initialisation des vues
    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        // Configuration des événements
        loginButton.setOnClickListener(v -> loginUser());
        registerLink.setOnClickListener(v -> navigateToRegister());
    }

    // Configuration des observateurs
    private void setupObservers() {
        // Observer l'état de l'utilisateur
        authViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                logUserDetails(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName());
                navigateToTaskActivity();
            } else {
                Log.d("FirebaseUser", "Aucun utilisateur connecté.");
            }
        });

        // Observer les messages d'erreur
        authViewModel.getErrorMessage().observe(this, this::showErrorMessage);
    }

    // Connexion de l'utilisateur
    private void loginUser() {
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer l'email et le mot de passe.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Essayer de connecter l'utilisateur avec Firebase
        authViewModel.login(new User(email, password));
    }

    // Navigation vers la page d'inscription
    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Navigation vers TaskActivity
    private void navigateToTaskActivity() {
        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
        startActivity(intent);
        finish(); // Fermer l'activité actuelle
    }

    // Affichage des détails de l'utilisateur dans le Logcat
    private void logUserDetails(String userId, String email, String displayName) {
        Log.d("FirebaseUser", "User ID: " + userId);
        Log.d("FirebaseUser", "Email: " + email);
        Log.d("FirebaseUser", "Display Name: " + displayName);
    }

    // Affichage des messages d'erreur
    private void showErrorMessage(String message) {
        if (message != null) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
