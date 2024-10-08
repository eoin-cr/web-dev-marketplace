package eoin.webdev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.Date;

import static eoin.webdev.BasketController.BASKET_COOKIE_NAME;
import static eoin.webdev.BasketController.CUSTOMER_ACCOUNT_COOKIE_NAME;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseProductRepository purchaseProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private final BasketController basketController;
    @Autowired
    public PurchaseController(BasketController basketController) {
        this.basketController = basketController;
    }

    @GetMapping("/purchase")
    public String purchase(Model model,
                           @CookieValue(value = BASKET_COOKIE_NAME, defaultValue = "") String basketCookieValue,
                           @CookieValue(value=CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String customerCookieValue,
                           HttpServletResponse response) {
        if (customerCookieValue.isEmpty()) {
            model.addAttribute("error", "You must be logged in to purchase an product");
            model.addAttribute("continueTo", "cart");
            return "login";
        }

        Map<Long, Integer> productsInBasket = basketController.parseBasketCookie(basketCookieValue);

        Purchase purchase = new Purchase();
        Optional<Customer> customerOptional = customerRepository.findByHash(customerCookieValue);
        Customer customer = null;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
        }

        purchase.setPurchasecustomer(customer);

        Date purchaseDate = new Date(System.currentTimeMillis());
        purchase.setPurchasetime(purchaseDate);

        purchaseRepository.save(purchase);

        for (Map.Entry<Long, Integer> entry : productsInBasket.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Optional<Product> productOptional = productRepository.findById(productId);
            Product product = null;
            if (productOptional.isPresent()) {
                product = productOptional.get();
            }

            PurchaseProduct purchaseProduct = new PurchaseProduct();
            purchaseProduct.setPurchase(purchase);
            purchaseProduct.setProduct(product);
            purchaseProduct.setQuantity(Long.valueOf(quantity));

            purchaseProductRepository.save(purchaseProduct);
        }

        // Clear the basket cookie after the purchase is completed
        Cookie basketCookie = new Cookie(BASKET_COOKIE_NAME, "");
        response.addCookie(basketCookie);

        return "purchaseConfirm";
    }

    @GetMapping("/allPurchases")
    public String allPurchases(Model model) {
        List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findAll();
        model.addAttribute("purchaseProducts", purchaseProducts);
        return "allPurchases";
    }

    @GetMapping("/customerPurchaseTable")
    public String customerPurchaseTable(@CookieValue(name = CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String customerHash,
                                        Model model) {
        Optional<Customer> customer = customerRepository.findByHash(customerHash);

        if (customer.isEmpty()) {
            model.addAttribute("error", "You must be logged in to view your purchases");
            model.addAttribute("continueTo", "customerPurchaseTable");
            return "login";
        }

        List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findByPurchase_Purchasecustomer(customer.get());
        model.addAttribute("purchaseProducts", purchaseProducts);
        return "customerPurchaseTable";
    }
}
