package ui.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.response.ListEventsItem
import data.response.Responsedicoding
import data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> get() = _searchResults

    // Fungsi untuk memanggil API finished dan upcoming events
    fun fetchFinishedEvents() {
        val apiService = ApiConfig.getApiService()

        // Panggil API dengan parameter active = 0 untuk finished events
        apiService.getEvents(0).enqueue(object : Callback<Responsedicoding> {
            override fun onResponse(call: Call<Responsedicoding>, response: Response<Responsedicoding>) {
                if (response.isSuccessful) {
                    val finished = response.body()?.listEvents?.take(5)  // Ambil maksimal 5 event
                    _finishedEvents.postValue(finished ?: emptyList())
                } else {
                    _finishedEvents.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<Responsedicoding>, t: Throwable) {
                _finishedEvents.postValue(emptyList())
            }
        })
    }

    fun fetchUpcomingEvents() {
        val apiService = ApiConfig.getApiService()

        // Panggil API dengan parameter active = 1 untuk upcoming events
        apiService.getEvents(1).enqueue(object : Callback<Responsedicoding> {
            override fun onResponse(call: Call<Responsedicoding>, response: Response<Responsedicoding>) {
                if (response.isSuccessful) {
                    val upcoming = response.body()?.listEvents?.take(5)  // Ambil maksimal 5 event
                    _upcomingEvents.postValue(upcoming ?: emptyList())
                } else {
                    _upcomingEvents.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<Responsedicoding>, t: Throwable) {
                _upcomingEvents.postValue(emptyList())
            }
        })
    }

    fun searchEvents(query: String) {
        val apiService = ApiConfig.getApiService()

        // Panggil API dengan query pencarian
        apiService.searchEvents(query).enqueue(object : Callback<Responsedicoding> {
            override fun onResponse(call: Call<Responsedicoding>, response: Response<Responsedicoding>) {
                if (response.isSuccessful) {
                    val searchResults = response.body()?.listEvents ?: emptyList()
                    _searchResults.postValue(searchResults)
                } else {
                    _searchResults.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<Responsedicoding>, t: Throwable) {
                _searchResults.postValue(emptyList())
            }
        })
    }

}