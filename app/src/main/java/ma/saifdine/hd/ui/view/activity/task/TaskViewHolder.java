package ma.saifdine.hd.ui.view.activity.task;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.Task;

/**
 * TaskViewHolder est un ViewHolder pour représenter une tâche dans RecyclerView.
 *
 * Objectifs :
 * - Gérer individuellement les éléments graphiques associés à une tâche.
 * - Fournir une méthode "bind" pour afficher dynamiquement les données d'une tâche.
 *
 * Fonctionnalités clés :
 * - Lie les données de la tâche aux TextViews associés.
 * - Sépare la gestion de la vue pour suivre le principe de responsabilité unique (SRP).
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {

    // Champs graphiques pour afficher les détails de la tâche
    private final TextView titleTextView;
    private final TextView statusTextView;
    private final TextView dueDateTextView;

    /**
     * Constructeur pour initialiser les vues graphiques du layout.
     *
     * @param itemView Vue correspondant à un item individuel.
     */
    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.taskTitleTextView);
        statusTextView = itemView.findViewById(R.id.taskStatusTextView);
        dueDateTextView = itemView.findViewById(R.id.taskDueDateTextView);
    }

    /**
     * Lie les données d'une tâche aux vues correspondantes.
     *
     * @param task Instance de la tâche à afficher.
     */
    public void bind(Task task) {
        titleTextView.setText(task.getTitle()); // Affiche le titre
        statusTextView.setText(task.getStatus()); // Affiche le statut
        // Convertir la date en chaîne
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(task.getDueDate());
        dueDateTextView.setText(formattedDate);// Affiche la date d'échéance
    }
}
