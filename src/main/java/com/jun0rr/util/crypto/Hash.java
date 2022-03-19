/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jun0rr.util.crypto;

import com.jun0rr.util.UTF8String;
import com.jun0rr.util.match.Match;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class Hash {
  
  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
  
  private final MessageDigest digest;
  
  public Hash(DigestAlgorithm algo) {
    digest = getMessageDigest(algo);
  }
  
  public static Hash md5() {
    return new Hash(DigestAlgorithm.MD5);
  }
  
  public static Hash sha1() {
    return new Hash(DigestAlgorithm.SHA_1);
  }
  
  public static Hash sha256() {
    return new Hash(DigestAlgorithm.SHA_256);
  }
  
  public static Hash sha512() {
    return new Hash(DigestAlgorithm.SHA_512);
  }
  
  public static Hash create(DigestAlgorithm algo) {
    return new Hash(algo);
  }
  
  public String of(String str) {
    return bytesToHex(digest.digest(UTF8String.from(str).getBytes()));
  }
  
  public String of(byte[] bs) {
    return bytesToHex(digest.digest(
        Match.notNull(bs).getOrFail("Bad null byte array"))
    );
  }
  
  public String of(byte[] bs, int off, int len) {
    digest.update(Match.notNull(bs).getOrFail("Bad null byte array"), off, len);
    return bytesToHex(digest.digest());
  }
  
  public Hash put(String str) {
    if(str != null && !str.isEmpty()) {
      digest.update(UTF8String.from(str).getBytes());
    }
    return this;
  }
  
  public Hash put(byte[] bs) {
    if(bs != null && bs.length > 0) {
      digest.update(bs);
    }
    return this;
  }
  
  public Hash put(byte[] bs, int off, int len) {
    if(bs != null && off >= 0 && off + len <= bs.length) {
      digest.update(bs, off, len);
    }
    return this;
  }
  
  public String get() {
    return bytesToHex(digest.digest());
  }
  
  public byte[] getBytes() {
    return digest.digest();
  }
  
  private static MessageDigest getMessageDigest(DigestAlgorithm algo) {
    try {
      return MessageDigest.getInstance(Match.notNull(algo).getOrFail().getAlgorithmName());
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(ex.toString(), ex);
    }
  }
  
  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }
  
}
