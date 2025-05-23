package com.product.service.impl;

import com.product.exception.NegativeAmountException;
import com.product.exception.UnAuthorizedException;
import com.product.model.dto.request.ProductRequest;
import com.product.model.dto.request.UpdateStockRequest;
import com.product.model.dto.response.ProductResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.model.dto.response.UpdateStockResponse;
import com.product.repository.Product;
import com.product.repository.ProductType;
import com.product.repository.IProductRepository;
import com.product.repository.IProductTypeRepository;
import com.product.service.IProductService;
import com.product.service.client.UserClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements IProductService {

    private final ModelMapper modelMapper;
    private final IProductRepository IProductRepository;
    private final IProductTypeRepository IProductTypeRepository;
    private final UserClient userClient;

    public ProductServiceImpl(IProductRepository IProductRepository, IProductTypeRepository IProductTypeRepository, ModelMapper modelMapper, UserClient userClient) {

        this.IProductRepository = IProductRepository;
        this.IProductTypeRepository = IProductTypeRepository;
        this.modelMapper = modelMapper;
        this.userClient = userClient;

    }


    public List<ProductResponse> getAllProducts() {
        return IProductRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }


    public Optional<ProductResponse> getProductById(Long id) throws ResourceNotFoundException {
        Product productFound = IProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No product found with the id: " + id));
        return Optional.of(modelMapper.map(productFound, ProductResponse.class));


    }


    public ProductResponse createProduct(ProductRequest productRequest) throws ResourceNotFoundException {
        ProductType productTypeFound = IProductTypeRepository.findById(productRequest.getProductTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("No type found with the id: " + productRequest.getProductTypeId()));


        Product newProduct = new Product();

        newProduct.setName(productRequest.getName());
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setStock(productRequest.getStock());
        newProduct.setProductType(productTypeFound);


        Product productSave = IProductRepository.save(newProduct);

        return modelMapper.map(productSave, ProductResponse.class);

    }


    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) throws ResourceNotFoundException, UnAuthorizedException {

        Optional<Product> productFounded = Optional.ofNullable(IProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no product to update with ID:" + id)));
        ProductType productType = IProductTypeRepository.findById(productRequest.getProductTypeId()).orElseThrow(() -> new ResourceNotFoundException("Product type not found with ID: " + productRequest.getProductTypeId()));

        productFounded.ifPresent(product -> {
            product.setStock(productRequest.getStock());
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setProductType(productType);
            IProductRepository.save(product);
        });


        return Optional.of(modelMapper.map(productFounded, ProductResponse.class));


    }


    public void deleteProduct(Long id) throws ResourceNotFoundException {

        Optional<Product> productFound = IProductRepository.findById(id);

        if (!productFound.isPresent()) {
            throw new ResourceNotFoundException("The product cannot be deleted with the id: " + id);
        }

        IProductRepository.deleteById(id);

    }

    public UpdateStockResponse updateStock(Long idProduct, UpdateStockRequest updateStockRequest) throws UnAuthorizedException, ResourceNotFoundException {

        UpdateStockRequest request = new UpdateStockRequest();

        Product product = IProductRepository.findById(idProduct).orElseThrow(() -> new ResourceNotFoundException("No product found with the id: " + idProduct));

        product.setStock(updateStockRequest.getNewStock());

        Product updatedProduct = IProductRepository.save(product);

        return modelMapper.map(updatedProduct, UpdateStockResponse.class);
    }

    public void reduceStock(Long id, UpdateStockRequest request) throws ResourceNotFoundException, NegativeAmountException {
        if (request.getNewStock() <= 0) {
            throw new NegativeAmountException("The amount to reduce cannot be negative.");
        }

        Product productFound = IProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no product to reduce stock with ID:" + id));

        if (productFound.getStock() >= request.getNewStock()) {
            productFound.setStock(productFound.getStock() - request.getNewStock());
            IProductRepository.save(productFound);
        } else {
            throw new NegativeAmountException("Not enough stock to reduce by the specified amount.");
        }
    }


}



