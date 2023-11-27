package com.elhg.hotel.service;



import com.elhg.hotel.entity.Hotel;

import java.util.List;

public interface HotelService {

    Hotel create(Hotel hotel);
    List<Hotel> getAll();
    Hotel get(String id);

}
