package pe.edu.upc.oncontrol.profile.interfaces.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.CreatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.DeactivatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.UpdatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.services.patient.PatientProfileCommandService;
import pe.edu.upc.oncontrol.profile.domain.services.patient.PatientProfileQueryService;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient.PatientProfileCreateResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient.PatientProfileUpdateResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient.PatientProfileViewResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.patient.CreatePatientProfileCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.patient.PatientProfileToResourceAssembler;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.patient.UpdatePatientProfileCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/patients", produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientProfileController {

    private final PatientProfileCommandService patientProfileCommandService;
    private final PatientProfileQueryService patientProfileQueryService;
    private final TokenContextFacade tokenContextFacade;

    public PatientProfileController(
            PatientProfileCommandService patientProfileCommandService,
            PatientProfileQueryService patientProfileQueryService,
            TokenContextFacade tokenContextFacade) {
        this.patientProfileCommandService = patientProfileCommandService;
        this.patientProfileQueryService = patientProfileQueryService;
        this.tokenContextFacade = tokenContextFacade;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<PatientProfileViewResource> create(@Valid @RequestBody PatientProfileCreateResource resource, HttpServletRequest request) {
        CreatePatientProfileCommand command = CreatePatientProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        PatientProfile created = patientProfileCommandService.createProfile(command, request);
        PatientProfileViewResource view = PatientProfileToResourceAssembler.toResourceFromEntity(created);
        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<PatientProfileViewResource> getByUuid(@PathVariable UUID uuid) {
        return patientProfileQueryService.findByUuid(uuid)
                .map(profile -> ResponseEntity.ok(PatientProfileToResourceAssembler.toResourceFromEntity(profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<PatientProfileViewResource> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody PatientProfileUpdateResource resource
    ) {
        UpdatePatientProfileCommand command = UpdatePatientProfileCommandFromResourceAssembler.toCommandFromResource(uuid, resource);
        patientProfileCommandService.updateProfile(command);
        return patientProfileQueryService.findByUuid(uuid)
                .map(updated -> ResponseEntity.ok(PatientProfileToResourceAssembler.toResourceFromEntity(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{uuid}/deactivate")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> deactivate(@PathVariable UUID uuid) {
        patientProfileCommandService.deactivateProfile(new DeactivatePatientProfileCommand(uuid));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/uuid")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Map<String, UUID>> getMyUuid(HttpServletRequest request) {
        Long userId = tokenContextFacade.extractUserIdFromToken(request);

        return patientProfileQueryService.findByUserId(userId)
                .map(profile -> ResponseEntity.ok(Map.of("uuid", profile.getUuid())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PatientProfileViewResource>> searchPatients(@RequestParam String name) {
        List<PatientProfile> results = patientProfileQueryService.searchByFullName(name);
        List<PatientProfileViewResource> view = results.stream()
                .map(PatientProfileToResourceAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(view);
    }


}
