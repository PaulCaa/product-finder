package ar.com.pablocaamano.product.finder.utils;

import ar.com.pablocaamano.product.finder.model.Product;

import java.util.List;
import java.util.Map;

/**
 * @author Pablo Caamaño
 */
public class Mapper {

    public static Product toProduct(Map<String,Object> input) {
        Product p = new Product();
        p.setId(input.get("id").toString());
        p.setCurrency(input.get("currency_id").toString());
        p.setPrice(input.get("price").toString());
        p.setCondition(input.get("condition").toString());
        List<Map<String,Object>> atts = (List<Map<String, Object>>) input.get("attributes");
        if(atts != null || !atts.isEmpty()) {
            for (Map<String, Object> a : atts) {
                if (a.get("id").toString().equalsIgnoreCase("BRAND")) p.setBrand(a.get("value_name").toString());
            }
        }
        return p;
    }
}
