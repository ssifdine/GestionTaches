package ma.saifdine.hd.ui.view.activity.task.listenner;

import ma.saifdine.hd.domaine.model.Task;

/**
 * Interface pour gérer les interactions utilisateur avec les éléments de la liste de tâches.
 *
 * Objectifs :
 * - Séparer la logique d'interaction utilisateur de l'Adapter.
 * - Permettre au développeur d'implémenter des actions spécifiques sur les clics.
 *
 * Méthodes :
 * - onTaskClick : Appelé lors d'un clic court (exemple : modifier une tâche).
 * - onTaskLongClick : Appelé lors d'un clic long (exemple : supprimer une tâche).
 */
public interface OnTaskClickListener {
    /**
     * Méthode déclenchée lors d'un clic court sur une tâche.
     *
     * @param task Tâche cliquée.
     */
    void onTaskClick(Task task);

    /**
     * Méthode déclenchée lors d'un clic long sur une tâche.
     *
     * @param task Tâche cliquée.
     */
    void onTaskLongClick(Task task);
}
