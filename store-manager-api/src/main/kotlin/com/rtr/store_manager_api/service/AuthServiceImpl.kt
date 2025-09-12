package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.UserSession
import com.rtr.store_manager_api.dto.*
import com.rtr.store_manager_api.exception.ResourceNotFoundException
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.repository.UserSessionRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val sessionRepository: UserSessionRepository,
    private val passwordEncoder: PasswordEncoder,
): AuthService {

    override fun login(dto: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByEmail(dto.email)
            ?: throw ResourceNotFoundException("Usuário não encontrado para email: ${dto.email}")

        if (!passwordEncoder.matches(dto.password, user.password)) {
            throw IllegalArgumentException("Credenciais inválidas")
        }

        sessionRepository.findByUserIdAndActive(user.id, true).ifPresent {
            it.active = false
            it.endedAt = LocalDateTime.now()
            sessionRepository.save(it)
        }

        val session = sessionRepository.save(UserSession(user = user))

        return LoginResponseDTO(
            sessionId = session.id,
            userId = user.id.toString(),
            active = session.active
        )
    }

    override fun logout(sessionId: String): LogoutResponseDTO {
        val session = sessionRepository.findById(sessionId)
            .orElseThrow { ResourceNotFoundException("Sessão não encontrada: $sessionId") }

        session.active = false
        session.endedAt = LocalDateTime.now()
        sessionRepository.save(session)

        return LogoutResponseDTO(message = "Logout realizado com sucesso")
    }
}
