package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.CreateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.CreateTreatmentResource;

@Component
public class CreateTreatmentAssembler {
    public CreateTreatmentCommand toCommand(CreateTreatmentResource resource) {
        return new CreateTreatmentCommand(
                resource.title(),
                resource.startDate(),
                resource.endDate(),
                resource.doctorProfileUuid(),
                resource.patientProfileUuid()
        );
    }
}
