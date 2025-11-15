package pe.edu.upc.oncontrol.appointment.interfaces.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.commands.CancelAppointmentByDoctorCommand;
import pe.edu.upc.oncontrol.appointment.domain.model.commands.CancelAppointmentByPatientCommand;
import pe.edu.upc.oncontrol.appointment.domain.model.commands.MarkAppointmentStatusCommand;
import pe.edu.upc.oncontrol.appointment.domain.services.AppointmentCommandService;
import pe.edu.upc.oncontrol.appointment.domain.services.AppointmentQueryService;
import pe.edu.upc.oncontrol.appointment.interfaces.rest.resources.AppointmentDetail;
import pe.edu.upc.oncontrol.appointment.interfaces.rest.resources.CreateAppointmentResource;
import pe.edu.upc.oncontrol.appointment.interfaces.rest.transform.AppointmentAssembler;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController {
    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;
    private final AppointmentAssembler appointmentAssembler;
    private final TokenContextFacade tokenContextFacade;
    private final ProfileAccessAcl profileAccessAcl;

    public AppointmentController(AppointmentCommandService appointmentCommandService, AppointmentQueryService appointmentQueryService, AppointmentAssembler appointmentAssembler, TokenContextFacade tokenContextFacade, ProfileAccessAcl profileAccessAcl) {
        this.appointmentCommandService = appointmentCommandService;
        this.appointmentQueryService = appointmentQueryService;
        this.appointmentAssembler = appointmentAssembler;
        this.tokenContextFacade = tokenContextFacade;
        this.profileAccessAcl = profileAccessAcl;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void create(@RequestBody CreateAppointmentResource resource, HttpServletRequest request) {
        Long userId = tokenContextFacade.extractUserIdFromToken(request);
        UUID doctorProfileUuid = profileAccessAcl.getDoctorProfileUuidByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        var command = appointmentAssembler.toCreateCommand(resource, doctorProfileUuid);
        appointmentCommandService.createAppointment(command);
    }

    @GetMapping("/doctor/{patientUuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AppointmentDetail> getAppointmentsForDoctorAndPatient(
            @PathVariable UUID patientUuid, HttpServletRequest request) {
        Long userId = tokenContextFacade.extractUserIdFromToken(request);
        UUID doctorProfileUuid = profileAccessAcl.getDoctorProfileUuidByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));
        return appointmentQueryService.getAppointmentsForDoctorAndPatient(doctorProfileUuid, patientUuid)
                .stream()
                .map(appointmentAssembler::toDetail)
                .toList();
    }

    @GetMapping("/calendar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public List<AppointmentDetail> getCalendar(HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = tokenContextFacade.extractUserIdFromToken(request);
        if (Objects.equals(role, "ROLE_ADMIN")) {
            UUID doctorProfileUuid = profileAccessAcl.getDoctorProfileUuidByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
            return appointmentQueryService.getAppointmentsForDoctor(doctorProfileUuid)
                    .stream()
                    .map(appointmentAssembler::toDetail)
                    .toList();
        } else {
            UUID patientProfileUuid = profileAccessAcl.getPatientProfileUuidByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
            return appointmentQueryService.getAppointmentsForPatient(patientProfileUuid)
                    .stream()
                    .map(appointmentAssembler::toDetail)
                    .toList();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public AppointmentDetail getDetail(@PathVariable Long id, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = tokenContextFacade.extractUserIdFromToken(request);
        Appointment appointment;

        if ("ROLE_PATIENT".equals(role)) {
            UUID patientProfileUuid = profileAccessAcl.getPatientProfileUuidByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
            appointment = appointmentQueryService.findByIdAndPatient(id, patientProfileUuid);
        } else {
            UUID doctorProfileUuid = profileAccessAcl.getDoctorProfileUuidByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
            appointment = appointmentQueryService.findByIdAndDoctor(id, doctorProfileUuid);
        }

        return appointmentAssembler.toDetail(appointment);
    }

    @PatchMapping("/mark")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void markStatus(@RequestBody MarkAppointmentStatusCommand command, HttpServletRequest request) {
        Long userId = tokenContextFacade.extractUserIdFromToken(request);
        UUID doctorProfileUuid = profileAccessAcl.getDoctorProfileUuidByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));

        if (!Objects.equals(command.doctorProfileUuid(), doctorProfileUuid)) {
            throw new AccessDeniedException("You are not authorized to update this appointment.");
        }

        appointmentCommandService.markStatus(command);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT')")
    public void cancel(@PathVariable Long id, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = tokenContextFacade.extractUserIdFromToken(request);

        if ("ROLE_ADMIN".equals(role)) {
            UUID doctorProfileUuid = profileAccessAcl.getDoctorProfileUuidByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));
            appointmentCommandService.cancelByDoctor(new CancelAppointmentByDoctorCommand(id, doctorProfileUuid));
        } else if ("ROLE_PATIENT".equals(role)) {
            UUID patientProfileUuid = profileAccessAcl.getPatientProfileUuidByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
            appointmentCommandService.cancelByPatient(new CancelAppointmentByPatientCommand(id, patientProfileUuid));
        } else {
            throw new AccessDeniedException("Role not allowed to cancel appointments.");
        }
    }



}
