package pe.edu.upc.oncontrol.communication.infrastructure.persisntence.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByDoctorUuidAndPatientUuidOrderByCreatedAtAsc(UUID doctorUuid, UUID patinetUuid);
    Page<ChatMessage> findByDoctorUuidAndPatientUuidOrderByCreatedAtDesc(
            UUID doctorUuid,
            UUID patientUuid,
            Pageable pageable
    );
}
