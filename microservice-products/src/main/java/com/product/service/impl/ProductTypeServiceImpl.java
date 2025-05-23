package com.product.service.impl;

import com.product.model.dto.request.ProductTypeRequest;
import com.product.model.dto.response.ProductTypeResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.repository.ProductType;
import com.product.repository.IProductTypeRepository;
import com.product.service.IProductTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductTypeServiceImpl implements IProductTypeService {

    private final ModelMapper modelMapper;
    private final IProductTypeRepository IProductTypeRepository;

    public ProductTypeServiceImpl(IProductTypeRepository IProductTypeRepository, ModelMapper modelMapper) {
        this.IProductTypeRepository = IProductTypeRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public List<ProductTypeResponse> getAllProductsTypes() {
       return IProductTypeRepository.findAll()
               .stream()
               .map(productType -> modelMapper.map(productType, ProductTypeResponse.class))
               .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductTypeResponse> getProductTypeById(Long id) throws ResourceNotFoundException {
       ProductType productType = IProductTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No productType found with the id: " + id));
        return Optional.of(modelMapper.map(productType, ProductTypeResponse.class));
    }


    @Override
    public ProductTypeResponse createProductType(ProductTypeRequest productTypeRequest) throws ResourceNotFoundException {
      ProductType productType = new ProductType();
      productType.setName(productTypeRequest.getName());

      IProductTypeRepository.save(productType);

      return modelMapper.map(productType, ProductTypeResponse.class);
    }

    @Override
    public Optional<ProductTypeResponse> updateProductType(Long id, ProductTypeRequest productTypeRequest) throws ResourceNotFoundException {
       Optional<ProductType> productTypeFounded = Optional.ofNullable(IProductTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product type not found with ID: " + id)));
       productTypeFounded.ifPresent(productType -> {
           productType.setName(productTypeRequest.getName());

           IProductTypeRepository.save(productType);
       });

       return Optional.of(modelMapper.map(productTypeFounded, ProductTypeResponse.class));
    }

    @Override
    public void deleteProductType(Long id) throws ResourceNotFoundException {
       Optional<ProductType> productType =  IProductTypeRepository.findById(id);
       if (!productType.isPresent()){
           throw  new ResourceNotFoundException("The productType cannot be deleted with the id: " + id);
       }
        IProductTypeRepository.deleteById(id);

    }


    }

