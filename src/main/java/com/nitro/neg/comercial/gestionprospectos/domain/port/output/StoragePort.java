package com.nitro.neg.comercial.gestionprospectos.domain.port.output;

import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

/**
 * Contrato para el almacenamiento de archivos binarios (Imágenes/Documentos).
 */
public interface StoragePort {

    /**
     * Sube un archivo y retorna la entidad Foto con la URL generada.
     *
     * @param prospectoId   ID para organizar la estructura de carpetas.
     * @param nombreArchivo Nombre original del archivo.
     * @param contenido     Bytes del archivo.
     * @param contentType   Tipo MIME (ej: image/jpeg).
     */
    Mono<Foto> subirImagen(String prospectoId, String nombreArchivo, ByteBuffer contenido, String contentType);
}