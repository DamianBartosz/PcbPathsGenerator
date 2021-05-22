package com.example.pcbgenerator.rest_service;

import com.example.pcbgenerator.genetic_elements.GeneticAlgorithm;
import com.example.pcbgenerator.genetic_elements.Individual;
import com.example.pcbgenerator.pcb.Pcb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PcbController {


    @GetMapping("/pcb")
    public ResponseEntity getPaths(@RequestBody PcbJsonData pcbJsonData) {
        String mess = pcbJsonData.validate();
        if (mess != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mess);
        }
        return ResponseEntity.ok(GeneticAlgorithm.ga(new Pcb(pcbJsonData)));
    }

}
