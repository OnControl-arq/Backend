package pe.edu.upc.oncontrol.billing.interfaces.rest;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import pe.edu.upc.oncontrol.billing.domain.services.paymentmethod.PaymentMethodCommandService;
import pe.edu.upc.oncontrol.billing.domain.services.paymentmethod.PaymentMethodQueryService;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.ActivePaymentMethodCommandResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.DeactivatePaymentMethodResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.PaymentMethodResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.SetDefaultPaymentMethodResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.paymentmethod.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentMethodController {
    private final PaymentMethodCommandService commandService;
    private final PaymentMethodQueryService queryService;
    private final PaymentMethodCommandService paymentMethodCommandService;

    public PaymentMethodController(PaymentMethodCommandService commandService, PaymentMethodQueryService queryService, PaymentMethodCommandService paymentMethodCommandService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.paymentMethodCommandService = paymentMethodCommandService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole( 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<PaymentMethodResource> add(@Valid @RequestBody PaymentMethodResource resource, HttpServletRequest request){
        var command = AddPaymentMethodCommandFromResourceAssembler.toCommandFromResource(resource);
        var paymentMethod = paymentMethodCommandService.add(command, request);
        var response = PaymentMethodResourceFromEntityAssembler.toResourceFromEntity(paymentMethod);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/set-default")
    @PreAuthorize("hasAnyRole( 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Void> setDefault(@Valid @RequestBody SetDefaultPaymentMethodResource resource, HttpServletRequest request){
        var command = SetDefaultPaymentMethodCommandFromResourceAssembler.toCommand(resource);
        commandService.setDefault(command, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate")
    @PreAuthorize("hasAnyRole( 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deactivate(@Valid @RequestBody DeactivatePaymentMethodResource resource, HttpServletRequest request){
        var command = DeactivatePaymentMethodCommandFromResourceAssembler.toCommand(resource);
        commandService.deactivate(command, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/active")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> active(@Valid @RequestBody ActivePaymentMethodCommandResource resource){
        var command = ActivePaymentMethodCommandFromResourceAssembler.toCommand(resource);
        commandService.active(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{methodId}")
    @PreAuthorize("hasAnyRole( 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<PaymentMethodResource> getById(@PathVariable Long methodId, HttpServletRequest request) {
        return queryService.getById(methodId, request)
                .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{adminId}/default-active")
    @PreAuthorize("hasAnyRole( 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<PaymentMethodResource> getDefaultActiveByAdminId(@PathVariable Long adminId, HttpServletRequest request) {
        return queryService.getDefaultByAdminId(adminId, request)
                .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasAnyRole( 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<PaymentMethodResource>> getAllByAdminId(@PathVariable Long adminId, HttpServletRequest request){
        List<PaymentMethod> methods = queryService.getAllByAdminId(adminId, request);
        List<PaymentMethodResource> resources = methods.stream()
                .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

}
