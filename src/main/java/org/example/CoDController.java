package org.example;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoDController {

    private final CoDService service;

    public CoDController(CoDService service) {
        this.service = service;
    }
    @PostMapping("/CoD")
    public ResponseEntity<CoDResponse> colorOfDay(@Valid @RequestBody CoDRequest request) {
       return service.colorOfDay(request);
    }
}
