package pe.edu.upc.oncontrol.billing.application.internal.commandservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.ActivatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.CreatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.DeactivatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.UpdatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.services.plan.PlanCommandService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlanCommandServiceImpl implements PlanCommandService {

    private final PlanRepository planRepository;

    public PlanCommandServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public Plan create(CreatePlanCommand command) {
        Plan plan = new Plan(command);
        return planRepository.save(plan);
    }

    @Override
    public Plan update(UpdatePlanCommand command) {
        Plan plan = planRepository.findById(command.planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with ID " + command.planId()));
        plan.updateDetails(command);
        return planRepository.save(plan);
    }

    @Override
    public void deactivate(DeactivatePlanCommand command) {
        Plan plan = planRepository.findById(command.planId())
                .orElseThrow(() -> new EntityNotFoundException("Plan with ID " + command.planId() + " not found"));
        if(!plan.isActive()){
            throw new IllegalStateException("Plan is already deactivated");
        }
        plan.deactivate();
        planRepository.save(plan);
    }

    @Override
    public Plan activate(ActivatePlanCommand command){
        Plan plan = planRepository.findById(command.planId())
                .orElseThrow(()-> new EntityNotFoundException("Plan with ID " + command.planId() + " not found"));
        if(plan.isActive()){
            throw new IllegalStateException("Plan is already active");
        }
        plan.activate();
        planRepository.save(plan);
        return plan;
    }
}
