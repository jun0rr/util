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

package com.jun0rr.util;

import com.jun0rr.util.crypto.Crypto;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2016
 */
public class ResourceLoader {
  
  private final Class loader;
  
  public ResourceLoader(Class cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Bad Null Class");
    }
    this.loader = cls;
  }
  
  
  public static ResourceLoader self() {
    return new ResourceLoader(ResourceLoader.class);
  }
  
  
  public static ResourceLoader of(Class cls) {
    return new ResourceLoader(cls);
  }
  
  
  public static ResourceLoader caller() throws ResourceLoadException {
    try {
      Thread cur = Thread.currentThread();
      StackTraceElement[] els = cur.getStackTrace();
      ClassLoader cld = cur.getContextClassLoader();
      if(cld == null) {
        cld = ResourceLoader.class.getClassLoader();
      }
      return of(cld.loadClass(selectStackLevel(els).getClassName()));
    } catch(Exception e) {
      e.printStackTrace();
      throw new ResourceLoadException(e.toString(), e);
    }
  }
  
  
  private static StackTraceElement selectStackLevel(StackTraceElement[] els) {
    int level2 = Math.min(2, els.length -1);
    int level3 = Math.min(3, els.length -1);
    String cname2 = els[level2].getClassName();
    int lpt = cname2.lastIndexOf(".");
    cname2 = cname2.substring(0, lpt);
    String cname3 = els[level3].getClassName();
    lpt = cname3.lastIndexOf(".");
    cname3 = cname3.substring(0, lpt);
    return cname2.equals(cname3) ? els[level2] : els[level3];
  }
  
  
  public Class loader() {
    return loader;
  }
  
  
  private void testResource(String resource) {
    if(resource == null || resource.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Empty Resource");
    }
  }
  
  
  private URI getURI(String resource) throws ResourceLoadException {
    try {
      return findResource(resource).toURI();
    } catch(URISyntaxException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  private URL findResource(String resource) throws ResourceLoadException {
    testResource(resource);
    String cpath = "/"+ loader.getName().replace(".", "/") + ".class";
    String def = loader.getResource(cpath).toString().replace(cpath, "");
    try {
      ClassLoader cl = (loader.getClassLoader() != null 
          ? loader.getClassLoader() 
          : ClassLoader.getSystemClassLoader());
      Enumeration<URL> en = cl.getResources(
          (resource.startsWith("/") ? resource.substring(1) : resource)
      );
      URL url = null;
      while(en.hasMoreElements()) {
        url = en.nextElement();
        //System.out.println("- ResourceLoader.findResource.nextElement: " + url);
        String surl = url.toString();
        if(surl.contains(def)) break;
      }
      if(url == null) {
        throw new ResourceLoadException("Resource Not Found: "+ resource);
      }
      return url;
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  public Path loadPath(String resource) throws ResourceLoadException {
    URI uri = this.getURI(resource);
    Path path = null;
    try {
      path = Paths.get(uri);
    }
    catch(FileSystemNotFoundException e) {
      try {
        String[] pts = uri.toString().split("!");
        FileSystem fs = FileSystems.newFileSystem(URI.create(pts[0]), new HashMap());
        path = Paths.get(uri);
      }
      catch(IOException ex) {
        throw new ResourceLoadException(e.toString(), e);
      }
    }
    return path;
  }
  
  
  public InputStream loadStream(String resource) {
    this.testResource(resource);
    try {
      return findResource(resource).openStream();
    } catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  public BufferedReader loadReader(String resource) {
    this.testResource(resource);
    return new BufferedReader(
        new InputStreamReader(
            loadStream(resource))
    );
  }
  
  
  public ReadableByteChannel loadChannel(String resource) {
    this.testResource(resource);
    return Channels.newChannel(
        loadStream(resource)
    );
  }
  
  
  public byte[] loadContentBytes(String resource) throws ResourceLoadException {
    try (InputStream input = this.loadStream(resource);
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      byte[] bs = new byte[4096];
      int read = -1;
      while((read = input.read(bs)) != -1) {
        bos.write(bs, 0, read);
      }
      return bos.toByteArray();
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  public String loadContentString(String resource) throws ResourceLoadException {
    try (InputStream input = this.loadStream(resource);
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      byte[] bs = new byte[4096];
      int read = -1;
      while((read = input.read(bs)) != -1) {
        bos.write(bs, 0, read);
      }
      return bos.toString(StandardCharsets.UTF_8.name());
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  public String loadStringPath(String resource) throws ResourceLoadException {
    return this.loadPath(resource).toAbsolutePath().toString();
  }
  
  
  
  public static void main(String[] args) throws IOException {
    System.out.println(args[0]);
    Path p = self().loadPath(args[0]).toAbsolutePath();
    System.out.println(p);
    System.out.println(Crypto.createPrivateKey(Files.readAllBytes(p)));
  }
  
  
  
  
  
  
  public static class ResourceLoadException extends RuntimeException {

    public ResourceLoadException(String message) {
      super(message);
    }


    public ResourceLoadException(String message, Throwable cause) {
      super(message, cause);
    }


    public ResourceLoadException(Throwable cause) {
      super("["+ cause.getClass().getSimpleName()
          + "] "+ cause.getMessage(), cause);
    }
    
  }
  
  
  
}
