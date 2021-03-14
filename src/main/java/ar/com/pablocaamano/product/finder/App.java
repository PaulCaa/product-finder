package ar.com.pablocaamano.product.finder;


import ar.com.pablocaamano.product.finder.model.Category;
import ar.com.pablocaamano.product.finder.service.MLService;

/**
 * @author Pablo Caamaño
 */
public class App {
    public static void main(String[] args) {

        MLService ml = new MLService();
        Category c = ml.obtainCategory("Motos");
        System.out.println("Categoria: " + c.toString());
        if(c.getId() != null) {
            ml.listProduct(c.getId());
        }
    }

}