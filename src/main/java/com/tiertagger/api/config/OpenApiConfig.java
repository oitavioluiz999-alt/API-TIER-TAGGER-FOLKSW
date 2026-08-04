package com.tiertagger.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    public static final String SCHEME_JWT = "bearer-jwt";
    public static final String SCHEME_API_KEY = "api-key";

    @Bean
    public OpenAPI tierTaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TierTagger API")
                        .version("1.0.0")
                        .description("API do TierTagger - Sistema de Tiers para Minecraft (Crystal, Sword, Mace...).\n"
                                + "Autenticação via JWT Bearer para rotas administrativas.\n"
                                + "Leitura pública via API Key ou rota aberta.")
                        .contact(new Contact().name("TierTagger").url("https://railway.app/")))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_JWT).addList(SCHEME_API_KEY))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_JWT,
                                new SecurityScheme()
                                        .name(SCHEME_JWT)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addSecuritySchemes(SCHEME_API_KEY,
                                new SecurityScheme()
                                        .name("X-API-Key")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)));
    }
}

