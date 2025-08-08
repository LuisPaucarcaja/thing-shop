package ProductMicroservice.feature.cart.repository;

import ProductMicroservice.feature.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {
            "cartItems.productVariant.visualVariant.mediaList"
    })
    Cart findByUserId(Long userId);

}
