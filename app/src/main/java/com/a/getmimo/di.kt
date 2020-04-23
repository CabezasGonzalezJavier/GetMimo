package com.a.getmimo

import android.app.Application
import com.a.getmimo.data.Repository
import com.a.getmimo.data.RepositoryInterface
import com.a.getmimo.data.source.*
import com.a.getmimo.domain.entity.networking.ResponseHandler
import com.a.getmimo.domain.usecases.GetLessons
import com.a.getmimo.ui.MainActivity
import com.a.getmimo.ui.MainViewModel
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
    factory<LocalDataSource> { RoomDataSource() }
    factory<RemoteDataSource> { MimoDataSource(get(), get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single(named("baseUrl")) { "https://mimochallenge.azurewebsites.net/api/lessons" }
    single { MimoDB(get(named("baseUrl"))) }
    single { ResponseHandler() }
}

val dataModule = module {
    factory<RepositoryInterface> { Repository(get(), get()) }
}

val useCasesModule = module {
    factory { GetLessons(get()) }
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get(), get()) }
    }
}