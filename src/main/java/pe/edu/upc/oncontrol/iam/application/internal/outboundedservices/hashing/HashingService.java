package pe.edu.upc.oncontrol.iam.application.internal.outboundedservices.hashing;

public interface HashingService {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}

