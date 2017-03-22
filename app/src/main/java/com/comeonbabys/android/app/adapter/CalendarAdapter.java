package com.comeonbabys.android.app.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.db.dto.NoteDTO;
import com.comeonbabys.android.app.db.dto.ProfileDTO;
import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.view.customview.Day;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class CalendarAdapter extends BaseAdapter {

    static final int FIRST_DAY_OF_WEEK = 0;
    Context context;
    Calendar cal;
    public String[] days;
    List<NoteDTO> noteList = new ArrayList<NoteDTO>();
    ArrayList<Day> dayList = new ArrayList<Day>();
    private static final String TAG = "CalendarAdapter";

    public CalendarAdapter(Context context, Calendar cal) {
        this.cal = cal;
        this.context = context;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setCycleDates();
        refreshDays();
    }

    //Установка дат начала цикла и конца критических дней
    Calendar cycleStart;
    Calendar redDaysEnd;
    Calendar ovulationDate;
    Calendar fertStartDate;
    Calendar fertEndDate;
    private void setCycleDates() {
        ProfileDTO profileDTO = AppSession.getSession().getSystemUser().getProfileDTO();
        if (profileDTO != null && profileDTO.getLast_cycle() != null && !TextUtils.isEmpty(profileDTO.getLast_cycle())
                && profileDTO.getRed_days() != 0 && profileDTO.getMenstrual_cycle() != 0) {
            String[] split = profileDTO.getLast_cycle().split("-");
            cycleStart = new GregorianCalendar(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]));
            redDaysEnd = new GregorianCalendar();
            redDaysEnd.setTime(cycleStart.getTime());
            redDaysEnd.add(Calendar.DAY_OF_MONTH, profileDTO.getRed_days());
            ovulationDate = new GregorianCalendar();
            ovulationDate.setTime(cycleStart.getTime());
            ovulationDate.add(Calendar.DAY_OF_MONTH, profileDTO.getMenstrual_cycle() - 14);
            fertStartDate = new GregorianCalendar();
            fertStartDate.setTime(ovulationDate.getTime());
            fertStartDate.add(Calendar.DAY_OF_MONTH, -4);
            fertEndDate = new GregorianCalendar();
            fertEndDate.setTime(ovulationDate.getTime());
            fertEndDate.add(Calendar.DAY_OF_MONTH, +2);
            Log.d(TAG, "Cycle last date" + profileDTO.getLast_cycle()
                    + " Red" + profileDTO.getRed_days()
                    + " Duration" + profileDTO.getMenstrual_cycle()
                    + " Ovulation " + ovulationDate.get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


//    public int getPrevMonth() {
//        if (cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)) {
//            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR - 1));
//        } else {
//
//        }
//        int month = cal.get(Calendar.MONTH);
//        if (month == 0) {
//            return month = 11;
//        }
//        return month - 1;
//    }

    //unused
    public int getPrevMonth() {
        if (cal.get(Calendar.MONTH) == Calendar.JANUARY) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        }
        int month = cal.get(Calendar.MONTH);
        if (month == 0) {
            return month = 11;
        }
        return month - 1;
    }

    public int getMonth() {
        return cal.get(Calendar.MONTH);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.day_view, null);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        Day day = dayList.get(position);

        float heightPixels = (float) ((context.getResources().getDisplayMetrics().widthPixels / 7) * 1.2);
        v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) heightPixels));
        TextView tvDay = (TextView) v.findViewById(R.id.textView1);
        LinearLayout rl = (LinearLayout) v.findViewById(R.id.rl);
        RelativeLayout rl1 = (RelativeLayout) v.findViewById(R.id.rl1);
        ImageView imgTopRight = (ImageView) v.findViewById(R.id.imageTopRight);
        ImageView imgBottomLeft = (ImageView) v.findViewById(R.id.imageBottomLeft);
        ImageView imgBottomRight = (ImageView) v.findViewById(R.id.imageBottomRight);

        imgBottomLeft.setVisibility(View.INVISIBLE);
        imgBottomRight.setVisibility(View.INVISIBLE);
        imgTopRight.setVisibility(View.INVISIBLE);
        tvDay.setVisibility(View.VISIBLE);
        rl.setVisibility(View.VISIBLE);
        rl1.setVisibility(View.VISIBLE);

        //Установка карандашика, если для текущего дня существует заметка
        if (day.getNote() != null) {
            imgTopRight.setVisibility(View.VISIBLE);
        } else {
            imgTopRight.setVisibility(View.INVISIBLE);
        }

        //Установка базового бекграунда дня
        rl.setBackgroundResource(R.drawable.date_number_background);

