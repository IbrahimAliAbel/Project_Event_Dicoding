package ui.ui.dashboard

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

class UpcomingViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _events = MutableLiveData<List<ListEventsItem>>(emptyList())
    val events: LiveData<List<ListEventsItem>?> = _events

    fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(active = 1)
        client.enqueue(object : Callback<Responsedicoding> {
            override fun onResponse(call: Call<Responsedicoding>, response: Response<Responsedicoding>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _events.value = response.body()?.listEvents ?: emptyList()
                    Log.d("UpcomingViewModel", "Events: ${_events.value}")
                } else {
                    Log.e("UpcomingViewModel", "Response not successful: ${response.errorBody()?.string()}")
                    _events.value = emptyList()
                }
            }

            override fun onFailure(call: Call<Responsedicoding>, t: Throwable) {
                _isLoading.value = false
                Log.e("UpcomingViewModel", "Failure: ${t.message}")
                _events.value = emptyList()
            }
        })
    }
}