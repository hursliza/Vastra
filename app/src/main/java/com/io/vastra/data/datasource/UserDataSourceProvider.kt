package com.io.vastra.data.datasource


/**
 * Provides dataSource for user with passed userId.
 * In case if userId is not passed, returns last requested dataSource or throws
 * an exception in case if dataSource is not exists.
 *
 * In case if userId differs from last requested dataSource - returns dataSource for user with new id.
 * */
class UserDataSourceProvider(userId: String) {
    companion object {
        @Volatile
        private var INSTANCE: UserDataSourceProvider? = null;

        val instance: UserDataSourceProvider
        get() = INSTANCE ?: throw InstantiationError("Cannot return an instance for datasource that was not instantiated");

        fun getInstance(requestedUserId: String?): UserDataSourceProvider {
            if (requestedUserId == null) {
                return instance;
            }


            return INSTANCE?.also {
                if(it.getDataSource().userId !== requestedUserId)
                return instantiate(requestedUserId);
            } ?: instantiate(requestedUserId);
        }

        fun instantiate(userId: String): UserDataSourceProvider
                = synchronized(this) {
            UserDataSourceProvider(userId).also {
                INSTANCE = it;
            }
        }
    }

    private var dataSource: UserDataSource = UserDataSource(userId);

    fun getDataSource(): UserDataSource = dataSource;

}