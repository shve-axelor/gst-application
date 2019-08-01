package com.axelor.gst.db.repo;

import javax.persistence.PersistenceException;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.google.inject.Inject;

public class InvoiceRepo extends InvoiceRepository {
  @Inject SequenceService sequenceservice;

  @Override
  public Invoice save(Invoice entity) {
    try {
      if (entity.getReference() == null) {
        Sequence generateseqinvoice = sequenceservice.getSequenceObjectForParticularModel("Invoice");
        entity.setReference(generateseqinvoice.getNextNumbers());
        int nextnumber = Integer.parseInt(generateseqinvoice.getNextNumber()) + 1;
        generateseqinvoice.setNextNumber("" + nextnumber);
        String generatedsequence = sequenceservice.generateSeq(generateseqinvoice);
        generateseqinvoice.setNextNumbers(generatedsequence);
        Beans.get(SequenceRepository.class).save(generateseqinvoice);
      }
    } catch (Exception e) {
      throw new PersistenceException("Seq not Generated For Party. Generate It First");
    }

    return super.save(entity);
  }
}
