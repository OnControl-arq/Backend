package pe.edu.upc.oncontrol.billing.domain.model.commands.plan;

public record UpdatePlanCommand(
        Long planId,
        String name
) {}
