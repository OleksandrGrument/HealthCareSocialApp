package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.adapter.ListCommunityAdapter;
import com.comeonbaby.android.app.adapter.ListEventAdapter;
import com.comeonbaby.android.app.adapter.ListQAAdapter;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.common.IntentConstants;
import com.comeonbaby.android.app.common.ServiceConsts;
import com.comeonbaby.android.app.db.dto.CommunityDTO;
import com.comeonbaby.android.app.db.dto.CommunityQADTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.server.ServerEmulator;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.ConstsCore;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommunityFragment extends BaseContainerFragment implements OnItemClickListener, OnClickListener {
    private static final String TAG = "CommunityFragment";
	ButtonCustom buttonEvent, buttonMySuccess, buttonHusband, buttonMyRecipe, buttonQA;
	ListView listCommunity;
	ImageView imageNew;
	final int LOAD_SUCCESS = 0;
	final int LOAD_NO_NETWORK = 1;
	final int TYPE_LOAD_EVENT = 1;
	final int TYPE_LOAD_MYSUCCESS = 2;
	final int TYPE_LOAD_MYRECIPE = 3;
	final int TYPE_LOAD_HUSBAND = 4;
	final int TYPE_LOAD_QA = 5;
	PrefsHelper prefsHelper;
	private Handler handler;
	List<CommunityQADTO> comListQA = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_community, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prefsHelper = PrefsHelper.getPrefsHelper();
		initUIObject();
        initHandler();
	}

	private void initUIObject() {
		((TextViewCustom) getActivity().findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		buttonEvent = (ButtonCustom) getActivity().findViewById(R.id.buttonEvent);
		buttonMySuccess = (ButtonCustom) getActivity().findViewById(R.id.buttonMySuccess);
		buttonMyRecipe = (ButtonCustom) getActivity().findViewById(R.id.buttonMyRecipe);
		buttonHusband = (ButtonCustom) getActivity().findViewById(R.id.buttonMyHusband);
		buttonQA = (ButtonCustom) getActivity().findViewById(R.id.buttonQA);
		listCommunity = (ListView) getActivity().findViewById(R.id.listviewCommunity);
		imageNew = (ImageView) getActivity().findViewById(R.id.imgNew);

		listCommunity.setOnItemClickListener(this);
		buttonEvent.setOnClickListener(this);
		buttonMySuccess.setOnClickListener(this);
		buttonMyRecipe.setOnClickListener(this);
		buttonHusband.setOnClickListener(this);
		buttonQA.setOnClickListener(this);
		imageNew.setOnClickListener(this);

		//Установка выбраной ранее категории и видимости кнопки добавления нового контента
		if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1) == 1) {
			buttonEvent.setSelected(true);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.GONE);
		} else if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1) == 2) {
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(true);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.VISIBLE);
		} else if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1) == 3) {
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(true);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.VISIBLE);
		} else if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1) == 4) {
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(true);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.VISIBLE);
		} else if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1) == 5) {
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(true);
			imageNew.setVisibility(View.VISIBLE);
		}
	}

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                if (data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                if (msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                ((MainActivity) getActivity()).hideProgress();
                switch (msg.what) {
                    case Constants.MSG_GET_COMUNITY_SUCCESS: {
                        Log.d(TAG, "GET COMMUNITY SUCCESS!!!!");
                        List<CommunityDTO> commList = null;
                        if (data.containsKey(ExtraConstants.DATA)) {
                            try {
                                JSONArray jsarr = new JSONArray(data.getString(ExtraConstants.DATA));
                                commList = CommunityDTO.parseListCommunity(jsarr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(commList==null) {
							try {
								throw new Exception("!!!");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else{
							Collections.reverse(commList);
						}
                        final ListCommunityAdapter adapter = new ListCommunityAdapter(commList, getActivity());
                        listCommunity.setAdapter(adapter);
                        break;
                    }
                    case Constants.MSG_GET_COMUNITY_FAIL: {
                        Log.d(TAG, "GET COMMUNITY FAIL!!!!");
                        break;
                    }
					case Constants.MSG_GET_Q_A_SUCCESS: {
						Log.d(TAG, "GET COMMUNITY Q_A SUCCESS!!!!");
						comListQA = null;
						if (data.containsKey(ExtraConstants.DATA)) {
							try {
								JSONArray jsarr = new JSONArray(data.getString(ExtraConstants.DATA));
								comListQA = new CommunityQADTO().parseListCommunity(jsarr);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if(comListQA==null) {
							try {
								throw new Exception("!!!");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else{
							Collections.reverse(comListQA);
						}
						final ListQAAdapter adapter = new ListQAAdapter(comListQA, getActivity());
						listCommunity.setAdapter(adapter);
						break;

					}

					case Constants.MSG_GET_Q_A_FAIL: {
						Log.d(TAG, "GET COMMUNITY Q_A FAIL!!!!");
						break;
					}


					case Constants.MSG_GET_NOTICES_SUCCESS: {
						Log.d(TAG, "GET NOTICES SUCCESS!!!!");
						List<CommunityDTO> commList = null;
						if (data.containsKey(ExtraConstants.DATA)) {
							try {
								JSONArray jsarr = new JSONArray(data.getString(ExtraConstants.DATA));
								Log.v("CommunityFragment!!: ",jsarr.toString());
								commList = CommunityDTO.parseListCommunity(jsarr);
                                final ListEventAdapter adapter = new ListEventAdapter(commList, getActivity());
                                listCommunity.setAdapter(adapter);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						break;
					}
					case Constants.MSG_GET_NOTICES_FAIL: {
						Log.d(TAG, "GET NOTICES FAIL!!!!");
						break;
					}
                }
            }
        };
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(getActivity().findViewById(R.id.layoutRootCommunityFragment), msg, Snackbar.LENGTH_LONG).show();
    }

    //Обработчик нажатий на кнопки
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        //Event
		case R.id.buttonEvent:
			stateAllButtons(false);
			resetListView();
			buttonEvent.setSelected(true);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.GONE);
			loadListCommunity();
			prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1);
            stateAllButtons(true);
			break;
        //My story
		case R.id.buttonMySuccess:
			stateAllButtons(false);
			resetListView();
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(true);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.VISIBLE);
			loadListCommunity();
			prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 2);
            stateAllButtons(true);
			break;
        //My recype
		case R.id.buttonMyRecipe:
			stateAllButtons(false);
			resetListView();
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(true);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.VISIBLE);
			loadListCommunity();
			prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 3);
            stateAllButtons(true);
			break;
        //Husband story
		case R.id.buttonMyHusband:
			stateAllButtons(false);
			resetListView();
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(true);
			buttonQA.setSelected(false);
			imageNew.setVisibility(View.VISIBLE);
			loadListCommunity();
			prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 4);
            stateAllButtons(true);
			break;
        //Questions and Answers
		case R.id.buttonQA:
			stateAllButtons(false);
			resetListView();
			buttonEvent.setSelected(false);
			buttonMySuccess.setSelected(false);
			buttonMyRecipe.setSelected(false);
			buttonHusband.setSelected(false);
			buttonQA.setSelected(true);
			imageNew.setVisibility(View.VISIBLE);
//			loadListQA();
			loadListCommunity();
			prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 5);
            stateAllButtons(true);
			break;
        //Кнопка добавления собственного контента в зависимости от выбраной вкладки
		case R.id.imgNew:
            //Добавление своей истории
			if (buttonMySuccess.isSelected()) {
				Intent intent = new Intent(getActivity(), CommunityDetailsNewActivity.class);
				intent.putExtra(IntentConstants.INTENT_content_type, ConstsCore.SUCCESS_TYPE);
				startActivity(intent);
            //Добавление своего рецепта
			} else if (buttonMyRecipe.isSelected()) {
				Intent intent = new Intent(getActivity(), CommunityDetailsNewActivity.class);
				intent.putExtra(IntentConstants.INTENT_content_type, ConstsCore.RECIPE_TYPE);
				startActivity(intent);
            //Добавление истории мужа
			} else if (buttonHusband.isSelected()) {
				Intent intent = new Intent(getActivity(), CommunityDetailsNewActivity.class);
				intent.putExtra(IntentConstants.INTENT_content_type, ConstsCore.HUSBAND_TYPE);
				startActivity(intent);
            //Добавление нового вопроса
			} else if (buttonQA.isSelected()) {
				Intent intent = new Intent(getActivity(), QADetailsNewActivity.class);
				startActivity(intent);
			}
		default:
			break;
		}
	}

    //Обработка нажатий на элементы списка
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Нажатие на элемент списка ивентов
		if (buttonEvent.isSelected()) {
			ListEventAdapter adapter = (ListEventAdapter) parent.getAdapter();
			CommunityDTO item = (CommunityDTO) adapter.getItem(position);
			HtmlContentFragment fragment = new HtmlContentFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(ServiceConsts.EXTRA_COMMUNITY, item);
			fragment.setArguments(bundle);
			((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
        //Нажатие на элемент списка моих историй
		} else if (buttonMySuccess.isSelected()) {
			ListCommunityAdapter adapter = (ListCommunityAdapter) parent.getAdapter();
			CommunityDTO item = (CommunityDTO) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), CommunityDetailsActivity.class);
			intent.putExtra(ServiceConsts.EXTRA_COMMUNITY, item);
			startActivity(intent);
        //Нажатие на элемент списка рецептов
		} else if (buttonMyRecipe.isSelected()) {
			ListCommunityAdapter adapter = (ListCommunityAdapter) parent.getAdapter();
			CommunityDTO item = (CommunityDTO) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), CommunityDetailsActivity.class);
			intent.putExtra(ServiceConsts.EXTRA_COMMUNITY, item);
			startActivity(intent);
        //Нажатие на элемент списка историй мужа
		} else if (buttonHusband.isSelected()) {
			ListCommunityAdapter adapter = (ListCommunityAdapter) parent.getAdapter();
			CommunityDTO item = (CommunityDTO) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), CommunityDetailsActivity.class);
			intent.putExtra(ServiceConsts.EXTRA_COMMUNITY, item);
			startActivity(intent);
        //Нажатие на элемент списка вопросов
		} else if (buttonQA.isSelected()) {
			ListQAAdapter adapter = (ListQAAdapter) parent.getAdapter();
			CommunityQADTO item = (CommunityQADTO) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), QADetailsActivity.class);
			intent.putExtra(ServiceConsts.EXTRA_COMMUNITY_QA, item);
			startActivity(intent);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
        //Сохраняем вкладку по умолчанию после уничтожения активити
		prefsHelper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1);
	}

	/**
	 * @author PvTai This method used for set enable/disable all button
	 */
	private void stateAllButtons(boolean value) {
		buttonEvent.setEnabled(value);
		buttonEvent.setEnabled(value);
		buttonMySuccess.setEnabled(value);
		buttonHusband.setEnabled(value);
		buttonMyRecipe.setEnabled(value);
	}

	private void resetListView() {
		List<CommunityDTO> dataSource = new ArrayList<>();
		ListEventAdapter adapter = new ListEventAdapter(dataSource, getActivity());
		listCommunity.setAdapter(adapter);
	}

	private void loadListQA() {
        try {
            stateAllButtons(true);
//            List<CommunityQADTO> questions = ServerEmulator.getListQA();

            if (comListQA != null) {
                List<CommunityQADTO> listResult = new ArrayList<>();
                for (CommunityQADTO iterable_element : comListQA) {
                    if (!(iterable_element.isIs_private() && iterable_element.getUser().getSystemID() != AppSession.getSession().getSystemUser().getSystemID())) {
                        listResult.add(iterable_element);
                    }
                }
                if (buttonQA.isSelected()) {
                    final ListQAAdapter adapter = new ListQAAdapter(listResult, getActivity());
                    listCommunity.setAdapter(adapter);
                }
            }
            ((MainActivity) getActivity()).hideProgress();
        } catch (Exception e) {
            ((MainActivity) getActivity()).hideProgress();
            resetListView();
            e.printStackTrace();
        }
//		((LoginActivity) getActivity()).showProgress();
//		GetCommunityQACommand.start(getActivity(), 0, 0);
    }

