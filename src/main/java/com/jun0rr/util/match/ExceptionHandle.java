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
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/01/2018
 */
public interface ExceptionHandle<E extends Throwable> {
  
  public ExceptionHandle<E> withMessage(String msg);
  
  public ExceptionHandle<E> causedBy(Throwable root);

  public void doThrow() throws E;
  
  public void doThrow(String msg) throws E;
  
  public void doThrow(Throwable root) throws E;
  
  public void doThrow(String msg, Throwable root) throws E;
  
  
  
  public static <T extends Throwable> ExceptionHandle<T> of(Class<T> exClass) {
    return new DefaultExceptionHandle(exClass);
  }
  
}
