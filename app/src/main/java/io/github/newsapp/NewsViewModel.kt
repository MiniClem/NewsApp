package io.github.newsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.newsapp.model.News
import io.github.newsapp.repository.NewsRemoteDataSource
import io.github.newsapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(private val newsRemoteDataSource: NewsRemoteDataSource) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<News>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<News>>>
        get() = _dataState

    fun getNews(): LiveData<DataState<List<News>>> {
        viewModelScope.launch {
            newsRemoteDataSource.getNews()
                .onEach { dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
        return dataState
    }
}