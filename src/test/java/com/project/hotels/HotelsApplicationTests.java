package com.project.hotels;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelsApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnListOfHotels() throws Exception {
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(3))))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("DoubleTree by Hilton Minsk")));
    }

    @Test
    public void shouldReturnHotelById() throws Exception {
        int hotelId = 1;
        mockMvc.perform(get("/hotels/" + hotelId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(hotelId)))
                .andExpect(jsonPath("$.name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$.address.street", is("Pobediteley Avenue")))
                .andExpect(jsonPath("$.arrivalTime.checkIn", is("14:00")));
    }

    @Test
    public void shouldReturnHotelsBySearch() throws Exception {
        String city = "Minsk";
        mockMvc.perform(get("/search").param("city", city))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].address", containsString(city)));
    }

    @Test
    public void shouldCreateNewHotel() throws Exception {
        String newHotelJson = "{ \"name\": \"Test Hotel\", \"brand\": \"Test Brand\", \"description\": \"Test desc\", \"address\": {\"houseNumber\": 10, \"street\": \"Test Street\", \"city\": \"Test City\", \"country\": \"Test Country\", \"postCode\": \"123456\"}, \"contact\": {\"phone\": \"+375 17 309-81-01\", \"email\": \"test@hotel.com\"}, \"arrivalTime\": {\"checkIn\": \"14:00\", \"checkOut\": \"12:00\"}}";

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newHotelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Hotel")))
                .andExpect(jsonPath("$.description", is("Test desc")));
    }

    @Test
    public void shouldReturnHistogramData() throws Exception {
        String param = "amenities";
        mockMvc.perform(get("/histogram/" + param))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['Business center']", is(3)))
                .andExpect(jsonPath("$.['Concierge']", is(3)))
                .andExpect(jsonPath("$.['Swimming Pool']", is(1)));
    }

}
