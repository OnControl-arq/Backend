package pe.edu.upc.oncontrol.billing.interfaces.rest;

import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.ActivateSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.DeactivateSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.queries.subscriptionKey.*;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyCode;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyStatus;
import pe.edu.upc.oncontrol.billing.domain.services.subscriptionKey.SubscriptionKeyCommandService;
import pe.edu.upc.oncontrol.billing.domain.services.subscriptionKey.SubscriptionKeyQueryService;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey.*;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscriptionkey.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/subscription-keys", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionKeyController {
    private final SubscriptionKeyCommandService subscriptionKeyCommandService;
    private final SubscriptionKeyQueryService subscriptionKeyQueryService;

    public SubscriptionKeyController(SubscriptionKeyCommandService subscriptionKeyCommandService, SubscriptionKeyQueryService subscriptionKeyQueryService) {
        this.subscriptionKeyCommandService = subscriptionKeyCommandService;
        this.subscriptionKeyQueryService = subscriptionKeyQueryService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SubscriptionKeyResource> createKey(@Valid @RequestBody CreateKeyResource resource){
        var command = CreateKeyCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdKey = subscriptionKeyCommandService.createKey(command);
        var response = SubscriptionKeyResourceFromEntityAssembler.toResourceFromEntity(createdKey);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/use")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<SubscriptionKeyUsageResource> useKey(@Valid @RequestBody UseKeyResource resource, HttpServletRequest request){
        var command = UseKeyCommandFromResourceAssembler.toCommandFromResource(resource);
        var usage = subscriptionKeyCommandService.useKey(command, request);
        var usageResource = SubscriptionKeyUsageResourceFromEntityAssembler.toResourceFromEntity(usage);
        return new ResponseEntity<>(usageResource, HttpStatus.CREATED);
    }

    @PatchMapping("/deactivate/{keyId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deactivateKey(@PathVariable Long keyId){
        var command = new DeactivateSubscriptionKeyCommand(keyId);
        subscriptionKeyCommandService.deactivateKey(command);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate/{keyId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> activateKey(@PathVariable Long keyId){
        var command = new ActivateSubscriptionKeyCommand(keyId);
        subscriptionKeyCommandService.activateKey(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{keyId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SubscriptionKeyResource> getById(@PathVariable Long keyId){
        return subscriptionKeyQueryService.getById(new FindSubscriptionKeyByIdQuery(keyId))
                .map(SubscriptionKeyResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<SubscriptionKeyPreviewResource> getByCode(@PathVariable String code){
        return subscriptionKeyQueryService.getByCode(new FindSubscriptionKeyByCodeQuery(new SubscriptionKeyCode(code)))
                .map(SubscriptionKeyPreviewResourceFromEntityAssembler::toPreviewResourceEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<SubscriptionKeyResource>> getByStatus(@PathVariable String status){
        try {
            var subscriptionKeyStatus = SubscriptionKeyStatus.valueOf(status.toUpperCase());
            var keys = subscriptionKeyQueryService.getByStatus(new FindSubscriptionKeysByStatusQuery(subscriptionKeyStatus));
            var resources = keys.stream()
                    .map(SubscriptionKeyResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<SubscriptionKeyResource>> getByPlanId(@PathVariable Long planId){
        var keys = subscriptionKeyQueryService.getByPlanId(new FindSubscriptionKeysByPlanIdQuery(planId));
        var resources = keys.stream()
                .map(SubscriptionKeyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/usage/user/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<SubscriptionKeyUsageResource>> getUsageByUserId(@PathVariable Long userId, HttpServletRequest request){
        var usages = subscriptionKeyQueryService.getUsageByUserId(new FindActiveUsageByUserIdQuery(userId), request);
        var resources = usages.stream()
                .map(SubscriptionKeyUsageResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/usage/key/{keyId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<SubscriptionKeyUsageResource>> getUsageByKeyId(@PathVariable Long keyId){
        var usages = subscriptionKeyQueryService.getUsageByKey(new FindUsageBySubscriptionKeyIdQuery(keyId));
        var resources = usages.stream()
                .map(SubscriptionKeyUsageResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

}
