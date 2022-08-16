package com.cardosofgui.valorantcharacters.data

import com.cardosofgui.valorantcharacters.implementation.AgentImplementation

class AgentRepository(private val agentImplementation: AgentImplementation) {

    fun getAllAgents() = agentImplementation.getAllAgent()
}