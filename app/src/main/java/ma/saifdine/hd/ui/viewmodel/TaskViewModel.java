package ma.saifdine.hd.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.saifdine.hd.domaine.model.Task;
import ma.saifdine.hd.domaine.repository.TaskRepository;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAlltasks();
    }

    public void insert(Task task){
        taskRepository.insert(task);
    }

    public void update(Task task){
        taskRepository.update(task);
    }

    public void delete(Task task){
        taskRepository.delete(task);
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public LiveData<List<Task>> getTasksByStatus(String status){
        return taskRepository.getTasksByStatus(status);
    }
}
