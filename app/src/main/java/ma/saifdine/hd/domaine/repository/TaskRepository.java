package ma.saifdine.hd.domaine.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.saifdine.hd.data.local.TaskDao;
import ma.saifdine.hd.data.local.TaskDatabase;
import ma.saifdine.hd.domaine.model.Task;

public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> alltasks;

    public ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskRepository(Application application){
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        alltasks = taskDao.getAllTasks();
    }

    public void insert(Task task){
        executorService.execute(()-> taskDao.insert(task));
    }

    public void update(Task task){
        executorService.execute(()-> taskDao.update(task));
    }

    public void delete(Task task){
        executorService.execute(()-> taskDao.delete(task));
    }

    public LiveData<List<Task>> getAlltasks(){
        return alltasks;
    }

    public LiveData<List<Task>> getTasksByStatus(String status){
        return taskDao.getTasksByStatus(status);
    }



}
