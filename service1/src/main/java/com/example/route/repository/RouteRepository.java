package com.example.route.repository;

import com.example.route.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByNameContainingIgnoreCase(String name);
    List<Route> findByDistance(Long distance);
    List<Route> findByDistanceGreaterThan(Long distance);
    List<Route> findByDistanceGreaterThanEqual(Long distance);
    List<Route> findByDistanceLessThan(Long distance);
    List<Route> findByDistanceLessThanEqual(Long distance);
    List<Route> findByCreationDateBefore(LocalDate date);
    List<Route> findByCreationDateAfter(LocalDate date);

    @Query("SELECT DISTINCT r.distance FROM Route r WHERE r.distance IS NOT NULL ORDER BY r.distance ASC")
    List<Long> findDistinctDistances();

    @Query("SELECT COUNT(r) FROM Route r WHERE r.distance < :value")
    long countByDistanceLessThan(@Param("value") Long value);

    @Query("DELETE FROM Route r WHERE r.distance = :value")
    long deleteByDistance(@Param("value") Long value);
}
