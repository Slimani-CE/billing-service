package ma.enset.distributedsystems.billingservice;

import ma.enset.distributedsystems.billingservice.entities.Bill;
import ma.enset.distributedsystems.billingservice.entities.ProductItem;
import ma.enset.distributedsystems.billingservice.feign.CustomerRestclient;
import ma.enset.distributedsystems.billingservice.feign.InventoryRestClient;
import ma.enset.distributedsystems.billingservice.model.Customer;
import ma.enset.distributedsystems.billingservice.model.Product;
import ma.enset.distributedsystems.billingservice.repositories.BillRepository;
import ma.enset.distributedsystems.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication implements CommandLineRunner {

    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestclient customerRestclient;
    private InventoryRestClient inventoryRestClient;

    public BillingServiceApplication(BillRepository billRepository, ProductItemRepository productItemRepository, CustomerRestclient customerRestclient, InventoryRestClient inventoryRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestclient = customerRestclient;
        this.inventoryRestClient = inventoryRestClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Customer customer = customerRestclient.getCustomerById(1L);
        Bill bill = billRepository.save(new Bill(null, new Date(), null, customer.getId(), customer));
        PagedModel<Product> productPagedModel = inventoryRestClient.pageProducts(1, 10);
        productPagedModel.forEach(p -> {
            ProductItem productItem = ProductItem.builder()
                    .price(p.getPrice())
                    .quantity(1 + new Random().nextInt(100))
                    .bill(bill)
                    .productID(p.getId())
                    .build();
        });
    }
}
