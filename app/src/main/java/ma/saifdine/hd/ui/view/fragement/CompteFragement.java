package ma.saifdine.hd.ui.view.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

import ma.saifdine.hd.R;
import ma.saifdine.hd.infra.utils.PrefUtils;
import ma.saifdine.hd.ui.view.fragement.adapter.ComptePagerAdapter;

public class CompteFragement extends Fragment {

    // Constants for preference keys
    private static final String PREF_USER_EMAIL = "user_email";
    private static final String PREF_USER_NAME = "user_name";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ComptePagerAdapter pagerAdapter;
    private MaterialTextView materialTextViewUsername, materialTextViewEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_compte,container,false);
        // Initialisation des vues
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        materialTextViewUsername = view.findViewById(R.id.usernameId);
        materialTextViewEmail = view.findViewById(R.id.emailId);

        getUserInfo();


        // Configuration du ViewPager2
        pagerAdapter = new ComptePagerAdapter(requireActivity());
        viewPager.setAdapter(pagerAdapter);

        // Lier le TabLayout avec le ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Changer Email");
                            break;
                        case 1:
                            tab.setText("Changer Mot de Passe");
                            break;
                    }
                }).attach();
        return view;
    }

    // Get user information
    private void getUserInfo() {
        String email = PrefUtils.getInstance(requireContext()).read(PREF_USER_EMAIL, "Email non disponible");
        String username = PrefUtils.getInstance(requireContext()).read(PREF_USER_NAME,"Username non disponible");

        if (materialTextViewUsername != null) materialTextViewUsername.setText(username);
        if (materialTextViewEmail != null) materialTextViewEmail.setText(email);
    }
}
