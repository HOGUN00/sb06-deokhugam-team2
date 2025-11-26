package com.codeit.sb06deokhugamteam2.book.storage;

import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import com.codeit.sb06deokhugamteam2.common.exception.exceptions.AWSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Component
public class S3Storage {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucket;
    private final Duration expiration;

    public S3Storage(
            @Value("${spring.cloud.aws.s3.bucket}")
            String bucket,
            @Value("${spring.cloud.aws.s3.presigned-url-expiration}")
            int expiration,
            S3Client s3Client,
            S3Presigner s3Presigner
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucket = bucket;
        this.expiration = Duration.ofSeconds(expiration);
    }

    public void putThumbnail(UUID bookId, byte[] bytes, String contentType) {
        try {
            PutObjectRequest putObjectRequest =  PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(bookId.toString())
                    .build();

            RequestBody requestBody = RequestBody.fromBytes(bytes);
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (S3Exception e) {
            Map<String, Object> details = Map.of(
                    "message", e.getMessage()
            );
            throw new AWSException(ErrorCode.AWS_EXCEPTION, details);
        }
    }

    public String getThumbnail(UUID bookId) {
        try {
            GetObjectRequest getObjectRequest =  GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(bookId.toString())
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

            return presignedGetObjectRequest.url().toString();
        } catch (S3Exception e) {
            Map<String, Object> details = Map.of(
                    "message", e.getMessage()
            );
            throw new AWSException(ErrorCode.AWS_EXCEPTION, details);
        }
    }
}
