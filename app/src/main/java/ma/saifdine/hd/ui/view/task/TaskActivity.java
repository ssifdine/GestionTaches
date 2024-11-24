package ma.saifdine.hd.ui.view.task;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.Task;
import ma.saifdine.hd.ui.view.task.listenner.OnTaskClickListener;
import ma.saifdine.hd.ui.viewmodel.TaskViewModel;

public class TaskActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;  // ViewModel pour interagir avec les données
    private TaskAdapter taskAdapter;     // Adaptateur pour afficher les tâches
    private FloatingActionButton fabAddTask;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Initialisation de la Toolbar
        initToolbar();

        // Initialisation du RecyclerView et de son adaptateur
        initRecyclerView();

        // Initialisation de la FloatingActionButton
        initFloatingActionButton();

        // Configuration du ViewModel et observation des données
        setupViewModel();
    }

    /**
     * Méthode pour initialiser et configurer la Toolbar.
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.toolbar_title));  // Défini un titre à la Toolbar
    }

    /**
     * Méthode pour initialiser le RecyclerView et configurer l'adaptateur.
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Configuration en liste verticale

        // Initialisation de l'adaptateur avec gestion des événements (clic court et long)
        taskAdapter = new TaskAdapter(new ArrayList<>(), new OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Toast.makeText(TaskActivity.this, "Modifier : " + task.getTitle(), Toast.LENGTH_SHORT).show();
                // Implémenter la logique de modification ici
            }

            @Override
            public void onTaskLongClick(Task task) {
                taskViewModel.delete(task);  // Suppression via le ViewModel
                Toast.makeText(TaskActivity.this, "Supprimé : " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(taskAdapter);
    }

    /**
     * Méthode pour initialiser la FloatingActionButton et gérer ses clics.
     */
    private void initFloatingActionButton() {
        fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());  // Affiche le dialog pour ajouter une tâche
    }

    /**
     * Méthode pour configurer le ViewModel et observer les données.
     */
    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Observer les changements de la liste des tâches
        taskViewModel.getAllTasks().observe(this, tasks -> {
            if (tasks != null) {
                taskAdapter.setTasks(tasks);  // Mettre à jour l'adaptateur
            }
        });
    }

    /**
     * Méthode pour afficher une boîte de dialogue permettant d'ajouter une nouvelle tâche.
     */
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("Ajouter une nouvelle tâche");
        builder.setIcon(R.drawable.addtachexml);

        // Charger le layout personnalisé pour le dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        // Champs du layout
        EditText titleInput = dialogView.findViewById(R.id.edit_task_title);
        EditText descriptionInput = dialogView.findViewById(R.id.edit_task_description);
        EditText dateInput = dialogView.findViewById(R.id.edit_task_date);
        Spinner statusSpinner = dialogView.findViewById(R.id.spinner_task_status);

        // Configuration du Spinner pour le statut
        setupStatusSpinner(statusSpinner);

        // Configuration des boutons du dialog
        builder.setPositiveButton("Ajouter", (dialog, which) -> handleAddTask(titleInput, descriptionInput, dateInput, statusSpinner));
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        // Personnalisation et affichage du dialog
        showCustomDialog(builder);
    }

    /**
     * Méthode pour configurer le Spinner des statuts.
     */
    private void setupStatusSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.task_status_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Méthode pour traiter l'ajout d'une nouvelle tâche.
     */
    private void handleAddTask(EditText titleInput, EditText descriptionInput, EditText dateInput, Spinner statusSpinner) {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Le titre et la date sont obligatoires", Toast.LENGTH_SHORT).show();
        } else {
            Task newTask = new Task(title, description, date, status);
            addTask(newTask);
            Toast.makeText(this, "Tâche ajoutée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Méthode pour afficher le dialog avec des styles personnalisés.
     */
    private void showCustomDialog(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.show();

        // Personnalisation des boutons
        customizeDialogButtons(dialog);
    }

    /**
     * Méthode pour personnaliser les boutons du dialog.
     */
    private void customizeDialogButtons(AlertDialog dialog) {
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.accent));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        negativeButton.setTextColor(getResources().getColor(R.color.error));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    /**
     * Méthode pour ajouter une tâche via le ViewModel.
     */
    private void addTask(Task task) {
        taskViewModel.insert(task);
    }
}
