package com.smartsoft.github

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class User(var id: Int, var login: String, var url: String)

interface GitHubService {
    @GET("users")
    fun fetchUsers(): Call<List<User>>
}

class GitHub(private val gitHubService: GitHubService) {

    fun fetchUsersAsync(onSuccess: (users: List<User>) -> Unit) {

        val cb = object : Callback<List<User>?> {
            override fun onFailure(call: Call<List<User>?>?, t: Throwable?) {
                t?.printStackTrace() ?: print("fetchUsersRemote2")
            }

            override fun onResponse(call: Call<List<User>?>?, response: Response<List<User>?>?) {
                val users = response!!.body()!!
                onSuccess(users)
            }
        }

        return gitHubService.fetchUsers().enqueue(cb)
    }

    //throws an exception in modern android OSs
    fun fetchUsersSync(): List<User> {
        println("fetchUsersSync 1")
        try {
            val call: Call<List<User>> = gitHubService.fetchUsers()
            println("fetchUsersSync 2")
            val response: Response<List<User>> = call.execute()
            println("fetchUsersSync 3")
            return response.body() ?: throw IllegalStateException()
        } catch (e: Exception) {
            println("fetchUsersSync ex")
            e.printStackTrace()
            throw e
        }
    }

    companion object {
        val dummyUsers = (1..100).map {
            User(it, "User $it", "https://user.com/image${it}")
        }
    }

}

fun <T> MutableLiveData<T>.observe(owner: LifecycleOwner, observer: (value: T) -> Unit) {
    observe(owner, Observer<T> {
        observer(it)
    })
}

class GhViewModel(private val gitHub: GitHub) : ViewModel() {

    val users: MutableLiveData<List<User>> = MutableLiveData()

    fun observe(owner: LifecycleOwner, listener: (value: List<User>) -> Unit) {
        users.observe(owner, listener)
    }


}

class AppCtx {

    fun mkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun mkGitHubService(): GitHubService {
        val rr = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(mkHttpClient())
            .build()
        return rr.create(GitHubService::class.java)
    }

    fun mkGitHub(): GitHub {
        return GitHub(mkGitHubService())
    }

    fun mkGhViewModel(): GhViewModel {
        return GhViewModel(mkGitHub())
    }

}