package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureExecutionForecast;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.ProcedureExecutionPredictionResource;

public class ProcedureExecutionPredictionResourceAssembler {
    public static ProcedureExecutionPredictionResource toResourceFromForecast(ProcedureExecutionForecast forecast){
        return new ProcedureExecutionPredictionResource(
                forecast.id(),
                forecast.procedureName(),
                forecast.scheduledAt(),
                forecast.status()
        );
    }
}
