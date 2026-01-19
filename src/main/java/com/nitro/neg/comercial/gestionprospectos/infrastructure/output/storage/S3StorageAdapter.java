package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.storage;

import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.StoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3StorageAdapter implements StoragePort {

    private final S3AsyncClient s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public Mono<Foto> subirImagen(String prospectoId, String nombreArchivo, ByteBuffer contenido, String contentType) {
        String key = generarKeyS3(prospectoId, nombreArchivo);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        return Mono.fromFuture(() -> s3Client.putObject(putRequest, AsyncRequestBody.fromByteBuffer(contenido)))
                .map(response -> {
                    String url = s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(key).build()).toExternalForm();
                    log.info("Imagen subida a S3: {}", key);
                    return new Foto(UUID.randomUUID().toString(), url, key, nombreArchivo);
                });
    }

    private String generarKeyS3(String prospectoId, String filename) {
        LocalDate now = LocalDate.now();
        return String.format("%d/%02d/%s/%s", now.getYear(), now.getMonthValue(), prospectoId, filename);
    }
}