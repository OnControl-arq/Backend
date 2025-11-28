package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

public enum ProcedureStatus {
    PENDING, // initial yet to start
    ACTIVE, // currently in progress
    COMPLETED, // successfully finished
    CANCELLED, // doctor cancelled
}
