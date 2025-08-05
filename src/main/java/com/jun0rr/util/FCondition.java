package com.jun0rr.util;

import com.jun0rr.util.match.Match;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A functional interface that represents a fluent chain of conditional logic
 * applied to objects of type T, returning values of type X. It allows combining 
 * predicates and associating functions to be executed when conditions are met,
 * similar to an if-else structure that returns values.
 *
 * <p>This interface is useful for building readable and reusable conditional flows
 * that transform input objects into output values without relying on imperative 
 * control structures.</p>
 *
 * <p><strong>Example usage:</strong></p>
 *
 * <pre>{@code
 * FCondition<Integer, String> numberClassifier = FCondition.<Integer, String>of(i -> i <= 5)
 *     .then(i -> String.format("%d is small", i))
 *     .elseIf(i -> i <= 10)
 *     .then(i -> String.format("%d is medium", i))
 *     .elseIf(i -> i <= 20)
 *     .then(i -> String.format("%d is large", i))
 *     .elseApply(i -> String.format("%d is very large", i));
 *
 * String result1 = numberClassifier.eval(3);  // "3 is small"
 * String result2 = numberClassifier.eval(7);  // "7 is medium"
 * String result3 = numberClassifier.eval(15); // "15 is large"
 * String result4 = numberClassifier.eval(25); // "25 is very large"
 *
 * // Type checking example
 * FCondition<Object, String> typeChecker = FCondition.<String, String>of(String.class)
 *     .then(s -> "Found string: " + s)
 *     .instanceOf(Integer.class)
 *     .then(i -> "Found integer: " + i)
 *     .otherwise(o -> "Unknown type: " + o.getClass().getSimpleName());
 *
 * String result = typeChecker.eval("Hello"); // "Found string: Hello"
 * }</pre>
 *
 * @param <T> The type of object being evaluated.
 * @param <X> The type of value returned by the condition evaluation.
 *
 * @author Juno Roesler
 */
public interface FCondition<T,X> extends Cloneable {
  
  /**
   * Combines the last predicate with the given one using logical OR.
   *
   * @param p The predicate to combine with OR logic.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> or(Predicate<T> p);
  
  /**
   * Combines the last predicate with the given one using logical AND.
   *
   * @param p The predicate to combine with AND logic.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> and(Predicate<T> p);
  
  /**
   * Defines the function to execute if the current condition is met.
   *
   * @param f The function to execute, returning a value of type X.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> then(Function<T,X> f);
  
  /**
   * Defines an exception to throw if the current condition is met.
   *
   * @param x A function that returns an exception based on the input.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> thenThrows(Function<T, ? extends Exception> x);
  
  /**
   * Adds a new conditional branch (similar to else-if).
   *
   * @param p The predicate for the new condition.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> elseIf(Predicate<T> p);
  
  /**
   * Defines the function to execute if none of the previous conditions are met.
   *
   * @param f The function to execute, returning a value of type X.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> elseApply(Function<T,X> f);
  
  /**
   * Defines an exception to throw if none of the previous conditions are met.
   *
   * @param x A function that returns an exception based on the input.
   * @return The updated FCondition instance.
   */
  public FCondition<T,X> elseThrows(Function<T, ? extends Exception> x);
  
  /**
   * Creates a new condition that checks if the input is an instance of the given class.
   *
   * @param <U> The expected type.
   * @param c The class to check against.
   * @return A new FCondition instance for type U.
   */
  public <U> FCondition<U,X> instanceOf(Class<U> c);
  
  /**
   * Transforms the condition to operate on a different input type.
   *
   * @param <U> The new input type.
   * @param x The mapping function to transform T to U.
   * @return A new FCondition instance for type U.
   */
  public <U> FCondition<U,X> map(Function<T,U> x);
  
  /**
   * Defines a fallback function to execute if no condition is met.
   * This version generalizes the input type to Object.
   *
   * @param c The function to execute, accepting Object and returning X.
   * @return A new FCondition instance for Object.
   */
  public FCondition<Object,X> otherwise(Function<Object,X> c);
  
  /**
   * Evaluates the object against the condition chain and returns the result
   * of the first matching condition's associated function.
   *
   * @param obj The object to evaluate.
   * @return The result of the matching function, or null if no condition matches.
   */
  public X eval(T obj);
  
  /**
   * Creates a copy of the current condition.
   *
   * @return A cloned FCondition instance.
   */
  public FCondition<T,X> clone();
  
  
  /**
   * Creates a new FCondition instance from a predicate.
   *
   * @param <U> The type of object to be evaluated.
   * @param <V> The type of value to be returned.
   * @param p The initial predicate.
   * @return A new FCondition instance.
   */
  public static <U,V> FCondition<U,V> of(Predicate<U> p) {
    return new FConditionImpl<U,V>(p);
  }
  
  /**
   * Creates a new FCondition instance that checks for type compatibility.
   * The condition will match if the input object is assignable from the given class.
   *
   * @param <U> The expected type.
   * @param <V> The type of value to be returned.
   * @param c The class to check against.
   * @return A new FCondition instance.
   */
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
