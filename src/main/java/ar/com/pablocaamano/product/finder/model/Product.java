package ar.com.pablocaamano.product.finder.model;

import java.util.Objects;

/**
 * @author Pablo Caamaño
 */
public class Product {

    private String id;
    private String currency;
    private String price;
    private String brand;
    private String condition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", currency='" + currency + '\'' +
                ", price='" + price + '\'' +
                ", brand='" + brand + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(currency, product.currency) &&
                Objects.equals(price, product.price) &&
                Objects.equals(brand, product.brand) &&
                Objects.equals(condition, product.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, price, brand, condition);
    }
}
