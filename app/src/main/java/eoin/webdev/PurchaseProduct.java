package eoin.webdev;

import javax.persistence.*;

@Entity
@Table(name="purchaseproduct")
public class PurchaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseproductid;
    @ManyToOne
    @JoinColumn(name="purchaseid")
    private Purchase purchase;
    @ManyToOne
    @JoinColumn(name="productid")
    private Product product;
    private Long quantity;

    public PurchaseProduct(Long purchaseproductid, Purchase purchase, Product product, Long quantity) {

    }

    public PurchaseProduct() {

    }

    public Long getPurchaseproductid() {
        return purchaseproductid;
    }

    public void setPurchaseproductid(Long purchaseproductid) {
        this.purchaseproductid = purchaseproductid;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
