package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;

public class InvoiceLineServiceImpl implements InvoiceLineService {

  @Override
  public InvoiceLine calcluateInvoiceLineFields(InvoiceLine invoiceline, Invoice invoice) {
    Party party = invoice.getParty();
    Company company = invoice.getCompany();
    BigDecimal netamount = BigDecimal.ZERO, igst = BigDecimal.ZERO, cgst = BigDecimal.ZERO, sgst = BigDecimal.ZERO, grossamount = BigDecimal.ZERO;
    if (party != null && company != null) {
      Address invoiceaddress = invoice.getInvoiceAddress();
      Address companyaddress = company.getAddressList();
      netamount = invoiceline.getPrice().multiply(BigDecimal.valueOf(invoiceline.getQty()));
      if (invoiceaddress.getState() != companyaddress.getState()) {
        igst = netamount.multiply(invoiceline.getGstRate());
      } else {
        BigDecimal gstrate = invoiceline.getGstRate();
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
}
