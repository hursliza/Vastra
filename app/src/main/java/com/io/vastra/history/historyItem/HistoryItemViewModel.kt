package com.io.vastra.history.historyItem

import androidx.lifecycle.*
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.entities.RunDescription
import java.security.InvalidKeyException

class HistoryItemViewModel(owner: LifecycleOwner? = null, val openedIdx: Int = 0): ViewModel()  {

    private var runHistory: List<RunDescription>? = null;
    private val _runDescription: MutableLiveData<RunDescription> = MutableLiveData();
    val runDescription: LiveData<RunDescription>
    get() = _runDescription;

    init {
        if (owner != null) {


        UserDataSourceProvider.instance.getDataSource().currentUser.observe(owner) {
            it.runHistory?.let {
                history ->
                runHistory = history.values.toList();
                _runDescription.postValue(runHistory!![openedIdx]);
                }
            }
        }
    }
}

class HistoryItemViewModelFactory(private val owner: LifecycleOwner, val openedIdx: Int = 0): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryItemViewModel::class.java)) {
            return HistoryItemViewModel(owner, openedIdx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}