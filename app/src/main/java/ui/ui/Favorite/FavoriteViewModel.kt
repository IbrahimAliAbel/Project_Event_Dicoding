package ui.ui.Favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import database.AppDatabase
import database.FavoriteEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel (application: Application) : AndroidViewModel(application) {

    private val favoriteEventDao = AppDatabase.getDatabase(application).favoriteEventDao()
    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addFavorite(event: FavoriteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteEventDao.insert(event)
        }
    }

    fun removeFavorite(event: FavoriteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteEventDao.delete(event)
        }
    }

    fun fetchFavorites() {
        _isLoading.value = true
        // Ambil data favorit dan atur isLoading menjadi false setelah selesai
        viewModelScope.launch {
            // Logika pengambilan data favorit (jika perlu)
            _isLoading.value = false
        }
    }
}