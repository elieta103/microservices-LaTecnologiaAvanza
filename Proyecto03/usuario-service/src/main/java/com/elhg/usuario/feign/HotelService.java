package com.elhg.usuario.feign;


import com.elhg.usuario.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

    @GetMapping("/hoteles/{hotelId}")
    Hotel getHotel(@PathVariable String hotelId);

}
