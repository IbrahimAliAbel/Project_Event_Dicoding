package ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.retrofit.ApiConfig
import data.response.EventDetailResponse
import data.response.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _eventDetail = MutableLiveData<ListEventsItem?>()
    val eventDetail: LiveData<ListEventsItem?> get() = _eventDetail

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getEventDetail(eventId: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getDetailEvent(eventId).enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(call: Call<EventDetailResponse>, response: Response<EventDetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {

                    Log.d("DetailEventViewModel", "Response: ${response.body()}")

                    _eventDetail.value = response.body()?.event
                    _error.value = null
                } else {

                    Log.e("DetailEventViewModel", "Response not successful: ${response.errorBody()?.string()}")
                    _error.value = "Gagal memuat data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailEventViewModel", "Failure: ${t.message}")
                _error.value = "Kesalahan jaringan: ${t.message}"
            }
        })
    }
}