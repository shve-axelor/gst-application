package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;
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
      Contact contact =
          contactlist.stream().filter(c -> c.getType().equals("Primary")).findFirst().orElse(null);
      invoice.setPartyContact(contact);
      List<Address> addresslist = party.getAddressList();
      Address invoiceaddress =
          addresslist.stream().filter(a -> a.getType().equals("invoice")).findFirst().orElse(null);
      Address shippingaddress =
          addresslist.stream().filter(a -> a.getType().equals("shipping")).findFirst().orElse(null);
      invoice.setInvoiceAddress(invoiceaddress);
      invoice.setShippingAddress(shippingaddress);
      if (inInvoiceAddShippingAdd) {
        invoice.setShippingAddress(invoice.getInvoiceAddress());
      }
    } else {
      invoice.setShippingAddress(null);
      invoice.setInvoiceAddress(null);
      invoice.setPartyContact(null);
    }
    return invoice;
  }
}
