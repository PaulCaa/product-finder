package ar.com.pablocaamano.product.finder;


import ar.com.pablocaamano.product.finder.model.Category;
import ar.com.pablocaamano.product.finder.model.Product;
import ar.com.pablocaamano.product.finder.service.MLService;

import java.util.List;

/**
 * @author Pablo Caamaño
 */
public class App {
    public static void main(String[] args) {

        MLService ml = new MLService();
        Category c = ml.obtainCategory("Motos");
        System.out.println("Resultado: " + c.toString());
        if(c.getId() != null) {
            List<Product> prods = ml.listProduct(c.getId());
            ml.showStatistics();
        }
    }

}