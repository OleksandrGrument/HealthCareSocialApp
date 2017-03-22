package com.comeonbabys.android.app.db.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDTO implements Serializable {

    public static final int EXELENT = 1;
    public static final int GOOD = 2;
    public static final int BAD = 3;

	private static final long serialVersionUID = 7690088718639447401L;
	private int year;
	private int month;
	private int week;
	private int statusMonth;
	private int statusWeek;

	private Map<Integer, Float> bbts;
	private int countAlcohol;
	private int stateAlcohol;
	private int glassesAlcohol;
	private double avergeAlcohol;
	private int countBath;
	private int stateBath;
	private int countCoffee;
	private int stateCoffee;
	private int countExercise;
	private int stateExercise;
	private int countFolic;
	private int stateFolic;
	private int countFood;
	private int stateFood;
	private int countSleepBefore;
	private int countSleep;
	private int minutesSleep;
    private int averageSleep;
	private int stateSleep;
	private int countNuts;
	private int stateNuts;
	private int countSmoke;
	private int stateSmoke;
	private int countTea;
	private int stateTea;
	private int countVitamin;
	private int stateVitamin;
	private int countWater;
	private int stateWater;
	private double litresWater;
	private double averageWater;
	private int[] emotions;

	public ReportDTO() {
		bbts = new HashMap<>();
		emotions = new int[5];
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getYear() {return year;}
	public void setYear(int year) {this.year = year;}
	public int getMonth() {return month;}
	public void setMonth(int month) {this.month = month;}
	public int getWeek() {return week;}
	public void setWeek(int week) {this.week = week;}
	public int getStatusMonth() {return statusMonth;}
	public void setStatusMonth(int statusMonth) {this.statusMonth = statusMonth;}
	public int getStatusWeek() {return statusWeek;}
	public void setStatusWeek(int statusWeek) {this.statusWeek = statusWeek;}
	public Map<Integer, Float> getBbts() {return bbts;}
	public void setBbts(Map<Integer, Float> bbts) {this.bbts = bbts;}
	public int getCountAlcohol() {return countAlcohol;}
	public void setCountAlcohol(int countAlcohol) {this.countAlcohol = countAlcohol;}
	public int getStateAlcohol() {return stateAlcohol;}
	public void setStateAlcohol(int stateAlcohol) {this.stateAlcohol = stateAlcohol;}
    public int getGlassesAlcohol() {return glassesAlcohol;}
    public void setGlassesAlcohol(int glassesAlcohol) {this.glassesAlcohol = glassesAlcohol;}
    public double getAvergeAlcohol() {return avergeAlcohol;}
    public void setAvergeAlcohol(double avergeAlcohol) {this.avergeAlcohol = avergeAlcohol;}
    public int getCountBath() {return countBath;}
	public void setCountBath(int countBath) {this.countBath = countBath;}
	public int getStateBath() {return stateBath;}
	public void setStateBath(int stateBath) {this.stateBath = stateBath;}
	public int getCountCoffee() {return countCoffee;}
	public void setCountCoffee(int countCoffee) {this.countCoffee = countCoffee;}
	public int getStateCoffee() {return stateCoffee;}
	public void setStateCoffee(int stateCoffee) {this.stateCoffee = stateCoffee;}
	public int getCountExercise() {return countExercise;}
	public void setCountExercise(int countExercise) {this.countExercise = countExercise;}
	public int getStateExercise() {return stateExercise;}
	public void setStateExercise(int stateExercise) {this.stateExercise = stateExercise;}
	public int getCountFolic() {return countFolic;}
	public void setCountFolic(int countFolic) {this.countFolic = countFolic;}
	public int getStateFolic() {return stateFolic;}
	public void setStateFolic(int stateFolic) {this.stateFolic = stateFolic;}
	public int getCountFood() {return countFood;}
	public void setCountFood(int countFood) {this.countFood = countFood;}
	public int getStateFood() {return stateFood;}
	public void setStateFood(int stateFood) {this.stateFood = stateFood;}
	public int getCountSleepBefore() {return countSleepBefore;}
	public void setCountSleepBefore(int countSleepBefore) {this.countSleepBefore = countSleepBefore;}
	public int getCountSleep() {return countSleep;}
	public void setCountSleep(int countSleep) {this.countSleep = countSleep;}
	public int getMinutesSleep() {return minutesSleep;}
	public void setMinutesSleep(int minutesSleep) {this.minutesSleep = minutesSleep;}
    public int getAverageSleep() {return averageSleep;}
    public void setAverageSleep(int averageSleep) {this.averageSleep = averageSleep;}
    public int getStateSleep() {return stateSleep;}
	public void setStateSleep(int stateSleep) {this.stateSleep = stateSleep;}
	public int getCountNuts() {return countNuts;}
	public void setCountNuts(int countNuts) {this.countNuts = countNuts;}
	public int getStateNuts() {return stateNuts;}
	public void setStateNuts(int stateNuts) {this.stateNuts = stateNuts;}
	public int getCountSmoke() {return countSmoke;}
	public void setCountSmoke(int countSmoke) {this.countSmoke = countSmoke;}
	public int getStateSmoke() {return stateSmoke;}
	public void setStateSmoke(int stateSmoke) {this.stateSmoke = stateSmoke;}
	public int getCountTea() {return countTea;}
	public void setCountTea(int countTea) {this.countTea = countTea;}
	public int getStateTea() {return stateTea;}
	public void setStateTea(int stateTea) {this.stateTea = stateTea;}
	public int getCountVitamin() {return countVitamin;}
	public void setCountVitamin(int countVitamin) {this.countVitamin = countVitamin;}
	public int getStateVitamin() {return stateVitamin;}
	public void setStateVitamin(int stateVitamin) {this.stateVitamin = stateVitamin;}
	public int getCountWater() {return countWater;}
	public void setCountWater(int countWater) {this.countWater = countWater;}
    public double getLitresWater() {return litresWater;}
    public void setLitresWater(double litresWater) {this.litresWater = litresWater;}
    public int getStateWater() {return stateWater;}
    public double getAverageWater() {return averageWater;}
    public void setAverageWater(double averageWater) {this.averageWater = averageWater;}
    public void setStateWater(int stateWater) {this.stateWater = stateWater;}
    public int[] getEmotions() {return emotions;}
    public void setEmotions(int[] emotions) {this.emotions = emotions;}

    //Полученое отчета для недели/месяца (обработка данных из списка NoteDTO)
    //Если нужен отчет для месяца, тогда week=0, иначе передаем номер недели
    public static ReportDTO getReport(int year, int month, int week,  List<NoteDTO> list) {
        ReportDTO report = new ReportDTO();
        report.setYear(year);
        report.setMonth(month);
        report.setWeek(week);
        for(NoteDTO noteDTO : list) {
            if(noteDTO.getBbt() != null) {
                report.getBbts().put(noteDTO.getDay(), Float.parseFloat(noteDTO.getBbt()));
            }
            if(noteDTO.getAlcohol_consumption() != null && !noteDTO.getAlcohol_consumption().equals("0")) {
                //int alcohol = Integer.parseInt(noteDTO.getAlcohol_consumption());
                report.setCountAlcohol(report.getCountAlcohol() + 1);
                report.setGlassesAlcohol(report.getGlassesAlcohol() + Integer.parseInt(noteDTO.getAlcohol_consumption()));
            }
            if(noteDTO.getHip_bath() != null) {
                report.setCountBath(report.getCountBath() + 1);
            }
            if(noteDTO.getCoffee_intake() != null && noteDTO.getCoffee_intake().equals("2")) {
                report.setCountCoffee(report.getCountCoffee() + 1);
            }
            if(noteDTO.isHas_exercise() != null && noteDTO.isHas_exercise().equals("true")) {
                report.setCountExercise(report.getCountExercise() + 1);
            }
            if(noteDTO.getFolate() != null && noteDTO.getFolate().equals("true")) {
                report.setCountFolic(report.getCountFolic() + 1);
            }
            if(noteDTO.getRecommended_foods() != null && !noteDTO.getRecommended_foods().equals("")) {
                report.setCountFood(report.getCountFood() + 1);
            }
            if(noteDTO.getGoing_to_bed_to() != null && noteDTO.getGoing_to_bed_from() != null) {
                //Сохраняем колличество учитываемых дней
                report.setCountSleep(report.getCountSleep() + 1);
                //Разбиваем время на часы и минуты
                String[] toBed = noteDTO.getGoing_to_bed_to().split(":");
                String[] fromBed = noteDTO.getGoing_to_bed_from().split(":");
                //Если время, когда лягли спать меньше 12:00, то это следующий день
                int day = 1;
                if(Integer.parseInt(toBed[0]) < 12) day = 2;
                Calendar from = new GregorianCalendar(1970, 0, 1, Integer.parseInt(fromBed[0]), Integer.parseInt(fromBed[1]));
                Calendar to = new GregorianCalendar(1970, 0, day, Integer.parseInt(toBed[0]), Integer.parseInt(toBed[1]));

                //Высчитываем минуты сна
                long ms = to.getTimeInMillis() - from.getTimeInMillis();
                int minutes = 24*60 - Math.abs((int) (ms/60000L));
                //Сохраняем колличество проспаных минут
                report.setMinutesSleep(report.getMinutesSleep() + minutes);
                //Если ложились спать после 12:00 то значит лягли до полуночи
                if(Integer.parseInt(toBed[0]) > 12) {  //TODO как определить когда лег спать?
                    report.setCountSleepBefore(report.getCountSleepBefore() + 1);
                }
            }
            if(noteDTO.isHas_nut() != null && noteDTO.isHas_nut().equals("true")) {
                report.setCountNuts(report.getCountNuts() + 1);
            }
            if(noteDTO.getSmoking() != null && noteDTO.getSmoking().equals("true")) {
                report.setCountSmoke(report.getCountSmoke() + 1);
            }
            if(noteDTO.isHas_tea() != null && noteDTO.isHas_tea().equals("true")) {
                report.setCountTea(report.getCountTea() + 1);
            }
            if(noteDTO.getVitamin() != null && noteDTO.getVitamin().equals("true")) {
                report.setCountVitamin(report.getCountVitamin() + 1);
            }
            if(noteDTO.getWater_intake() != null) {
                double litres = Double.parseDouble(noteDTO.getWater_intake());
                report.setCountWater(report.getCountWater() + 1);
                report.setLitresWater(report.getLitresWater() + litres);
            }
            if(noteDTO.getEmotional_state() != null) {
                int state = Integer.parseInt(noteDTO.getEmotional_state());
                int[] em = report.getEmotions();
                em[state] = ++em[state];
            }
        }
        //Расчитать рекомендуеые значения
        Calendar calMax = new GregorianCalendar(year, month - 1, 1);
        if(week == 0) report.calculateStates(calMax.getActualMaximum(Calendar.DAY_OF_MONTH));
        else report.calculateStates(7);
        return report;
    }

    //Расчитать рекомендуеые значения
    public void calculateStates(int numOfDays) {
        //Для месяца
        if(numOfDays > 7) {
            if(countAlcohol < 11) setStateAlcohol(EXELENT);
            else if(countAlcohol < 21) setStateAlcohol(GOOD);
            else setStateAlcohol(BAD);
            if(countAlcohol != 0) {
                avergeAlcohol = ((double) Math.round(((double) glassesAlcohol/numOfDays)*100)) / 100;
            }

            if(countBath > 20) setStateBath(EXELENT);
            else if(countBath > 10) setStateBath(GOOD);
            else setStateBath(BAD);

            if(countCoffee < 11) setStateCoffee(EXELENT);
            else if(countCoffee < 21) setStateCoffee(GOOD);
            else setStateCoffee(BAD);

            if(countExercise > 20) setStateExercise(EXELENT);
            else if(countExercise > 10) setStateExercise(GOOD);
            else setStateExercise(BAD);

            if(countFolic > 20) setStateFolic(EXELENT);
            else if(countFolic > 10) setStateFolic(GOOD);
            else setStateFolic(BAD);

            if(countFood > 20) setStateFood(EXELENT);
            else if(countFood > 10) setStateFood(GOOD);
            else setStateFood(BAD);

            if(countSleepBefore > 20) setStateSleep(EXELENT);
            else if(countSleepBefore > 10) setStateSleep(GOOD);
            else setStateSleep(BAD);
            averageSleep = countSleep == 0 ? 0 : (int) (minutesSleep/countSleep);

            if(countNuts > 20) setStateNuts(EXELENT);
            else if(countNuts > 10) setStateNuts(GOOD);
            else setStateNuts(BAD);

            if(countSmoke < 11) setStateSmoke(EXELENT);
            else if(countSmoke < 21) setStateSmoke(GOOD);
            else setStateSmoke(BAD);

            if(countTea > 20) setStateTea(EXELENT);
            else if(countTea > 10) setStateTea(GOOD);
            else setStateTea(BAD);

            if(countVitamin > 20) setStateVitamin(EXELENT);
            else if(countVitamin > 10) setStateVitamin(GOOD);
            else setStateVitamin(BAD);

			if(countWater != 0) {
				averageWater = ((double) Math.round(((double) litresWater/countWater)*100)) / 100;
			}
			if(averageWater >= 1.5) setStateWater(EXELENT);
			else if(averageWater >= 1) setStateWater(GOOD);
			else setStateWater(BAD);
        //Для недели
        } else {
            if(countAlcohol < 3) setStateAlcohol(EXELENT);
            else if(countAlcohol < 6) setStateAlcohol(GOOD);
            else setStateAlcohol(BAD);
            if(countAlcohol != 0) {
                avergeAlcohol = ((double) Math.round(((double) glassesAlcohol/numOfDays)*100)) / 100;
            }

            if(countBath > 5) setStateBath(EXELENT);
            else if(countBath > 2) setStateBath(GOOD);
            else setStateBath(BAD);

            if(countCoffee < 3) setStateCoffee(EXELENT);
            else if(countCoffee < 6) setStateCoffee(GOOD);
            else setStateCoffee(BAD);

            if(countExercise > 5) setStateExercise(EXELENT);
            else if(countExercise > 2) setStateExercise(GOOD);
            else setStateExercise(BAD);

            if(countFolic > 5) setStateFolic(EXELENT);
            else if(countFolic > 2) setStateFolic(GOOD);
            else setStateFolic(BAD);

            if(countFood > 5) setStateFood(EXELENT);
            else if(countFood > 2) setStateFood(GOOD);
            else setStateFood(BAD);

            if(countSleepBefore > 5) setStateSleep(EXELENT);
            else if(countSleepBefore > 2) setStateSleep(GOOD);
            else setStateSleep(BAD);
            averageSleep = countSleep == 0 ? 0 : (int) (minutesSleep/countSleep);

            if(countNuts > 5) setStateNuts(EXELENT);
            else if(countNuts > 2) setStateNuts(GOOD);
            else setStateNuts(BAD);

            if(countSmoke < 3) setStateSmoke(EXELENT);
            else if(countSmoke < 6) setStateSmoke(GOOD);
            else setStateSmoke(BAD);

            if(countTea > 5) setStateTea(EXELENT);
            else if(countTea > 2) setStateTea(GOOD);
            else setStateTea(BAD);

            if(countVitamin > 5) setStateVitamin(EXELENT);
            else if(countVitamin > 2) setStateVitamin(GOOD);
            else setStateVitamin(BAD);

            if(countWater != 0) {
                averageWater = ((double) Math.round(((double) litresWater/countWater)*100)) / 100;
            }
            if(averageWater >= 1.5) setStateWater(EXELENT);
            else if(averageWater >= 1) setStateWater(GOOD);
            else setStateWater(BAD);
        }
        calculateFinalState();
    }

    //Общие месячные/недельные значения
    private void calculateFinalState() {
            int state = stateAlcohol + stateBath + stateCoffee + stateExercise + stateFolic + stateFood +
                    stateSleep + stateNuts + stateSmoke + stateTea + stateVitamin + stateWater;
            int finalState = Math.round(state / 12);
            statusMonth = finalState;
            statusWeek = finalState;
    }
}