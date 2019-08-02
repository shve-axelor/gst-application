package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;

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
      Address defaultaddress =
          addresslist.stream().filter(a -> a.getType().equals("default")).findFirst().orElse(null);
      Address invoiceaddress =
          addresslist.stream().filter(a -> a.getType().equals("invoice")).findFirst().orElse(null);
      Address shippingaddress =
          addresslist.stream().filter(a -> a.getType().equals("shipping")).findFirst().orElse(null);
      shippingaddress = shippingaddress == null ? defaultaddress : shippingaddress;
      invoice.setInvoiceAddress(invoiceaddress == null ? defaultaddress : invoiceaddress);
      invoice.setShippingAddress(
          inInvoiceAddShippingAdd ? invoice.getInvoiceAddress() : shippingaddress);
    } else {
      invoice.setShippingAddress(null);
      invoice.setInvoiceAddress(null);
      invoice.setPartyContact(null);
    }
    return invoice;
  }

  @Override
  public List<Product> getSelectedProducts(List<Integer> ids) { // TODO Auto-generated method stub
    List<Product> productlists =
        Beans.get(ProductRepository.class).all().filter("self.id in (?1)", ids).fetch();
    return productlists;
  }
}
