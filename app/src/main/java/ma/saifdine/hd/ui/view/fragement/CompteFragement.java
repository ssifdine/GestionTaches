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

import ma.saifdine.hd.R;
import ma.saifdine.hd.ui.view.fragement.adapter.ComptePagerAdapter;

public class CompteFragement extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ComptePagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_compte,container,false);
        // Initialisation des vues
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

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
}
