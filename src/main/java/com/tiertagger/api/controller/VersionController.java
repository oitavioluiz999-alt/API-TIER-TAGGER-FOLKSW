package com.tiertagger.api.controller;

import com.tiertagger.api.config.AppProperties;
import com.tiertagger.api.dto.VersionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint público usado pelo mod para verificar atualizações.
 */
@RestController
@RequestMapping("/api/version")
@Tag(name = "Version", description = "Versão da API e do client")
public class VersionController {

    private final AppProperties properties;

    public VersionController(AppProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    @Operation(summary = "Retorna a versão da API e a versão mais recente do client")
    public VersionResponse version() {
        return new VersionResponse(
                properties.getAppVersion(),
                properties.getClientLatestVersion(),
                properties.getClientDownloadUrl(),
                false);
    }
}
