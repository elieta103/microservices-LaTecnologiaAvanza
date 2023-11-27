package com.elhg.calificacion.repository;


import com.elhg.calificacion.entities.Calificacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CalificacionRepository extends MongoRepository<Calificacion,Long> {

    List<Calificacion> findByUsuarioId(String usuarioId);
    List<Calificacion> findByHotelId(String hotelId);
}
