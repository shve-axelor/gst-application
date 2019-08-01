package com.axelor.gst.web;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController extends JpaSupport {

  @Inject SequenceService sequenceservice;

  public void setSequenceNextNumber(ActionRequest request, ActionResponse response) {
    Sequence sequence = request.getContext().asType(Sequence.class);
    String generatedsequence = sequenceservice.generateSeq(sequence);
    response.setValue("nextNumbers", generatedsequence);
  }
}