//Старая логика
//        if (day.getYear() == cal.get(Calendar.YEAR) && day.getMonth() == cal.get(Calendar.MONTH) && day.getDay() == cal.get(Calendar.DAY_OF_MONTH)) {
//            rl.setBackgroundResource(R.drawable.date_number_today_background);
//        } else {
//            rl.setBackgroundResource(R.drawable.date_number_background);
//        }

        //Установка отметок и цветов дней цикла
        Calendar currentDay = new GregorianCalendar(day.getYear(), day.getMonth(), day.getDay());
        if (cycleStart != null) {
            //Установка меток для критических дней
            if (currentDay.getTime().getTime() >= cycleStart.getTime().getTime()
                    && currentDay.getTime().getTime() < redDaysEnd.getTime().getTime()) {
                rl.setBackgroundResource(R.color.color_red_days_background);
                imgBottomRight.setVisibility(View.VISIBLE);
            } else {
                //Установка отметки овуляции
                if (currentDay.getTime().getTime() == ovulationDate.getTime().getTime()) {
                    imgBottomLeft.setImageResource(R.drawable.icon_giaophoi_cal);
                    imgBottomLeft.setVisibility(View.VISIBLE);
                    rl.setBackgroundResource(R.color.color_ovulation_day_background);
                }
                //Установка меток для "опасных" дней
                else if (currentDay.getTime().getTime() >= fertStartDate.getTime().getTime()
                        && currentDay.getTime().getTime() <= fertEndDate.getTime().getTime()) {
                    imgBottomLeft.setVisibility(View.VISIBLE);
                    imgBottomLeft.setImageResource(R.drawable.icon_rungtrung_cal);
                    rl.setBackgroundResource(R.color.color_fert_days_background);
                }
            }
        }

        //Установка бекграунда дня (текущий день календаря или нет)
        if(day.getYear() == cal.get(Calendar.YEAR) && day.getMonth() == cal.get(Calendar.MONTH) && day.getDay() == cal.get(Calendar.DAY_OF_MONTH)) {
            //rl1.setBackgroundColor(v.getResources().getColor(R.color.color_current_day_front));
            rl1.setBackgroundColor(0xC8CFD8DC); //TODO считывание из ресурсов "color_current_day_front" (deprecated)
        }

        //Если день пустой (перед или после текущего месяца), то не показываем его
        if (day.getDay() == 0) {
            rl1.setVisibility(View.GONE);
            v.setEnabled(false);
        } else {
            tvDay.setVisibility(View.VISIBLE);
            tvDay.setText(String.valueOf(day.getDay()));
            v.setEnabled(true);
        }
        return v;
    }

    //Возврат true, если день попадает в текущий месяц текущего календаря
    @Override
    public boolean isEnabled(int position) {
        Day d = (Day) getItem(position);
        if (d.getDay() == 0 || isLargeDay(d))
            return false;
        return true;
    }

    //Возвращает true, если дата дня больше даты текущего календаря
    private boolean isLargeDay(Day day) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        if (day.getYear() > cal.get(Calendar.YEAR))
            return true;
        else if (day.getYear() == cal.get(Calendar.YEAR)) {
            if (day.getMonth() > cal.get(Calendar.MONTH))
                return true;
            else if (day.getMonth() == cal.get(Calendar.MONTH)) {
                if (day.getDay() > cal.get(Calendar.DAY_OF_MONTH))
                    return true;
            }
        }
        return false;
    }

    //Присвоить список заметок адаптеру
    public void setListNote(List<NoteDTO> notes) {
        noteList = notes;
//        Log.d(TAG, "Колличество заметок" + noteList.size());
    }

    //Найти заметку для даты из списка или вернуть null
    private NoteDTO getNote(int day, int year, int month) {
        if (noteList != null)
            for (NoteDTO note : noteList) {
                if (day == note.getDay() && month == note.getMonth() && year == note.getYear()) {
                    return note;
                }
            }
        return null;
    }

    //Пересобрать dayList и days[] для текущего месяца, найти заметки для дней
    public void refreshDays() {
        // clear items
        dayList.clear();

        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int) cal.get(Calendar.DAY_OF_WEEK);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        TimeZone tz = TimeZone.getDefault();
        int max = 0;
        int mode = 0;

        // определение размера массива
        if (firstDay == 1) {
            max = lastDay + (FIRST_DAY_OF_WEEK * 6);
            int du = 42 - max;
            mode = du % 7;
            days = new String[max + mode];
        } else {
            max = lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1);
            int du = 42 - max;
            mode = du % 7;
            days = new String[max + mode];
        }

        int j = FIRST_DAY_OF_WEEK;

        // запись пустых дней перед текущим месяцем
        if (firstDay > 1) {
            for (j = 0; j < (firstDay - FIRST_DAY_OF_WEEK); j++) {
                days[j] = "";
                Day d = new Day(context, 0, 0, 0);
                dayList.add(d);
            }
        } else {
            for (j = 0; j < (FIRST_DAY_OF_WEEK * 6); j++) {
                days[j] = "";
                Day d = new Day(context, 0, 0, 0);
                dayList.add(d);
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        // запись дней текущего месяца
        int dayNumber = 1;
        if (j > 0 && dayList.size() > 0 && j != 1) {
            dayList.remove(j - 1);
        }
        for (int i = j - 1; i < days.length - mode; i++) {
            Day d = new Day(context, dayNumber, year, month);
            Calendar cTemp = Calendar.getInstance();
            cTemp.set(year, month, dayNumber);
            int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));
            NoteDTO note = getNote(dayNumber, year, month + 1);  //получаем NoteDTO для текущего дня
            d.addNote(note);                                     //добавляем NoteDTO в Day
            d.setAdapter(this);
            d.setStartDay(startDay);

            days[i] = "" + dayNumber;
            dayNumber++;
            dayList.add(d);                                        //добавляем день в ArrayList<Day>
        }

        // запись пустых дней после текущего месяца
        for (int i = 0; i < mode; i++) {
            days[max + i] = "";
            Day d = new Day(context, 0, 0, 0);
            dayList.add(d);
        }
    }
}