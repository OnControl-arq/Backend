package pe.edu.upc.oncontrol.storage.infrastructure;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;

    @Value("${aws.credentials.secretKey}")
    private String secretKey;

    public PresignedUrlResponse generateUploadUrl(String category, String filename, String contentType, Long userId) {
        String objectKey = category + "/" + userId + "/" + filename;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .build();

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(p -> p
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
        );

        String uploadUrl = presignedRequest.url().toString();
        String accessUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + objectKey;

        return new PresignedUrlResponse(uploadUrl, accessUrl);
    }

    public record PresignedUrlResponse(String uploadUrl, String accessUrl) {}

}
