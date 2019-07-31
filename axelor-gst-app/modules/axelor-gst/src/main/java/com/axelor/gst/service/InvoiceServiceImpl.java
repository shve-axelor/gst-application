package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;

public class InvoiceServiceImpl implements InvoiceService {
  @Override
  public Invoice calculateInvoiceFields(Invoice invoice, List<InvoiceLine> invoicelinelist) {
    BigDecimal netamount = BigDecimal.ZERO,
        netigst = BigDecimal.ZERO,
        netcgst = BigDecimal.ZERO,
        netsgst = BigDecimal.ZERO,
        netgrossamount = BigDecimal.ZERO;
    if (invoicelinelist != null) {
      for (InvoiceLine invoiceline : invoicelinelist) {
        netamount = invoiceline.getNetAmount().add(netamount);
        netigst = invoiceline.getiGst().add(netigst);
        netcgst = invoiceline.getcGst().add(netcgst);
        netsgst = invoiceline.getsGst().add(netsgst);
        netgrossamount = invoiceline.getGrossAmount().add(netgrossamount);
      }
    }
    invoice.setNetIgst(netigst);
    invoice.setNetSgst(netsgst);
    invoice.setNetCgst(netcgst);
    invoice.setNetAmount(netamount);
    invoice.setGrossAmount(netgrossamount);
    return invoice;
  }

  @Override
  public Invoice setPartyContactAddress(Invoice invoice) {
    boolean inInvoiceAddShippingAdd = invoice.getIsInvoiced();
    Party party = invoice.getParty();
    if (party != null) {
      List<Contact> contactlist = party.getContactList();
      contactlist = contactlist.stream().filter(c -> c.getType().equals("Primary")).collect(Collectors.toList());
      if (contactlist != null && !contactlist.isEmpty()) {    
        invoice.setPartyContact(contactlist.get(0));
      } else {
        invoice.setPartyContact(null);
      }
      List<Address> addresslist = party.getAddressList();
      List<Address> invoiceaddresslist = addresslist.stream().filter(a -> a.getType().equals("invoice")).collect(Collectors.toList());
      List<Address> shippingaddresslist = addresslist.stream().filter(a -> a.getType().equals("shipping")).collect(Collectors.toList());
      if(invoiceaddresslist != null && !invoiceaddresslist.isEmpty()) {
        invoice.setInvoiceAddress(invoiceaddresslist.get(0));
      }else {
        invoice.setInvoiceAddress(null);
      }
      if (inInvoiceAddShippingAdd) {
        invoice.setShippingAddress(invoice.getInvoiceAddress());
      }else {
        if(shippingaddresslist != null && !shippingaddresslist.isEmpty()) {
          invoice.setShippingAddress(shippingaddresslist.get(0));
        }else {
          invoice.setShippingAddress(null);
        }
      }
    }
    return invoice;
  }
}
