package com.jun0rr.util;

import com.jun0rr.util.match.Match;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * A functional interface that represents a fluent chain of conditional logic
 * applied to objects of type T. It allows combining predicates and associating
 * actions to be executed when conditions are met, similar to an if-else structure.
 *
 * <p>This interface is useful for building readable and reusable conditional flows
 * without relying on imperative control structures.</p>
 *
 * <p><strong>Example usage:</strong></p>
 *
 * <pre>{@code
 * Condition<String> cond = Condition.of(s -> s.length() > 8)
 *   .then(s -> System.out.println("Long string"))
 *   .elseIf(s -> s.length() > 4)
 *   .then(s -> System.out.println("Medium string"))
 *   .elseAccept(s -> System.out.println("Short string"));
 *
 * cond.eval("Hello"); // Output: Medium string
 * cond.eval("Hi");    // Output: Short string
 * cond.eval("Supercalifragilistic"); // Output: Long string
 * }</pre>
 *
 * @param <T> The type of object being evaluated.
 *
 * @author Juno Roesler
 */
public interface Condition<T> extends Cloneable {
  

  /**
     * Combines the last predicate with the given one using logical OR.
     *
     * @param p The predicate to combine.
     * @return The updated Condition instance.
     */
    Condition<T> or(Predicate<T> p);

    /**
     * Combines the last predicate with the given one using logical AND.
     *
     * @param p The predicate to combine.
     * @return The updated Condition instance.
     */
    Condition<T> and(Predicate<T> p);

    /**
     * Defines the action to execute if the condition is met.
     *
     * @param c The consumer to execute.
     * @return The updated Condition instance.
     */
    Condition<T> then(Consumer<T> c);

    /**
     * Defines an exception to throw if the condition is met.
     *
     * @param x A function that returns an exception based on the input.
     * @return The updated Condition instance.
     */
    Condition<T> thenThrows(Function<T, ? extends Exception> x);

    /**
     * Adds a new conditional branch (similar to else-if).
     *
     * @param p The predicate for the new condition.
     * @return The updated Condition instance.
     */
    Condition<T> elseIf(Predicate<T> p);

    /**
     * Defines the action to execute if none of the previous conditions are met.
     *
     * @param c The consumer to execute.
     * @return The updated Condition instance.
     */
    Condition<T> elseAccept(Consumer<T> c);

    /**
     * Defines an exception to throw if none of the previous conditions are met.
     *
     * @param x A function that returns an exception based on the input.
     * @return The updated Condition instance.
     */
    Condition<T> elseThrows(Function<T, ? extends Exception> x);

    /**
     * Creates a new condition that checks if the input is an instance of the given class.
     *
     * @param <U> The expected type.
     * @param c The class to check against.
     * @return A new Condition instance for type U.
     */
    <U> Condition<U> instanceOf(Class<U> c);

    /**
     * Defines a fallback action to execute if no condition is met.
     * This version generalizes the type to Object.
     *
     * @param c The consumer to execute.
     * @return A new Condition instance for Object.
     */
    Condition<Object> otherwise(Consumer<Object> c);

    /**
     * Transforms the condition to operate on a different type.
     *
     * @param <U> The new type.
     * @param x The mapping function.
     * @return A new Condition instance for type U.
     */
    <U> Condition<U> map(Function<T, U> x);

    /**
     * Evaluates the object against the condition chain and executes the corresponding action.
     *
     * @param obj The object to evaluate.
     */
    void eval(T obj);

    /**
     * Creates a copy of the current condition.
     *
     * @return A cloned Condition instance.
     */
    Condition<T> clone();

    /**
     * Creates a new Condition instance from a predicate.
     *
     * @param <U> The type of object.
     * @param p The initial predicate.
     * @return A new Condition instance.
     */
    static <U> Condition<U> of(Predicate<U> p) {
      return new ConditionImpl<U>(p);
    }

    /**
     * Creates a new Condition instance that checks for type compatibility.
     *
     * @param <U> The expected type.
     * @param c The class to check against.
     * @return A new Condition instance.
     */
    static <U> Condition<U> of(Class<U> c) {
      return new ConditionImpl<U>(o -> c.isAssignableFrom(o.getClass()));
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
    public Condition<T> thenThrows(Function<T, ? extends Exception> x) {
      return elseAccept(o->{throw Unchecked.<RuntimeException>unchecked(x.apply(o));});
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
      return elseAccept(o->{throw Unchecked.<RuntimeException>unchecked(x.apply(o));});
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
    public <U> Condition<U> map(Function<T,U> x) {
      return new ConditionImpl(preds, cons);
    }

    @Override
    public Condition<T> clone() {
      Condition<T> cd = new ConditionImpl();
      preds.forEach(p->cd.elseIf(p));
      cons.forEach(c->cd.then(c));
      return cd;
    }
    
    @Override
    public void eval(T obj) {
      Deque<Predicate> preds = new LinkedList<>(this.preds);
      Deque<Consumer> cons = new LinkedList<>(this.cons);
      while(!preds.isEmpty()) {
        Predicate p = preds.poll();
        Consumer<Object> c = cons.poll();
        if(p.test(obj) && c != null) {
          Unchecked.call(()->c.accept(obj));
          return;
        }
      }
      Consumer c = cons.poll();
      if(c != null) {
        Unchecked.call(()->c.accept(obj));
      }
    }

  }
  
}
