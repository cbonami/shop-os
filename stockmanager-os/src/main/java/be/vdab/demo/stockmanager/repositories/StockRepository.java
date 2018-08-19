package be.vdab.demo.stockmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import be.vdab.demo.stockmanager.model.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {
}
