package com.project.hotels.repository;

import com.project.hotels.model.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> byFilters(String name, String brand, String city, String country, List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            Join<Hotel, Address> addressJoin;
            Join<Address, City> cityJoin;

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%"));
            }

            if (brand != null && !brand.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase().trim() + "%"));
            }

            if (city != null && !city.isEmpty()) {
                addressJoin = root.join("address", JoinType.LEFT);
                cityJoin = addressJoin.join("city", JoinType.LEFT);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(cityJoin.get("name")), "%" + city.toLowerCase().trim() + "%"));
            }

            if (country != null && !country.isEmpty()) {
                addressJoin = root.join("address", JoinType.LEFT);
                cityJoin = addressJoin.join("city", JoinType.LEFT);
                Join<City, Country> countryJoin = cityJoin.join("country", JoinType.LEFT);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(countryJoin.get("name")), "%" + country.toLowerCase().trim() + "%"));
            }

            if (amenities != null && !amenities.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<HotelAmenity> hotelAmenityRoot = subquery.from(HotelAmenity.class);

                List<String> lowerCaseAmenities  = amenities.stream()
                        .map(String::toLowerCase)
                        .toList();

                subquery.select(hotelAmenityRoot.get("hotel").get("id"))
                        .where(criteriaBuilder.lower(hotelAmenityRoot.get("amenity").get("name")).in(lowerCaseAmenities))
                        .groupBy(hotelAmenityRoot.get("hotel").get("id"))
                        .having(criteriaBuilder.equal(criteriaBuilder.count(hotelAmenityRoot), amenities.size()));

                predicate = criteriaBuilder.and(predicate, root.get("id").in(subquery));
            }

            return predicate;
        };
    }
}
