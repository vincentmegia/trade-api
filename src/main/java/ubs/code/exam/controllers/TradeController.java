package ubs.code.exam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubs.code.exam.models.Trade;
import ubs.code.exam.repositories.TradeRepository;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("trades")
@RestController
public class TradeController {
    @Autowired
    private TradeRepository tradeRepository;

    @RequestMapping("/all")
    public ResponseEntity<?> all() {
        List<Trade> trades = tradeRepository.getAll();
        if (trades.size() == 0)
            return new ResponseEntity<>(trades, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{tradeId}")
    public Trade getById(@PathVariable long tradeId) {
        Trade trade = tradeRepository.getById(tradeId);
        return trade;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Trade trade) {
        boolean status = tradeRepository.add(trade);
        if (status)
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
}
