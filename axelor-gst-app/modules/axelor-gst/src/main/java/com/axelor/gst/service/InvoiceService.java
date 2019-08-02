package com.axelor.gst.service;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;

public interface InvoiceService {
  public Invoice calculateInvoiceFields(Invoice invoice, List<InvoiceLine> invoicelinelist);
  public Invoice setPartyContactAddress(Invoice invoice);
  public List<Product> getSelectedProducts(List<Integer> ids);
}
