package com.elhg.usuario.feign;


import com.elhg.usuario.entities.Calificacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "CALIFICACION-SERVICE")
public interface CalificacionService {

    @PostMapping
    public ResponseEntity<Calificacion> guardarCalificacion(Calificacion calificacion);

    // Pendiente de Implementar en CalificacionService
    @PostMapping("/calificaciones/{calificacionId}")
    public ResponseEntity<Calificacion> actualizarCalificacion(@PathVariable String calificacionId, Calificacion calificacion);

    // Pendiente de Implementar en CalificacionService
    @DeleteMapping("/calificaciones/{calificacionId}")
    public void eliminarCalificacion(@PathVariable String calificacionId);

}
