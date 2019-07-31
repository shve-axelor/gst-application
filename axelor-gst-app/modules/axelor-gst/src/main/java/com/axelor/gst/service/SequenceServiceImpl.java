package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

  @Override
  @Transactional
  public Sequence generateSeqParty() {
    Sequence sequence =
        Beans.get(SequenceRepository.class)
            .all()
            .filter("self.model.name = ?1", "Party")
            .fetchOne();
    if (sequence != null) {
      return sequence;
    }
    return null;
  }

  @Override
  @Transactional
  public String generateSeq(Sequence sequence) {
    if (sequence != null) {
      String prefix = sequence.getPrefix();
      int padding = sequence.getPadding();
      int nextnumber = Integer.parseInt(sequence.getNextNumber());
      String generateseq = prefix;
      String suffix = "";
      int countdigits = 0;
      int dummynumber = nextnumber;
      while(dummynumber !=0) {
        dummynumber = dummynumber/10;
        countdigits++;
      }
      suffix = sequence.getSuffix();
      for (int i = 0; i < padding - countdigits; i++) {
        generateseq = generateseq + "0";
      }
      if (suffix != null) {
        generateseq = generateseq + nextnumber + suffix;
      } else {
        generateseq = generateseq + nextnumber;
      }
      return generateseq;
    }
    return null;
  }

  @Override
  @Transactional
  public Sequence generateSeqInvoice() {
    Sequence sequence =
        Beans.get(SequenceRepository.class)
            .all()
            .filter("self.model.name = ?1", "Invoice")
            .fetchOne();
    if (sequence != null) {
      return sequence;
    }
    return null;
  }
}
