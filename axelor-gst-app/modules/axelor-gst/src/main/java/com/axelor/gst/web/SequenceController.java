package com.axelor.gst.web;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceController extends JpaSupport {

  @Inject SequenceService services;
  
  @Transactional
  public void setReferenceParty(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);
    if (party.getReference() == null) {
      Sequence generateseqparty = services.generateSeqParty();
      if(generateseqparty != null) {
        response.setValue("reference", generateseqparty.getNextNumbers());
        int nextnumber = Integer.parseInt(generateseqparty.getNextNumber()) + 1;
        generateseqparty.setNextNumber(""+nextnumber);  
        String generatedsequence = services.generateSeq(generateseqparty);     
        generateseqparty.setNextNumbers(generatedsequence);
        Beans.get(SequenceRepository.class).save(generateseqparty);
      }else {
        response.setError("Sequence For This Model Is Not Created Yet Please Create It First.");
      }
    }
  }

  public void setSequenceNextNumber(ActionRequest request, ActionResponse response) {
    Sequence sequence = request.getContext().asType(Sequence.class);
    String generatedsequence = services.generateSeq(sequence);   
    response.setValue("nextNumbers", generatedsequence);
  }
  
  @Transactional
  public void setReferenceInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice.getReference() == null) {
      Sequence generateseqinvoice = services.generateSeqInvoice();
      if(generateseqinvoice != null) {
        response.setValue("reference", generateseqinvoice.getNextNumbers());
        int nextnumber = Integer.parseInt(generateseqinvoice.getNextNumber()) + 1;
        generateseqinvoice.setNextNumber(""+nextnumber);
        String generatedsequence = services.generateSeq(generateseqinvoice);
        generateseqinvoice.setNextNumbers(generatedsequence);
        Beans.get(SequenceRepository.class).save(generateseqinvoice);
        
      }else {
        response.setError("Sequence For This Model Is Not Created Yet Please Create It First.");
      }
    }
  }
}
