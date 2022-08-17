package com.cardosofgui.valorantcharacters.framework.viewmodel

import Agent
import Datum
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cardosofgui.valorantcharacters.data.AgentRepository
import com.cardosofgui.valorantcharacters.usecase.GetAgentsUseCase
import java.lang.Exception

class AgentViewModel(private val getAgentsUseCase: GetAgentsUseCase) : ViewModel() {

    private var _agent = mutableStateListOf<Datum>()
    val agent : List<Datum?>
        get() = _agent

    var agentStatusRequest = MutableLiveData<Boolean?>()

    fun getAllAgents() {
        Thread{
            try {
                getAgentsUseCase.invoke()?.data?.let { _agent.addAll(it) }
                agentStatusRequest.postValue(true)
            } catch (e : Exception) {
                Log.e("REQUEST AGENTS", "$e")
                agentStatusRequest.postValue(false)
            }
        }.start()
    }
}