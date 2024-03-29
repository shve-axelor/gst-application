package com.axelor.gst.web;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController extends JpaSupport {
  @Inject InvoiceLineService invoicelineservice;

  public void setItems(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
    Product product = invoiceline.getProduct();
    invoiceline = invoicelineservice.setProductInInvoiceline(product,invoiceline);
    response.setValues(invoiceline);
  }

  public void setGstFields(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    invoiceline = invoicelineservice.calcluateInvoiceLineFields(invoiceline, invoice);
    response.setValues(invoiceline);
  }
}
