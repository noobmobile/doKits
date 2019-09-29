package com.dont.kits.utils;

import java.util.concurrent.TimeUnit;

public class Time {

    public static String getRemainingTime(long secondsIn) {
  	long dayCount = TimeUnit.SECONDS.toDays(secondsIn);
  	long secondsCount = secondsIn - TimeUnit.DAYS.toSeconds(dayCount);
  	long hourCount = TimeUnit.SECONDS.toHours(secondsCount);
  	secondsCount -= TimeUnit.HOURS.toSeconds(hourCount);
  	long minutesCount = TimeUnit.SECONDS.toMinutes(secondsCount);
  	secondsCount -= TimeUnit.MINUTES.toSeconds(minutesCount);
  	StringBuilder sb = new StringBuilder();
  	if (dayCount != 0)
  	    sb.append(String.format("%d %s, ", dayCount, (dayCount == 1) ? "dia" : "dias"));
  	if (hourCount != 0)
  	    sb.append(String.format("%d %s, ", hourCount, (hourCount == 1) ? "hora" : "horas"));
  	if (minutesCount != 0)
  	    sb.append(String.format("%d %s e ", minutesCount, (minutesCount == 1) ? "minuto" : "minutos"));
  	if (secondsCount != 0)
  	    sb.append(String.format("%d %s.", secondsCount, (secondsCount == 1) ? "segundo" : "segundos"));
  	return sb.toString();
      }
    
}
