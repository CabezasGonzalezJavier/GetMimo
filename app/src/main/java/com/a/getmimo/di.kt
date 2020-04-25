package com.a.getmimo


import android.app.Application
import com.a.getmimo.data.Repository
import com.a.getmimo.data.RepositoryInterface
import com.a.getmimo.data.source.local.LocalDataSource
import com.a.getmimo.data.source.local.MimoDatabase
import com.a.getmimo.data.source.local.RoomDataSource
import com.a.getmimo.data.source.remote.MimoDB
import com.a.getmimo.data.source.remote.MimoDataSource
import com.a.getmimo.data.source.remote.RemoteDataSource
import com.a.getmimo.domain.entity.networking.ResponseHandler
import com.a.getmimo.domain.usecases.GetLessons
import com.a.getmimo.domain.usecases.SaveLesson
import com.a.getmimo.ui.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, useCasesModule, scopesModule))
    }
}

private val appModule = module {
    single { MimoDatabase.getInstance(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> {
        MimoDataSource(
            get(),
            get()
        )
    }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single(named("baseUrl")) { "https://mimochallenge.azurewebsites.net/api/" }
    single { MimoDB(get(named("baseUrl"))) }
    single { ResponseHandler() }
}

val dataModule = module {
    factory<RepositoryInterface> { Repository(get(), get()) }
}

val useCasesModule = module {
    factory { GetLessons(get()) }
    factory { SaveLesson(get()) }
}

private val scopesModule = module {

    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get(), get()) }
    }

    scope(named<DoneActivity>()) {
        viewModel { (idLesson: Int, startLesson: Long) ->
            DoneViewModel(
                idLesson,
                startLesson,
                get(),
                get()
            )
        }
    }
}