package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.response.ExpanseCategory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ExpanseCategoriesViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository): ViewModel() {
    private val repository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    val expanseCategoriesState = mutableStateOf((emptyList<ExpanseCategory>()))
    private val _accessToken = MutableLiveData("")
    val accessToken: MutableLiveData<String> = _accessToken
    var whatToSeeState = mutableStateOf("")

    init{
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION","Network exception in Expense Categories screen")
        }

        viewModelScope.launch(handler+ Dispatchers.IO){
            val expanseCategories = getExpanseCategories()
            expanseCategoriesState.value = expanseCategories
            dataStorePreferenceRepository.getAccessToken.
            catch { Log.d("ERROR","EXPECTION while getting the token in the expense categories screen") }
                .collect{
                    Log.d("TOKEN","Access token on expense categories screen: $it")
                    _accessToken.postValue(it)
                }
        }

    }
    suspend fun getExpanseCategories(): List<ExpanseCategory>{
        return repository.getExpanseCategories()._embedded.expanseCategories
    }

}