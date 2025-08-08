package ProductMicroservice.feature.cart.repository;

import ProductMicroservice.feature.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

}
