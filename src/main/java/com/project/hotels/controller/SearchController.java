package com.project.hotels.controller;

import com.project.hotels.dto.ErrorResponse;
import com.project.hotels.dto.HotelShortDetailsResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Search controller",
        description = "Controller to search for hotels according to certain parameters")
@RestController
@RequestMapping("/search")
public class SearchController {
    private final HotelService hotelService;

    public SearchController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(
            summary = "Returns a summary of the found hotels",
            description = "This method receives the parameters and their values, then returns a list of hotels that match the search criteria. " +
                    "Possible parameters: name, brand, city, country, amenities.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The hotels were successfully found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Search by brand=Hilton",
                                            value = "[{\"id\": 1, " +
                                                    "\"name\": \"DoubleTree by Hilton Minsk\", " +
                                                    "\"description\": \"The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...\", " +
                                                    "\"address\": \"9 Pobediteley Avenue, Minsk, 220004, Belarus\", " +
                                                    "\"phone\": \"+375 17 309-80-00\"}]",
                                            description = "Example response for successful brand search"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Objects not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Search by brand=unknown",
                                            value = "{\"error\": \"Data retrieval failed: Objects not found\"}",
                                            description = "Example response for failed search"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public ResponseEntity<Object> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities) {
        try {
            List<HotelShortDetailsResponse> result = hotelService.searchHotels(name, brand, city, country, amenities);
            return ResponseEntity.ok(result);
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
        }
    }
}
