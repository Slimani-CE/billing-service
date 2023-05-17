package ma.enset.distributedsystems.billingservice.feign;

import ma.enset.distributedsystems.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.*;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryRestClient {
    @GetMapping(path = "/products")
    PagedModel<Product> pageProducts() ;

    @GetMapping(path = "/products/{id}")
    Product getProductById(@PathVariable Long id);
}
