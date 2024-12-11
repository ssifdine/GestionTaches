package ma.saifdine.hd.ui.view.activity.drawer;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.view.View;
import android.widget.TextView;

import ma.saifdine.hd.R;
import ma.saifdine.hd.infra.utils.PrefUtils;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar customToolbar;

    private TextView userName, userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        // Link the custom toolbar
        customToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolbar);

        // Set the navigation icon (menu icon) programmatically
        customToolbar.setNavigationIcon(R.drawable.menuu);

        // Set the behavior for the navigation icon (it should open the drawer when clicked)
        customToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when the navigation icon (menu) is clicked
                drawerLayout.openDrawer(navigationView);
            }
        });

        // Configure the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Add ActionBarDrawerToggle for syncing the drawer state with the toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, customToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Modifier la couleur de l'icône du hamburger
        Drawable drawerIcon = toggle.getDrawerArrowDrawable();
        if (drawerIcon != null) {
            drawerIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        // Handle navigation menu clicks
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            // Handle menu item clicks
            drawerLayout.closeDrawers();
            return true;
        });

        // Accédez à l'en-tête du NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Récupérez les TextViews dans l'en-tête
        userName = headerView.findViewById(R.id.user_name);
        userEmail = headerView.findViewById(R.id.user_email);

        // Modifiez dynamiquement les données utilisateur
        String name = getUserName();  // Vous récupérez dynamiquement le nom
        String email = getUserEmail(); // Vous récupérez dynamiquement l'email

        // Mettez à jour les TextViews
        if (userName != null && userEmail != null) {
            userName.setText(name != null ? name : "Nom inconnu");
            userEmail.setText(email != null ? email : "Email non disponible");
        }
    }

    @Override
    public void onBackPressed() {
        // Close the drawer if it's open
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    private String getUserName() {
        return PrefUtils.getInstance(this).read("user_name", "Username non disponible");
    }

    // Méthodes simulées pour récupérer le nom et l'email de l'utilisateur
    private String getUserEmail() {
        return PrefUtils.getInstance(this).read("user_email", "Email non disponible");
    }
}
