package be.vdab.demo.productcatalogue.services;

import be.vdab.demo.productcatalogue.exceptions.ProductNotFoundException;
import be.vdab.demo.productcatalogue.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {

    //{productId, Product}
    private Map<String, Product> fakeProductDAO = new HashMap<>();

    public ProductService() {
        fakeProductDAO.put("1", new Product("1", "Widget", "Premium ACME Widgets", new BigDecimal(1.20)));
        fakeProductDAO.put("2", new Product("2", "Sprocket", "Grade B sprockets", new BigDecimal(4.10)));
        fakeProductDAO.put("3", new Product("3", "Anvil", "Large Anvils", new BigDecimal(45.50)));
        fakeProductDAO.put("4", new Product("4", "Cogs", "Grade Y cogs", new BigDecimal(1.80)));
        fakeProductDAO.put("5", new Product("5", "Multitool", "Multitools", new BigDecimal(154.10)));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(fakeProductDAO.values());
    }

    public Product getProduct(String id) throws ProductNotFoundException {
        return Optional.ofNullable(fakeProductDAO.get(id))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with productId: " + id));
    }

}
