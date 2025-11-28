package pe.edu.upc.oncontrol.treatment.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureExecutionCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.procedure.ProcedureExecutionService;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;

@Service
public class ProcedureExecutionServiceImpl implements ProcedureExecutionService {

    private final ProcedureExecutionRepository procedureExecutionRepository;

    public ProcedureExecutionServiceImpl(ProcedureExecutionRepository procedureExecutionRepository) {
        this.procedureExecutionRepository = procedureExecutionRepository;
    }

    @Override
    public void markProcedureExecution(MarkProcedureExecutionCommand command) {
        ProcedureExecution procedureExecution = procedureExecutionRepository
                .findByIdAndProcedure_Treatment_PatientProfileUuid(command.executionId(), command.patientProfileUuid())
                .orElseThrow(() -> new EntityNotFoundException("Procedure execution not found for the given ID and patient profile UUID."));

        procedureExecution.markCompleted(command.completionDate());
        procedureExecutionRepository.save(procedureExecution);
    }
}