//	private void loadListCommunity() {
//        try {
//            List<CommunityDTO> commList = ServerEmulator.getListCommunity();
//            Log.d("COMMUNITY", "List size = " + commList.size());
//            if (commList != null) {
//                if (buttonEvent.isSelected()) {
//                    final ListEventAdapter adapter = new ListEventAdapter(commList, getActivity());
//                    adapter.getFilter().filter(ConstsCore.EVENT_TYPE + "", new Filter.FilterListener() {
//                        @Override
//                        public void onFilterComplete(int count) {
//                            listCommunity.setAdapter(adapter);
//                            ((MainActivity) getActivity()).hideProgress();
//                        }
//                    });
//                } else if (buttonMySuccess.isSelected()) {
//                    final ListCommunityAdapter adapter = new ListCommunityAdapter(commList, getActivity());
//                    adapter.getFilter().filter(ConstsCore.SUCCESS_TYPE + "", new Filter.FilterListener() {
//                        @Override
//                        public void onFilterComplete(int count) {
//                            listCommunity.setAdapter(adapter);
//                            ((MainActivity) getActivity()).hideProgress();
//                        }
//                    });
//                } else if (buttonMyRecipe.isSelected()) {
//                    final ListCommunityAdapter adapter = new ListCommunityAdapter(commList, getActivity());
//                    adapter.getFilter().filter(ConstsCore.RECIPE_TYPE + "", new Filter.FilterListener() {
//                        @Override
//                        public void onFilterComplete(int count) {
//                            listCommunity.setAdapter(adapter);
//                            ((MainActivity) getActivity()).hideProgress();
//                        }
//                    });
//                } else if (buttonHusband.isSelected()) {
//                    final ListCommunityAdapter adapter = new ListCommunityAdapter(commList, getActivity());
//                    adapter.getFilter().filter(ConstsCore.HUSBAND_TYPE + "", new Filter.FilterListener() {
//                        @Override
//                        public void onFilterComplete(int count) {
//                            listCommunity.setAdapter(adapter);
//                            ((MainActivity) getActivity()).hideProgress();
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            ((MainActivity) getActivity()).hideProgress();
//            resetListView();
//            e.printStackTrace();
//        }
//    }
//		((LoginActivity) getActivity()).showProgress();
//		GetCommunityCommand.start(getActivity(), 0, 0);


	private void loadListCommunity() {
		try {
            ((MainActivity) getActivity()).showProgress();
			if (buttonEvent.isSelected()) {
//                List<CommunityDTO> commList = ServerEmulator.getListCommunity();
//                final ListEventAdapter adapter = new ListEventAdapter(commList, getActivity());
//                adapter.getFilter().filter(ConstsCore.EVENT_TYPE + "", new Filter.FilterListener() {
//                    @Override
//                    public void onFilterComplete(int count) {
//                        listCommunity.setAdapter(adapter);
//                        ((MainActivity) getActivity()).hideProgress();
//                    }
//                });
				Commands.getNotices(handler);
            } else if (buttonMySuccess.isSelected()) {
				Commands.getComunityRecords(handler, ConstsCore.SUCCESS_TYPE);
			} else if (buttonMyRecipe.isSelected()) {
//				((MainActivity) getActivity()).showProgress();
//				List<CommunityDTO> commList = new ArrayList<>();
                Commands.getComunityRecords(handler, ConstsCore.RECIPE_TYPE);
			} else if (buttonHusband.isSelected()) {
//				((MainActivity) getActivity()).showProgress();
//				List<CommunityDTO> commList = new ArrayList<>();
//				final ListCommunityAdapter adapter = new ListCommunityAdapter(commList, getActivity());
                Commands.getComunityRecords(handler, ConstsCore.HUSBAND_TYPE);
			} else if (buttonQA.isSelected()) {
				Commands.getComunityQA(handler, ConstsCore.QA_TYPE);
			}
		} catch (Exception e) {
			((MainActivity) getActivity()).hideProgress();
			resetListView();
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		addActions();
		if (buttonEvent.isSelected() || buttonMySuccess.isSelected() || buttonMyRecipe.isSelected() || buttonHusband.isSelected())
			loadListCommunity();
		else if (buttonQA.isSelected())
			loadListCommunity();
	}

	@Override
	public void onPause() {
//		removeActions();
		super.onPause();
	}

//	private void addActions() {
//		((LoginActivity) getActivity()).addAction(
//				ServiceConsts.GET_COMMUNITY_SUCCESS_ACTION,
//				new ListCommunitySuccessAction());
//		((LoginActivity) getActivity()).addAction(
//				ServiceConsts.GET_COMMUNITY_FAIL_ACTION,
//				new ListCommunityFailAction());
//		((LoginActivity) getActivity()).addAction(
//				ServiceConsts.GET_COMMUNITY_QA_SUCCESS_ACTION,
//				new ListCommunityQASuccessAction());
//		((LoginActivity) getActivity()).addAction(
//				ServiceConsts.GET_COMMUNITY_QA_FAIL_ACTION,
//				new ListCommunityQAFailAction());
//		((LoginActivity) getActivity()).updateBroadcastActionList();
//	}

//	private void removeActions() {
//		((LoginActivity) getActivity())
//				.removeAction(ServiceConsts.GET_COMMUNITY_SUCCESS_ACTION);
//		((LoginActivity) getActivity())
//				.removeAction(ServiceConsts.GET_COMMUNITY_FAIL_ACTION);
//		((LoginActivity) getActivity())
//				.removeAction(ServiceConsts.GET_COMMUNITY_QA_SUCCESS_ACTION);
//		((LoginActivity) getActivity())
//				.removeAction(ServiceConsts.GET_COMMUNITY_QA_FAIL_ACTION);
//		((LoginActivity) getActivity()).updateBroadcastActionList();
//	}
//
//	private class ListCommunitySuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			try {
//				stateAllButtons(true);
//				ListCommunitysDTO notes = (ListCommunitysDTO) bundle.getSerializable(ServiceConsts.EXTRA_LIST_COMMUNITY);
//				if (notes != null && notes.getListCommunity() != null) {
//					if (buttonEvent.isSelected()) {
//						final ListEventAdapter adapter = new ListEventAdapter(notes.getListCommunity(), getActivity());
//						adapter.getFilter().filter(ConstsCore.EVENT_TYPE + "",
//								new FilterListener() {
//									@Override
//									public void onFilterComplete(int count) {
//										listCommunity.setAdapter(adapter);
//										((LoginActivity) getActivity()).hideProgress();
//									}
//								});
//					} else if (buttonMySuccess.isSelected()) {
//						final ListCommunityAdapter adapter = new ListCommunityAdapter(notes.getListCommunity(), getActivity());
//						adapter.getFilter().filter(ConstsCore.SUCCESS_TYPE + "",
//								new FilterListener() {
//									@Override
//									public void onFilterComplete(int count) {
//										listCommunity.setAdapter(adapter);
//										((LoginActivity) getActivity()).hideProgress();
//									}
//								});
//					} else if (buttonMyRecipe.isSelected()) {
//						final ListCommunityAdapter adapter = new ListCommunityAdapter(notes.getListCommunity(), getActivity());
//						adapter.getFilter().filter(ConstsCore.RECIPE_TYPE + "",
//								new FilterListener() {
//									@Override
//									public void onFilterComplete(int count) {
//										listCommunity.setAdapter(adapter);
//										((LoginActivity) getActivity()).hideProgress();
//									}
//								});
//					} else if (buttonHusband.isSelected()) {
//						final ListCommunityAdapter adapter = new ListCommunityAdapter(notes.getListCommunity(), getActivity());
//						adapter.getFilter().filter(ConstsCore.HUSBAND_TYPE + "",
//								new FilterListener() {
//									@Override
//									public void onFilterComplete(int count) {
//										listCommunity.setAdapter(adapter);
//										((LoginActivity) getActivity()).hideProgress();
//									}
//								});
//					}
//				}
//			} catch (Exception e) {
//				((LoginActivity) getActivity()).hideProgress();
//				resetListView();
//				e.printStackTrace();
//			}
//		}
//	}

//	private class ListCommunityFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			stateAllButtons(true);
//			resetListView();
//			((LoginActivity) getActivity()).hideProgress();
//		}
//	}
//
//	private class ListCommunityQASuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			try {
//				stateAllButtons(true);
//				ListCommunityQADTO notes = (ListCommunityQADTO) bundle
//						.getSerializable(ServiceConsts.EXTRA_LIST_COMMUNITY_QA);
//				if (notes != null && notes.getListCommunityQA() != null) {
//					List<CommunityQADTO> list = notes.getListCommunityQA();
//					List<CommunityQADTO> listResult = new ArrayList<CommunityQADTO>();
//					for (CommunityQADTO iterable_element : list) {
//						if (!(iterable_element.isIs_private() && iterable_element
//								.getModel().getUserIdSystem() != AppSession
//								.getSession().getSystemUser().getUserIdSystem())) {
//							listResult.add(iterable_element);
//						}
//					}
//					if (buttonQA.isSelected()) {
//						final ListQAAdapter adapter = new ListQAAdapter(listResult,
//								getActivity());
//						listCommunity.setAdapter(adapter);
//					}
//				}
//				((LoginActivity) getActivity()).hideProgress();
//			} catch (Exception e) {
//				((LoginActivity) getActivity()).hideProgress();
//				resetListView();
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private class ListCommunityQAFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			stateAllButtons(true);
//			resetListView();
//			((LoginActivity) getActivity()).hideProgress();
//		}
//	}
}