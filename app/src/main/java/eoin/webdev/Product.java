package eoin.webdev;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {
    @Id
    private Long productid;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean hidden;

    public List<PurchaseProduct> getProductPurchases() {
        return productPurchases;
    }

    public void setProductPurchases(List<PurchaseProduct> productPurchases) {
        this.productPurchases = productPurchases;
    }

    @OneToMany(mappedBy = "product")
    private List<PurchaseProduct> productPurchases;

    public Product(Long productid, String name, String description, BigDecimal price, boolean hidden) {

    }
    public Product() {

    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
