package pe.edu.upc.oncontrol.billing.interfaces.rest;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.ActivatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.CreatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.DeactivatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.UpdatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.services.plan.PlanCommandService;
import pe.edu.upc.oncontrol.billing.domain.services.plan.PlanQueryService;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan.PlanResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan.UpdateDetailsPlanResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.plan.CreatePlanCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.plan.PlanResourceFromEntityAssembler;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.plan.UpdatePlanCommandFromResourceAssembler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/plans", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlanController {
    private final PlanCommandService planCommandService;
    private final PlanQueryService planQueryService;

    public PlanController(PlanCommandService planCommandService, PlanQueryService planQueryService) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<PlanResource> create(@Valid @RequestBody PlanResource resource){
        CreatePlanCommand command = CreatePlanCommandFromResourceAssembler.toCommandFromResource(resource);
        Plan createPlan = planCommandService.create(command);
        PlanResource planResource = PlanResourceFromEntityAssembler.toResourceFromEntity(createPlan);
        return new ResponseEntity<>(planResource, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<PlanResource>> getAllPlans(HttpServletRequest request){
        List<Plan> plans = planQueryService.getAccessiblePlans(request);
        List<PlanResource> resources = plans.stream()
                .map(PlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{planId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<PlanResource> getById(@PathVariable Long planId, HttpServletRequest request){
        return planQueryService.getById(planId, request)
                .map(plan -> ResponseEntity.ok(PlanResourceFromEntityAssembler.toResourceFromEntity(plan)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{planId}/rename")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<PlanResource> update(@Valid @PathVariable Long planId, @RequestBody UpdateDetailsPlanResource resource){
        UpdatePlanCommand command = UpdatePlanCommandFromResourceAssembler.toCommandFromResource(planId, resource);
        Plan updated = planCommandService.update(command);
        return ResponseEntity.ok(PlanResourceFromEntityAssembler.toResourceFromEntity(updated));
    }

    @PatchMapping("/{planId}/deactivate")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long planId) {
        planCommandService.deactivate(new DeactivatePlanCommand(planId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{planId}/activate")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<PlanResource> activate(@PathVariable Long planId) {
        Plan activatedPlan = planCommandService.activate(new ActivatePlanCommand(planId));
        return ResponseEntity.ok(PlanResourceFromEntityAssembler.toResourceFromEntity(activatedPlan));
    }


}
