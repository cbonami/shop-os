package be.vdab.demo.shopfront.resources;

import be.vdab.demo.shopfront.model.Product;
import be.vdab.demo.shopfront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @RequestMapping()
    public List<Product> getProducts() {
        return productService.getProducts();
    }
}
