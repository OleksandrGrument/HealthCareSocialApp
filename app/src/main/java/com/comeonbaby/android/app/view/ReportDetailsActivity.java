package com.comeonbaby.android.app.view;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Constants;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.ReportDTO;
import com.comeonbaby.android.app.view.customview.TextViewCustom;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.view.LineChartView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;

public class ReportDetailsActivity extends BaseActivity implements OnClickListener {

    //private String TAG = "ReportDetailsActivity";
    ReportDTO report;
    private String[] mMonth = new String[]{};
    String title;
    boolean is_month = false;

    //* First chart
    private LineChartView mChart;
    private String[] chartLabels;
    private float[] chartValues;
    int minY = 35, maxY = 38;

    //Статусы отчета
    private static String REPORT_EXELENT;
    private static String REPORT_GOOD;
    private static String REPORT_BAD;

    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_report_details);

        REPORT_EXELENT = getResources().getString(R.string.report_exelent);
        REPORT_GOOD = getResources().getString(R.string.report_good);
        REPORT_BAD = getResources().getString(R.string.report_bad);

        initObjectUI();
    }

    private void initObjectUI() {
        Intent intent = getIntent();
        title = intent.getStringExtra(Constants.INTENT_STRING_TITLE);
        report = (ReportDTO) intent.getSerializableExtra(Constants.INTENT_ITEM_REPORT);
        is_month = intent.getBooleanExtra(Constants.EX_IS_MONTH, false);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(title);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        mChart = (LineChartView) findViewById(R.id.linechart1);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        //Отобразить результаты отчета
        showProgress();
        try {
            loadDataReport(report);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        hideProgress();
    }

    //Вернуть текстовое представление статуса отчета
    private String getStatusText(int status) {
        String str;
        switch (status) {
            case 1:
                str = REPORT_EXELENT;
                break;
            case 2:
                str = REPORT_GOOD;
                break;
            case 3:
                str = REPORT_BAD;
                break;
            default:
                str = "";
        }
        return str;
    }

    //Считывает данные из отчета и отображает их
    private void loadDataReport(ReportDTO report) throws Exception {
        Calendar cal = new GregorianCalendar(report.getYear(), report.getMonth() - 1, 1);
        calcDate(cal);
        // calc bbts
        calcBBT(report);
        // fill data
        if (report != null) {
            if (report.getStateFood() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepFood)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote1)).setText(String.format(is_month ? getString(R.string.text_report_1_1)
                        : getString(R.string.text_report_1_2), "" + report.getCountFood()));
                ((TextViewCustom) findViewById(R.id.textRating1)).setText(getStatusText(report.getStateFood()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating1)), report.getStateFood());
            }
            if (report.getStateNuts() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepNuts)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote2)).setText(String.format(is_month ? getString(R.string.text_report_2_1)
                        : getString(R.string.text_report_2_2), "" + report.getCountNuts()));
                ((TextViewCustom) findViewById(R.id.textRating2)).setText(getStatusText(report.getStateNuts()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating2)), report.getStateNuts());
            }
            if (report.getStateTea() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepTea)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote3)).setText(String.format(is_month ? getString(R.string.text_report_3_1)
                        : getString(R.string.text_report_3_2), "" + report.getCountTea()));
                ((TextViewCustom) findViewById(R.id.textRating3)).setText(getStatusText(report.getStateTea()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating3)), report.getStateTea());
            }
            if (report.getStateExercise() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepExercises)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote4)).setText(String.format(is_month ? getString(R.string.text_report_4_1)
                        : getString(R.string.text_report_4_2), "" + report.getCountExercise()));
                ((TextViewCustom) findViewById(R.id.textRating4)).setText(getStatusText(report.getStateExercise()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating4)), report.getStateExercise());
            }
            if (report.getStateSleep() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepSleep)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote5)).setText(String.format(is_month ? getString(R.string.text_report_5_1)
                        : getString(R.string.text_report_5_2), "" + report.getCountSleepBefore()));
                ((TextViewCustom) findViewById(R.id.textRating5)).setText(getStatusText(report.getStateSleep()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating5)), report.getStateSleep());
            }
            if (report.getAverageSleep() > 0) {
                ((LinearLayout) findViewById(R.id.layoutRepSleepAver)).setVisibility(View.VISIBLE);
                int hours, minutes;
                int averageSleepMinutes = report.getAverageSleep();
                hours = (int) averageSleepMinutes / 60;
                minutes = averageSleepMinutes - (hours * 60);
                ((TextViewCustom) findViewById(R.id.textNote6)).setText(String.format(is_month ? getString(R.string.text_report_6_1)
                        : getString(R.string.text_report_6_2), "" + hours, "" + minutes));
                //setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating6)), report.getStateSleep());
            }
            if (report.getStateWater() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepWater)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote7)).setText(String.format(is_month ? getString(R.string.text_report_7_1)
                        : getString(R.string.text_report_7_2), "" + report.getAverageWater()));
                ((TextViewCustom) findViewById(R.id.textRating7)).setText(getStatusText(report.getStateWater()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating7)), report.getStateWater());
            }
            if (report.getStateBath() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepHeating)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote8)).setText(String.format(is_month ? getString(R.string.text_report_8_1)
                        : getString(R.string.text_report_8_2), "" + report.getCountBath()));
                ((TextViewCustom) findViewById(R.id.textRating8)).setText(getStatusText(report.getStateBath()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating8)), report.getStateBath());
            }
            if (report.getStateVitamin() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepVitamin)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote9)).setText(String.format(is_month ? getString(R.string.text_report_9_1)
                        : getString(R.string.text_report_9_2), "" + report.getCountVitamin()));
                ((TextViewCustom) findViewById(R.id.textRating9)).setText(getStatusText(report.getStateVitamin()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating9)), report.getStateVitamin());
            }
            if (report.getStateFolic() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepFolic)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote10)).setText(String.format(is_month ? getString(R.string.text_report_10_1)
                        : getString(R.string.text_report_10_2), "" + report.getCountFolic()));
                ((TextViewCustom) findViewById(R.id.textRating10)).setText(getStatusText(report.getStateFolic()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating10)), report.getStateFolic());
            }
            if (report.getStateCoffee() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepCoffee)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote11)).setText(String.format(
                        is_month ? getString(R.string.text_report_11_1) : getString(R.string.text_report_11_2),
                        is_month ? "" + (cal.getActualMaximum(Calendar.DAY_OF_MONTH) - report.getCountCoffee()) :
                        "" + (7 - report.getCountCoffee())));
                ((TextViewCustom) findViewById(R.id.textRating11)).setText(getStatusText(report.getStateCoffee()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating11)), report.getStateCoffee());
            }
            if (report.getStateAlcohol() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepAlcohol)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote12)).setText(
                        String.format(is_month ? getString(R.string.text_report_12_1_1) : getString(R.string.text_report_12_2_2),
                                "" + report.getCountAlcohol(), "" + report.getAvergeAlcohol()));
                ((TextViewCustom) findViewById(R.id.textRating12)).setText(getStatusText(report.getStateAlcohol()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating12)), report.getStateAlcohol());
            }
            if (report.getStateSmoke() != 0) {
                ((LinearLayout) findViewById(R.id.layoutRepSmoking)).setVisibility(View.VISIBLE);
                ((TextViewCustom) findViewById(R.id.textNote13)).setText(String.format(
                        is_month ? getString(R.string.text_report_13_1) : getString(R.string.text_report_13_2),
                        is_month ? "" + (cal.getActualMaximum(Calendar.DAY_OF_MONTH) - report.getCountSmoke()) :
                        "" + (7 - report.getCountSmoke())));
                ((TextViewCustom) findViewById(R.id.textRating13)).setText(getStatusText(report.getStateSmoke()));
                setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating13)), report.getStateSmoke());
            }
            if (report.getEmotions() != null) {
                ((LinearLayout) findViewById(R.id.layoutRepEmotion)).setVisibility(View.VISIBLE);
                int[] em = report.getEmotions();
                ((TextViewCustom) findViewById(R.id.textNote14)).setText(String.format(is_month ? getString(R.string.text_report_14_1_1)
                                : getString(R.string.text_report_14_2_2),
                        "" + em[0], "" + em[1], "" + em[2], "" + em[3], "" + em[4]));
                //((TextViewCustom) findViewById(R.id.textRating14)).setText("TODO EMOTION AND BACKGROUND");
                //setBackgroundStatus(((TextViewCustom) findViewById(R.id.textRating14)), 1);
            }
            ((LinearLayout) findViewById(R.id.layoutNote)).setVisibility(View.VISIBLE);
        }
    }

    //Устанавливает цвет бекграунда для статусов отчета
    private void setBackgroundStatus(View view, int status) {
        if (status == ReportDTO.EXELENT)
            view.setBackgroundResource(R.drawable.bg_rating_report_excellent);
        else if (status == ReportDTO.GOOD)
            view.setBackgroundResource(R.drawable.bg_rating_report_good);
        else if (status == ReportDTO.BAD)
            view.setBackgroundResource(R.drawable.bg_rating_report_bad);
    }

    private void calcDate(Calendar cal) {
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar curCal = Calendar.getInstance();
        if(cal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == curCal.get(Calendar.MONTH)) {
            days = curCal.get(Calendar.DAY_OF_MONTH);
        }
        mMonth = new String[days];
        int j = 0;
        for (int i = 1; i <= days; i++) {
            mMonth[j] = i + "";
            j++;
        }
    }

    //Обработка данных для графика температур
    private void calcBBT(ReportDTO dto) throws Exception {
        String separator = getResources().getString(R.string.report_labels_separator);
        float defTempValue = 36.5F;
        boolean needChart = false;
        int length = is_month ? mMonth.length : 7;
        //Записываем в массив значения температуры по умолчанию
        chartValues = new float[length];
        for (int i = 0; i < length; i++) {
            chartValues[i] = 36.5F;
        }
        //Создаем массив с названиями меток внизу графика
        if(is_month) {
            chartLabels = new String[length];
            for (int i = 0; i < length; i++) {
                chartLabels[i] = i%2 != 0 ? separator : "" + (i + 1);
            }
        } else {
            Calendar cal = Calendar.getInstance();
            //chartLabels = new String[] {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
            chartLabels = getResources().getStringArray(R.array.days_of_week);
        }
        //Если есть записи тепературы, тогда строим график
        if (dto != null && dto.getBbts() != null && dto.getBbts().size() > 0) {
            Map<Integer, Float> bbts = dto.getBbts();
            needChart = true;
            if (is_month) {
                for (int day = 0; day < chartLabels.length; day++) {
                    if (bbts.get(day + 1) != null)
                        chartValues[day] = bbts.get(day + 1);
                    if (minY > chartValues[day] && chartValues[day] != 0)
                        minY = Math.round(chartValues[day] - 1);
                    if (maxY < chartValues[day])
                        maxY = Math.round(chartValues[day]) + 1;
                }
            } else {
                int year = getIntent().getIntExtra("year", 0);
				int month = getIntent().getIntExtra("month", 0);
				int week = getIntent().getIntExtra("week", 0);
                Calendar cal;
                Set<Integer> dates = bbts.keySet();
                for (int i = 0; i < 7; i++) {
                    for (Integer date : dates) {
                        cal = new GregorianCalendar(year, month-1, 1);
                        if(week > 1 && date < 7 ) {
                            cal.add(Calendar.MONTH, 1);
                            cal.set(Calendar.DAY_OF_MONTH, date);
                        } else {
                            cal.set(Calendar.DAY_OF_MONTH, date);
                        }
                        int day = cal.get(Calendar.DAY_OF_WEEK);
                        if(i + 1 == day) chartValues[i] = bbts.get(date);
                    }
                    if (minY > chartValues[i] && chartValues[i] != 0)
                        minY = Math.round(chartValues[i] - 1);
                    if (maxY < chartValues[i])
                        maxY = Math.round(chartValues[i]) + 1;
                }
            }
        }
        // draw chart
        if (needChart) {
            ((LinearLayout) findViewById(R.id.layoutChart)).setVisibility(View.VISIBLE);
            openChart(mChart);
        } else {
            ((LinearLayout) findViewById(R.id.layoutChart)).setVisibility(View.GONE);
        }
    }

    //Отобразить график температур
    public void openChart(LineChartView chart) throws Exception {
        LineSet dataset = new LineSet(chartLabels, chartValues);

        // Tooltip
        Context context = ((LineChartView) findViewById(R.id.linechart1)).getContext();
        Tooltip mTip = new Tooltip(context, R.layout.linechart_three_tooltip, R.id.value);
        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(58), (int) Tools.fromDpToPx(25));
        mTip.setValueFormat(new DecimalFormat("#.#°"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);
            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);
            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }
        mChart.setTooltips(mTip);

        //Data
        dataset.setColor(getResources().getColor(R.color.color_excellent));
        dataset.setFill(Color.TRANSPARENT);
        dataset.setDotsRadius(Tools.fromDpToPx(4.0F));
        dataset.setDotsColor(getResources().getColor(R.color.color_excellent));
        dataset.setThickness(Tools.fromDpToPx(2.0F));
        dataset.setSmooth(false);
        chart.addData(dataset);

        //Grid
        Paint gridPaint = new Paint();
        gridPaint.setColor(getResources().getColor(R.color.color_white));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));

        chart.setTopSpacing(Tools.fromDpToPx(1));
        chart.setBorderSpacing(Tools.fromDpToPx(6));
        chart.setAxisBorderValues(minY, maxY, 1);
        chart.setYLabels(AxisRenderer.LabelPosition.OUTSIDE);
        chart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE);
        chart.setLabelsFormat(new DecimalFormat("# °C"));
        chart.setLabelsColor(getResources().getColor(R.color.color_black));
        chart.setXAxis(false).setYAxis(false);
        chart.setGrid((maxY-minY)*2, 0, gridPaint);

        chart.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }
}