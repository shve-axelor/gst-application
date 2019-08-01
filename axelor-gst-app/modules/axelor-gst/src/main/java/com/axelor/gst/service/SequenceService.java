package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {
  public Sequence getSequenceObjectForParticularModel(String model);

  public String generateSeq(Sequence sequence);
}
