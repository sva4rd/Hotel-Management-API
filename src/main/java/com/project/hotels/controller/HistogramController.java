package com.project.hotels.controller;

import com.project.hotels.dto.ErrorResponse;
import com.project.hotels.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Histogram controller", description = "Controller for obtaining histogram data by some parameter")
@RestController
@RequestMapping("/histogram")
public class HistogramController {
    private final HotelService hotelService;

    public HistogramController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(
            summary = "Returns the data for the hotel histogram",
            description = "This method returns the number of hotels grouped by each value of the specified parameter. " +
                    "Possible parameters: brand, city, county, amenities.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The histogram data has been retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Histogram by city",
                                            value = "{\"Minsk\": 1, \"Moscow\": 2, \"Mogilev\": 0}",
                                            description = "Example response for histogram by city"
                                    ),
                                    @ExampleObject(
                                            name = "Histogram by amenities",
                                            value = "{\"Free parking\": 1, \"Free WiFi\": 20, \"Non-smoking rooms\": 5, \"Fitness center\": 0}",
                                            description = "Example response for histogram by amenities"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{param}")
    public ResponseEntity<Object> getHotelHistogram(@PathVariable String param) {
        try {
            Map<String, Integer> result = hotelService.getHotelHistogram(param);
            return ResponseEntity.ok(result);
        } catch (Exception error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error.getMessage()));
        }
    }
}
