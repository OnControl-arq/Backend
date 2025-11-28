package pe.edu.upc.oncontrol.treatment.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureExecutionCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.procedure.ProcedureExecutionQueryService;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.procedure.ProcedureExecutionService;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.MarkProcedureExecutionResource;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.ProcedureExecutionViewResource;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.MarkProcedureExecutionCommandAssembler;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.ProcedureExecutionToResourceAssembler;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/procedure-executions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcedureExecutionController {

    private final ProcedureExecutionService procedureExecutionService;
    private final ProcedureExecutionQueryService procedureExecutionQueryService;
    private final ProcedureExecutionToResourceAssembler procedureExecutionToResourceAssembler;
    private final MarkProcedureExecutionCommandAssembler markProcedureExecutionCommandAssembler;

    public ProcedureExecutionController(ProcedureExecutionService procedureExecutionService, ProcedureExecutionQueryService procedureExecutionQueryService, ProcedureExecutionToResourceAssembler procedureExecutionToResourceAssembler, MarkProcedureExecutionCommandAssembler markProcedureExecutionCommandAssembler) {
        this.procedureExecutionService = procedureExecutionService;
        this.procedureExecutionQueryService = procedureExecutionQueryService;
        this.procedureExecutionToResourceAssembler = procedureExecutionToResourceAssembler;
        this.markProcedureExecutionCommandAssembler = markProcedureExecutionCommandAssembler;
    }

    @PatchMapping("/{executionId}/complete")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> markExecutionCompleted(@PathVariable Long executionId,
                                                       @Valid @RequestBody MarkProcedureExecutionResource resource){
        MarkProcedureExecutionCommand command = markProcedureExecutionCommandAssembler.toCommand(executionId, resource);
        procedureExecutionService.markProcedureExecution(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/procedure/{procedureId}/patient")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<ProcedureExecutionViewResource>> getByProcedureForPatient(@PathVariable Long procedureId,
                                                                                         @RequestParam UUID patientProfileUuid) {
        List<ProcedureExecution> executions = procedureExecutionQueryService.getByProcedureIdForPatient(procedureId, patientProfileUuid);
        return ResponseEntity.ok(procedureExecutionToResourceAssembler.toResourceList(executions));
    }

    @GetMapping("/procedure/{procedureId}/doctor")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProcedureExecutionViewResource>> getByProcedureForDoctor(@PathVariable Long procedureId,
                                                                                        @RequestParam UUID doctorProfile){
        List<ProcedureExecution> executions = procedureExecutionQueryService.getByProcedureIdForDoctor(procedureId, doctorProfile);
        return ResponseEntity.ok(procedureExecutionToResourceAssembler.toResourceList(executions));
    }

}
