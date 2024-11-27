package ma.saifdine.hd.domaine.repository;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {

    private final FirebaseAuth auth;


    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth(){
        return auth;
    }
}
