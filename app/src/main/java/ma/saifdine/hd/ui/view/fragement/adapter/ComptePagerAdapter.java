package ma.saifdine.hd.ui.view.fragement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ma.saifdine.hd.ui.view.fragement.ChangerEmailFragment;
import ma.saifdine.hd.ui.view.fragement.ChangerPasswordFragment;

public class ComptePagerAdapter extends FragmentStateAdapter {

    public ComptePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChangerEmailFragment(); // Fragment pour changer email
            case 1:
                return new ChangerPasswordFragment(); // Fragment pour changer mot de passe
            default:
                return new ChangerEmailFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Nombre total d'onglets
    }
}
