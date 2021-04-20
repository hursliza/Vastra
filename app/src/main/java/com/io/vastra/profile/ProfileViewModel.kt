package com.io.vastra.profile
import androidx.lifecycle.*
import com.io.vastra.data.datasource.UserDataSource
import com.io.vastra.data.models.Award

class ProfileViewModel(viewOwner: LifecycleOwner, userId: String): ViewModel() {
    private val userDataSource: UserDataSource = UserDataSource(userId);

    private val _userAwards: MutableLiveData<Collection<Award>> = MutableLiveData(listOf());
    val userAwards: LiveData<Collection<Award>>
    get() = _userAwards;

    init {
        userDataSource.currentUser.observe(viewOwner) {
            val userAwards = it.userAwards.values.map {
                it.description?.let { it1 -> Award(it1) }
            }.filterNotNull();
            _userAwards.postValue(userAwards);
        }
    }
}

class ProfileViewModelFactory(private val context: LifecycleOwner, private val userId: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(context, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}