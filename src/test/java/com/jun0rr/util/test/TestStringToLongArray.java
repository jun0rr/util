/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestStringToLongArray {
  
  public static final String STRING = "GraalVM Community Edition container images are published in this GitHub Container Registry. There are different GraalVM Community Edition container images provided depending on the architecture and the Java version.";
  
  public static final long[] LONG_ARRAY = {5148284395095215392L, 4859222839845087604L, 8728052375412304239L, 7935451873672454505L, 7954889800353145191L, 7310222223055593584L, 8458442250318800228L, 2335518967439321459L, 2325943680712925728L, 4859223969085615717L, 8223663515058664308L, 8248674908895733106L, 7286931341036250217L, 7378696581495551008L, 5148284395095215392L, 4859222839845087604L, 8728052375412304239L, 7935451873672454505L, 7954889800353145191L, 7310222287480780393L, 7234298466726670437L, 7954598764740636526L, 2338608890056372835L, 7523672631308219762L, 7286931323839607912L, 7286905979543822454L, 28554812663492142L};
  
  public static final int[] INT_ARRAY = {1198678369, 1817595168, 1131375981, 1970170228, 2032158052, 1769236847, 1847616367, 1853120873, 1852142112, 1768776039, 1702043745, 1919230064, 1969384553, 1936221540, 543780384, 1952999795, 541550964, 1215652384, 1131376244, 1634299493, 1914720869, 1734964084, 1920544288, 1416127858, 1696620914, 1696621673, 1717986674, 1701737504, 1198678369, 1817595168, 1131375981, 1970170228, 2032158052, 1769236847, 1847616367, 1853120873, 1852142112, 1768776039, 1702043760, 1919907433, 1684366368, 1684369509, 1852074350, 1730178926, 544499813, 543257187, 1751741541, 1668576626, 1696620910, 1679848552, 1696615009, 1986076790, 1701999465, 7302702};
  
  public static final float[] FLOAT_ARRAY = {62049.38f, 1.03629735E27f, 239.42744f, 3.0222302E32f, 5.201093E34f, 1.8467237E25f, 1.2409452E28f, 1.8908026E28f, 1.7752509E28f, 1.7935962E25f, 7.175827E22f, 4.538322E30f, 2.8702586E32f, 1.8412313E31f, 1.9772305E-19f, 7.3654275E31f, 1.6890847E-19f, 251272.5f, 239.43146f, 2.6912788E20f, 3.1755015E30f, 1.1024411E24f, 4.935522E30f, 3.99253753E12f, 4.733601E22f, 4.733943E22f, 2.720039E23f, 7.0379084E22f, 62049.38f, 1.03629735E27f, 239.42744f, 3.0222302E32f, 5.201093E34f, 1.8467237E25f, 1.2409452E28f, 1.8908026E28f, 1.7752509E28f, 1.7935962E25f, 7.175834E22f, 4.7430443E30f, 1.6926077E22f, 1.6929613E22f, 1.767251E28f, 7.5763415E23f, 2.0702145E-19f, 1.9096089E-19f, 4.409834E24f, 4.5094684E21f, 4.7335994E22f, 1.1839468E22f, 4.730942E22f, 1.1415285E33f, 7.155885E22f, 1.0233265E-38f};
  
  public static final short[] SHORT_ARRAY = {18290, 24929, 27734, 19744, 17263, 28013, 30062, 26996, 31008, 17764, 26996, 26991, 28192, 25455, 28276, 24937, 28261, 29216, 26989, 24935, 25971, 8289, 29285, 8304, 30050, 27753, 29544, 25956, 8297, 28192, 29800, 26995, 8263, 26996, 18549, 25120, 17263, 28276, 24937, 28261, 29216, 21093, 26473, 29556, 29305, 11808, 21608, 25970, 25888, 24946, 25888, 25705, 26214, 25970, 25966, 29728, 18290, 24929, 27734, 19744, 17263, 28013, 30062, 26996, 31008, 17764, 26996, 26991, 28192, 25455, 28276, 24937, 28261, 29216, 26989, 24935, 25971, 8304, 29295, 30313, 25701, 25632, 25701, 28773, 28260, 26990, 26400, 28526, 8308, 26725, 8289, 29283, 26729, 29797, 25460, 30066, 25888, 24942, 25632, 29800, 25888, 19041, 30305, 8310, 25970, 29545, 28526, 46};
  
  @Test
  public void stringToLongArray() {
    System.out.printf("------ STRING >> LONG[] ------%n");
    ByteBuffer buf = StandardCharsets.UTF_8.encode(STRING);
    System.out.printf("BUFFER: size=%d, size/8=%.2f, mod=%d%n", buf.limit(), buf.limit()/8f, buf.limit()%8);
    int mod = buf.limit() % Long.BYTES;
    if(mod > 0) {
      ByteBuffer bm = ByteBuffer.allocate(buf.limit() + Long.BYTES);
      int olim = buf.limit();
      buf.limit(buf.limit() - mod);
      bm.put(buf);
      for(int i = 0; i < Long.BYTES - mod; i++) {
        bm.put((byte)0);
      }
      buf.limit(olim);
      bm.put(buf);
      buf = bm.flip();
    }
    LongBuffer lb = buf.asLongBuffer();
    long[] array = new long[lb.capacity()];
    for(int i = 0; i < lb.capacity(); i++) {
      array[i] = lb.get();
    }
    System.out.printf("STRING = '%s'%n", STRING);
    System.out.printf("LongBuffer = '%s'%n", lb);
    System.out.printf("STRING -> LONG[] = %s%n", Arrays.toString(array));
  }
  
  @Test
  public void longArrayToString() {
    System.out.printf("------ LONG[] >> STRING ------%n");
    ByteBuffer buf = ByteBuffer.allocate(LONG_ARRAY.length * Long.BYTES);
    for(int i = 0; i < LONG_ARRAY.length; i++) {
      buf.putLong(LONG_ARRAY[i]);
    }
    String str = StandardCharsets.UTF_8.decode(buf.flip()).toString();
    System.out.printf("LONG[] = %s%n", Arrays.toString(LONG_ARRAY));
    System.out.printf("LONG[] -> STRING = %s%n", str);
  }
  
  @Test
  public void stringToIntArray() {
    System.out.printf("------ STRING >> INT[] ------%n");
    ByteBuffer buf = StandardCharsets.UTF_8.encode(STRING);
    System.out.printf("BUFFER: size=%d, size/4=%.2f, mod=%d%n", buf.limit(), buf.limit()/4f, buf.limit()%4);
    int mod = buf.limit() % Integer.BYTES;
    if(mod > 0) {
      ByteBuffer bm = ByteBuffer.allocate(buf.limit() + Integer.BYTES);
      int olim = buf.limit();
      buf.limit(buf.limit() - mod);
      bm.put(buf);
      for(int i = 0; i < Integer.BYTES - mod; i++) {
        bm.put((byte)0);
      }
      buf.limit(olim);
      bm.put(buf);
      buf = bm.flip();
    }
    IntBuffer lb = buf.asIntBuffer();
    int[] array = new int[lb.capacity()];
    for(int i = 0; i < lb.capacity(); i++) {
      array[i] = lb.get();
    }
    System.out.printf("STRING = '%s'%n", STRING);
    System.out.printf("IntBuffer = '%s'%n", lb);
    System.out.printf("STRING -> INT[] = %s%n", Arrays.toString(array));
  }
  
  @Test
  public void intArrayToString() {
    System.out.printf("------ INT[] >> STRING ------%n");
    ByteBuffer buf = ByteBuffer.allocate(INT_ARRAY.length * Integer.BYTES);
    for(int i = 0; i < INT_ARRAY.length; i++) {
      buf.putInt(INT_ARRAY[i]);
    }
    String str = StandardCharsets.UTF_8.decode(buf.flip()).toString();
    System.out.printf("INT[] = %s%n", Arrays.toString(INT_ARRAY));
    System.out.printf("INT[] -> STRING = %s%n", str);
  }
  
  @Test
  public void stringToFloatArray() {
    System.out.printf("------ STRING >> FLOAT[] ------%n");
    ByteBuffer buf = StandardCharsets.UTF_8.encode(STRING);
    System.out.printf("BUFFER: size=%d, size/4=%.2f, mod=%d%n", buf.limit(), buf.limit()/4f, buf.limit()%4);
    int mod = buf.limit() % Float.BYTES;
    if(mod > 0) {
      ByteBuffer bm = ByteBuffer.allocate(buf.limit() + Float.BYTES);
      int olim = buf.limit();
      buf.limit(buf.limit() - mod);
      bm.put(buf);
      for(int i = 0; i < Float.BYTES - mod; i++) {
        bm.put((byte)0);
      }
      buf.limit(olim);
      bm.put(buf);
      buf = bm.flip();
    }
    FloatBuffer lb = buf.asFloatBuffer();
    float[] array = new float[lb.capacity()];
    for(int i = 0; i < lb.capacity(); i++) {
      array[i] = lb.get();
    }
    System.out.printf("STRING = '%s'%n", STRING);
    System.out.printf("FloatBuffer = '%s'%n", lb);
    System.out.printf("STRING -> FLOAT[] = %s%n", Arrays.toString(array));
  }
  
  @Test
  public void floatArrayToString() {
    System.out.printf("------ FLOAT[] >> STRING ------%n");
    ByteBuffer buf = ByteBuffer.allocate(FLOAT_ARRAY.length * Float.BYTES);
    for(int i = 0; i < FLOAT_ARRAY.length; i++) {
      buf.putFloat(FLOAT_ARRAY[i]);
    }
    String str = StandardCharsets.UTF_8.decode(buf.flip()).toString();
    System.out.printf("FLOAT[] = %s%n", Arrays.toString(FLOAT_ARRAY));
    System.out.printf("FLOAT[] -> STRING = %s%n", str);
  }
  
  @Test
  public void stringToShortArray() {
    System.out.printf("------ STRING >> SHORT[] ------%n");
    ByteBuffer buf = StandardCharsets.UTF_8.encode(STRING);
    System.out.printf("BUFFER: size=%d, size/4=%.2f, mod=%d%n", buf.limit(), buf.limit()/2f, buf.limit()%2);
    int mod = buf.limit() % Short.BYTES;
    if(mod > 0) {
      ByteBuffer bm = ByteBuffer.allocate(buf.limit() + Short.BYTES);
      int olim = buf.limit();
      buf.limit(buf.limit() - mod);
      bm.put(buf);
      for(int i = 0; i < Short.BYTES - mod; i++) {
        bm.put((byte)0);
      }
      buf.limit(olim);
      bm.put(buf);
      buf = bm.flip();
    }
    ShortBuffer lb = buf.asShortBuffer();
    short[] array = new short[lb.capacity()];
    for(int i = 0; i < lb.capacity(); i++) {
      array[i] = lb.get();
    }
    System.out.printf("STRING = '%s'%n", STRING);
    System.out.printf("ShortBuffer = '%s'%n", lb);
    System.out.printf("STRING -> SHORT[] = %s%n", Arrays.toString(array));
  }
  
  @Test
  public void shortArrayToString() {
    System.out.printf("------ SHORT[] >> STRING ------%n");
    ByteBuffer buf = ByteBuffer.allocate(SHORT_ARRAY.length * Short.BYTES);
    for(int i = 0; i < SHORT_ARRAY.length; i++) {
      buf.putShort(SHORT_ARRAY[i]);
    }
    String str = StandardCharsets.UTF_8.decode(buf.flip()).toString();
    System.out.printf("SHORT[] = %s%n", Arrays.toString(SHORT_ARRAY));
    System.out.printf("SHORT[] -> STRING = %s%n", str);
  }
  
  @Test
  public void longToString() {
    System.out.printf("------ LONG >> STRING ------%n");
    long l = 5148284395095215392L;
    ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
    buf.putLong(l);
    buf.flip();
    System.out.printf("LONG = %d%n", l);
    System.out.printf("LONG >> STRING = '%s'%n", StandardCharsets.UTF_8.decode(buf).toString());
  }
  
}
