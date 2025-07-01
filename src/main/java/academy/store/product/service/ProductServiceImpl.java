package academy.store.product.service;

import academy.store.product.entity.Category;
import academy.store.product.entity.Product;
import academy.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATE");
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            return null;
        }

        return productRepository.findById(product.getId())
                .map(productDB -> {
                    if (product.getName() != null) productDB.setName(product.getName());
                    if (product.getDescription() != null) productDB.setDescription(product.getDescription());
                    if (product.getCategory() != null) productDB.setCategory(product.getCategory());
                    if (product.getPrice() != null) productDB.setPrice(product.getPrice());

                    return productRepository.save(productDB);
                })
                .orElse(null);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product productDB = getProduct(id);
        if (null == productDB) {
            return null;
        }
        productDB.setStatus("DELETED");
        return productRepository.save(productDB);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product productDB = getProduct(id);
        if (null == productDB) {
            return null;
        }
        Double stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return productRepository.save(productDB);
    }
}
