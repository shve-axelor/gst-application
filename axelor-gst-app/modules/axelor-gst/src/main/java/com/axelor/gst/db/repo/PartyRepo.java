package com.axelor.gst.db.repo;

import javax.persistence.PersistenceException;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.google.inject.Inject;

public class PartyRepo extends PartyRepository {
  @Inject SequenceService sequenceservice;

  @Override
  public Party save(Party entity) {
    if (entity.getReference() == null) {
      try {
        Sequence generateseqparty = sequenceservice.getSequenceObjectForParticularModel("Party");
        entity.setReference(generateseqparty.getNextNumbers());
        int nextnumber = Integer.parseInt(generateseqparty.getNextNumber()) + 1;
        generateseqparty.setNextNumber("" + nextnumber);
        String generatedsequence = sequenceservice.generateSeq(generateseqparty);
        generateseqparty.setNextNumbers(generatedsequence);
        Beans.get(SequenceRepository.class).save(generateseqparty);
      } catch (Exception e) {
        throw new PersistenceException("Seq not Generated For Party. Generate It First");
      }
    }
    return super.save(entity);
  }
}
