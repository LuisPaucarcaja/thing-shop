package MicroservicePago.feature.shipping.repository;

import MicroservicePago.feature.shipping.entity.ShippingDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingDistrictRepository extends JpaRepository<ShippingDistrict, Long> {
}
