package com.project.hotels.repository;

import com.project.hotels.model.Hotel;
import com.project.hotels.model.HotelAmenity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> byFilters(String name, String brand, String city, String country, List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%"));
            }

            if (brand != null && !brand.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase().trim() + "%"));
            }

            if (city != null && !city.isEmpty()) {
                Join<Object, Object> addressJoin = root.join("address");
                Join<Object, Object> cityJoin = addressJoin.join("city");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(cityJoin.get("name")), "%" + city.toLowerCase().trim() + "%"));
            }

            if (country != null && !country.isEmpty()) {
                Join<Object, Object> addressJoin = root.join("address");
                Join<Object, Object> cityJoin = addressJoin.join("city");
                Join<Object, Object> countryJoin = cityJoin.join("country");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(countryJoin.get("name")), "%" + country.toLowerCase().trim() + "%"));
            }

            if (amenities != null && !amenities.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<HotelAmenity> hotelAmenityRoot = subquery.from(HotelAmenity.class);

                subquery.select(hotelAmenityRoot.get("hotel").get("id"))
                        .where(hotelAmenityRoot.get("amenity").get("name").in(amenities))
                        .groupBy(hotelAmenityRoot.get("hotel").get("id"))
                        .having(criteriaBuilder.equal(criteriaBuilder.count(hotelAmenityRoot), amenities.size()));

                predicate = criteriaBuilder.and(predicate, root.get("id").in(subquery));
            }

            return predicate;
        };
    }
}
