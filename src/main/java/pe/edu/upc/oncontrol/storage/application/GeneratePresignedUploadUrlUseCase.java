package pe.edu.upc.oncontrol.storage.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.storage.infrastructure.S3PresignedUrlService;

@Service
@RequiredArgsConstructor
public class GeneratePresignedUploadUrlUseCase {

    private final S3PresignedUrlService s3PresignedUrlService;

    public S3PresignedUrlService.PresignedUrlResponse handle(Request request) {

        return s3PresignedUrlService.generateUploadUrl(
                request.category(),
                request.filename(),
                request.contentType(),
                request.userId()
        );
    }

    public record Request(String category, String filename, String contentType, Long userId) {}
}