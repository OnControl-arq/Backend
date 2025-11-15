package pe.edu.upc.oncontrol.profile.interfaces.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.commands.link.*;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.profile.domain.services.link.DoctorPatientLinkCommandService;
import pe.edu.upc.oncontrol.profile.domain.services.link.DoctorPatientLinkQueryService;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link.CreateLinkResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link.DoctorPatientLinkViewResource;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.link.CreateLinkCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.profile.interfaces.rest.transform.link.DoctorPatientLinkToResourceAssembler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/doctor-patient-links", produces = MediaType.APPLICATION_JSON_VALUE)
public class DoctorPatientLinkController {

    private final DoctorPatientLinkCommandService linkCommandService;
    private final DoctorPatientLinkQueryService linkQueryService;

    public DoctorPatientLinkController(
            DoctorPatientLinkCommandService linkCommandService,
            DoctorPatientLinkQueryService linkQueryService
    ) {
        this.linkCommandService = linkCommandService;
        this.linkQueryService = linkQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorPatientLinkViewResource> create(@Valid @RequestBody CreateLinkResource resource) {
        CreateLinkCommand command = CreateLinkCommandFromResourceAssembler.toCommandFromResource(resource);
        linkCommandService.createLink(command);

        Optional<DoctorPatientLink> linkOpt = linkQueryService.findByDoctorUuidAndPatientUuid(
                resource.doctorUuid(), resource.patientUuid());

        return linkOpt.map(link ->
                new ResponseEntity<>(DoctorPatientLinkToResourceAssembler.toResourceFromEntity(link), HttpStatus.CREATED)
        ).orElse(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @GetMapping("/{externalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DoctorPatientLinkViewResource> getByExternalId(@PathVariable UUID externalId) {
        return linkQueryService.findByExternalId(externalId)
                .map(link -> ResponseEntity.ok(DoctorPatientLinkToResourceAssembler.toResourceFromEntity(link)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doctor/{doctorUuid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<DoctorPatientLinkViewResource>> getByDoctorAndStatus(
            @PathVariable UUID doctorUuid,
            @RequestParam(name = "status", required = false, defaultValue = "ACTIVE") LinkStatus status) {

        List<DoctorPatientLink> links = linkQueryService.findAllByDoctorUuidAndStatus(doctorUuid, status);

        List<DoctorPatientLinkViewResource> resources = links.stream()
                .map(DoctorPatientLinkToResourceAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/links/{doctorUuid}/{patientUuid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_PATIENT')")
    public ResponseEntity<DoctorPatientLink> getLinkBetweenDoctorAndPatient(
            @PathVariable UUID doctorUuid, @PathVariable UUID patientUuid) {
        Optional<DoctorPatientLink> link = linkQueryService.findByDoctorUuidAndPatientUuid(doctorUuid, patientUuid);
        return link.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{externalId}/accept")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> accept(@PathVariable UUID externalId) {
        linkCommandService.acceptLink(new AcceptLinkCommand(externalId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{externalId}/reject")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> reject(@PathVariable UUID externalId) {
        linkCommandService.rejectLink(new RejectLinkCommand(externalId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{externalId}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID externalId) {
        linkCommandService.deleteLink(new DeleteLinkCommand(externalId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{externalId}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> activate(@PathVariable UUID externalId) {
        linkCommandService.activateLink(new ActivateLinkCommand(externalId));
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/doctor/{doctorUuid}/pending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<DoctorPatientLinkViewResource>> getPendingLinksByDoctor(@PathVariable UUID doctorUuid) {
        List<DoctorPatientLink> pendingLinks = linkQueryService.findAllByDoctorUuidAndStatus(doctorUuid, LinkStatus.PENDING);

        List<DoctorPatientLinkViewResource> resources = pendingLinks.stream()
                .map(DoctorPatientLinkToResourceAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/patient/{patientUuid}/pending")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<DoctorPatientLinkViewResource>> getPendingLinksByPatient(@PathVariable UUID patientUuid) {
        List<DoctorPatientLink> pendingLinks = linkQueryService.findAllByPatientUuidAndStatus(patientUuid, LinkStatus.PENDING);

        List<DoctorPatientLinkViewResource> resources = pendingLinks.stream()
                .map(DoctorPatientLinkToResourceAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/patient/{patientUuid}/active")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<DoctorPatientLinkViewResource>> getActiveLinksByPatient(@PathVariable UUID patientUuid){
        List<DoctorPatientLink> activeLinks = linkQueryService.findAllByPatientUuidAndStatus(patientUuid, LinkStatus.ACTIVE);

        List<DoctorPatientLinkViewResource> resources = activeLinks.stream()
                .map(DoctorPatientLinkToResourceAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }


}