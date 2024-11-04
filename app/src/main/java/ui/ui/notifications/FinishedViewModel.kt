package ui.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.response.ListEventsItem
import data.response.Responsedicoding
import data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(active = 0)
        client.enqueue(object : Callback<Responsedicoding> {
            override fun onResponse(call: Call<Responsedicoding>, response: Response<Responsedicoding>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _events.value = response.body()?.listEvents ?: emptyList()
                } else {
                    _events.value = emptyList()
                    Log.e("FinishedViewModel", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Responsedicoding>, t: Throwable) {
                _isLoading.value = false
                _events.value = emptyList()
                Log.e("FinishedViewModel", "Failure: ${t.message}")
            }
        })
    }
}