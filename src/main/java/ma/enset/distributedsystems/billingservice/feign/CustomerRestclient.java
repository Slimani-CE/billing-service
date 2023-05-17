package ma.enset.distributedsystems.billingservice.feign;

import ma.enset.distributedsystems.billingservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestclient {
    @GetMapping(path = "/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id);
}
