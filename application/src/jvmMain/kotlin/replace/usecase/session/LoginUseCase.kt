package replace.usecase.session

import replace.usecase.session.dto.LoginRequest

object LoginUseCase {
    suspend fun execute(
        loginRequest: LoginRequest,
    ): UserSessionDto {
        val userSession = sessionRepository.login(loginDto)

        return userSession.toDto()
    }
}
