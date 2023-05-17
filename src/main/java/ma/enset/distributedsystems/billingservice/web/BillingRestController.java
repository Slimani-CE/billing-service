package ma.enset.distributedsystems.billingservice.web;

import ma.enset.distributedsystems.billingservice.entities.Bill;
import ma.enset.distributedsystems.billingservice.feign.CustomerRestclient;
import ma.enset.distributedsystems.billingservice.feign.InventoryRestClient;
import ma.enset.distributedsystems.billingservice.model.Product;
import ma.enset.distributedsystems.billingservice.repositories.BillRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class BillingRestController {

    private final BillRepository billRepository;
    private final CustomerRestclient customerRestclient;
    private final InventoryRestClient inventoryRestClient;

    public BillingRestController(BillRepository billRepository, CustomerRestclient customerRestclient, InventoryRestClient inventoryRestClient) {
        this.billRepository = billRepository;
        this.customerRestclient = customerRestclient;
        this.inventoryRestClient = inventoryRestClient;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable("id") Long id){
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerRestclient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            Product product = inventoryRestClient.getProductById(productItem.getProductID());
            productItem.setProduct(product);
        });
        return bill;
    }

}
