package com.axelor.gst.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSequence {
  String testsequence = "SHUBH0000050505AXELOR";
  SequenceTesting sequencetest = new SequenceTesting();

  @Test
  public void testSequenceGeneration() {
    System.out.println();
    assertEquals(testsequence, sequencetest.testing(10,"SHUBH","AXELOR","50505"));
  }
}
