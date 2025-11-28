package pe.edu.upc.oncontrol.treatment.interfaces;


import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CancelProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CreateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.StartProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.CreateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureExecutionForecast;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentCommandService;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentQueryService;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.*;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/treatments", produces = MediaType.APPLICATION_JSON_VALUE)
public class TreatmentController {

    private final TreatmentCommandService treatmentService;
    private final TreatmentQueryService treatmentQueryService;
    private final CreateTreatmentAssembler createTreatmentAssembler;
    private final AddProcedureAssembler addProcedureAssembler;
    private final LogSymptomAssembler logSymptomAssembler;
    private final StartProcedureCommandAssembler startProcedureCommandAssembler;
    private final SymptomLogViewToResourceAssembler symptomLogViewToResourceAssembler;

    public TreatmentController(TreatmentCommandService treatmentService,
                               TreatmentQueryService treatmentQueryService,
                               CreateTreatmentAssembler createTreatmentAssembler,
                               AddProcedureAssembler addProcedureAssembler,
                               LogSymptomAssembler logSymptomAssembler, StartProcedureCommandAssembler startProcedureCommandAssembler, SymptomLogViewToResourceAssembler symptomLogViewToResourceAssembler) {
        this.treatmentService = treatmentService;
        this.treatmentQueryService = treatmentQueryService;
        this.createTreatmentAssembler = createTreatmentAssembler;
        this.addProcedureAssembler = addProcedureAssembler;
        this.logSymptomAssembler = logSymptomAssembler;
        this.startProcedureCommandAssembler = startProcedureCommandAssembler;
        this.symptomLogViewToResourceAssembler = symptomLogViewToResourceAssembler;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateTreatmentResource resource) {
        CreateTreatmentCommand command = createTreatmentAssembler.toCommand(resource);
        UUID externalId = treatmentService.createTreatment(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(externalId);
    }

    @PostMapping("/{treatmentId}/procedures")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addProcedure(@PathVariable UUID treatmentId,
                                             @Valid @RequestBody AddProcedureResource resource) {
        CreateProcedureCommand command = addProcedureAssembler.toCommand(treatmentId, resource);
        treatmentService.addProcedure(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/procedures/{procedureId}/cancel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelProcedure(@PathVariable Long procedureId,
                                                @RequestParam UUID doctorProfileUuid) {
        CancelProcedureCommand command = new CancelProcedureCommand(procedureId, doctorProfileUuid);
        treatmentService.cancelProcedure(command);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/procedures/{procedureId}/start")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> startProcedure(@PathVariable Long procedureId,
                                               @Valid @RequestBody StartProcedureResource resource){
        StartProcedureCommand command = startProcedureCommandAssembler.toCommand(procedureId, resource);
        treatmentService.startProcedure(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{treatmentId}/symptoms")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> logSymptom(@PathVariable UUID treatmentId,
                                           @Valid @RequestBody LogSymptomResource resource) {
        CreateSymptomCommand command = logSymptomAssembler.toCommand(treatmentId, resource);
        treatmentService.logSymptom(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/symptom-logs/patient")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<SymptomLogViewResource>> getLogsByPatientInRange(
            @RequestParam UUID patientUuid,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<SymptomLog> logs = treatmentQueryService.getSymptomLogsByPatientInRange(patientUuid, from, to);
        List<SymptomLogViewResource> resources = symptomLogViewToResourceAssembler.toResourcerList(logs);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/symptom-logs/{symptomId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public ResponseEntity<SymptomLogViewResource> getSymptomLogById(
            @PathVariable Long symptomId){
        return treatmentQueryService.getSymptomLogById(symptomId)
                .map(symptomLog -> ResponseEntity.ok(symptomLogViewToResourceAssembler.toResource(symptomLog)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{externalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Treatment> getByExternalId(@PathVariable UUID externalId) {
        return treatmentQueryService.getTreatmentByExternalId(externalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doctor/{doctorUuid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<Treatment>> getByDoctor(@PathVariable UUID doctorUuid) {
        List<Treatment> treatments = treatmentQueryService.getActiveTreatmentsByDoctor(doctorUuid);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/patient/{patientUuid}")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<Treatment>> getByPatient(@PathVariable UUID patientUuid) {
        List<Treatment> treatments = treatmentQueryService.getActiveTreatmentsByPatient(patientUuid);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/{externalId}/predicted-executions")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public ResponseEntity<List<ProcedureExecutionPredictionResource>> getPredictedExecutions(
            @PathVariable UUID externalId){
        List<ProcedureExecutionForecast> forecast = treatmentQueryService.getForecastForTreatment(externalId);

        List<ProcedureExecutionPredictionResource> resources = forecast.stream()
                .map(ProcedureExecutionPredictionResourceAssembler::toResourceFromForecast)
                .toList();
        return ResponseEntity.ok(resources);
    }
    @GetMapping("/{treatmentExternalId}/procedures")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public ResponseEntity<List<ProcedureViewResource>> getByTreatment(@PathVariable UUID treatmentExternalId) {
        var procedures = treatmentQueryService.getProceduresByExternalId(treatmentExternalId);
        var resources = ProcedureToResourceAssembler.toResourceListFromEntities(procedures);
        return ResponseEntity.ok(resources);
    }


}

