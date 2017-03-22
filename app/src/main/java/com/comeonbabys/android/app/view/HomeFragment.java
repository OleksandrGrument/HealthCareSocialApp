package com.comeonbabys.android.app.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.db.dto.NoteDTO;
import com.comeonbabys.android.app.db.dto.NotesHolder;
import com.comeonbabys.android.app.requests.Constants;
import com.comeonbabys.android.app.view.customview.Day;
import com.comeonbabys.android.app.view.customview.ExtendedCalendarView;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends BaseContainerFragment implements ExtendedCalendarView.OnDayClickListener, ExtendedCalendarView.OnButtonClickListener {
	ExtendedCalendarView calendarView;

	private static Calendar calendar;
	private static String TAG = "HomeFragment";
	private boolean onDayClicked = false;

    BroadcastReceiver br;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUIObject();
        initBrodcastReceiver();
	}

    private void initBrodcastReceiver() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("!!!!!!!!", "RECEIVED BROADCAST");
                loadListNotesByDay();
            }
        };
        IntentFilter intFilt = new IntentFilter(Constants.GET_NOTES_SUCCESS_ACTION);
        getActivity().registerReceiver(br, intFilt);
    }

	private void initUIObject() {
		calendarView = (ExtendedCalendarView) getActivity().findViewById(R.id.calendar);
		calendarView.setOnDayClickListener(this);
		calendarView.setButtonClickListener(this);
		((TextViewCustom) getActivity().findViewById(R.id.textEdit)).setSelected(true);
		((TextViewCustom) getActivity().findViewById(R.id.textEdit1)).setSelected(true);
		((TextViewCustom) getActivity().findViewById(R.id.textEdit2)).setSelected(true);
		((TextViewCustom) getActivity().findViewById(R.id.textEdit3)).setSelected(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(onDayClicked) {
			onDayClicked = false;
		} else {
			calendar = Calendar.getInstance();
		}
		loadListNotesByDay();  //загрузка списка заметок
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
		try {
			getActivity().unregisterReceiver(br);
		} catch (IllegalArgumentException exc) {}
    }

    @Override
	public void onDayClicked(AdapterView<?> adapter, View view, int i, long id, Day day) {
		onDayClicked = true;
		launchActivity(day);
	}

	@Override
	public void onButtonClicked(Calendar cal) {
		calendar = cal;
		loadListNotesByDay();
	}

	private void launchActivity(Day day) {
		Intent intent = new Intent(getActivity(), DayHomeActivity.class);
		intent.putExtra("DAY", day);
		intent.putExtra("NOTE", day.getNote());
		startActivity(intent);
	}

	//Загрузить список заметок и присвоить их в calendarView
	private void loadListNotesByDay() {
		((MainActivity) getActivity()).showProgress();
		List<NoteDTO> listNotes = NotesHolder.getInstanse().getMonthNotesList(calendar);
		calendarView.addListNoteCalendar(listNotes);
		((MainActivity) getActivity()).hideProgress();
	}
}