package tecnocard.com.chiringuito;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.icu.lang.UScript;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserViewModel extends AndroidViewModel{

    private ChiringuitoRepository mRepository;
    private LiveData<List<Usuario>> mAllUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ChiringuitoRepository(application);
        mAllUsers = mRepository.getmAllUsers();
    }

    public LiveData<List<Usuario>> getAllUsers() { return mAllUsers; }

    public void insert(Usuario usuario) {
        mRepository.insert(usuario);
    }

    public void delete(Usuario usuario){
        mRepository.delete(usuario);
    }

    public void edit(Usuario usuario) {
        mRepository.update(usuario);
    }

    public Usuario get(Integer id) throws ExecutionException, InterruptedException {
        return mRepository.get(id);
    }

}
