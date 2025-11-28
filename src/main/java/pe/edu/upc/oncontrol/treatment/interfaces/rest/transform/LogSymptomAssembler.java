package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.LogSymptomResource;

import java.util.UUID;

@Component
public class LogSymptomAssembler {
    public CreateSymptomCommand toCommand(UUID treatmentExternalId, LogSymptomResource resource) {
        return new CreateSymptomCommand(
                treatmentExternalId,
                resource.patientProfileUuid(),
                resource.symptomType(),
                resource.severity(),
                resource.notes(),
                resource.loggedAt()
        );
    }
}
