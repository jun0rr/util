/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jun0rr.util.test;

import com.jun0rr.util.crypto.EncryptedFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 *
 * @author juno
 */
public class TestEncryptedFile {
   
  //public static final Path SRC = Paths.get("/home/juno/Documentos/CRLVDigitalDetran.pdf");
  
  public static final Path SRC = Paths.get("C:/Java/certificado_internet_das_coisas.pdf");
  
  //public static final Path DST = Paths.get("/home/juno/Documentos/CRLVDigitalDetran_encrypted.txt.gz");
  
  public static final Path DST = Paths.get("C:/Java/certificado_internet_das_coisas.txt.gz");
  
  //public static final Path DST2 = Paths.get("/home/juno/Documentos/CRLVDigitalDetran2.pdf");
  
  public static final Path DST2 = Paths.get("C:/Java/certificado_internet_das_coisas2.pdf");
  
  //public static final int SPLIT_SIZE = 30*1024; // 30K
  
  public static final int SPLIT_SIZE = 350*1024; // 350K
  
  //@Test
  public void encrypt_decrypt() throws IOException {
    try {
      System.out.println("* Encrypting...");
      EncryptedFile f = EncryptedFile.of("inadonuj", SRC, DST)
          //.split(SPLIT_SIZE)
          .encrypt(p->System.out.println(p.getProgressBar()));
      System.out.println("* Decrypting...");
      f = EncryptedFile.of("inadonuj", DST, DST2)
          //.split(SPLIT_SIZE)
          .decrypt(p->System.out.println(p.getProgressBar()));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
}
