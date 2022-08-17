package com.cardosofgui.valorantcharacters.implementation

import Agent
import com.cardosofgui.valorantcharacters.domain.EndPoint

class AgentImplementation(private val valorantServiceAPI : EndPoint) {

    fun getAllAgent(): Agent? {
        val callback = valorantServiceAPI.getAllAgents().execute()

        return if (callback.isSuccessful) callback.body()
        else null
    }

}