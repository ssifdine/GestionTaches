package ma.saifdine.hd.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
import ma.saifdine.hd.ui.viewmodel.TaskViewModel;

public class TaskActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private FloatingActionButton fabAddTask;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Initialisation de la Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Titre de la Toolbar
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.toolbar_title));

        // Initialize the FloatingActionButton
        fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskClickListener() {

            @Override
            public void onTaskClick(Task task) {
                // Exemple : Modifier une tâche
                Toast.makeText(TaskActivity.this, "Modifier : " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTaskLongClick(Task task) {
                // Exemple : Supprimer une tâche
                taskViewModel.delete(task);
                Toast.makeText(TaskActivity.this, "Supprimé : " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Observer les données de la base
        taskViewModel.getAllTasks().observe(this, tasks -> taskAdapter.setTasks(tasks));

    }

    private void showAddTaskDialog() {
        // Créer un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter une nouvelle tâche");

        // Inflater un layout personnalisé pour le dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        // Récupérer les champs du layout personnalisé
        EditText titleInput = dialogView.findViewById(R.id.edit_task_title);
        EditText descriptionInput = dialogView.findViewById(R.id.edit_task_description);
        EditText dateInput = dialogView.findViewById(R.id.edit_task_date);
        Spinner statusSpinner = dialogView.findViewById(R.id.spinner_task_status);

        // Configurer le Spinner pour le statut
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.task_status_array, // Défini dans res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Ajouter les boutons au dialog
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            // Récupérer les données saisies
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String status = statusSpinner.getSelectedItem().toString();

            // Vérifier les champs obligatoires
            if (title.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Le titre et la date sont obligatoires", Toast.LENGTH_SHORT).show();
            } else {
                // Créer une nouvelle tâche
                Task newTask = new Task(title, description, date, status);
                // Ajouter la tâche à votre base de données ou liste
                addTask(newTask);
                Toast.makeText(this, "Tâche ajoutée avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        // Afficher le dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Méthode pour ajouter une tâche
    private void addTask(Task task) {
        // Implémentez ici l'ajout de la tâche (par exemple, dans une liste ou une base de données)
        taskViewModel.insert(task);
    }

}