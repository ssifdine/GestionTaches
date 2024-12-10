package ma.saifdine.hd.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ma.saifdine.hd.domaine.model.User;
import ma.saifdine.hd.domaine.repository.AuthRepository;
import ma.saifdine.hd.infra.utils.PrefUtils;

public class AuthViewModel extends AndroidViewModel {

    private static final String PREF_USER_EMAIL = "user_email";
    private static final String PREF_USER_PASSWORD = "user_password";

    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> currentUser;
    private final MutableLiveData<String> errorMessage;
    private final PrefUtils prefUtils;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
        currentUser = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        prefUtils = PrefUtils.getInstance(application); // Initialize PrefUtils
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Logs in a user and saves their credentials locally.
     */
    public void login(User user) {
        authRepository.getAuth().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.setValue(authRepository.getAuth().getCurrentUser());
                        saveUserCredentials(user); // Save email and password locally
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    /**
     * Registers a new user and saves their credentials locally.
     */
    public void register(User user) {
        authRepository.getAuth().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.setValue(authRepository.getAuth().getCurrentUser());
                        saveUserCredentials(user); // Save email and password locally
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    /**
     * Logs out the user and clears their saved credentials.
     */
    public void logout() {
        authRepository.getAuth().signOut();
        currentUser.setValue(null);
        clearUserCredentials(); // Clear saved credentials
    }

    /**
     * Saves the user's email and password in SharedPreferences.
     */
    private void saveUserCredentials(User user) {
        prefUtils.write(PREF_USER_EMAIL, user.getEmail());
        prefUtils.write(PREF_USER_PASSWORD, user.getPassword());
    }

    /**
     * Clears the user's email and password from SharedPreferences.
     */
    private void clearUserCredentials() {
        prefUtils.write(PREF_USER_EMAIL, "");
        prefUtils.write(PREF_USER_PASSWORD, "");
    }

    /**
     * Retrieves the saved user credentials from SharedPreferences.
     *
     * @return A User object containing the saved email and password.
     */
    public User getSavedUserCredentials() {
        String email = prefUtils.read(PREF_USER_EMAIL, "");
        String password = prefUtils.read(PREF_USER_PASSWORD, "");
        return new User(email, password);
    }
}
