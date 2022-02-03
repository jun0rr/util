/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

import com.jun0rr.util.match.Match;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

/**
 * DateDiff representa a diferença de tempo entre duas
 * datas.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see us.pserver.tools.date.SimpleDate
 * @version 3.0 - 2011.11.16
 */
public class DateDiff {
	
	private int years;
	private int months;
	private long days;
	private int hours;
	private int mins;
	private int secs;
	private long millis;
	private int lastDayOfMonth;
	private int factor;
	private LocalDateTime date1;
	private LocalDateTime date2;
	
	
	/**
	 * Construtor padrão sem argumentos.
	 */
	public DateDiff() {
		date1 = null;
		date2 = null;
		this.reset();
	}
	
	
	/**
	 * Construtor que recebe as duas datas para cálculo
	 * da diferença de tempo.
	 * @param d1 Primeira data
	 * @param d2 Segunda data
	 */
	public DateDiff(LocalDateTime d1, LocalDateTime d2) {
    this.date1 = Match.notNull(d1).getOrFail();
    this.date2 = Match.notNull(d2).getOrFail();
    this.calculate();
	}


	private void reset() {
		years = 0;
		months = 0;
		days = 0;
		hours = 0;
		mins = 0;
		secs = 0;
		millis = 0;
		lastDayOfMonth = 0;
		factor = 1;
	}


	/**
	 * Define a primeira data utilizada para cálculo 
	 * da diferença.
	 * @param d Primeira data.
	 * @return esta instância de DateDiff modificada.
	 */
	public DateDiff setDate1(LocalDateTime d) {
		this.date1 = Match.notNull(d).getOrFail();
		return this;
	}
	
	
	/**
	 * Define a segunda data utilizada para cálculo 
	 * da diferença.
	 * @param d Segunda data.
	 * @return esta instância de DateDiff modificada.
	 */
	public DateDiff setDate2(LocalDateTime d) {
		this.date2 = Match.notNull(d).getOrFail();
		return this;
	}
	
	
	/**
	 * Retorna a primeira data utilizada no cálculo 
	 * da diferença.
	 * @return Primeira data.
	 */
	public LocalDateTime date1() {
		return date1;
	}
	
	
	/**
	 * Retorna a segunda data utilizada no cálculo 
	 * da diferença.
	 * @return Segunda data.
	 */
	public LocalDateTime date2() {
		return date2;
	}


	/**
	 * Retorna a diferença de anos entre as datas.
	 * @return diferença em anos.
	 */
	public int years() {
		return years;
	}


	/**
	 * Retorna a diferença de meses entre as datas.
	 * @return diferença em meses.
	 */
	public int months() {
		return months;
	}


	/**
	 * Retorna a diferença de dias entre as datas.
	 * @return diferença em dias.
	 */
	public int days() {
		return (int) days;
	}


	/**
	 * Retorna a diferença de horas entre as datas.
	 * @return diferença em horas.
	 */
	public int hours() {
		return hours;
	}


	/**
	 * Retorna a diferença de minutos entre as datas.
	 * @return diferença em minutos.
	 */
	public int minutes() {
		return mins;
	}


	/**
	 * Retorna a diferença de segundos entre as datas.
	 * @return diferença em segundos.
	 */
	public int seconds() {
		return secs;
	}


	/**
	 * Retorna a diferença de milisegundos entre as datas.
	 * @return diferença em milisegundos.
	 */
	public long millis() {
		return millis;
	}
	
	
	/**
	 * Calcula a diferença de tempo entre as datas.
   * Este método deve ser chamado a cada alteração
   * nas configurações de DateDiff.
   * @return Esta intância modificada de <code>DateDiff</code>
	 */
	public DateDiff calculate() {
		if(date1 == null || date2 == null) return null;
		this.reset();
    LocalDateTime old1 = date1, old2 = date2;
		//se date2 for antes de date1, inverte
    if(date1.isAfter(date2)) {
			factor = -1;
			LocalDateTime temp = date1;
			date1 = date2;
			date2 = temp;
		}
		//calcula a diferença de tempo em milisegundos
		millis = date2.toInstant(ZoneOffset.UTC).toEpochMilli() - date1.toInstant(ZoneOffset.UTC).toEpochMilli();
		//resto dos segundos
		secs = (int) (millis / 1000 % 60);
		//resto dos minutos
		mins = (int) (millis / 1000 / 60 % 60);
		//resto das horas
		hours = (int) (millis / 1000 / 60 / 60 % 24);
		//resto dos dias
		days = (millis / 1000 / 60 / 60 / 24);
		//resto dos milisegundos
		millis = millis % 1000;
		LocalDateTime date = LocalDateTime.from(date1);
		lastDayOfMonth = date.toLocalDate().lengthOfMonth();
		while(days >= lastDayOfMonth) {
			days -= lastDayOfMonth;
			months++;
      date = date.plusMonths(1);
      lastDayOfMonth = date.toLocalDate().lengthOfMonth();
			if(months >= 12) {
				months = 0;
				years++;
			}
		}
    date1 = old1;
    date2 = old2;
		return this;
	}
	
	
  /**
   * Retorna a diferença de tempo
   * somente em milisegundos entre as datas.
   * @return Diferença somente em milisegundos
   * entre as datas.
   */
	public long toMillis() {
    if(date1 == null || date2 == null) {
      return -1;
    }
    long totalMillis = 0;
    if(date1.isAfter(date2)) {
      totalMillis = date1.toInstant(ZoneOffset.UTC).toEpochMilli() 
              - date2.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    else {
      totalMillis = date2.toInstant(ZoneOffset.UTC).toEpochMilli() 
              - date1.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    return totalMillis;
	}
  
  
  /**
   * Retorna a diferença de tempo
   * somente em segundos entre as datas.
   * @return Diferença somente em segundos
   * entre as datas.
   */
  public long toSeconds() {
    long ml = this.toMillis();
    if(ml <= 0) return ml;
    return ml / 1000;
  }
  
  
  /**
   * Retorna a diferença de tempo
   * somente em minutos entre as datas.
   * @return Diferença somente em minutos
   * entre as datas.
   */
  public long toMinutes() {
    long sec = this.toSeconds();
    if(sec <= 0) return sec;
    return sec / 60;
  }


  /**
   * Retorna a diferença de tempo
   * somente em horas entre as datas.
   * @return Diferença somente em horas
   * entre as datas.
   */
  public long toHours() {
    long min = this.toMinutes();
    if(min <= 0) return min;
    return min / 60;
  }


  /**
   * Retorna a diferença de tempo
   * somente em dias entre as datas.
   * @return Diferença somente em dias
   * entre as datas.
   */
  public long toDays() {
    long hrs = this.toHours();
    if(hrs <= 0) return hrs;
    return hrs / 24;
  }


  /**
   * Retorna a diferença de tempo somente
   * em meses entre as datas.
   * @return Diferença somente em meses
   * entre as datas.
   */
  public int toMonths() {
    if(date1 == null || date2 == null) {
      return -1;
    }
    this.calculate();
    return this.months() + this.years() * 12;
  }


	@Override
	public String toString() {
		String s = "";
		if(years != 0) s += String.format("%d year(s), ", years);
		if(months != 0) s += String.format("%d month(s), ", months);
		if(days != 0) s += String.format("%d day(s), ", days);
    s += String.format("%dh:%dm:%ds.%dms", hours, mins, secs, millis);
		if(factor < 0) {
			s = s.replaceAll("-", "");
			s = "-" + s;
		}
		return s;
	}

}
