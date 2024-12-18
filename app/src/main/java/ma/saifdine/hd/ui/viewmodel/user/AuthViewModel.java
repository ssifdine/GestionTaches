package ma.saifdine.hd.ui.viewmodel.user;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ma.saifdine.hd.domaine.model.User;
import ma.saifdine.hd.domaine.repository.AuthRepository;
import ma.saifdine.hd.infra.utils.PrefUtils;
import ma.saifdine.hd.ui.viewmodel.user.callback.UsernameCallback;

public class AuthViewModel extends AndroidViewModel {

    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_USER_NAME = "user_name";
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
                        getUsername(Objects.requireNonNull(authRepository.getAuth().getCurrentUser()).getUid(),username -> {
                            if (username != null && !username.isEmpty()) {
                                user.setUsername(username);
                                saveUserCredentials(user);
                            } else {
                                Log.e("Firebase", "Username not found! Setting default.");
                                user.setUsername("Unknown");
                            }
                        });
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
                        String userId = Objects.requireNonNull(authRepository.getAuth().getCurrentUser()).getUid();
                        // Ajouter le username dans Realtime Database
                        saveUsernameToDatabase(userId, user.getUsername());
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void changeEmail(String newEmail, String currentPassword) {
        FirebaseUser user = authRepository.getAuth().getCurrentUser();
        if(user != null){
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()),currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    user.updateEmail(newEmail).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            // Succès
                            System.out.println("Email mis à jour avec succès.");
                        } else {
                            // Gestion des erreurs
                            System.out.println("Erreur lors de la mise à jour de l'email : " + updateTask.getException().getMessage());
                        }
                    });
                } else {
                    // Gestion des erreurs de ré-authentification
                    System.out.println("Erreur de ré-authentification : " + task.getException().getMessage());
                }
            });
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    /**
     * Logs out the user and clears their saved credentials.
     */
    public void logout() {
        authRepository.getAuth().signOut();
        currentUser.setValue(null);
        clearUserCredentials(); // Clear saved credentials
    }

    private void getUsername(String userId, UsernameCallback callback) {
        // Navigate to the user node by ID
        authRepository.getDatabase().child("users").child(userId).child("username")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve the username value
                            String username = dataSnapshot.getValue(String.class);
                            Log.d("Firebase", "Username: " + username);
                            callback.onUsernameRetrieved(username != null ? username : "");

                            // Show username (or use it in your app)
                            Toast.makeText(getApplication(), "Username: " + username, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Firebase", "Username not found for userId: " + userId);
                            callback.onUsernameRetrieved("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors
                        Log.e("Firebase", "Error retrieving username: " + databaseError.getMessage());
                        callback.onUsernameRetrieved("");
                    }
                });
    }

    public LiveData<User> getCurrentUserDetails() {
        MutableLiveData<User> userDetails = new MutableLiveData<>();

            String userId = Objects.requireNonNull(authRepository.getAuth().getCurrentUser()).getUid();

            authRepository.getDatabase().child("users").child(userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                userDetails.setValue(user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Log errors or handle them as needed
                        }
                    });
        return userDetails;
    }

    private void saveUsernameToDatabase(String userId, String username) {
        // Créer un objet pour stocker les informations utilisateur
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);

        // Ajouter l'utilisateur à la base de données sous un nœud avec l'ID utilisateur
        authRepository.getDatabase().child("users").child(userId).setValue(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Nom d'utilisateur enregistré avec succès !");
                    Toast.makeText(getApplication(), "Utilisateur enregistré", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erreur lors de l'enregistrement du username", e);
                    Toast.makeText(getApplication(), "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Saves the user's email and password in SharedPreferences.
     */
    private void saveUserCredentials(User user) {
        prefUtils.write(PREF_USER_ID, Objects.requireNonNull(authRepository.getAuth().getCurrentUser()).getUid());
        prefUtils.write(PREF_USER_NAME, user.getUsername() != null ? user.getUsername() : "Unknown");
        prefUtils.write(PREF_USER_EMAIL, user.getEmail() != null ? user.getEmail() : "");
        prefUtils.write(PREF_USER_PASSWORD, user.getPassword() != null ? user.getPassword() : "");
    }

    /**
     * Clears the user's email and password from SharedPreferences.
     */
    private void clearUserCredentials() {
        prefUtils.write(PREF_USER_ID,"");
        prefUtils.write(PREF_USER_NAME,"");
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
