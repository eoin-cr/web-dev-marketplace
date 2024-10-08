package eoin.webdev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class BasketController {
    @Autowired
    private ProductRepository productRepository;

    public static final String BASKET_COOKIE_NAME = "basket";
    public static final String CUSTOMER_ACCOUNT_COOKIE_NAME = "customer";



    @GetMapping("/cart")
    public String cart(Model model, @CookieValue(value = BASKET_COOKIE_NAME, defaultValue = "") String basketCookieValue, @CookieValue(value=CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String customerCookieValue ){
        Map<Long, Integer> rawMap = parseBasketCookie(basketCookieValue);
        Map<Product, Integer> productMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry: rawMap.entrySet()) {
            Optional<Product> productOption = productRepository.findById(entry.getKey());
            productOption.ifPresent(value -> productMap.put(value, entry.getValue()));
        }
        model.addAttribute("products", productMap);
        model.addAttribute("totalcost", productMap.entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey().getPrice().doubleValue() * entry.getValue())
                .sum());
        model.addAttribute("isLoggedIn", !customerCookieValue.isEmpty());

        return "cart";
    }

    @RequestMapping(value="/addToBasket", method = { RequestMethod.POST, RequestMethod.GET })
    public String addToBasket(@RequestParam("productId") Long productId,
                              @RequestParam(value = "redirect", defaultValue = "") String redirect,
                              HttpServletResponse response,
                              @CookieValue(value = BASKET_COOKIE_NAME, defaultValue = "") String basketCookieValue) {
        Map<Long, Integer> basket = parseBasketCookie(basketCookieValue);
        basket.put(productId, basket.getOrDefault(productId, 0) + 1);
        updateBasketCookie(basket, response);
        return "redirect:/" + redirect;
    }

    @GetMapping("/removeFromBasket")
    public String removeFromBasket(@RequestParam("productId") Long productId,
                                   HttpServletResponse response,
                                   @CookieValue(value = BASKET_COOKIE_NAME, defaultValue = "") String basketCookieValue) {
        Map<Long, Integer> basket = parseBasketCookie(basketCookieValue);
        basket.remove(productId);
        updateBasketCookie(basket, response);
        return "redirect:/cart";
    }

    @GetMapping("/decrementInBasket")
    public String decrementInBasket(@RequestParam("productId") Long productId,
                                    HttpServletResponse response,
                                    @CookieValue(value = BASKET_COOKIE_NAME, defaultValue = "") String basketCookieValue) {
        Map<Long, Integer> basket = parseBasketCookie(basketCookieValue);
        if (basket.get(productId) == 1) {
            basket.remove(productId);
        } else {
            basket.put(productId, basket.get(productId) - 1);
        }
        updateBasketCookie(basket, response);
        return "redirect:/cart";
    }

    public Map<Long, Integer> parseBasketCookie(String basketCookieValue) {
        Map<Long, Integer> basket = new HashMap<>();
        if (!basketCookieValue.isEmpty()) {
            String[] products = basketCookieValue.split("/");
            for (String product : products) {
                String[] parts = product.split(":");
                Long productId = (long) Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                basket.put(productId, quantity);
            }
        }
        return basket;
    }

    private void updateBasketCookie(Map<Long, Integer> basket, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Integer> entry : basket.entrySet()) {
            if (!sb.isEmpty()) {
                sb.append("/");
            }
            sb.append(entry.getKey()).append(":").append(entry.getValue());
        }
        Cookie basketCookie = new Cookie(BASKET_COOKIE_NAME, sb.toString());
        response.addCookie(basketCookie);
    }
}
