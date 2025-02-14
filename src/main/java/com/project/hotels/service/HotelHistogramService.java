package com.project.hotels.service;

import com.project.hotels.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelHistogramService {
    private final EntityManager entityManager;

    public HotelHistogramService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Map<String, Integer> getHotelHistogram(String param) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Hotel> root = query.from(Hotel.class);

        Expression<String> groupByField;
        Join<Hotel, Address> addressJoin;
        Join<Address, City> cityJoin;

        switch (param.toLowerCase()) {
            case "brand":
                groupByField = root.get("brand");
                break;
            case "city":
                addressJoin = root.join("address", JoinType.RIGHT);
                cityJoin = addressJoin.join("city", JoinType.RIGHT);
                groupByField = cityJoin.get("name");
                break;
            case "country":
                addressJoin = root.join("address", JoinType.RIGHT);
                cityJoin = addressJoin.join("city", JoinType.RIGHT);
                Join<City, Country> countryJoin = cityJoin.join("country", JoinType.RIGHT);
                groupByField = countryJoin.get("name");
                break;
            case "amenities":
                return getAmenitiesHistogram(cb, query);
            default:
                throw new IllegalArgumentException("Invalid parameter: " + param);
        }

        return getResults(groupByField, cb, cb.count(root), query);
    }

    private Map<String, Integer> getAmenitiesHistogram(CriteriaBuilder cb, CriteriaQuery<Object[]> query) {
        Root<HotelAmenity> root = query.from(HotelAmenity.class);
        Join<HotelAmenity, Amenity> amenityJoin = root.join("amenity", JoinType.RIGHT);

        Expression<Long> countValue = cb.countDistinct(root.get("hotel").get("id"));

        return getResults(amenityJoin.get("name"), cb, countValue, query);
    }

    private Map<String, Integer> getResults(Expression<String> groupByField, CriteriaBuilder cb,
                                            Expression<Long> countValue, CriteriaQuery<Object[]> query) {
        query.multiselect(groupByField, countValue);
        query.groupBy(groupByField);
        List<Object[]> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Long) row[1]).intValue(),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }
}
