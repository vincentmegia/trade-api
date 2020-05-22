package ubs.code.exam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import ubs.code.exam.repositories.TradeRepository;

@RestController
public class ResourceController {
    @Autowired
    private TradeRepository tradeRepository;

    @DeleteMapping("/erase")
    public ResponseEntity<?> erase() {
        boolean status = tradeRepository.deleteAll();
        if (status)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
