package com.comeonbabys.android.app.db.dto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.comeonbabys.android.app.requests.Constants;
import com.comeonbabys.android.app.requests.ExtraConstants;
import com.comeonbabys.android.app.requests.commands.Commands;
import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.view.BaseActivity;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Oleg Shevchenko on 27.01.2017.
 */

public class NotesHolder {

    private static NotesHolder instance;
    private static final String TAG = "NotesHolder";
    private static Handler handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                JSONArray jsdata = null;
                if (data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                switch (msg.what) {
                    case Constants.MSG_GET_NOTES_SUCCESS: {
                        Log.d(TAG, "!!!Get notes success!!!");
                        try {
                            if (data.containsKey(ExtraConstants.DATA)) {
                                jsdata = new JSONArray(data.getString(ExtraConstants.DATA));
                            }
                        } catch (JSONException exc) {exc.printStackTrace();}

                        if (jsdata != null) {
                            try {
                                for (int i = 0; i < jsdata.length(); i++) {
                                    NoteDTO note = new NoteDTO();
                                    note.parseFromJson(((JSONObject) jsdata.get(i)).toString());
                                    getInstanse().saveNote(note, false);
                                }
                                Intent intent = new Intent(Constants.GET_NOTES_SUCCESS_ACTION);
                                Activity act = BaseActivity.baseActivity;
                                if(act != null) {
                                    act.sendBroadcast(intent);
                                }

                            } catch (JSONException exc) {
                                exc.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "NOTES DATA NULL");
                        }
                        break;
                    }
                    //Unsuccessful login
                    case Constants.MSG_GET_NOTES_FAIL: {
                        Log.d(TAG, "GET NOTES FAIL MSG");
                        break;
                    }
                    //No connection
                    case Constants.MSG_ERROR: {
                        Log.d(TAG, "GET NOTES ERROR MSG");
                        break;
                    }
                }
            }
        };

    public static void updateNotes() {
        Log.d(TAG, "!!!Start get notes operation!!!");
        NotesHolder.getInstanse();
        Commands.getNotesOperation(handler, AppSession.getSession().getSystemUser(), null, null);
    }

    //Map<year, <Map<month, listOfNotes>>>
    private Map<Integer, Map<Integer, List<NoteDTO>>> notesHolder;

    //Singleton
    private NotesHolder() {
        notesHolder = new HashMap<>();
    }

    //Возвращает текущее хранилище заметок
    public static NotesHolder getInstanse() {
        if(instance == null) {
            instance = new NotesHolder();
            //instance.getNotesFromBase();
        }
        return instance;
    }

    public static NotesHolder newInstance(){
        instance = null;
        instance = getInstanse();
        return  instance;
    }

    //Возвращает карту с заметками на указаный год (null, если заметок на год нет)
    public Map<Integer, List<NoteDTO>> getYearNotesMap(int year) {
        return notesHolder.get(year);
    }

    //Возвращает карту с заметками на указаный год (null, если заметок на год нет)
    public Map<Integer, List<NoteDTO>> getYearNotesMap(Calendar cal) {
        if(cal == null) return null;
        return getYearNotesMap(cal.get(Calendar.YEAR));
    }

    //Возвращает список заметок на указаный год и месяц
    public List<NoteDTO> getMonthNotesList(int year, int month) {
        Map<Integer, List<NoteDTO>> yearNotes = getYearNotesMap(year);
        if(yearNotes != null && !yearNotes.isEmpty()) {
            return yearNotes.get(month);
        }
        return null;
    }

    //Возвращает список заметок на указаный год и месяц
    public List<NoteDTO> getMonthNotesList(Calendar cal) {
        if(cal == null) return null;
        return getMonthNotesList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
    }

    //Возвращает заметку для указаной даты, или null, если такой нет
    public NoteDTO getNote(int year, int month, int day) {
        List<NoteDTO> monthNotesList = getMonthNotesList(year, month);
        if(monthNotesList != null && !monthNotesList.isEmpty()) {
            for(NoteDTO note : monthNotesList) {
                if(note.getDay() == day) return note;
            }
        }
        return null;
    }

    //Возвращает заметку для указаной даты, или null, если такой нет
    public NoteDTO getNote(Calendar cal) {
        if(cal == null) return null;
        return getNote(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    //Сохранить заметку (boolean saveToDB: true - если надо сохранить в базу)
    public boolean saveNote(NoteDTO note, boolean saveToDB) {
        if(note == null) return false;
        //Получаем карту годовых заметок, если нет, то создаем новую карту
        Map<Integer, List<NoteDTO>> yearNotes = notesHolder.get(note.getYear());
        if(yearNotes == null) {
            notesHolder.put(note.getYear(), new HashMap<Integer, List<NoteDTO>>());
            yearNotes = notesHolder.get(note.getYear());
        }
        //Получаем список заметок на месяц, если нет, то создаем новый список
        List<NoteDTO> monthNotes = yearNotes.get(note.getMonth());
        if(monthNotes == null) {
            yearNotes.put(note.getMonth(), new ArrayList<NoteDTO>());
            monthNotes = yearNotes.get(note.getMonth());
        }
        //Ищем заметку с такой датой в списке, если есть, то заменяем
        for(int i = 0; i < monthNotes.size(); i++) {
            NoteDTO noteDTO = monthNotes.get(i);
            if(noteDTO.getDay() == note.getDay()) {
                monthNotes.set(i, note);
                if(saveToDB) {
                    SugarRecord.save(note);
                    Log.d(TAG, "SUGAR UPDATE NOTE id=" + note.getId());
                }
                return true;
            }
        }
        //Если заметки с такой датой нет, то добавляем новую
        if(saveToDB) {
            SugarRecord.save(note);
            Log.d(TAG, "SUGAR SAVE NEW NOTE id=" + note.getId());
        }
        return monthNotes.add(note);
    }

    //Возвращает карту, где ключ - номер недели месяца, значение - список отчетов для этой недели
    //Учитываются недели, которые начались в этом месяце
    //Если неделя заканчивается в следующем месяце, то берем оставшиеся заметки из него
    public Map<Integer, List<NoteDTO>> getMonthWeekNotesMap(Calendar cal) {
        Map<Integer, List<NoteDTO>> weekNotesMap = new HashMap<>();
        //Проверяем есть ли заметки на этот месяц, если есть, то получаем их список по номеру месяца
        List<NoteDTO> listCurMonth = getMonthNotesList(cal);
        //Проходимся по всем заметкам этого месяца
        if (listCurMonth != null) {
            for (NoteDTO noteDTO : listCurMonth) {
                Calendar calNote = new GregorianCalendar(noteDTO.getYear(), noteDTO.getMonth() - 1, noteDTO.getDay());
                //Если номер дня меньше текущей даты, то неделя началась в прошлом месяце, этот день не учитываем
                //TODO Если первый день не СУББОТА, тогда что?
                if (calNote.get(Calendar.DAY_OF_WEEK) > calNote.get(Calendar.DAY_OF_MONTH)) {
                    continue;
                    //Если номер дня больше текущей даты
                } else {
                    //Определяем номер полной недели месяца
                    int week = (((calNote.get(Calendar.DAY_OF_MONTH) - calNote.get(Calendar.DAY_OF_WEEK))) / 7) + 1;
                    //Проверяем есть ли в weekNotesMap ключ с номером недели, если нет, то создаем
                    if (weekNotesMap.get(week) == null) {
                        weekNotesMap.put(week, new ArrayList<NoteDTO>(7));
                    }
                    //Получаем список заметок для нужной недели по ее номеру и добавляем туда новую
                    weekNotesMap.get(week).add(noteDTO);
                }
            }
        }

        //Проверяем заметки из следующего месяца, если неделя не закончилась,
        //то ищем еще первые даты следующего месяца
        Calendar cal1 = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        int maxDate = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal1.set(Calendar.DAY_OF_MONTH, maxDate);
        int lastDayOfMonth = cal1.get(Calendar.DAY_OF_WEEK);
        int lastDateNextMonth = 7 - lastDayOfMonth;
        //(Теоретически) считаем максимальный номер недели, котрая началась в этом месяце
        int maxWeekNumber = ((maxDate + (7 - lastDayOfMonth)) / 7);
        //Если последний день месяца не конец недели и месяц не последний в году,
        //тогда получаем заметки из следующего месяца
        if (lastDayOfMonth != (cal.getFirstDayOfWeek() + 6)) {
            cal1.add(Calendar.MONTH, 1);
            List<NoteDTO> listNextMonth = getMonthNotesList(cal1);
            if (listNextMonth != null) {
                //Проходимся по всем заметкам след месяца
                for (NoteDTO noteDTO : listNextMonth) {
                    //Если дата заметки меньше или равна максимальной нужной дате следующего месяца,
                    //то добавляем её в список последней недели
                    if (noteDTO.getDay() <= lastDateNextMonth) {
                        if (weekNotesMap.get(maxWeekNumber) == null) {
                            weekNotesMap.put(maxWeekNumber, new ArrayList<NoteDTO>());
                        }
                        weekNotesMap.get(maxWeekNumber).add(noteDTO);
                    }
                }
            }
        }
        return weekNotesMap;
    }

    public Map<Integer, List<NoteDTO>> getMonthWeekNotesMap(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        return getMonthWeekNotesMap(calendar);
    }

    private  void getNotesFromBase(){
        try {
            List<NoteDTO> list = NoteDTO.listAll(NoteDTO.class);
            for (NoteDTO note: list) {
                Log.d(TAG, "Get note from DB. ID=" + note.getId());
                saveNote(note, false);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Current user notes: \n");
        int counter = 0;
        Set<Integer> years = notesHolder.keySet();
        for (Integer year : years) {
            Set<Integer> monthes = notesHolder.get(year).keySet();
            for (Integer month : monthes) {
                List<NoteDTO> list = getMonthNotesList(year, month);
                builder.append("\t" + year + "." + month + "\tdays: ");
                for (NoteDTO note : list) {
                    builder.append(note.getDay() + " ");
                    counter++;
                }
                builder.append("\t\t Total: " + list.size() + " notes\n");
            }
        }
        builder.append("All notes number: " + counter);
        return builder.toString();
    }


}
