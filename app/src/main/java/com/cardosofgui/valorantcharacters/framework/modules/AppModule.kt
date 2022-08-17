package com.cardosofgui.valorantcharacters.framework.modules

import com.cardosofgui.valorantcharacters.data.AgentRepository
import com.cardosofgui.valorantcharacters.domain.EndPoint
import com.cardosofgui.valorantcharacters.framework.viewmodel.AgentViewModel
import com.cardosofgui.valorantcharacters.implementation.AgentImplementation
import com.cardosofgui.valorantcharacters.usecase.GetAgentsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val BASE_URL = "https://valorant-api.com/v1/"

    fun load() {
        loadKoinModules(viewModelModule() + postsModule() + netWorkModule())
    }

    private fun viewModelModule() = module {
        viewModel { AgentViewModel(get()) }
    }

    private fun postsModule() : Module {
        return  module {
            single<AgentRepository> { AgentRepository(get()) }

            single<AgentImplementation> { AgentImplementation(get()) }

            single<GetAgentsUseCase> { GetAgentsUseCase(get()) }
        }
    }

    private fun netWorkModule() : Module {
        return module {
            single { createService() }
        }
    }

    private fun createService() : EndPoint {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndPoint::class.java)
    }
}

