@file:Suppress("EXPERIMENTAL_API_USAGE", "UNCHECKED_CAST")

package com.smartsoft.github

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ss.ssutil.UiState


data class RPage(val count: Int = 0, val rows: List<RJob> = emptyList())

data class RJob(
    val id: Int,
    val customerName: String,
    val openDate: String
)

data class User(var id: Int, var login: String, var url: String)

interface GitHubService {
    @GET("users")
    suspend fun fetchUsers(): List<User>

    companion object {
        val dummyUsers = (1..100).map {
            User(it, "User $it", "https://user.com/image${it}")
        }
    }
}


interface JobsService {
    @GET("job/jobs.jsp")
    suspend fun fetchJobs(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("active") active: String = "true"
    ): RPage
}


object AppCtx {

    const val baseUrlGitHub = "https://api.github.com/"
    const val baseUrlR = "http://72.67.57.37:8282/api/"
//    const val baseUrlRamko = "http://72.67.57.37:8282/api/job/jobs.jsp?active=true&limit=20&offset=0&orderBy=updated+desc"

    private fun mkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun mkRetrofit(baseUrl: String): Retrofit {
        val httpClient = mkHttpClient()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    private fun mkRetrofitGitHub(): Retrofit {
        return mkRetrofit(baseUrlGitHub)
    }

    private fun mkRetrofitJobs(): Retrofit {
        return mkRetrofit(baseUrlR)
    }

    private fun mkGitHubService(): GitHubService {
        val retrofit = mkRetrofitGitHub()
        return retrofit.create(GitHubService::class.java)
    }

    private fun mkJobsService(): JobsService {
        val retrofit = mkRetrofitJobs()
        return retrofit.create(JobsService::class.java)
    }

    private fun mkUsersRepo(): UsersRepo {
        return UsersRepo(mkGitHubService())
    }

    private fun mkJobsRepo(): JobsRepo {
        return JobsRepo(mkJobsService())
    }

    private fun mkViewModel(): AppViewModel {
        return AppViewModel(mkUsersRepo(), mkJobsRepo())
    }


    fun mkViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                    return mkViewModel() as T
                }
                throw IllegalArgumentException("Unknown  class")
            }
        }
    }


}

class UsersRepo(private val service: GitHubService) {
    suspend fun fetchUsers() = service.fetchUsers()
}

class JobsRepo(private val service: JobsService) {
    suspend fun fetchJobs(offset: Int, limit: Int) = service.fetchJobs(offset, limit)
}


class AppViewModel(
    userRepo: UsersRepo,
    jobsRepo: JobsRepo
) : ViewModel() {

    val ghLive: GhLive = GhLive(userRepo, this)
    val ghFlow: GhFlow = GhFlow(userRepo, this)
    val jobFlow: JobFlow = JobFlow(jobsRepo, this)


}

class GhLive(private val repo: UsersRepo, val vm: ViewModel) {

    private val action: MutableLiveData<GhAction> = MutableLiveData(GhAction.Clear)

    fun update(a: GhAction) {
        action.postValue(a)
    }

    val users: LiveData<UiState<List<User>>> = action.switchMap {
        val a = it ?: GhAction.Clear
        liveData {
            when (a) {
                GhAction.Clear -> {
                    val list = emptyList<User>()
                    val ui = UiState.Success(list)
                    emit(ui)
                }
                GhAction.Dummy -> {
                    val ui = UiState.Success(GitHubService.dummyUsers)
                    emit(ui)
                }
                GhAction.Fetch -> {
                    try {
                        val returnedUsers: List<User> = repo.fetchUsers()
                        val ui = UiState.Success(returnedUsers)
                        emit(ui)
                    } catch (e: Exception) {
                        emit(UiState.Error(e))
                    }
                }
            }
        }
    }

}

class GhFlow(private val repo: UsersRepo, private val vm: ViewModel) {

    private val actionChannel: ConflatedBroadcastChannel<GhAction> by lazy {
        ConflatedBroadcastChannel<GhAction>().also { channel ->
//            channel.offer(GhAction.Fetch)
        }
    }

    @FlowPreview
    val actionFlow: Flow<GhAction>
        get() = actionChannel.asFlow()

    @FlowPreview
    val usersFlow: Flow<UiState<List<User>>>
        get() {
            val transform: suspend (GhAction) -> Flow<UiState<List<User>>> = { a ->
                flow {
                    when (a) {
                        GhAction.Clear -> {
                            emit(UiState.Success(emptyList<User>()))
                        }
                        GhAction.Dummy -> {
                            emit(UiState.Success(GitHubService.dummyUsers))
                        }
                        GhAction.Fetch -> {
                            try {
                                val returnedUsers: List<User> = repo.fetchUsers()
                                emit(UiState.Success(returnedUsers))
                            } catch (e: Exception) {
                                emit(UiState.Error(e))
                            }
                        }
                    }
                }
            }
            return actionFlow.flatMapLatest(transform)
        }

    val usersLiveData: LiveData<UiState<List<User>>> get() = usersFlow.asLiveData(Dispatchers.Default + vm.viewModelScope.coroutineContext)

    fun update(a: GhAction) {
        actionChannel.offer(a)
    }

}

class JobFlow(private val repo: JobsRepo, private val vm: ViewModel) {

    private val actionChannel: ConflatedBroadcastChannel<RAction> by lazy {
        ConflatedBroadcastChannel<RAction>().also { channel ->
            channel.offer(RAction.Clear)
        }
    }

    @FlowPreview
    val actionFlow: Flow<RAction>
        get() = actionChannel.asFlow()

    @FlowPreview
    val jobsFlow: Flow<UiState<RPage>>
        get() {
            val transform: suspend (RAction) -> Flow<UiState<RPage>> = { a ->
                flow {
                    when (a) {
                        is RAction.Clear -> {
                            emit(UiState.Success(RPage()))
                        }
                        is RAction.Query -> {
                            try {
                                val page: RPage = repo.fetchJobs(offset = a.offset, limit = a.limit)
                                emit(UiState.Success(page))
                            } catch (e: Exception) {
                                emit(UiState.Error(e))
                            }
                        }
                    }
                }
            }
            return actionFlow.flatMapLatest(transform)
        }

    val jobsLiveData: LiveData<UiState<RPage>> get() = jobsFlow.asLiveData(Dispatchers.Default + vm.viewModelScope.coroutineContext)


    fun update(a: RAction) {
        actionChannel.offer(a)
    }


}