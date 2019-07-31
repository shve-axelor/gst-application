package com.axelor.gst.service;

import java.util.List;

import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;

public class ProductServiceImpl implements ProductService {
  @Override
  public List<Product> getSelectedProducts(List<Integer> ids) {
    List<Product> productlists = Beans.get(ProductRepository.class).all().filter("self.id in (?1)", ids).fetch();   
    return productlists;
  }
}
