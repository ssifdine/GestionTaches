package ma.saifdine.hd.ui.view.activity.drawer;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ma.saifdine.hd.R;
import ma.saifdine.hd.infra.utils.PrefUtils;
import ma.saifdine.hd.ui.view.fragement.TaskFragment;
import ma.saifdine.hd.ui.viewmodel.user.AuthViewModel;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // UI Components
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar customToolbar;
    private TextView userName, userEmail;

    private AuthViewModel authViewModel;


    // Constants for preference keys
    private static final String PREF_USER_NAME = "user_name";
    private static final String PREF_USER_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        initViews();
        setupToolbar();
        setupDrawer();
        updateUserInfo();
        setupViewModel();
    }

    // Initialize views and components
    private void initViews() {
        customToolbar = findViewById(R.id.custom_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Access header views from NavigationView
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);
        userEmail = headerView.findViewById(R.id.user_email);
    }

    private void setupViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getCurrentUserDetails().observe(this, user -> {
            if (user != null) {
                userName.setText(user.getUsername() != null ? user.getUsername() : "Nom inconnu");
            }
        });
    }

    // Configure the custom toolbar
    private void setupToolbar() {
        setSupportActionBar(customToolbar);
        customToolbar.setNavigationIcon(R.drawable.menuu);
        customToolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(navigationView));
    }

    // Set up the drawer layout and navigation
    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, customToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Customize hamburger icon color
        Drawable drawerIcon = toggle.getDrawerArrowDrawable();
        if (drawerIcon != null) {
            drawerIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        // Handle navigation menu item clicks
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            return true;
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        TaskFragment fragment = new TaskFragment();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    // Update user information in the drawer header
    private void updateUserInfo() {
        String email = PrefUtils.getInstance(this).read(PREF_USER_EMAIL, "Email non disponible");

        if (userEmail != null) userEmail.setText(email);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_tache:
                replaceFragment(new TaskFragment());
                break;
        }
        return false;
    }
}
