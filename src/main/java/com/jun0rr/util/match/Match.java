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

package com.jun0rr.util.match;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 * @param <T>
 */
public class Match<T> extends Check<T,IllegalArgumentException> {
  
  protected Match(T t, Predicate<T> match, String message) {
    this(t, match, message, null);
  }
  
  protected Match(T t, Predicate<T> match, String message, Check<?,?> parent) {
    super(IllegalArgumentException.class, t, match, message, parent);
  }
  
  public Match(T t, Predicate<T> match) {
    this(t, match, DEFAULT_MESSAGE);
  }
  
  @Override
  public <U> Match<U> on(U val) {
    return new Match(val, match, this.defMessage, parent);
  }
  
  @Override
  public Match<T> match(Predicate<T> match) {
    return new Match<>(obj, match, this.defMessage, parent);
  }
  
  @Override
  public Match<T> failWith(String msg) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new Match<>(obj, match, msg, parent);
  }
  
  @Override
  public Match<T> failWith(String msg, Object ... args) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new Match<>(obj, match, String.format(msg, args), parent);
  }
  
  @Override
  public Match<T> and(Predicate<? super T> other) {
    return new Match(obj, match.and(other), defMessage, this);
  }

  @Override
  public Match<T> negate() {
    return new Match(obj, match.negate(), defMessage);
  }

  @Override
  public Match<T> or(Predicate<? super T> other) {
    return new Match(obj, match.or(other), defMessage, this);
  }


  @Override
  public String toString() {
    return "Match{" + "match=" + match + ", obj=" + obj + ", message=" + defMessage + ", parent=" + parent + '}';
  }
  
  
  
  public static <U> Match<U> of(U val, Predicate<U> match) {
    return new Match(val, match);
  }
  
  
  public static <U> Match<U> notNull(U val) {
    return new Match(val, v->v != null, "Bad null value");
  }
  
  
  public static Match<String> notEmpty(String str) {
    return of(str, s -> s != null && !s.isEmpty());
  }
  
  
  public static Match<String> contains(String str) {
    return of(str, s -> s != null && s.contains(str));
  }
  
  
  public static Match<String> notContains(String str) {
    return of(str, s -> s != null && !s.contains(str));
  }
  
  
  public static <U extends Collection<?>> Match<U> notEmpty(U col) {
    return of(col, c -> c != null && !c.isEmpty());
  }
  
  
  public static <U extends Collection<?>> Match<U> contains(U col, Object elt) {
    return of(col, c -> c != null && c.stream().anyMatch(elt::equals)).failWith("Collection does not contains the selected element");
  }
  
  
  public static <U extends Collection<?>> Match<U> notContains(U col, Object elt) {
    return of(col, c -> c != null && c.stream().noneMatch(elt::equals)).failWith("Collection contains the selected element");
  }
  
  
  public static <U> Match<U[]> notEmpty(U[] array) {
    return of(array, a -> array != null && Array.getLength(a) > 0).failWith("Bad null/empty Collection");
  }
  
  
  public static <U> Match<U[]> contains(U[] array, U elt) {
    return of(array, a -> array != null && Array.getLength(a) > 0 && List.of(array).stream().anyMatch(o->Objects.equals(o, elt))).failWith("Array does not contains the selected element");
  }
  
  
  public static <U> Match<U[]> notContains(U[] array, U elt) {
    return of(array, a -> array != null && Array.getLength(a) > 0 && List.of(array).stream().noneMatch(o->Objects.equals(o, elt))).failWith("Array contains the selected element");
  }
  
  
  public static Match<Path> exists(Path path) {
    return of(path, p -> p != null && Files.exists(path)).failWith("Path [%s] does not exists", path);
  }
  
  
  public static Match<Path> notExists(Path val) {
    return exists(val).negate().failWith("Path already exists");
  }
  
  
  public static Match<File> exists(File val) {
    return of(val, f -> f != null && Files.exists(f.toPath())).failWith("File [%s] does not exists", val);
  }
  
  
  public static Match<File> notExists(File val) {
    return exists(val).negate().failWith("File already exists");
  }
  
  
  public static <U extends Number> Match<U> between(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0,
        String.format("Bad Number Not Between parameters (%f.2 >= %f.2 >= %f.2)", 
            min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number> Match<U> betweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0,
        String.format("Bad Number Not Between parameters (%f.2 > %f.2 > %f.2)", 
            min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number> Match<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) <= 0 
            || Double.compare(v.doubleValue(), max.doubleValue()) >= 0,
        String.format("Bad Number Between parameters (%f.2 <= %f.2 <= %f.2)", 
            min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number> Match<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) < 0 
            || Double.compare(v.doubleValue(), max.doubleValue()) > 0,
        String.format("Bad Number Between parameters (%f.2 < %f.2 < %f.2)", 
            min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Date> Match<U> between(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        v.compareTo(min) >= 0 && v.compareTo(max) <= 0,
        String.format("Bad Date Not Between parameters (%s >= %s >= %s)", min, val, max)
    );
  }
  
  
  public static <U extends Date> Match<U> betweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        v.compareTo(min) > 0 && v.compareTo(max) < 0,
        String.format("Bad Date Not Between parameters (%s > %s > %s)", min, val, max)
    );
  }
  

  public static <U extends Date> Match<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        v.compareTo(min) <= 0 || v.compareTo(max) >= 0,
        String.format("Bad Date Between parameters (%s <= %s <= %s)", min, val, max)
    );
  }
  
  
  public static <U extends Date> Match<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch("Match: Bad null val");
    notNull(min).failIfNotMatch("Match: Bad null min");
    notNull(max).failIfNotMatch("Match: Bad null max");
    return new Match<>(val, v->
        v.compareTo(min) < 0 || v.compareTo(max) > 0,
        String.format("Bad Date Between parameters (%s < %s < %s)", min, val, max)
    );
  }
  
}
