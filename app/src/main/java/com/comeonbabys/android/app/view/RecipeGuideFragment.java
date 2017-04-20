package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.adapter.CustomListAdapter;
import com.comeonbabys.android.app.db.dto.Guide;
import com.comeonbabys.android.app.db.dto.Recipe;
import com.comeonbabys.android.app.requests.Constants;
import com.comeonbabys.android.app.requests.ExtraConstants;
import com.comeonbabys.android.app.requests.commands.Commands;

import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.view.customview.ButtonCustom;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.comeonbabys.android.app.db.dto.Guide.guide;
import static com.comeonbabys.android.app.db.dto.Recipe.recipes;

/**
 * Created by Home on 20.02.2017.
 */

public class RecipeGuideFragment extends BaseContainerFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    Handler handler;
    ButtonCustom buttonRecipe, buttonGuide;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_guide, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIObject();
        initHandler();
        activity.showProgress();
        Commands.getRecipeOperation(handler, AppSession.getSession().getSystemUser());
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                JSONArray guides = null;
                if (data.containsKey(ExtraConstants.MESSAGE))
                    message = data.getString(ExtraConstants.MESSAGE);
                //if (msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                ((MainActivity) getActivity()).hideProgress();
                switch (msg.what) {
                    case Constants.MSG_GET_GUIDE_SUCCESS: {
                        try {

                            guides = new JSONArray(data.getString(ExtraConstants.DATA));
                            guide = new ArrayList<>();
                            for (int i = 0; i < guides.length(); i++) {
                                guide.add(new Guide(guides.getJSONObject(i).getString("title"), guides.getJSONObject(i).getString("image"),guides.getJSONObject(i).getString("date"),guides.getJSONObject(i).getString("image"),guides.getJSONObject(i).getString("url")+""));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadList();
                        break;
                    }
                    case Constants.MSG_GET_RECIPE_SUCCESS: {
                        try {
                            guides = new JSONArray(data.getString(ExtraConstants.DATA));
                            recipes = new ArrayList<>();
                            for (int i = 0; i < guides.length(); i++) {
                                recipes.add(new Recipe(guides.getJSONObject(i).getString("title"), guides.getJSONObject(i).getString("image_thumbnail"), guides.getJSONObject(i).getString("url_naver"), guides.getJSONObject(i).getString("date")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadList();
                        break;
                    }

                    case Constants.MSG_ERROR: {
                        showSnackMessage(Constants.ERROR_MESSAGE_UNKNOWN);
                        break;
                    }
                }
            }
        };
    }

    private void showSnackMessage(String msg) {
        Snackbar.make(getActivity().getCurrentFocus(), msg, Snackbar.LENGTH_LONG).show();
    }

    private void initUIObject() {
        buttonRecipe = (ButtonCustom) getActivity().findViewById(R.id.buttonRecipe);
        buttonGuide = (ButtonCustom) getActivity().findViewById(R.id.buttonGuide);

        buttonRecipe.setOnClickListener(this);
        buttonGuide.setOnClickListener(this);

        buttonRecipe.setSelected(true);
        listView = (ListView) getActivity().findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRecipe:
                buttonRecipe.setSelected(true);
                buttonGuide.setSelected(false);
                activity.showProgress();
                Commands.getRecipeOperation(handler, AppSession.getSession().getSystemUser());
                loadList();
                break;
            case R.id.buttonGuide:
                buttonRecipe.setSelected(false);
                buttonGuide.setSelected(true);
                activity.showProgress();
                Commands.getGuideOperation(handler, AppSession.getSession().getSystemUser());
                loadList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void loadList() {
        if (buttonRecipe.isSelected()) {
            CustomListAdapter adapter = new CustomListAdapter(getActivity(), getListRecipeTitle(), getListRecipeImages(), getListRecipeContent(), getListDate());
            listView.setAdapter(adapter);
        } else if (buttonGuide.isSelected()) {
            ListAdapter myListAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1, getListForAdapter()
            );
            listView.setAdapter(myListAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (buttonRecipe.isSelected()) {
//                    CustomListAdapter adapter = (CustomListAdapter) parent.getAdapter();
//                    ComeOnGuideDTO item = new ComeOnGuideDTO();
//                    item.setTitle(recipes.get(position).getName());
//                    item.setContent(recipes.get(position).getContent());
//                    HtmlContentFragment fragment = new HtmlContentFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(ServiceConsts.EXTRA_LIST_NANIN, item);
//                    fragment.setArguments(bundle);
//                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipes.get(position).getContent()));
                    startActivityForResult(intent, 1);

                } else if (buttonGuide.isSelected()) {
                    Intent intent = new Intent(view.getContext(), ImageGuideActivity.class);
                    intent.putExtra(ImageGuideActivity.EXTRA_GUIDE_NUM, (int) id);
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<String> getListForAdapter() {
        ArrayList<String> list = new ArrayList<>();
        for (Guide guid : guide) {
            list.add(guid.getName() + "\n" + guid.getDate());
        }
        return list;
    }

    private ArrayList<String> getListRecipeTitle() {
        ArrayList<String> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            list.add(recipe.getName());
        }
        return list;
    }

    private ArrayList<String> getListRecipeImages() {
        ArrayList<String> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            list.add(recipe.getUrl_icon());
        }
        return list;
    }

    private ArrayList<String> getListRecipeContent() {
        ArrayList<String> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            list.add(recipe.getContent());
        }
        return list;
    }

    private ArrayList<String> getListDate() {
        ArrayList<String> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            list.add(recipe.getDate());
        }
        return list;
    }
}
