package com.axelor.gst.test;

public class SequenceTesting {

  public String testing(int padding,String prefix,String suffix,String nextnumbers) {
    
    int nextnumber = Integer.parseInt(nextnumbers);
    String generateseq = prefix;
    int countdigits = 0;
    int dummynumber = nextnumber;
    while(dummynumber !=0) {
      dummynumber = dummynumber/10;
      countdigits++;
    }
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
}
