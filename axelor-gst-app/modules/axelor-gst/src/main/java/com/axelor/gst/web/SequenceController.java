package com.axelor.gst.web;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceController extends JpaSupport {

  @Inject SequenceService sequenceservice;

  public void setSequenceNextNumber(ActionRequest request, ActionResponse response) {
    Sequence sequence = request.getContext().asType(Sequence.class);
    // System.out.println(request.getContext().get("_model"));
    String generatedsequence = sequenceservice.generateSeq(sequence);
    response.setValue("nextNumbers", generatedsequence);
  }

  @Transactional
  public void setReference(ActionRequest request, ActionResponse response) {
    Sequence generateseqparty = null;
    String modelname = (String) request.getContext().get("_model");
    if (modelname.equals("com.axelor.gst.db.Party")) {
      generateseqparty = sequenceservice.getSequenceObjectForParticularModel("Party");
    } else if (modelname.equals("com.axelor.gst.db.Invoice")) {
      generateseqparty = sequenceservice.getSequenceObjectForParticularModel("Invoice");
    }
    if (generateseqparty == null) {
      response.setError("Seq not Generated For This Model Yet. Generate It First");
    }
    response.setValue("reference", generateseqparty.getNextNumbers());
    int nextnumber = Integer.parseInt(generateseqparty.getNextNumber()) + 1;
    generateseqparty.setNextNumber("" + nextnumber);
    String generatedsequence = sequenceservice.generateSeq(generateseqparty);
    generateseqparty.setNextNumbers(generatedsequence);
    Beans.get(SequenceRepository.class).save(generateseqparty);
  }
}
