package com.example.frontend.core

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.frontend.data.api.MeetMyBarAPI
import com.example.frontend.data.repository.DrinkRepository
import com.example.frontend.data.repository.PreferencesRepository
import com.example.frontend.data.repository.bar.BarRepository
import com.example.frontend.domain.repository.BarRepositoryInterface
import com.example.frontend.domain.repository.DrinkRepositoryInterface
import com.example.frontend.domain.repository.PreferencesRepositoryInterface
import com.example.frontend.presentation.feature.addbar.AddBarViewModel
import com.example.frontend.presentation.feature.bar.BarScreenViewModel
import com.example.frontend.presentation.feature.biere.ListBiereViewModel
import com.example.frontend.presentation.feature.home.HomeViewModel
import com.example.frontend.presentation.feature.settings.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module
import org.koin.dsl.single

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

val appModule = module {
    single { Android.create() }
    single { createJson() }
    single { createHttpClient(get(), get(), enableNetworkLogs = true) }

    // API
    single { MeetMyBarAPI(get()) }

    // Data Store
    single<DataStore<Preferences>> {
        get<Context>().dataStore
    }

    // Repository
    single<DrinkRepositoryInterface> { DrinkRepository(get()) }
    single<BarRepositoryInterface> { BarRepository(get()) }
    single<PreferencesRepositoryInterface> { PreferencesRepository(get()) }

    // View Model
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ListBiereViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { AddBarViewModel(get()) }
    viewModel { BarScreenViewModel(get()) }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

fun createHttpClient(httpClientEngine: HttpClientEngine, json: Json, enableNetworkLogs: Boolean) =
    HttpClient(httpClientEngine) {
        install(ContentNegotiation) {
            json(json)
        }
        if (enableNetworkLogs) {
            install(Logging) {
                logger = CustomHttpLogger()
                level = LogLevel.ALL
            }
        }
    }

class CustomHttpLogger() : Logger {
    override fun log(message: String) {
        Log.i("CustomHttpLogger", "message : $message")
    }
}
