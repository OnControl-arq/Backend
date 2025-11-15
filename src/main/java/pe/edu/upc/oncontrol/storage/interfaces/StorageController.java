package pe.edu.upc.oncontrol.storage.interfaces;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.oncontrol.storage.application.GeneratePresignedUploadUrlUseCase;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final GeneratePresignedUploadUrlUseCase useCase;

    @PostMapping("/presign-upload")
    public ResponseEntity<?> generateUploadUrl(@RequestBody UploadRequest request) {
        var response = useCase.handle(new GeneratePresignedUploadUrlUseCase.Request(
                request.category(),
                request.filename(),
                request.contentType(),
                request.userId()
        ));
        return ResponseEntity.ok(response);
    }

    public record UploadRequest(
            @NotBlank String category,
            @NotBlank String filename,
            @NotBlank String contentType,
            @NotNull Long userId
    ) {}
}