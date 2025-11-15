package pe.edu.upc.oncontrol.iam.infrastructure.hashing.bycript;

import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.oncontrol.iam.application.internal.outboundedservices.hashing.HashingService;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}

