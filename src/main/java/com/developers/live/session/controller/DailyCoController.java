package com.developers.live.session.controller;

import com.developers.live.session.dto.dailyco.DailyCoCreateRequest;
import com.developers.live.session.dto.dailyco.DailyCoDeleteRequest;
import com.developers.live.session.dto.dailyco.DailyCoRequest;
import com.developers.live.session.dto.dailyco.DailyCoResponse;
import com.developers.live.session.service.DailyCoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/dailyco")
@Controller
public class DailyCoController {
    private final DailyCoService dailyCoService;

    @PostMapping
    public ResponseEntity<DailyCoResponse> create(@Valid @RequestBody DailyCoRequest request) throws Exception{
        DailyCoResponse response = dailyCoService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<DailyCoResponse> delete(@Valid @RequestBody DailyCoDeleteRequest request) throws Exception{
        DailyCoResponse response = dailyCoService.delete(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
