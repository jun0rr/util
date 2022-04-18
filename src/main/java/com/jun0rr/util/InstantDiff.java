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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * DateDiff representa a diferença de tempo entre duas
 * datas.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see us.pserver.tools.date.SimpleDate
 * @version 3.0 - 2011.11.16
 */
public class InstantDiff {
	
	private int years;
	private int months;
	private long days;
	private int hours;
	private int mins;
	private int secs;
	private long millis;
	private int lastDayOfMonth;
	private int factor;
	private final Instant inst1;
	private final Instant inst2;
	
	
	/**
	 * Construtor que recebe as duas datas para cálculo
	 * da diferença de tempo.
	 * @param i1 Primeira data
	 * @param i2 Segunda data
	 */
	public InstantDiff(Instant i1, Instant i2) {
    this.inst1 = Match.notNull(i1).getOrFail();
    this.inst2 = Match.notNull(i2).getOrFail();
    this.calculate();
	}
  
  
  public static InstantDiff of(Instant i1, Instant i2) {
    return new InstantDiff(i1, i2);
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
	 * Retorna a primeira data utilizada no cálculo 
	 * da diferença.
	 * @return Primeira data.
	 */
	public Instant instant1() {
		return inst1;
	}
	
	
	/**
	 * Retorna a segunda data utilizada no cálculo 
	 * da diferença.
	 * @return Segunda data.
	 */
	public Instant instant2() {
		return inst2;
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
	public InstantDiff calculate() {
		this.reset();
    Instant t1 = inst1.isAfter(inst2) ? inst2 : inst1;
    Instant t2 = inst2.isAfter(inst1) ? inst2 : inst1;
		//calcula a diferença de tempo em milisegundos
		millis = t2.toEpochMilli() - t1.toEpochMilli();
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
		LocalDateTime calc = LocalDateTime.ofInstant(inst1, ZoneOffset.UTC);
		lastDayOfMonth = calc.toLocalDate().lengthOfMonth();
		while(days >= lastDayOfMonth) {
			days -= lastDayOfMonth;
			months++;
      calc = calc.plusMonths(1);
      lastDayOfMonth = calc.toLocalDate().lengthOfMonth();
			if(months >= 12) {
				months = 0;
				years++;
			}
		}
		return this;
	}
	
	
  /**
   * Retorna a diferença de tempo
   * somente em milisegundos entre as datas.
   * @return Diferença somente em milisegundos
   * entre as datas.
   */
	public long toMillis() {
    if(inst1 == null || inst2 == null) {
      return -1;
    }
    long totalMillis = 0;
    if(inst1.isAfter(inst2)) {
      totalMillis = inst1.toEpochMilli() - inst2.toEpochMilli();
    }
    else {
      totalMillis = inst2.toEpochMilli() - inst1.toEpochMilli();
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
    if(inst1 == null || inst2 == null) {
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
