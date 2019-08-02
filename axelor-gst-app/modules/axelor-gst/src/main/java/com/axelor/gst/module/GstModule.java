package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.InvoiceRepo;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepo;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceLineServiceImpl;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImpl;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;
import com.axelor.gst.web.InvoiceController;
import com.axelor.gst.web.InvoiceLineController;
import com.axelor.gst.web.ProductController;
import com.axelor.gst.web.SequenceController;

public class GstModule extends AxelorModule{

  @Override
  protected void configure() {
    bind(SequenceController.class);
    bind(InvoiceController.class);
    bind(InvoiceLineController.class);
    bind(ProductController.class);
    bind(InvoiceService.class).to(InvoiceServiceImpl.class);
    bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
    bind(SequenceService.class).to(SequenceServiceImpl.class);
    bind(PartyRepository.class).to(PartyRepo.class);
    bind(InvoiceRepository.class).to(InvoiceRepo.class);
  }}
