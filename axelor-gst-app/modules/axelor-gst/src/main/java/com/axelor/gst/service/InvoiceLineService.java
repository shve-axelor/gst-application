package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;

public interface InvoiceLineService {
  public InvoiceLine calcluateInvoiceLineFields(InvoiceLine invoiceline, Invoice invoice);
  public InvoiceLine setProductInInvoiceline(Product product,InvoiceLine invoiceline);
}
