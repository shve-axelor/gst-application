package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;

public class InvoiceLineServiceImpl implements InvoiceLineService {

  @Override
  public InvoiceLine calcluateInvoiceLineFields(InvoiceLine invoiceline, Invoice invoice) {
    Party party = invoice.getParty();
    Company company = invoice.getCompany();
    BigDecimal netamount = BigDecimal.ZERO,
        igst = BigDecimal.ZERO,
        cgst = BigDecimal.ZERO,
        sgst = BigDecimal.ZERO,
        grossamount = BigDecimal.ZERO,
        gstrate = BigDecimal.ZERO;
    if (party != null && company != null) {
      Address invoiceaddress = invoice.getInvoiceAddress();
      Address companyaddress = company.getAddress();
      netamount = invoiceline.getPrice().multiply(BigDecimal.valueOf(invoiceline.getQty()));
      if (invoiceaddress.getState() != companyaddress.getState()) {
        gstrate = invoiceline.getGstRate().divide(BigDecimal.valueOf(100));
        igst = netamount.multiply(gstrate);
      } else {
        gstrate = invoiceline.getGstRate().divide(BigDecimal.valueOf(100));
        BigDecimal multi = netamount.multiply(gstrate);
        sgst = multi.divide(BigDecimal.valueOf(2));
        cgst = sgst;
      }
    }
    grossamount = netamount.add(sgst.add(igst.add(cgst)));
    invoiceline.setNetAmount(netamount);
    invoiceline.setiGst(igst);
    invoiceline.setsGst(sgst);
    invoiceline.setcGst(cgst);
    invoiceline.setGrossAmount(grossamount);
    return invoiceline;
  }

  @Override
  public InvoiceLine setProductInInvoiceline(Product product, InvoiceLine invoiceline) {
    if (product != null) {
      invoiceline.setItem("[" + product.getProductCode() + "]" + product.getProductName());
      invoiceline.setGstRate(product.getGstRate());
      invoiceline.setHsbn(product.getHsbn());
      invoiceline.setPrice(product.getSalePrice());
      invoiceline.setQty(1);
      invoiceline.setNetAmount(product.getSalePrice());
    } else {
      invoiceline.setItem(null);
      invoiceline.setGstRate(null);
      invoiceline.setHsbn(null);
      invoiceline.setPrice(null);
      invoiceline.setQty(null);
      invoiceline.setNetAmount(null);
    }
    return invoiceline;
  }
}
