package com.cardosofgui.valorantcharacters.framework.viewmodel

import Agent
import Datum
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cardosofgui.valorantcharacters.data.AgentRepository
import java.lang.Exception

class AgentViewModel(private val agentRepository: AgentRepository) : ViewModel() {

    private var _agent = mutableStateListOf<Datum>()
    val agent : List<Datum?>
        get() = _agent

    var agentStatusRequest = MutableLiveData<Boolean?>()

    fun getAllAgents() {
        Thread{
            try {
                Log.e("Deu bom fi", "Era pra ter ido")
                agentRepository.getAllAgents()?.data?.let { _agent.addAll(it) }
                agentStatusRequest.postValue(true)
            } catch (e : Exception) {
                Log.e("Deu ruim fi", "Olha o erro aqui $e")
                agentStatusRequest.postValue(false)
            }
        }.start()
    }
}