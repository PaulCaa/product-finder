package ar.com.pablocaamano.product.finder.model;

import java.util.Map;
import java.util.Objects;

/**
 * @author Pablo Caamaño
 */
public class Category {
    private String id;
    private String name;
    private String totalItems;

    public Category() {
        this.totalItems = "0";
    }

    public Category(String id, String name, String totalItems) {
        this.id = id;
        this.name = name;
        this.totalItems = totalItems;
    }

    public static Category map(Map<String, Object> input) {
        Category c = new Category();
        c.setId(input.get("id").toString());
        c.setName(input.get("name").toString());
        c.setTotalItems(input.get("total_items_in_this_category").toString());
        return c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", totalItems='" + totalItems + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(totalItems, category.totalItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, totalItems);
    }
}
