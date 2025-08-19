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


/**
 * Interface for handling and throwing exceptions in a flexible and fluent way.
 * 
 * @param <E> The type of Throwable (exception) this handle will manage.
 * 
 * This interface allows the creation of exception instances with custom messages
 * and causes, and provides methods to throw them in various configurations.
 * 
 * Designed to be used in validation frameworks like the Check<T, E> class,
 * enabling dynamic and reusable exception handling.
 * 
 * Author: Juno Roesler - juno.rr@gmail.com
 */
public interface ExceptionHandle<E extends Throwable> {

  /**
   * Sets a custom message to be used when the exception is thrown.
   * 
   * @param msg The message to associate with the exception.
   * @return A reference to this ExceptionHandle for fluent chaining.
   */
  public ExceptionHandle<E> withMessage(String msg);

  /**
   * Sets the root cause (Throwable) of the exception.
   * Useful for exception chaining.
   * 
   * @param root The cause of the exception.
   * @return A reference to this ExceptionHandle for fluent chaining.
   */
  public ExceptionHandle<E> causedBy(Throwable root);

  /**
   * Throws the exception using the current configuration (message and cause).
   * 
   * @throws E The configured exception.
   */
  public void doThrow() throws E;

  /**
   * Throws the exception with a specific message.
   * 
   * @param msg The message to use when throwing the exception.
   * @throws E The configured exception.
   */
  public void doThrow(String msg) throws E;

  /**
   * Throws the exception with a specific cause.
   * 
   * @param root The cause of the exception.
   * @throws E The configured exception.
   */
  public void doThrow(Throwable root) throws E;

  /**
   * Throws the exception with both a specific message and cause.
   * 
   * @param msg The message to use.
   * @param root The cause of the exception.
   * @throws E The configured exception.
   */
  public void doThrow(String msg, Throwable root) throws E;

  /**
   * Static factory method to create an ExceptionHandle for a given exception class.
   * 
   * @param <T> The type of Throwable to handle.
   * @param exClass The class of the exception to be handled.
   * @return A new instance of DefaultExceptionHandle configured for the given class.
   */
  public static <T extends Throwable> ExceptionHandle<T> of(Class<T> exClass) {
    return new DefaultExceptionHandle<>(exClass);
  }
  
}
