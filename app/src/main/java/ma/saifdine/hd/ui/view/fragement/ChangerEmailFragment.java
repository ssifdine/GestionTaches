package ma.saifdine.hd.ui.view.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import ma.saifdine.hd.R;
import ma.saifdine.hd.ui.viewmodel.user.AuthViewModel;

public class ChangerEmailFragment extends Fragment {

    private AuthViewModel authViewModel;
    private TextInputEditText textInputEditTextEmail , textInputEditTextMotdepasse;
    private MaterialButton materialButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changer_email, container, false);
        initView(view);
        setupViewModel();
        return view;
    }

    public void initView(View view){
        textInputEditTextEmail = view.findViewById(R.id.emailEditText);
        textInputEditTextMotdepasse = view.findViewById(R.id.passwordEditText);

        materialButton = view.findViewById(R.id.enrgButton);
        materialButton.setOnClickListener(v->changeEmail());
    }

    public void setupViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    public void changeEmail(){
        String nouveau_email = textInputEditTextEmail.getText().toString();
        String password = textInputEditTextMotdepasse.getText().toString();
        authViewModel.changeEmail(nouveau_email,password);
    }
}
