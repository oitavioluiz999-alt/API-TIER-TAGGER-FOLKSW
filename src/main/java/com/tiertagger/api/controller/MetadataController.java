package com.tiertagger.api.controller;

import com.tiertagger.api.dto.ModeResponse;
import com.tiertagger.api.dto.TierResponse;
import com.tiertagger.api.service.MetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints públicos de metadados (modalidades e tiers).
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Metadata", description = "Modalidades e tiers disponíveis")
public class MetadataController {

    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/modes")
    @Operation(summary = "Lista todas as modalidades")
    public List<ModeResponse> modes() {
        return metadataService.getModes();
    }

    @GetMapping("/tiers")
    @Operation(summary = "Lista todos os tiers")
    public List<TierResponse> tiers() {
        return metadataService.getTiers();
    }
}
