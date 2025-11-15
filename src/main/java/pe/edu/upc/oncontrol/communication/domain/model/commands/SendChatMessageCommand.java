package pe.edu.upc.oncontrol.communication.domain.model.commands;

import pe.edu.upc.oncontrol.communication.domain.model.valueobjects.MessageType;

import java.util.UUID;

public record SendChatMessageCommand(
        UUID doctorUuid,
        UUID patientUuid,
        String senderRole,
        String content,
        MessageType type,
        String fileUrl
) {}

