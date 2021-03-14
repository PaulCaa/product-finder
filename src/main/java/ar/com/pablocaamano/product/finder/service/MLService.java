package ar.com.pablocaamano.product.finder.service;

import ar.com.pablocaamano.product.finder.model.Category;
import ar.com.pablocaamano.product.finder.model.Product;
import ar.com.pablocaamano.product.finder.utils.Constants;
import ar.com.pablocaamano.product.finder.utils.Mapper;
import ar.com.pablocaamano.product.finder.utils.RestConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Pablo Caamaño
 */
public class MLService {

    /**
     * Obtener ID de categoría en MercadoLibre
     * @param catName String
     * @return Category
     */
    public Category obtainCategory(String catName) {
        String catId = null;
        Category category = null;
        System.out.println("Buscando categoria: " + catName);
        RestConnector conn = RestConnector.getInstance();
        List<Map<String, String>> catResp = (List<Map<String, String>>) conn.get(Constants.ML_CATEGORIES, null);
        for (Map<String, String> cat : catResp) {
            String name = cat.get("name");

            if (name.contains(catName)) {
                catId = cat.get("id");
            }
        }

        if(catId != null) {
            Map<Object,Object> subcatResp = (Map<Object, Object>) conn.get(Constants.ML_SUBCATEGORIES + catId, null);
            List<Map<String, Object>> content = (List<Map<String, Object>>) subcatResp.get("children_categories");
            for(Map<String, Object> subcat : content) {
                String subcatName = subcat.get("name").toString();
                if(subcatName.contains(catName)){
                    category = Category.map(subcat);
                }
            }
        }
        return category;
    }

    public List<Product> listProduct(String idCategory) {
        int countPag = 0;
        int totalPag  = 0;
        List<Product> prodList = new ArrayList<>();
        System.out.println("Listando productos en categoria " + idCategory);
        RestConnector conn = RestConnector.getInstance();
        while(countPag <= totalPag && countPag <= 10) {
            List<String> params = new ArrayList<>();
            params.add("offset");
            params.add(String.valueOf(countPag));
            Map<String, Object> resp = (Map<String, Object>) conn.get(Constants.ML_ITEMS + idCategory, params);
            Map<String,Integer> pag = (Map<String, Integer>) resp.get("paging");
            totalPag = pag.get("total");
            countPag++;
            List<Map<String,Object>> products = (List<Map<String, Object>>) resp.get("results");
            for(Map<String,Object> p : products) {
                prodList.add(Mapper.toProduct(p));
            }
        }
        System.out.println("Se listaron" + prodList.size() + " productos");
        return prodList;
    }
}
