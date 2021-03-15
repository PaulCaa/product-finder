package ar.com.pablocaamano.product.finder.service;

import ar.com.pablocaamano.product.finder.model.Category;
import ar.com.pablocaamano.product.finder.model.Product;
import ar.com.pablocaamano.product.finder.utils.Constants;
import ar.com.pablocaamano.product.finder.utils.Mapper;
import ar.com.pablocaamano.product.finder.utils.RestConnector;

import java.util.*;

/**
 * @author Pablo Caamaño
 */
public class MLService {

    private Map<String, Integer> brandCounts;
    private Map<String,Float> brandAmounts;

    public MLService() {
        this.brandCounts = new HashMap<>();
        this.brandAmounts = new HashMap<>();
    }

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

    /**
     * Se obtiene lista de los productos por categoria
     * @param idCategory String
     * @return List<Product>
     */
    public List<Product> listProduct(String idCategory) {
        int countItems = 0;
        int totalItems  = 0;
        List<Product> prodList = new ArrayList<>();
        RestConnector conn = RestConnector.getInstance();
        List<String> params = new ArrayList<>();
        params.add("category");
        params.add(idCategory);
        params.add("offset");
        params.add(String.valueOf(countItems));
        while(countItems <= totalItems && countItems < Constants.MAX_PAGE_CANT) {
            System.out.println("Listando productos desde " + countItems + " en categoria "+ idCategory);
            try {
                Map<String, Object> resp = (Map<String, Object>) conn.get(Constants.ML_ITEMS , params);
                Map<String, Integer> pag = (Map<String, Integer>) resp.get("paging");
                totalItems = pag.get("total");
                List<Map<String, Object>> products = (List<Map<String, Object>>) resp.get("results");
                countItems += products.size();
                for (Map<String, Object> p : products) {
                    Product prod = Mapper.toProduct(p);
                    // SE VALIDA QUE LA MOTO SEA NUEVA
                    if (prod.getCondition().equalsIgnoreCase("new")) {
                        countBrandsAndPrices(prod);
                        prodList.add(Mapper.toProduct(p));
                    }
                }
            } catch(Exception exception){
                System.out.println("Error al consultar los productos de la categoria: " + idCategory + "y la pagina: " + countItems);
                System.out.println(exception.getMessage());
            }
            int paramIx = params.indexOf("offset");
            params.set(++paramIx,String.valueOf(countItems));
        }
        System.out.println("Se listaron " + prodList.size() + " productos");
        return prodList;
    }

    /**
     * Se cuentan los registros por marcas y se acumula el precio
     * @param p Product
     */
    private void countBrandsAndPrices(Product p) {
        String brand = p.getBrand();
        Float price = Float.valueOf(p.getPrice());
        if(p.getCurrency().equalsIgnoreCase("USD")) {
            brand += " [USD]";
        }
        brand = brand.toUpperCase();
        if(this.brandCounts.containsKey(brand) && this.brandAmounts.containsKey(brand)){
            Integer cant = this.brandCounts.get(brand);
            cant++;
            this.brandCounts.replace(brand,cant);
            Float amount = this.brandAmounts.get(brand);
            amount += price;
            this.brandAmounts.replace(brand,amount);
        } else {
            this.brandCounts.put(brand,1);
            this.brandAmounts.put(brand,price);
        }
    }

    /**
     * Se imprimen en consola precios promedios por marca
     */
    public void showStatistics() {
        System.out.println("\n\n|***********************************************|\n|***************** RESULTADOS ******************|");
        System.out.println("|\t\tMARCA\t\t|\t\tPRECIO PROMEDIO\t\t|");
        for(String key : this.brandCounts.keySet()){
            Integer cant = this.brandCounts.get(key);
            Float totalPrice = this.brandAmounts.get(key);
            System.out.println("|\t\t" + key +  "\t\t->\t\t$ " + totalPrice/cant);
        }
    }

    /**
     * Se calculan promedios por marca
     * @return Map<String, Float> => key = brand ; value = average price
     */
    public Map<String, Float> getStatistics() {
        Map<String, Float> avgs = new HashMap<>();
        for(String key : this.brandCounts.keySet()){
            Integer cant = this.brandCounts.get(key);
            Float totalPrice = this.brandAmounts.get(key);
            Float avg = totalPrice / cant;
            avgs.put(key,avg);
        }
        return avgs;
    }
}
