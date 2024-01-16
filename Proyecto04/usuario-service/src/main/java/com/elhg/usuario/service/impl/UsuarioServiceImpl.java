package com.elhg.usuario.service.impl;

import com.elhg.usuario.entities.Calificacion;
import com.elhg.usuario.entities.Hotel;
import com.elhg.usuario.entities.Usuario;
import com.elhg.usuario.exceptions.ResourceNotFoundException;
import com.elhg.usuario.feign.HotelService;
import com.elhg.usuario.repository.UsuarioRepository;
import com.elhg.usuario.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Usuario saveUsuario(Usuario usuario) {
        String randomUsuarioId = UUID.randomUUID().toString();
        usuario.setUsuarioId(randomUsuarioId);
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID : " + usuarioId));

        Calificacion[] calificacionesDelUsuario = restTemplate.getForObject("http://CALIFICACION-SERVICE/calificaciones/usuarios/"+usuario.getUsuarioId(),Calificacion[].class);
        logger.info("{}",calificacionesDelUsuario);

        List<Calificacion> calificaciones = Arrays.stream(calificacionesDelUsuario).collect(Collectors.toList());

        List<Calificacion> listaCalificaciones = calificaciones.stream().map(calificacion -> {
            logger.info("Id Hotel : {}",calificacion.getHotelId());

            /* Llamada con RestTemplate */
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hoteles/"+calificacion.getHotelId(),Hotel.class);
            //logger.info("Respuesta con codigo de estado : {}",forEntity.getStatusCode());
            //calificacion.setHotel(forEntity.getBody());

            /*Llamada con OpenFeign */
            Hotel hotel = hotelService.getHotel(calificacion.getHotelId());
            calificacion.setHotel(hotel);

            return calificacion;
        }).collect(Collectors.toList());

        usuario.setCalificacionList(listaCalificaciones);

        return usuario;
    }

}
