# Product-finder

Aplicacion de consola Java que consulta en API de mercadolibre productos por categoria y calcula precios promedios segmentando por marca.

### Endpoints

- Para obtener categorias se usan los siguientes endpoints expuestos:
```
  https://api.mercadolibre.com/sites/${SITE}/categories
  https://api.mercadolibre.com/categories/${CATEGORY_ID}
```

- Para listar los productos de la categoria consultada se utiliza el siguiente endpoint:
```
  https://api.mercadolibre.com/sites/${SITE}/search?category=${CATEGORY_ID}
```


### Ejecución

La app se puede ejecutar importando el proyecto en su IDE de preferencia o mediante comandos Maven. Para esto ejecutar lo siguiente:
```bash
  mvn clean install
  mvn exec:java -Dexec.mainClass=ar.com.pablocaamano.product.finder.App
```
