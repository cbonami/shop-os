package be.vdab.demo.productcatalogue.resources;

import be.vdab.demo.productcatalogue.exceptions.ProductNotFoundException;
import be.vdab.demo.productcatalogue.model.Product;
import be.vdab.demo.productcatalogue.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping("{id}")
    public Product getProduct(@PathVariable("id") String id) throws ProductNotFoundException {
        return productService.getProduct(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleProductNotFound(ProductNotFoundException snfe) {
    }
}
