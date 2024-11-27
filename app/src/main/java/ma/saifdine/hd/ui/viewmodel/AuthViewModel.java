package ma.saifdine.hd.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ma.saifdine.hd.domaine.model.User;
import ma.saifdine.hd.domaine.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> currentUser;
    private final MutableLiveData<String> errorMessage;

    public AuthViewModel() {
        authRepository = new AuthRepository();
        currentUser = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(User user) {
        authRepository.getAuth().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.setValue(authRepository.getAuth().getCurrentUser());
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void register(User user) {
        authRepository.getAuth().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.setValue(authRepository.getAuth().getCurrentUser());
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void logout() {
        authRepository.getAuth().signOut();
        currentUser.setValue(null);
    }
}
