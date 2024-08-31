package com.example.flightsearchapp.screen


import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import com.example.flightsearchapp.FlightSearchApplication
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.data.AirportDao
import com.example.flightsearchapp.data.FavoriteDao
import com.example.flightsearchapp.data.ImplementRepository
import kotlinx.coroutines.flow.Flow
import com.example.flightsearchapp.data.Repository
import com.example.flightsearchapp.data.airport
import com.example.flightsearchapp.data.favorite
import kotlinx.coroutines.flow.toList

class FlightSearchViewModel(
   private val repository:Repository
) : ViewModel() {

    private var _uistate = mutableStateOf(UiState())
    val uiState: State<UiState> = _uistate

    var favoriteUiState by  mutableStateOf(UiState())
     fun getList(nameORIATA:String):Flow<List<airport>> =  repository.getAutoCompleteList(name = nameORIATA)

    fun getShortedList(nameS:String):Flow<List<airport>> = repository.getListExceptSearched(nameS)

    fun addToFavorite(departure_code: String,destination_code: String) {
       _uistate.value = _uistate.value.copy(
           departure_code = departure_code,
           destination_code = destination_code
       )
    }
    suspend fun addFav()= repository.insertFavoritePlace(favoriteUiState.toFavorite())

    fun getFavorite():Flow<List<favorite>> = repository.getFavoriteFlights()

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
          initializer {
              val application = this[APPLICATION_KEY] as FlightSearchApplication
              FlightSearchViewModel(application.container.repository)
          }
        }
        }
    }
data class UiState(
    val id:Int=0,
    val departure_code:String="",
    val destination_code:String=""
)
fun UiState.toFavorite():favorite = favorite(
     id = id,
     departure_code = departure_code,
    destination_code = destination_code
)