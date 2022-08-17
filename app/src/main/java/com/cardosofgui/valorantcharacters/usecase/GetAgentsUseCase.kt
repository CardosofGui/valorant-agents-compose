package com.cardosofgui.valorantcharacters.usecase

import com.cardosofgui.valorantcharacters.data.AgentRepository

class GetAgentsUseCase(
    private val repository : AgentRepository
) {

    operator fun invoke() = repository.getAllAgents()

}