package ma.saifdine.hd.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ma.saifdine.hd.R;
import ma.saifdine.hd.domaine.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task); // Pour modifier une tâche
        void onTaskLongClick(Task task); // Pour supprimer une tâche
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.statusTextView.setText(task.getStatus());
        holder.dueDateTextView.setText(task.getDueDate());

        // Gestion des clics sur les tâches
        holder.itemView.setOnClickListener(v -> listener.onTaskClick(task));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onTaskLongClick(task);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, statusTextView, dueDateTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitleTextView);
            statusTextView = itemView.findViewById(R.id.taskStatusTextView);
            dueDateTextView = itemView.findViewById(R.id.taskDueDateTextView);
        }
    }
}
