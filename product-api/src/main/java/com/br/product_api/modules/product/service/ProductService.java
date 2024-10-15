package com.br.product_api.modules.product.service;

import com.br.product_api.config.exception.SuccessResponse;
import com.br.product_api.config.exception.ValidationException;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.service.CategoryService;
import com.br.product_api.modules.product.dto.*;
import com.br.product_api.modules.product.interfaces.ProductInterface;
import com.br.product_api.modules.product.model.Product;
import com.br.product_api.modules.product.repository.ProductRepository;
import com.br.product_api.modules.sales.client.SalesClient;
import com.br.product_api.modules.sales.dto.SalesConfirmationDTO;
import com.br.product_api.modules.sales.dto.SalesProductResponse;
import com.br.product_api.modules.sales.enums.SalesStatus;
import com.br.product_api.modules.sales.rabbitMq.SalesConfirmationSender;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.service.SupplierService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.br.product_api.config.requestUtils.RequestUtil.getActualRequest;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService implements ProductInterface {

    private static final Integer ZERO = 0;
    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String SERVICE_ID = "serviceId";

    private final ProductRepository repository;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    private final SalesConfirmationSender salesConfirmationSender;
    private final SalesClient salesClient;

    public ProductService(ProductRepository repository,
                          @Lazy SupplierService supplierService, @Lazy CategoryService categoryService, SalesConfirmationSender salesConfirmationSender, SalesClient salesClient) {
        this.repository = repository;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
        this.salesConfirmationSender = salesConfirmationSender;
        this.salesClient = salesClient;
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        validateProductRequest(request);

        var category = categoryService.findCategoryById(request.categoryId());
        var supplier = supplierService.findSupplierById(request.supplierId());

        var newProduct = new Product(request.name(), supplier, category, request.quantity());
        return responseBuilder(repository.save(newProduct));
    }

    @Override
    public List<ProductResponse> findBySupplierId(Integer id) {
        var productsBySupplier = repository.findBySupplierId(id);
        return productListResponseBuilder(productsBySupplier);
    }

    @Override
    public List<ProductResponse> findByCategoryId(Integer id) {
        var productsByCategory = repository.findByCategoryId(id);
        return productListResponseBuilder(productsByCategory);
    }

    @Override
    public List<ProductResponse> findProductByName(String name) {
        var productsByName = repository.findByNameIgnoreCaseContaining(name);
        return productListResponseBuilder(productsByName);
    }

    @Override
    public List<ProductResponse> findAll() {
        var allProducts = repository.findAll();
        return productListResponseBuilder(allProducts);
    }

    @Override
    public ProductResponse findById(Integer id) {
        Product product = repository.findById(id)
                    .orElseThrow(() -> new ValidationException("Product not found."));
        return responseBuilder(product);
    }

    @Override
    public void delete (Integer id) {
        repository.delete(repository.findById(id).orElseThrow(
                () -> new ValidationException("Product not found.")
        ));
    }

    @Override
    public Boolean existsByCategoryId(Integer id) {
        return repository.existsByCategoryId(id);
    }

    @Override
    public Boolean existsBySupplierId(Integer id) {
        return repository.existsBySupplierId(id);
    }

    @Override
    public ProductResponse update(ProductRequest request, Integer id) {
        Product productUpdate = repository.findById(id).orElseThrow(
                () -> new ValidationException("Product not found for the given ID.")
        );

        if (request.name() != productUpdate.getName()) {
            productUpdate.setName(request.name());
        }
        if (request.supplierId() != productUpdate.getSupplier().getId()) {
            productUpdate.setSupplier(supplierService.findSupplierById(productUpdate.getSupplier().getId()));
        }
        if (request.categoryId() != productUpdate.getCategory().getId()) {
            productUpdate.setCategory(categoryService.findCategoryById(productUpdate.getCategory().getId()));
        }
        if (request.quantity() != productUpdate.getQuantity()) {
            productUpdate.setQuantity(request.quantity());
        }
        return responseBuilder(repository.save(productUpdate));
    }

    private ProductResponse responseBuilder(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getQuantity(), product.getCreateAt(),
                new SupplierResponse(product.getSupplier().getId(), product.getSupplier().getName()),
                new CategoryResponse(product.getCategory().getId(), product.getCategory().getDescription()));
    }

    private List<ProductResponse> productListResponseBuilder (List<Product> list) {
        return list.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(),
                        product.getQuantity(), product.getCreateAt(),
                        new SupplierResponse(product.getSupplier().getId(), product.getSupplier().getName()),
                        new CategoryResponse(product.getCategory().getId(), product.getCategory().getDescription())))
                .toList();
    }

    private void validateProductRequest(ProductRequest product) {
        if (isEmpty(product.name())) {
            throw new ValidationException("A product name was not informed.");
        }

        if (isEmpty(product.quantity())) {
            throw new ValidationException("The quantity should be informed.");
        }

        if (product.quantity() < 0) {
            throw new ValidationException("The quantity should not be less to zero.");
        }

        if (isEmpty(product.categoryId())) {
            throw new ValidationException("A category ID was not informed.");
        }

        if (isEmpty(product.supplierId())) {
            throw new ValidationException("A supplier ID was not informed.");
        }
    }

    public ProductSalesResponse findProductSales(Integer id) {
        var product = privateFindById(id);
        var sales = getSalesByProductId(product.getId());
        return salesResponseBuilder(product, sales);
    }

    private ProductSalesResponse salesResponseBuilder(Product product, SalesProductResponse salesList) {
        return new ProductSalesResponse(product.getId(), product.getName(), product.getQuantity(), product.getCreateAt(),
                new SupplierResponse(product.getSupplier().getId(), product.getSupplier().getName()),
                new CategoryResponse(product.getCategory().getId(), product.getCategory().getDescription()),
                salesList.salesIds());
    }

    private SalesProductResponse getSalesByProductId(Integer productId) {
        try {
            var actualRequest = getActualRequest();
            var actualToken = actualRequest.getHeader(AUTHORIZATION);
            var transactionId = actualRequest.getHeader(TRANSACTION_ID);
            var serviceId = actualRequest.getAttribute(SERVICE_ID);

            var response = salesClient
                    .findSalesByProductId(productId, actualToken, transactionId)
                    .orElseThrow(() -> new ValidationException("Sales wasn't founded for this given id product."));

            return response;
        } catch (Exception exception) {
            throw new ValidationException("Sales cannot be found.");
        }
    }

    @Transactional
    public void updateProductStock(StockDTO product) {
        try {
            validateStockStream(product);

            var productsToUpDate = new ArrayList<Product>();
            product.products()
                    .forEach(saleProduct -> {
                        var existProduct = privateFindById(saleProduct.productId());
                        if (saleProduct.quantity() > existProduct.getQuantity()) {
                            throw new ValidationException(String
                                    .format("The product id %s is out of stock.", existProduct.getId()));
                        }
                        existProduct.updateStock(saleProduct.quantity());
                        productsToUpDate.add(existProduct);
                    });
            if (!isEmpty(productsToUpDate)) {
                repository.saveAll(productsToUpDate);
                var confirmationMessage = new SalesConfirmationDTO(product.salesId(), SalesStatus.APPROVED, product.transactionid());
                salesConfirmationSender.sendSalesConfirmation(confirmationMessage);
            }
        } catch (Exception e) {
            salesConfirmationSender
                    .sendSalesConfirmation(new SalesConfirmationDTO(product.salesId(), SalesStatus.REJECTED, product.transactionid()));
        }
    }

    private Product privateFindById(Integer id) {
        Product product = repository.findById(id).get();
        return product;
    }

    private void validateStockStream(StockDTO product) {
        if (isEmpty(product) || isEmpty(product.salesId())) {
            throw new ValidationException("The product data an sales ID cannot be null.");
        }

        if (isEmpty(product.products())) {
            throw new ValidationException("The list of sales must be informed.");
        }

        product.products()
                .forEach(saleProduct -> {
                    if (isEmpty(saleProduct.quantity()) || isEmpty(saleProduct.productId())) {
                        throw new ValidationException("Product ID and quantity must be informed.");
                    }
                });
    }

    public SuccessResponse verifyStock(VerifyStockQuantity request) {
        //var currentRequest = getActualRequest();
        //var transactionId = currentRequest.getHeader(TRANSACTION_ID);
        //var serviceId = currentRequest.getAttribute(SERVICE_ID);
        //log.info("Request to POST Product stock with data {} | [TransactionId: {}] | [serviceId: {}].");

        if (isEmpty(request) || isEmpty(request.products())) {
            throw new ValidationException("Request cannot be null.");
        }

        request.products()
                .forEach(this::validateStock);
        return new SuccessResponse(200, "Stock available.");
    }

    private void validateStock (ProductQuantityDTO productQuantity) {
        if (isEmpty(productQuantity.productId()) || isEmpty(productQuantity.quantity())) {
            throw new ValidationException("Product ID and quantity cannot be empty.");
        }
        var product = privateFindById(productQuantity.productId());

        if (productQuantity.quantity() > product.getQuantity()) {
            throw new ValidationException(String.format("Quantity not available. Product ID: %s", product.getId()));
        }
    }

}
