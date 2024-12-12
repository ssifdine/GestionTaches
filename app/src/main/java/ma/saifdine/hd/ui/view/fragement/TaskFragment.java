package ma.saifdine.hd.ui.view.fragement;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import java.util.Objects;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.Task;
import ma.saifdine.hd.infra.utils.PrefUtils;
import ma.saifdine.hd.ui.view.activity.task.TaskActivity;
import ma.saifdine.hd.ui.view.activity.task.TaskAdapter;
import ma.saifdine.hd.ui.view.activity.task.listenner.OnTaskClickListener;
import ma.saifdine.hd.ui.view.dialog.ConfirmationDialog;
import ma.saifdine.hd.ui.viewmodel.TaskViewModel;

public class TaskFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private FloatingActionButton fabAddTask;
    private TaskAdapter taskAdapter;

    private static final String PREF_USER_ID = "user_id";
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_task,container,false);
        initRecyclerView(view);
        initFloatingActionButton(view);
        setupViewModel();
        return view;
    }

    private void initRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskAdapter = new TaskAdapter(new ArrayList<>(), new OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTaskLongClick(Task task) {
                ConfirmationDialog.show(
                        getActivity(),
                        "Confirmation de suppression",
                        "Êtes-vous sûr de vouloir supprimer la tâche \"" + task.getTitle() + "\" ?",
                        requireActivity().getDrawable(R.drawable.supprimer), // Icône à afficher (modifiable)
                        () -> { // Action de confirmation
                            taskViewModel.delete(task);
                            Toast.makeText(getContext(), "Tâche supprimée : " + task.getTitle(), Toast.LENGTH_SHORT).show();
                        },
                        () -> { // Action d'annulation
                            Toast.makeText(getContext(), "Action annulée", Toast.LENGTH_SHORT).show();
                        }
                );

            }
        });
        recyclerView.setAdapter(taskAdapter);
    }

    private void initFloatingActionButton(View view) {
        fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }
    private void showAddTaskDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.DialogTheme);
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

    private void setupViewModel() {
        userId = PrefUtils.getInstance(requireContext()).read(PREF_USER_ID, "Identfiant non disponible");
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTasksByUserId(userId).observe(requireActivity(), tasks -> {
            if (tasks != null) {
                taskAdapter.setTasks(tasks);
            }
        });
    }

    private void setupStatusSpinner(AutoCompleteTextView statusSpinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.task_status_array));
        statusSpinner.setAdapter(adapter);
    }

    private void showDatePickerDialog(EditText dateInput) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choisir une date")
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateInput.setText(dateFormat.format(new Date(selection)));
        });
        datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

    private void handleAddTask(EditText titleInput, EditText descriptionInput, EditText dateInput, AutoCompleteTextView statusSpinner) {
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String date = dateInput.getText().toString();
        String status = statusSpinner.getText().toString();

        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || status.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date taskDate = dateFormat.parse(date);

            Task newTask = new Task(title, description, taskDate, status,userId);
            taskViewModel.insert(newTask);
            Toast.makeText(getContext(), "Tâche ajoutée avec succès.", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Format de date invalide.", Toast.LENGTH_SHORT).show();
        }
    }

    private void customizeDialogButtons(AlertDialog dialog) {
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.accent));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        negativeButton.setTextColor(getResources().getColor(R.color.error));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }


}
