package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.service.StationService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody final StationCreateRequest request) {
        final StationResponse response = stationService.createStation(request);
        return ResponseEntity
                .created(URI.create("/stations/" + response.getId()))
                .body(response);
    }
}
