package com.io.vastra.history.historyItem

import androidx.lifecycle.*
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.entities.RunDescription

class HistoryItemViewModel(owner: LifecycleOwner, clickedIdx: Int): ViewModel()  {

    private val _runDescription: MutableLiveData<RunDescription> = MutableLiveData();

    val runDescription: LiveData<RunDescription>
    get() = _runDescription;

    init {
        UserDataSourceProvider.instance.getDataSource().currentUser.observe(owner) {
            _runDescription.value = it.runHistory?.values?.toList()?.get(clickedIdx);
        }
    }
}

class HistoryItemViewModelFactory(private val owner: LifecycleOwner, private val clickedIdx: Int = 0): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryItemViewModel::class.java)) {
            return HistoryItemViewModel(owner, clickedIdx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}