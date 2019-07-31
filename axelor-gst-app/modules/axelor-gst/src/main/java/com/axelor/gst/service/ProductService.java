package com.axelor.gst.service;

import java.util.List;

import com.axelor.gst.db.Product;

public interface ProductService {
  public List<Product> getSelectedProducts(List<Integer> ids);
}
