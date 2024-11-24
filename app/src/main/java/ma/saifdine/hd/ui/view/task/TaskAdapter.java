package ma.saifdine.hd.ui.view.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.Task;
import ma.saifdine.hd.ui.view.task.listenner.OnTaskClickListener;

/**
 * TaskAdapter est un adapter pour afficher une liste de tâches dans un RecyclerView.
 *
 * Objectifs :
 * - Afficher dynamiquement des tâches avec leurs détails dans une interface utilisateur.
 * - Permettre une interaction utilisateur sur chaque tâche (clic court et clic long).
 *
 * Fonctionnalités clés :
 * - Gère une liste de tâches.
 * - Fournit un mécanisme de clic pour modifier ou supprimer une tâche.
 * - Sépare les responsabilités pour une architecture modulaire et réutilisable.
 *
 * @author
 * Saif Dine Hassidou
 * @version 1.0
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    // Liste des tâches à afficher
    private List<Task> tasks;

    // Listener pour gérer les interactions utilisateur (clics courts/longs)
    private final OnTaskClickListener listener;

    /**
     * Constructeur pour initialiser l'adapter.
     *
     * @param tasks    Liste des tâches.
     * @param listener Interface pour gérer les événements de clic sur les tâches.
     */
    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    /**
     * Méthode appelée lors de la création d'un ViewHolder.
     *
     * @param parent   Vue parent dans laquelle les items seront insérés.
     * @param viewType Type de la vue (utilisé pour les vues personnalisées).
     * @return TaskViewHolder Instance du ViewHolder pour gérer une tâche.
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout XML pour l'item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    /**
     * Méthode appelée pour lier les données d'une tâche au ViewHolder.
     *
     * @param holder   Instance de TaskViewHolder.
     * @param position Position de l'élément dans la liste des tâches.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Récupère la tâche correspondant à la position actuelle
        Task task = tasks.get(position);

        // Lie les données de la tâche au ViewHolder
        holder.bind(task);

        // Définit les listeners pour gérer les interactions utilisateur
        holder.itemView.setOnClickListener(v -> listener.onTaskClick(task)); // Clic court
        holder.itemView.setOnLongClickListener(v -> {
            listener.onTaskLongClick(task); // Clic long
            return true; // Indique que l'événement a été consommé
        });
    }

    /**
     * Retourne le nombre total d'éléments dans la liste.
     *
     * @return Taille de la liste des tâches.
     */
    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    /**
     * Met à jour la liste des tâches et notifie RecyclerView pour rafraîchir l'affichage.
     *
     * @param tasks Nouvelle liste des tâches.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged(); // Rafraîchit l'ensemble des données affichées
    }
}
