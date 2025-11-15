package pe.edu.upc.oncontrol.billing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Value;

import java.security.SecureRandom;

@Value
@Embeddable
public class SubscriptionKeyCode {
    String value;

    protected SubscriptionKeyCode() {
        this.value = null;
    }

    public SubscriptionKeyCode(String value){
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Key code cannot be null or blank");
        this.value = value;
    }

    public static SubscriptionKeyCode random(){
        return new SubscriptionKeyCode(generateRandomKey());
    }

    private static String generateRandomKey(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for(int i = 0; i < 16; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
