package pe.edu.upc.oncontrol.profile.interfaces.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.services.doctor.DoctorProfileCommandService;
import pe.edu.upc.oncontrol.profile.domain.services.doctor.DoctorProfileQueryService;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor.DoctorProfileCreateResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor.DoctorProfileUpdateResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor.DoctorProfileViewResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.doctor.CreateDoctorProfileCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.doctor.DoctorProfileToResourceAssembler;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.doctor.UpdateDoctorProfileCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
public class DoctorProfileController {

    private final DoctorProfileCommandService doctorProfileService;
    private final DoctorProfileQueryService doctorProfileQueryService;
    private final TokenContextFacade tokenContextFacade;

    public DoctorProfileController(
            DoctorProfileCommandService doctorProfileService,
            DoctorProfileQueryService doctorProfileQueryService,
            TokenContextFacade tokenContextFacade) {
        this.doctorProfileService = doctorProfileService;
        this.doctorProfileQueryService = doctorProfileQueryService;
        this.tokenContextFacade = tokenContextFacade;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorProfileViewResource> create(@Valid @RequestBody DoctorProfileCreateResource resource, HttpServletRequest request) {
        CreateDoctorProfileCommand command = CreateDoctorProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        DoctorProfile created = doctorProfileService.createProfile(command, request);
        DoctorProfileViewResource view = DoctorProfileToResourceAssembler.toResourceFromEntity(created);
        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DoctorProfileViewResource> getByUuid(@PathVariable UUID uuid) {
        return doctorProfileQueryService.findByUuid(uuid)
                .map(profile -> ResponseEntity.ok(DoctorProfileToResourceAssembler.toResourceFromEntity(profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorProfileViewResource> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody DoctorProfileUpdateResource resource
    ) {
        UpdateDoctorProfileCommand command = UpdateDoctorProfileCommandFromResourceAssembler.toCommandFromResource(uuid, resource);
        doctorProfileService.updateProfile(command);
        return doctorProfileQueryService.findByUuid(uuid)
                .map(updated -> ResponseEntity.ok(DoctorProfileToResourceAssembler.toResourceFromEntity(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{uuid}/deactivate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable UUID uuid) {
        doctorProfileService.deactivateProfile(new DeactivateDoctorProfileCommand(uuid));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/uuid")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, UUID>> getMyUuid(HttpServletRequest request) {
        Long userId = tokenContextFacade.extractUserIdFromToken(request);

        return doctorProfileQueryService.findByUserId(userId)
                .map(profile -> ResponseEntity.ok(Map.of("uuid", profile.getUuid())))
                .orElse(ResponseEntity.notFound().build());
    }


}
