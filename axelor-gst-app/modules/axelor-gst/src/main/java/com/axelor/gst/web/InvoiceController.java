package com.axelor.gst.web;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.axelor.app.AppSettings;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
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

  @SuppressWarnings("unchecked")
  public void setInvoicelineProducts(ActionRequest request, ActionResponse response) {
    Context ct = request.getContext();
    if (ct.get("productlist") != null) {
      Invoice invoice = ct.asType(Invoice.class);
      List<Integer> idList = (List<Integer>) ct.get("productlist");
      List<Product> productlist = invoiceservice.getSelectedProducts(idList);
      List<InvoiceLine> invoicelinelist = new ArrayList<InvoiceLine>();
      Company company = null;
      if (ct.get("selectedparty") != null) {
        Integer partyId =
            (Integer) ((LinkedHashMap<String, Object>) ct.get("selectedparty")).get("id");
        Party party =
            partyId != null ? Beans.get(PartyRepository.class).find(new Long(partyId)) : null;
        invoice.setParty(party);
      }
      if (ct.get("selectedcompany") != null) {
        Integer companyId =
            (Integer) ((LinkedHashMap<String, Object>) ct.get("selectedcompany")).get("id");
        company =
            companyId != null ? Beans.get(CompanyRepository.class).find(new Long(companyId)) : null;
      } else {
        company = Beans.get(CompanyRepository.class).all().fetchOne();
      }
      invoice.setCompany(company);
      invoice = invoiceservice.setPartyContactAddress(invoice);
      for (Product product : productlist) {
        InvoiceLine invoicelines = new InvoiceLine();
        invoicelines = invoicelineservice.setProductInInvoiceline(product, invoicelines);
        if (invoice.getInvoiceAddress() != null
            && invoice.getCompany() != null
            && invoice.getParty() != null) {
          invoicelines = invoicelineservice.calcluateInvoiceLineFields(invoicelines, invoice);
        }
        invoicelinelist.add(invoicelines);
      }
      invoice.setInvoiceItemsList(invoicelinelist);
      invoice = invoiceservice.calculateInvoiceFields(invoice, invoicelinelist);
      response.setValues(invoice);
    }
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
