/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util;

import com.jun0rr.util.match.Match;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author F6036477
 */
public interface FCondition<T,X> extends Cloneable {
  
  public FCondition<T,X> or(Predicate<T> p);
  
  public FCondition<T,X> and(Predicate<T> p);
  
  public FCondition<T,X> then(Function<T,X> f);
  
  public FCondition<T,X> elseIf(Predicate<T> p);
  
  public FCondition<T,X> elseApply(Function<T,X> f);
  
  public X eval(T obj);
  
  public FCondition<T,X> clone();
  
  
  public static <U,V> FCondition<U,V> of(Predicate<U> p) {
    return new FConditionImpl(p);
  }
  
  
  
  static class FConditionImpl<T,X> implements FCondition<T,X> {
    
    private final Deque<Predicate> preds;
    
    private final Deque<Function> funs;
    
    public FConditionImpl() {
      this.funs = new LinkedList<>();
      this.preds = new LinkedList<>();
    }
    
    public FConditionImpl(Predicate<T> p) {
      this();
      preds.add(Match.notNull(p).getOrFail());
    }
    
    private FConditionImpl(Deque<Predicate> p, Deque<Function> f) {
      this.preds = p;
      this.funs = f;
    }
    
    @Override
    public FCondition<T,X> then(Function<T,X> c) {
      funs.add(Match.notNull(c).getOrFail());
      return this;
    }

    @Override
    public FCondition<T,X> and(Predicate<T> p) {
      preds.add(preds.pollLast().and(Match.notNull(p).getOrFail()));
      return this;
    }
    
    @Override
    public FCondition<T,X> or(Predicate<T> p) {
      preds.add(preds.pollLast().or(Match.notNull(p).getOrFail()));
      return this;
    }
    
    @Override
    public FCondition<T,X> elseIf(Predicate<T> p) {
      preds.add(Match.notNull(p).getOrFail());
      return this;
    }
    
    @Override
    public FCondition<T,X> elseApply(Function<T,X> f) {
      funs.add(Match.notNull(f).getOrFail());
      return this;
    }

    @Override
    public FCondition<T,X> clone() {
      FCondition<T,X> cd = new FConditionImpl();
      preds.forEach(p->cd.elseIf(p));
      funs.forEach(c->cd.then(c));
      return cd;
    }
    
    @Override
    public X eval(T obj) {
      while(!preds.isEmpty()) {
        Predicate<T> p = preds.poll();
        Function<T,X> f = funs.poll();
        if(p.test(obj) && f != null) {
          return f.apply(obj);
        }
      }
      Function<T,X> f = funs.poll();
      if(f != null) {
        return f.apply(obj);
      }
      return null;
    }
    
  }
  
}
