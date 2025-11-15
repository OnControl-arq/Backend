package pe.edu.upc.oncontrol.communication.interfaces.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.billing.application.acl.SubscriptionAcl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageQueryService;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversations")
public class ChatMessageQueryController {
    private final ChatMessageQueryService chatMessageQueryService;
    private final ProfileAccessAcl profileAccessAcl;
    private final SubscriptionAcl subscriptionAcl;

    public ChatMessageQueryController(ChatMessageQueryService chatMessageQueryService, ProfileAccessAcl profileAccessAcl, SubscriptionAcl subscriptionAcl) {
        this.chatMessageQueryService = chatMessageQueryService;
        this.profileAccessAcl = profileAccessAcl;
        this.subscriptionAcl = subscriptionAcl;
    }

    @GetMapping("/{doctorUuid}/{patientUuid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public ResponseEntity<List<ChatMessage>> getConversation(
            @PathVariable UUID doctorUuid,
            @PathVariable UUID patientUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        if(!profileAccessAcl.isLinkActive(doctorUuid, patientUuid)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Plan> plan = subscriptionAcl.getActivePlanByAdminIdFromUuid(doctorUuid);
        if(plan.isEmpty() || !plan.get().isMessagingEnabled()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<ChatMessage> messages = chatMessageQueryService
                .getConversationPaged(doctorUuid, patientUuid, page, size);

        return ResponseEntity.ok(messages.getContent());
    }

}
