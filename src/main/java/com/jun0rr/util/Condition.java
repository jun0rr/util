/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util;

import com.jun0rr.util.match.Match;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @author F6036477
 */
public interface Condition<T> extends Cloneable {
  
  public Condition<T> or(Predicate<T> p);
  
  public Condition<T> and(Predicate<T> p);
  
  public Condition<T> then(Consumer<T> c);
  
  public Condition<T> elseIf(Predicate<T> p);
  
  public Condition<T> elseAccept(Consumer<T> c);
  
  public Condition<T> elseThrows(Function<T, ? extends Exception> x);
  
  public <U> Condition<U> instanceOf(Class<U> c);
  
  public Condition<Object> otherwise(Consumer<Object> c);
  
  public void eval(T obj);
  
  public Condition<T> clone();
  
  
  public static <U> Condition<U> of(Predicate<U> p) {
    return new ConditionImpl(p);
  }
  
  public static <U> Condition<U> of(Class<U> c) {
    return new ConditionImpl(o->c.isAssignableFrom(o.getClass()));
  }
  
  
  
  static class ConditionImpl<T> implements Condition<T> {
    
    private final Deque<Predicate> preds;
    
    private final Deque<Consumer> cons;
    
    public ConditionImpl() {
      this.cons = new LinkedList<>();
      this.preds = new LinkedList<>();
    }
    
    public ConditionImpl(Predicate<T> p) {
      this();
      preds.add(Match.notNull(p).getOrFail());
    }
    
    private ConditionImpl(Deque<Predicate> p, Deque<Consumer> c) {
      this.preds = p;
      this.cons = c;
    }
    
    @Override
    public Condition<T> then(Consumer<T> c) {
      cons.add(Match.notNull(c).getOrFail());
      return this;
    }

    @Override
    public Condition<T> or(Predicate<T> p) {
      preds.add(preds.pollLast().or(Match.notNull(p).getOrFail()));
      return this;
    }
    
    @Override
    public Condition<T> and(Predicate<T> p) {
      preds.add(preds.pollLast().and(Match.notNull(p).getOrFail()));
      return this;
    }
    
    @Override
    public Condition<T> elseIf(Predicate<T> p) {
      preds.add(Match.notNull(p).getOrFail());
      return this;
    }
    
    @Override
    public Condition<T> elseAccept(Consumer<T> c) {
      cons.add(Match.notNull(c).getOrFail());
      return this;
    }
    
    @Override
    public Condition<T> elseThrows(Function<T, ? extends Exception> x) {
      elseAccept(o->{throw Unchecked.<RuntimeException>unchecked(x.apply(o));});
      return this;
    }
    
    @Override
    public <U> Condition<U> instanceOf(Class<U> c) {
      preds.add(o->c.isAssignableFrom(o.getClass()));
      return new ConditionImpl(preds, cons);
    }
    
    @Override
    public Condition<Object> otherwise(Consumer<Object> c) {
      cons.add(Match.notNull(c).getOrFail());
      return new ConditionImpl(preds, cons);
    }

    @Override
    public Condition<T> clone() {
      Condition<T> cd = new ConditionImpl();
      preds.forEach(p->cd.elseIf(p));
      cons.forEach(c->cd.then(c));
      return cd;
    }
    
    private void accept(Consumer<Object> c, Object o) {
      try { c.accept(o); } 
      catch(ClassCastException e) {}
    }
    
    @Override
    public void eval(T obj) {
      while(!preds.isEmpty()) {
        Predicate p = preds.poll();
        Consumer<Object> c = cons.poll();
        if(p.test(obj) && c != null) {
          accept(c, obj);
          return;
        }
      }
      Consumer c = cons.poll();
      if(c != null) {
        accept(c, obj);
      }
    }

  }
  
}
