package pe.edu.upc.oncontrol.appointment.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientProfileUuidAndScheduledAt_ValueScheduledAfter(UUID patientUuid, LocalDateTime dateTime);
    List<Appointment> findByDoctorProfileUuidAndScheduledAt_ValueScheduledAfter(UUID doctorUuid, LocalDateTime dateTime);
    Optional<Appointment> findByIdAndDoctorProfileUuid(Long id, UUID doctorUuid);
    Optional<Appointment> findByIdAndPatientProfileUuid(Long id, UUID patientUuid);
    @Query("SELECT a FROM Appointment a WHERE a.doctorProfileUuid = :doctorProfileUuid AND a.patientProfileUuid = :patientProfileUuid")
    List<Appointment> findByDoctorProfileUuidAndPatientProfileUuid(UUID doctorProfileUuid, UUID patientProfileUuid);
    List<Appointment> findByStatus(AppointmentStatus status);
    boolean existsByDoctorProfileUuidAndPatientProfileUuid(UUID doctorUuid, UUID patientUuid);
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.status = 'SCHEDULED'
          AND a.meetingUrl.url IS NULL
          AND a.location.nameLocation IS NULL
          AND a.scheduledAt.valueScheduled BETWEEN :from AND :to
    """)
    List<Appointment> findVirtualAppointmentsWithoutMeetingUrl(@Param("from") LocalDateTime from,
                                                               @Param("to") LocalDateTime to);
    List<Appointment> findByStatusAndScheduledAt_ValueScheduledBefore(AppointmentStatus status, LocalDateTime now);

}
