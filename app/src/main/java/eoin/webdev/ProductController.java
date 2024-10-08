package eoin.webdev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static eoin.webdev.BasketController.CUSTOMER_ACCOUNT_COOKIE_NAME;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String catalogue(Model model, @CookieValue(value=CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String customerCookieValue) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("isLoggedIn", !customerCookieValue.isEmpty());
        return "catalogue";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model model, @CookieValue(value=CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String customerCookieValue) {
        Optional<Product> product = productRepository.findById((long) id);
        model.addAttribute("product", product.orElse(null));
        model.addAttribute("isLoggedIn", !customerCookieValue.isEmpty());
        return "product";
    }
}
