package com.teyyihan.e2ee_chat

import org.springframework.context.annotation.Bean
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler
import reactor.core.publisher.Mono
import java.util.stream.Collectors


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
                .csrf().disable().formLogin().disable().httpBasic().disable()
                .authorizeExchange()
                .pathMatchers("/resource/**").hasAuthority("user")
                .pathMatchers("/auth/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(HttpStatusServerAccessDeniedHandler(HttpStatus.UNAUTHORIZED))
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
        return http.build()
    }

    @Bean
    fun grantedAuthoritiesExtractor(): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
        val extractor = GrantedAuthoritiesExtractor()
        return ReactiveJwtAuthenticationConverterAdapter(extractor)
    }

    internal class GrantedAuthoritiesExtractor : JwtAuthenticationConverter() {
        override fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
            val resource = jwt.getClaimAsMap("realm_access")
            val roles = resource["roles"] as List<String>
            return roles.stream().map { role: String? -> SimpleGrantedAuthority(role) }.collect(Collectors.toList())
        }
    }

}