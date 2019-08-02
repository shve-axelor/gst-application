package com.axelor.gst.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.axelor.app.AppSettings;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController extends JpaSupport {
  @Inject InvoiceService invoiceservice;
  @Inject InvoiceLineService invoicelineservice;

  public void setInvoiceItemsList(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    List<InvoiceLine> invoicelinelist = new ArrayList<InvoiceLine>();
    if (invoice.getCompany() != null
        && invoice.getParty() != null
        && invoice.getInvoiceAddress() != null) {
      if (invoice.getInvoiceItemsList() != null && !invoice.getInvoiceItemsList().isEmpty()) {
        for (InvoiceLine invoiceline : invoice.getInvoiceItemsList()) {
          invoiceline = invoicelineservice.calcluateInvoiceLineFields(invoiceline, invoice);
          invoicelinelist.add(invoiceline);
        }
        invoice.setInvoiceItemsList(invoicelinelist);
      }
    } else {
      invoice.setInvoiceItemsList(null);
    }
    if (invoice.getIsInvoiced()) {
      invoice.setShippingAddress(invoice.getInvoiceAddress());
    }
    invoice = invoiceservice.calculateInvoiceFields(invoice, invoice.getInvoiceItemsList());
    response.setValues(invoice);
  }

  public void setPartyContact(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = invoiceservice.setPartyContactAddress(invoice);
    response.setValues(invoice);
  }

  public void setInvoiceNetFields(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    List<InvoiceLine> invoicelinelist = invoice.getInvoiceItemsList();
    invoice = invoiceservice.calculateInvoiceFields(invoice, invoicelinelist);
    response.setValues(invoice);
  }

  public void getImagePath(ActionRequest request, ActionResponse response) {
    String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");
    attachmentPath =
        attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;
    request.getContext().put("attachment", attachmentPath);
  }
}
