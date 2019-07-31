package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {
  public Sequence generateSeqParty();
  public String generateSeq(Sequence sequence);
  public Sequence generateSeqInvoice();
}
