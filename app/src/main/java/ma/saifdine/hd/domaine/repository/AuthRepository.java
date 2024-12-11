package ma.saifdine.hd.domaine.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthRepository {

    private final FirebaseAuth auth;

    private DatabaseReference database;


    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public FirebaseAuth getAuth(){
        return auth;
    }
}
