package eoin.webdev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static eoin.webdev.BasketController.CUSTOMER_ACCOUNT_COOKIE_NAME;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customer")
    public String index(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "user-tmp";
    }

    @GetMapping("/login")
    public String login( @RequestParam(value = "continueTo", defaultValue = "") String continueTo, Model model) {
        model.addAttribute("continueTo", continueTo);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "continueTo", required = false) String continueTo,
                        @CookieValue(value = CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String basketCookieValue,
                        HttpServletResponse response,
                        Model model) {

        Optional<Customer> customer = customerRepository.findByUsername(username);
        if (customer.isPresent() && customer.get().getPassword().equals(password)) {
            Cookie customerCookie = new Cookie(CUSTOMER_ACCOUNT_COOKIE_NAME, customer.get().getHash());
            response.addCookie(customerCookie);
            return "redirect:/" + continueTo;
        }

        model.addAttribute("error", "Invalid login credentials");
        model.addAttribute("continueTo", continueTo);
        return "login";
    }

    @PostMapping("/logout")
    public String login(HttpServletResponse response) {

        Cookie customerCookie = new Cookie(CUSTOMER_ACCOUNT_COOKIE_NAME, "");
        response.addCookie(customerCookie);

        return "redirect:/";
    }
    @GetMapping("/signup")
    public String signupGet( @RequestParam(value = "continueTo", defaultValue = "") String continueTo, Model model) {
        model.addAttribute("continueTo", continueTo);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "continueTo", required = false) String continueTo,
                        @CookieValue(value = CUSTOMER_ACCOUNT_COOKIE_NAME, defaultValue = "") String customerCookieValue,
                        HttpServletResponse response,
                        Model model) {

        Optional<Customer> customerOptional = customerRepository.findByUsername(username);
        if (customerOptional.isPresent()) {
            model.addAttribute("error", "There already exists a user with this username");
            model.addAttribute("continueTo", continueTo);
            return "signup";
        }

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setHash(org.apache.commons.codec.digest.DigestUtils.sha256Hex(username + password));
        customerRepository.save(customer);

        Cookie customerCookie = new Cookie(CUSTOMER_ACCOUNT_COOKIE_NAME, customer.getHash());
        response.addCookie(customerCookie);
        return "redirect:/" + continueTo;
    }
}
