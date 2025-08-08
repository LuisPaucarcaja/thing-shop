package MicroservicePago.feature.shipping.repository;

import MicroservicePago.feature.shipping.entity.ShippingCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingCityRepository extends JpaRepository<ShippingCity, Long> {

    @Query("SELECT c FROM ShippingCity c JOIN FETCH c.shippingDistricts d WHERE c.active = true AND d.active = true")
    List<ShippingCity> findActiveCitiesAndDistricts();
}
