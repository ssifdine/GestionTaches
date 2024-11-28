package ma.saifdine.hd.ui.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ma.saifdine.hd.R;

public class RegisterActivity extends AppCompatActivity {

    // UI Components
    private TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private MaterialButton registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        initViews();

        // Set listeners
        registerButton.setOnClickListener(v -> registerUser());
        loginLink.setOnClickListener(v -> navigateToLogin());
    }

    /**
     * Initialize all views from the layout
     */
    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
    }

    /**
     * Handles user registration
     */
    private void registerUser() {
        // Retrieve input data
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate input data
        if (!validateInputs(email, password, confirmPassword)) {
            return;
        }

        // TODO: Add registration logic here (e.g., Firebase or custom backend)


        // Navigate to login screen after successful registration
        navigateToLogin();
    }

    /**
     * Validate user inputs
     *
     * @param email           The entered email
     * @param password        The entered password
     * @param confirmPassword The re-entered password
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs(String email, String password, String confirmPassword) {
        boolean isValid = true;

        // Reset errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        confirmPasswordInputLayout.setError(null);

        // Check if email is empty or invalid
        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError(getString(R.string.error_email_required));
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Check if password is empty or too short
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError(getString(R.string.error_password_required));
            isValid = false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError(getString(R.string.error_password_too_short));
            isValid = false;
        }

        // Check if passwords match
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordInputLayout.setError(getString(R.string.error_confirm_password_required));
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError(getString(R.string.error_password_mismatch));
            isValid = false;
        }

        return isValid;
    }

    /**
     * Navigate to the login activity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
