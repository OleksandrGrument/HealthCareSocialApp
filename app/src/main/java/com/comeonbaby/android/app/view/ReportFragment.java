package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.adapter.ListReportAdapter;
import com.comeonbaby.android.app.common.Constants;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.db.dto.NotesHolder;
import com.comeonbaby.android.app.db.dto.ReportDTO;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends BaseContainerFragment implements OnClickListener, OnItemClickListener {

    ButtonCustom buttonMonth, buttonWeek;
    ListView listReport;
    final int LOAD_SUCCESS = 0;
    final int LOAD_NO_NETWORK = 1;
    final int TYPE_LOAD_MONTH = 1;
    final int TYPE_LOAD_WEEK = 2;
    PrefsHelper prefsHelper;
    static final String TAG = "REPORT_FRAGMENT";

    private Calendar cal;

    private TextView month;
    private RelativeLayout base;
    private RelativeLayout baseM;
    private ImageView next, prev;

    private NotesHolder notesHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefsHelper = PrefsHelper.getPrefsHelper();
        notesHolder = NotesHolder.getInstanse();
        initTitleDate();
        initUIObject();
    }

    //Отрисовка даты и кнопок внизу
    private void initTitleDate() {
        cal = Calendar.getInstance();
        base = (RelativeLayout) getActivity().findViewById(R.id.layoutDate);
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        base.setLayoutParams(param);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        TextView rancua = new TextView(getActivity());
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rancua.setLayoutParams(params);
        rancua.setBackgroundResource(R.drawable.bg_rancua_nguoc);
        base.addView(rancua);

        baseM = new RelativeLayout(getActivity());
        param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.BELOW, rancua.getId());
        param.addRule(RelativeLayout.CENTER_VERTICAL);
        baseM.setLayoutParams(param);
        baseM.setMinimumHeight(40);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 20;
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        prev = new ImageView(getActivity());
        prev.setId(R.id.image_prev_date_report);
        prev.setPadding((int) getResources().getDimension(
                R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
                R.dimen.padding_top_bottom_next_previous), (int) getResources().getDimension(
                R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
                R.dimen.padding_top_bottom_next_previous));
        prev.setLayoutParams(params);
        prev.setImageResource(R.drawable.navigation_previous_item);
        prev.setOnClickListener(this);
        baseM.addView(prev);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        month = new TextView(getActivity());
        month.setId(R.id.text_date_report);
        month.setLayoutParams(params);
        month.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
        month.setText(cal.get(Calendar.YEAR) + getString(R.string.text_year));

        baseM.addView(month);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.rightMargin = 20;
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        next = new ImageView(getActivity());
        next.setImageResource(R.drawable.navigation_next_item);
        next.setLayoutParams(params);
        next.setId(R.id.image_next_date_report);
        next.setPadding((int) getResources().getDimension(
                R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
                R.dimen.padding_top_bottom_next_previous), (int) getResources().getDimension(
                R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
                R.dimen.padding_top_bottom_next_previous));
        next.setOnClickListener(this);

        baseM.addView(next);
        base.addView(baseM);
    }

    //Установка слушателей кнопок-переключателей (месяц-неделя) и листа. Установка кнопки
    //которая была нажата в прошлый раз (сохраняется в Prefences - SHARED_PREF_TAB_REPORT: 1 или 2)
    //и соответственное перестроение текста даты внизу
    private void initUIObject() {
        ((TextViewCustom) getActivity().findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        buttonMonth = (ButtonCustom) getActivity().findViewById(R.id.buttonMonth);
        buttonWeek = (ButtonCustom) getActivity().findViewById(R.id.buttonWeek);
        listReport = (ListView) getActivity().findViewById(R.id.listviewReport);

        listReport.setOnItemClickListener(this);
        buttonMonth.setOnClickListener(this);
        buttonWeek.setOnClickListener(this);
        if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_TAB_REPORT, 1) == 1) {
            buttonMonth.setSelected(true);
            buttonWeek.setSelected(false);
            month.setText(cal.get(Calendar.YEAR) + getString(R.string.text_year));
        } else {
            buttonMonth.setSelected(false);
            buttonWeek.setSelected(true);
            month.setText(cal.get(Calendar.YEAR) + " " + getString(R.string.text_year) + " " + (cal.get(Calendar.MONTH) + 1) + " " + getString(R.string.text_month));
        }
    }

    //Загрузить нужный отчет при создании активити
    @Override
    public void onResume() {
        super.onResume();
        setNextDateBtnVisibility();
        if (buttonMonth.isSelected())
            showReportsForYear();
        else if (buttonWeek.isSelected())
            showReportsForMonth();

    }

    //Слушатель нажатий
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Кнопка предыдущей даты
            case R.id.image_prev_date_report:
                previousDate();
                break;
            //Кнопка следующей даты
            case R.id.image_next_date_report:
                nextDate();
                break;
            //Кнопка месячного отчета
            case R.id.buttonMonth:
                buttonMonth.setSelected(true);
                buttonMonth.setEnabled(false);
                buttonWeek.setSelected(false);
                month.setText(cal.get(Calendar.YEAR) + getString(R.string.text_year));
                showReportsForYear();
                prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_REPORT, 1);
                buttonWeek.setEnabled(true);
                break;
            //Кнопка недельного отчета
            case R.id.buttonWeek:
                buttonMonth.setSelected(false);
                buttonWeek.setSelected(true);
                buttonWeek.setEnabled(false);
                month.setText(cal.get(Calendar.YEAR) + " " + getString(R.string.text_year) + " " + (cal.get(Calendar.MONTH) + 1) + " " + getString(R.string.text_month));
                showReportsForMonth();
                prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_REPORT, 2);
                buttonMonth.setEnabled(true);
                break;
            default:
                break;
        }
    }

    //Определение предыдущей даты и перестройка списка
    private void previousDate() {
        if (buttonMonth.isSelected()) cal.add(Calendar.YEAR, -1);
        else cal.add(Calendar.MONTH, -1);
        setNextDateBtnVisibility();
        rebuildCalendar();
    }

    //Определение следующей даты и перестройка списка
    private void nextDate() {
        if (buttonMonth.isSelected()) cal.add(Calendar.YEAR, 1);
        else cal.add(Calendar.MONTH, 1);
        setNextDateBtnVisibility();
        rebuildCalendar();
    }

    //Set next date button disabled if it is current date
    private void setNextDateBtnVisibility() {
        Calendar currentDate = Calendar.getInstance();
        if(cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                (cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                (cal.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)))) {
            next.setVisibility(View.INVISIBLE);
        } else {
            next.setVisibility(View.VISIBLE);
        }
    }

    //Перерисовка текста даты внизу в зависимости от нажатой кнопки месяц-неделя
    //и отображение соответственных отчетов
    private void rebuildCalendar() {
        if (month != null) {
            if (buttonMonth.isSelected()) {
                month.setText(cal.get(Calendar.YEAR) + getString(R.string.text_year));
                showReportsForYear();          //месяц
            } else {
                month.setText(cal.get(Calendar.YEAR) + " " + getString(R.string.text_year) + " " + (cal.get(Calendar.MONTH) + 1) + " " + getString(R.string.text_month));
                showReportsForMonth();         //неделя
            }
        }
    }

    //Отобразить список отчетов для года
    private void showReportsForYear() {
        ((MainActivity) getActivity()).showProgress();
        resetListView();
        List<ReportDTO> monthReportsList = getReportsListForYear(cal.get(Calendar.YEAR));                          //Создаем список отчетов
        if(monthReportsList != null && !monthReportsList.isEmpty()) {
            final ListReportAdapter adapter = new ListReportAdapter(monthReportsList, getActivity(),
                    TYPE_LOAD_MONTH, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
            listReport.setAdapter(adapter);
        }
        ((MainActivity) getActivity()).hideProgress();
    }

    //Отобразить список отчетов для месяца
    private void showReportsForMonth() {
        ((MainActivity) getActivity()).showProgress();
        resetListView();
        List<ReportDTO> weekReportsList = getReportsListForMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1); //Создаем список отчетов
        if(weekReportsList != null && !weekReportsList.isEmpty()) {
            final ListReportAdapter adapter = new ListReportAdapter(weekReportsList, getActivity(),
                    TYPE_LOAD_WEEK, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
            listReport.setAdapter(adapter);
        }
        ((MainActivity) getActivity()).hideProgress();
    }

    //Очистка списка репортов перед началом процесса заполнения для следующей даты
    private void resetListView() {
        List<ReportDTO> emptyReportsList = new ArrayList<ReportDTO>();
        ListReportAdapter adapter = new ListReportAdapter(emptyReportsList, getActivity(),
                TYPE_LOAD_MONTH, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        listReport.setAdapter(adapter);
    }

    //Возвращает список месячных отчетов для текущего года
    private List<ReportDTO> getReportsListForYear(int year) {
        List<ReportDTO> listReport = new ArrayList<>();
        for (int month = 1; month < 13; month++) {
            List<NoteDTO> monthNotes = notesHolder.getMonthNotesList(year, month);
            //Получаем отчет для месяца, используя полученый список заметок. Добавляем его в список отчетов
            if (monthNotes != null && !monthNotes.isEmpty()) {
                listReport.add(ReportDTO.getReport(year, month, 0, monthNotes)); //week = 0
            }
        }
        return listReport;
    }

    //Возвращает список недельных отчетов для нужного месяца текущего года
    private List<ReportDTO> getReportsListForMonth(int year, int month) {
        List<ReportDTO> listReport = new ArrayList<>();
        //Получаем карту заметок месяца месяца, где ключ - номер недели
        Map<Integer, List<NoteDTO>> monthNotesMap = notesHolder.getMonthWeekNotesMap(year, month);
        if (monthNotesMap != null && !monthNotesMap.isEmpty()) {
            //Получаем список недельных заметок для месяца, используя полученую карту заметок.
            //Максимальное колличество недель - 5
            for (int week = 1; week <= 5; week++) {
                List<NoteDTO> weekNotes = monthNotesMap.get(week);
                //Получаем отчет для недели, используя список недельных заметок, и добавляем его в список отчетов
                if (weekNotes != null && !weekNotes.isEmpty()) {
                    listReport.add(ReportDTO.getReport(year, month, week, weekNotes));
                }
            }
        }
        return listReport;
    }

    //Устанавливаем вкладку месяц по умолчанию после уничтожения активити
    @Override
    public void onDetach() {
        prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_REPORT, 1);
        super.onDetach();
    }

    //Действие при нажатии на элемент списка
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListReportAdapter adapter = (ListReportAdapter) parent.getAdapter();
        ReportDTO item = (ReportDTO) adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ReportDetailsActivity.class);
        if (buttonMonth.isSelected()) {
            String title = item.getYear() + getString(R.string.text_year) + " " + item.getMonth() + getString(R.string.text_month);
            intent.putExtra(Constants.EX_IS_MONTH, true);
            intent.putExtra(Constants.INTENT_STRING_TITLE, title);
        }
        if (buttonWeek.isSelected()) {
            String title = item.getYear() + getString(R.string.text_year) + " " + item.getMonth() + getString(R.string.text_month) + " " + item.getWeek() + getString(R.string.text_week);
            intent.putExtra(Constants.EX_IS_MONTH, false);
            intent.putExtra(Constants.INTENT_STRING_TITLE, title);
            intent.putExtra("year", item.getYear());
            intent.putExtra("month", item.getMonth());
            intent.putExtra("week", item.getWeek());
        }
        intent.putExtra(Constants.INTENT_ITEM_REPORT, item);
        startActivity(intent);
    }
}