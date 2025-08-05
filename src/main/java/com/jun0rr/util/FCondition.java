package com.jun0rr.util;

import com.jun0rr.util.match.Match;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler
 */
public interface FCondition<T,X> extends Cloneable {
  
  public FCondition<T,X> or(Predicate<T> p);
  
  public FCondition<T,X> and(Predicate<T> p);
  
  public FCondition<T,X> then(Function<T,X> f);
  
  public FCondition<T,X> thenThrows(Function<T, ? extends Exception> x);
  
  public FCondition<T,X> elseIf(Predicate<T> p);
  
  public FCondition<T,X> elseApply(Function<T,X> f);
  
  public FCondition<T,X> elseThrows(Function<T, ? extends Exception> x);
  
  public <U> FCondition<U,X> instanceOf(Class<U> c);
  
  public <U> FCondition<U,X> map(Function<T,U> x);
  
  public FCondition<Object,X> otherwise(Function<Object,X> c);
  
  public X eval(T obj);
  
  public FCondition<T,X> clone();
  
  
  public static <U,V> FCondition<U,V> of(Predicate<U> p) {
    return new FConditionImpl<U,V>(p);
  }
  
  public static <U,V> FCondition<U,V> of(Class<U> c) {
    return new FConditionImpl<U,V>(o->c.isAssignableFrom(o.getClass()));
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
    public FCondition<T,X> thenThrows(Function<T, ? extends Exception> x) {
      return elseApply(o->{throw Unchecked.<RuntimeException>unchecked(x.apply(o));});
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
    
    public FCondition<T,X> elseThrows(Function<T, ? extends Exception> x) {
      return elseApply(o->{throw Unchecked.<RuntimeException>unchecked(x.apply(o));});
    }
    
    @Override
    public <U> FCondition<U,X> instanceOf(Class<U> c) {
      preds.add(o->c.isAssignableFrom(o.getClass()));
      return new FConditionImpl(preds, funs);
    }
    
    @Override
    public <U> FCondition<U,X> map(Function<T,U> x) {
      return new FConditionImpl(preds, funs);
    }
    
    @Override
    public FCondition<Object,X> otherwise(Function<Object,X> c) {
      funs.add(Match.notNull(c).getOrFail());
      return new FConditionImpl(preds, funs);
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
      Deque<Predicate> preds = new LinkedList<>(this.preds);
      Deque<Function> funs = new LinkedList<>(this.funs);
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
