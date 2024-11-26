package ma.saifdine.hd.ui.view.activity.task;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.Task;
import ma.saifdine.hd.ui.view.activity.task.listenner.OnTaskClickListener;
import ma.saifdine.hd.ui.view.dialog.ConfirmationDialog;
import ma.saifdine.hd.ui.viewmodel.TaskViewModel;

public class TaskActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private FloatingActionButton fabAddTask;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initRecyclerView();
        initFloatingActionButton();
        setupViewModel();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TaskAdapter(new ArrayList<>(), new OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Toast.makeText(TaskActivity.this, "Modifier : " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTaskLongClick(Task task) {
                ConfirmationDialog.show(
                        TaskActivity.this,
                        "Confirmation de suppression",
                        "Êtes-vous sûr de vouloir supprimer la tâche \"" + task.getTitle() + "\" ?",
                        getDrawable(R.drawable.supprimer), // Icône à afficher (modifiable)
                        () -> { // Action de confirmation
                            taskViewModel.delete(task);
                            Toast.makeText(TaskActivity.this, "Tâche supprimée : " + task.getTitle(), Toast.LENGTH_SHORT).show();
                        },
                        () -> { // Action d'annulation
                            Toast.makeText(TaskActivity.this, "Action annulée", Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });
        recyclerView.setAdapter(taskAdapter);
    }

    private void initFloatingActionButton() {
        fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            if (tasks != null) {
                taskAdapter.setTasks(tasks);
            }
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("Ajouter une nouvelle tâche");
        builder.setIcon(R.drawable.addtachexml);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.edit_task_title);
        EditText descriptionInput = dialogView.findViewById(R.id.edit_task_description);
        EditText dateInput = dialogView.findViewById(R.id.edit_task_date);
        AutoCompleteTextView statusSpinner = dialogView.findViewById(R.id.spinner_task_status);

        setupStatusSpinner(statusSpinner);
        dateInput.setOnClickListener(v -> showDatePickerDialog(dateInput));

        builder.setPositiveButton("Ajouter", (dialog, which) -> handleAddTask(titleInput, descriptionInput, dateInput, statusSpinner));
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        // Créer et afficher le dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Personnaliser les boutons du dialog
        customizeDialogButtons(dialog);
    }

    private void customizeDialogButtons(AlertDialog dialog) {
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.accent));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        negativeButton.setTextColor(getResources().getColor(R.color.error));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    private void setupStatusSpinner(AutoCompleteTextView spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.task_status_array,
                android.R.layout.simple_dropdown_item_1line
        );
        spinner.setAdapter(adapter);
    }

    private void showDatePickerDialog(EditText dateInput) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Sélectionnez une date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selection));
            dateInput.setText(formattedDate);
        });
    }

    private void handleAddTask(EditText titleInput, EditText descriptionInput, EditText dateInput, AutoCompleteTextView statusSpinner) {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String dateString = dateInput.getText().toString().trim();
        String status = statusSpinner.getText().toString();

        if (title.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(this, "Le titre et la date sont obligatoires", Toast.LENGTH_SHORT).show();
        } else {
            Date dueDate = parseDate(dateString);
            Task newTask = new Task(title, description, dueDate, status);
            addTask(newTask);
            Toast.makeText(this, "Tâche ajoutée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addTask(Task task) {
        taskViewModel.insert(task);
    }
}
