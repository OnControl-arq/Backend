package pe.edu.upc.oncontrol.communication.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oncontrol.communication.domain.model.valueobjects.MessageType;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.UUID;


@Entity
@Setter
@Getter
public class ChatMessage extends AuditableAbstractAggregateRoot<ChatMessage> {
    @Column(nullable = false)
    private UUID doctorUuid;
    @Column(nullable = false)
    private UUID patientUuid;
    @Column(nullable = false)
    private String senderRole;
    @Column(length = 1000)
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private String fileUrl;
    private boolean seen;
}
