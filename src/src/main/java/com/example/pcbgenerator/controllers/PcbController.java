package com.example.pcbgenerator.controllers;

import com.example.pcbgenerator.genetic_elements.GeneticAlgorithm;
import com.example.pcbgenerator.pcb.Pcb;
import com.example.pcbgenerator.pcb.PcbJsonData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PcbController {
    /**
     * Metoda zwraca domyślne wartości parametrów algorytmu genetycznego.
     */
    @GetMapping("default")
    public Pcb getDefault() {
        return new Pcb();
    }

    /**
     * Metoda przyjmuje dane płytki psc oraz ewentualne parametry algorytmu genetycznego i zwraca wygenerowane ścieżki lub błąd w przypadku niepoprawnych danch.
     */
    @PostMapping("/pcb")
    public ResponseEntity getPaths(@RequestBody PcbJsonData pcbJsonData) {
        String mess = pcbJsonData.validate();
        if (mess != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mess);
        }
        return ResponseEntity.ok(GeneticAlgorithm.ga(new Pcb(pcbJsonData)));
    }

}
