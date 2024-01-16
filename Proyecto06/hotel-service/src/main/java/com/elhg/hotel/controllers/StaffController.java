package com.elhg.hotel.controllers;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/staffs")
public class StaffController {


    @GetMapping
    public ResponseEntity<List<String>> listarStaffs(){
        List<String> staffs = Arrays.asList("Eliel", "Juan", "Luis", "July");
        return ResponseEntity.status(HttpStatus.OK).body(staffs);
    }


}
