package ma.enset.distributedsystems.billingservice.repositories;

import ma.enset.distributedsystems.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    public Collection<ProductItem> findProductItemById(Long id);
}
