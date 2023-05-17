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
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication implements CommandLineRunner {

    private final BillRepository billRepository;
    private final ProductItemRepository productItemRepository;
    private final CustomerRestclient customerRestclient;
    private final InventoryRestClient inventoryRestClient;
    private final RepositoryRestConfiguration repositoryRestConfiguration;

    public BillingServiceApplication(BillRepository billRepository, ProductItemRepository productItemRepository, CustomerRestclient customerRestclient, InventoryRestClient inventoryRestClient, RepositoryRestConfiguration repositoryRestConfiguration) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestclient = customerRestclient;
        this.inventoryRestClient = inventoryRestClient;
        this.repositoryRestConfiguration = repositoryRestConfiguration;
    }

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repositoryRestConfiguration.exposeIdsFor(Bill.class);
        repositoryRestConfiguration.exposeIdsFor(ProductItem.class);
        Customer customer = customerRestclient.getCustomerById(1L);
        Bill bill = billRepository.save(new Bill(null, new Date(), null, customer.getId(), customer));
        PagedModel<Product> productPagedModel = inventoryRestClient.pageProducts();
        productPagedModel.forEach(p -> {
            ProductItem productItem = ProductItem.builder()
                    .price(p.getPrice())
                    .quantity(1 + new Random().nextInt(100))
                    .bill(bill)
                    .productID(p.getId())
                    .build();
            productItemRepository.save(productItem);
            System.out.println(p);
        });
    }
}
