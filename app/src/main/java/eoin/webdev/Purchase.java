package eoin.webdev;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchaseid")
    private Long purchaseid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customerid")
    private Customer purchasecustomer;
    private Date purchasetime;

    public List<PurchaseProduct> getProducts() {
        return products;
    }

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseProduct> products;

    public Purchase(Long purchaseid, Date purchasetime, Customer purchasecustomer) {

    }

    public Customer getPurchasecustomer() {
        return purchasecustomer;
    }

    public void setPurchasecustomer(Customer purchasecustomer) {
        this.purchasecustomer = purchasecustomer;
    }

    public Purchase() {

    }

    public Long getPurchaseid() {
        return purchaseid;
    }

    public Date getPurchasetime() {
        return purchasetime;
    }

    public void setPurchasetime(Date purchasetime) {
        this.purchasetime = purchasetime;
    }

}
