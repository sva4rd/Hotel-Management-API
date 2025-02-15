package com.project.hotels.controller;

import com.project.hotels.dto.CreateHotelRequest;
import com.project.hotels.dto.ErrorResponse;
import com.project.hotels.dto.HotelFullDetailsResponse;
import com.project.hotels.dto.HotelShortDetailsResponse;
import com.project.hotels.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Hotel controller",
        description = "Controller for receiving and creating hotel entities")
@RestController
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(
            summary = "Returns a summary of the found hotels",
            description = "This method returns a list of all hotels")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The hotels were successfully found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Receive all hotels",
                                            value = "[{\"id\": 1, " +
                                                    "\"name\": \"DoubleTree by Hilton Minsk\", " +
                                                    "\"description\": \"The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...\", " +
                                                    "\"address\": \"9 Pobediteley Avenue, Minsk, 220004, Belarus\", " +
                                                    "\"phone\": \"+375 17 309-80-00\"}]",
                                            description = "Example response for successful receive"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public List<HotelShortDetailsResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @Operation(
            summary = "Returns a summary of the found hotels",
            description = "This method returns a list of all hotels")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The hotels were successfully found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelFullDetailsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Object not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Search by id=0",
                                            value = "{\"error\": \"Data retrieval failed: Object not found\"}",
                                            description = "Example response for failed search"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getHotelById(@PathVariable Long id) {
        try{
            HotelFullDetailsResponse response = hotelService.getHotelById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
        }
    }

    @Operation(
            summary = "Creates a hotel",
            description = "This method creates a hotel and returns its summary data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The hotel was successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortDetailsResponse.class)
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
    @PostMapping
    public ResponseEntity<Object> createHotel(@Valid @RequestBody CreateHotelRequest request) {
        try {
            HotelShortDetailsResponse response = hotelService.createHotel(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error.getMessage()));
        }
    }

    @Operation(
            summary = "Append amenities",
            description = "This method append amenities to the hotel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The amenities were successfully appended"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Append to the hotel with id=0",
                                            value = "{\"error\": \"Error: Hotel not found\"}",
                                            description = "Example response for failed appending"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/{id}/amenities")
    public ResponseEntity<Object> addAmenitiesToHotel(@PathVariable Long id, @RequestBody List<String> amenities) {
        try {
            hotelService.addAmenitiesToHotel(id, amenities);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
        }
    }
}
