package dtd.phs.sil.utils;

import java.util.Calendar;
import java.util.HashMap;

public class FrequencyHelpers {
	/**
	 * The order is IMPORTANT - please be carefull
	 * @author Pham Hung Son
	 *
	 */
	public enum Frequencies {
		ONCE,DAILY,WEEKLY,MONTHLY,YEARLY,
		EV_WEEK_DAY,EV_WEEKEND};
		//		,EV_5,EV_15,EV_30,
		//		EV_HOUR,EV_2_HOURS,EV_12_HOURS
		//	}

		static public final Frequencies[] FREQUENCIES = {
			Frequencies.ONCE,Frequencies.DAILY,Frequencies.WEEKLY,Frequencies.MONTHLY,Frequencies.YEARLY,
			Frequencies.EV_WEEK_DAY,Frequencies.EV_WEEKEND};
		//	,Frequencies.EV_5,Frequencies.EV_15,
		//		Frequencies.EV_30,Frequencies.EV_HOUR,Frequencies.EV_2_HOURS,Frequencies.EV_12_HOURS
		//	};

		static public final String[] FREQ_NAMES = {
			"Once","Daily","Weekly","Monthly","Yearly",
			"Weekday (Mon.-Fri.)","Weekend (Sat. Sun.)"
		};
		//	,"Every 5 minutes",
		//		"Every 15 minutes","Every 30 minutes","Every hour","Every 2 hours","Every 12 hours"};

		static public final HashMap<Frequencies, String> mapFreq2Str = new HashMap<Frequencies, String>();
		static {
			mapFreq2Str.put(Frequencies.ONCE, "Once");
			mapFreq2Str.put(Frequencies.DAILY, "Daily");
			mapFreq2Str.put(Frequencies.WEEKLY, "Weekly");
			mapFreq2Str.put(Frequencies.MONTHLY, "Monthly");
			mapFreq2Str.put(Frequencies.YEARLY, "Yearly");
			mapFreq2Str.put(Frequencies.EV_WEEK_DAY, "Weekday (Mon.-Fri.)");
			mapFreq2Str.put(Frequencies.EV_WEEKEND, "Weekend (Sat. Sun.)");

			//		mapFreq2Str.put(Frequencies.EV_5, "Every 5 minutes");
			//		mapFreq2Str.put(Frequencies.EV_15, "Every 15 minutes");
			//		mapFreq2Str.put(Frequencies.EV_30, "Every 30 minutes");
			//		mapFreq2Str.put(Frequencies.EV_HOUR, "Every hour");
			//		mapFreq2Str.put(Frequencies.EV_2_HOURS, "Every 2 hours");
			//		mapFreq2Str.put(Frequencies.EV_12_HOURS, "Every 12 hours");
		}

		/**
		 * Get next occurence that fullfilled (mathmatical formulated):
		 * nextOccurence(Calendar calendar,Frequencies f,long currentMillis) = time , such that:
		 * time = min{ t | t >= currentMillis ^ t >= calendar.getTimeInMillis() ^ t is fullfilled(calendar,f) }
		 * @param calendar
		 * @param f
		 * @return next occurence, null if there is no
		 */
		//TODO: create unit tests
		public static Calendar getNextCalendar(Calendar calendar,Frequencies f) {
			Calendar result = (Calendar) calendar.clone();

			long resultMills = calendar.getTimeInMillis();
			long minTime = Math.max(System.currentTimeMillis(),calendar.getTimeInMillis());

			switch (f) {
			case ONCE:
				if ( minTime > resultMills ) return null;
				else return result;
			case DAILY:
				adjustResultCalendarForNow(calendar, result, minTime);
				long millis = result.getTimeInMillis();
				if ( minTime > millis ) result.add(Calendar.DATE, 1);
				break;
			case WEEKLY:
				while ( resultMills < minTime ) {
					result.add(Calendar.DATE, 7);
					resultMills = result.getTimeInMillis();
				}
				break;
			case MONTHLY:
				while ( resultMills < minTime ) {
					result.add(Calendar.MONTH, 1);
					resultMills = result.getTimeInMillis();
				}
				break;
			case YEARLY:
				while ( resultMills < minTime ) {
					result.add(Calendar.YEAR, 1);
					resultMills = result.getTimeInMillis();
				}
				break;
			case EV_WEEK_DAY:
				adjustResultCalendarForNow(calendar, result, minTime);
				resultMills = result.getTimeInMillis();
				while ( ! isWeekDay(result) || resultMills < minTime) {
					result.add(Calendar.DATE, 1);
					resultMills = result.getTimeInMillis();
				}
				break;
			case EV_WEEKEND:
				adjustResultCalendarForNow(calendar, result, minTime);
				while ( isWeekDay(result)  || resultMills < minTime) {
					result.add(Calendar.DATE, 1);
					resultMills = result.getTimeInMillis();
				}
				break;			
//			case EV_5:
//				adjustTimeForEveryNMinutes(calendar, result, resultMills, minTime, 5);
//				break;
//			case EV_15:
//				adjustTimeForEveryNMinutes(calendar, result, resultMills, minTime, 15);
//				break;
//			case EV_30:
//				adjustTimeForEveryNMinutes(calendar, result, resultMills, minTime, 30);
//				break;
//			case EV_HOUR:
//				adjustTimeForEveryNMinutes(calendar, result, resultMills, minTime, 60);
//				break;
//			case EV_2_HOURS:
//				adjustTimeForEveryNMinutes(calendar, result, resultMills, minTime, 120);
//				break;
//			case EV_12_HOURS:
//				adjustTimeForEveryNMinutes(calendar, result, resultMills, minTime, 720);
//				break;
			}
			return result;
		}

		//False
//		private static void adjustTimeForEveryNMinutes(
//				Calendar calendar,
//				Calendar result, 
//				long resultMills, 
//				long minTime, 
//				int every) {
//			adjustResultCalendarForNow(calendar, result, minTime);
//			while (resultMills < minTime) {
//				result.add(Calendar.MINUTE, every);
//				resultMills = result.getTimeInMillis();
//			}
//		}

		/**
		 * Set the result such that: it's nearest of minTime  moment &&same HH:mm:ss as the calendar
		 * @param calendar
		 * @param result
		 * @param minTime
		 */
		private static void adjustResultCalendarForNow(
				Calendar calendar,
				Calendar result, 
				long minTime) {
			result.setTimeInMillis(minTime);
			result.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			result.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			result.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		}

		private static boolean isWeekDay(Calendar result) {
			switch (result.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY: return true;
			case Calendar.TUESDAY: return true;
			case Calendar.WEDNESDAY: return true;
			case Calendar.THURSDAY: return true;
			case Calendar.FRIDAY: return true;
			case Calendar.SATURDAY: return false;
			case Calendar.SUNDAY: return false;
			}
			return true;
		}

		public static int indexOf(Frequencies freq) {
			for(int i = 0 ; i < FREQUENCIES.length ; i++)
				if ( FREQUENCIES[i] == freq) return i;
			return -1;
		}
}	
