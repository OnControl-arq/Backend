package pe.edu.upc.oncontrol.billing.interfaces.rest;

import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.CancelSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.RenewSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.services.subscription.SubscriptionCommandService;
import pe.edu.upc.oncontrol.billing.domain.services.subscription.SubscriptionQueryService;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.suscription.StartSubscriptionCommandResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.suscription.SubscriptionResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscription.StartSubscriptionCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscription.SubscriptionResourceEntityAssembler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionController {
    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionController(SubscriptionCommandService subscriptionCommandService, SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResource> start(@Valid @RequestBody StartSubscriptionCommandResource resource, HttpServletRequest request){
        var command = StartSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.start(command, request);
        return new ResponseEntity<>(SubscriptionResourceEntityAssembler.toResourceFromEntity(result), HttpStatus.CREATED);
    }

    @PatchMapping("/{subscriptionId}/cancel")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Void> cancel(@PathVariable Long subscriptionId){
        subscriptionCommandService.cancel(new CancelSubscriptionCommand(subscriptionId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{subscriptionId}/force-cancel")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> forceCancel(@PathVariable Long subscriptionId){
        subscriptionCommandService.forceCancel(new CancelSubscriptionCommand(subscriptionId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{subscriptionId}/renew")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResource> renew(@PathVariable Long subscriptionId){
        var result =subscriptionCommandService.renew(new RenewSubscriptionCommand(subscriptionId));
        return ResponseEntity.ok(SubscriptionResourceEntityAssembler.toResourceFromEntity(result));
    }

    @GetMapping("/{subscriptionId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResource> getById(@PathVariable Long subscriptionId, HttpServletRequest request){
        return subscriptionQueryService.getById(subscriptionId, request)
                .map(SubscriptionResourceEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{adminId}/active")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResource> getActiveByAdminId(@PathVariable Long adminId, HttpServletRequest request){
        return subscriptionQueryService.getActiveByAdminId(adminId, request)
                .map(SubscriptionResourceEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{adminId}/history")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<SubscriptionResource>> getHistory(@PathVariable Long adminId, HttpServletRequest request){
        var list = subscriptionQueryService.getHistoryByAdminId(adminId, request).stream()
                .map(SubscriptionResourceEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }
}
