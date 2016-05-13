/*
 * Copyright 2016 Bobby
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.codehex.facilis;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.codehex.facilis.app.AppController;
import in.codehex.facilis.app.Config;
import in.codehex.facilis.model.ViewOrderItem;


/**
 * A fragment that is used to place bid for the {@link ViewOrdersFragment} class.
 */
public class PlaceBidFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<ViewOrderItem> mViewOrderItemList;
    ViewOrderItemsAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mOrderId, mBidId;

    public PlaceBidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_bid, container, false);

        initObjects(view);
        prepareObjects();

        return view;
    }

    /**
     * Initialize the objects.
     *
     * @param view the root view of the layout.
     */
    private void initObjects(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.place_bid_list);

        mViewOrderItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViewOrderItemsAdapter(getContext(), mViewOrderItemList);
        userPreferences = getActivity().getSharedPreferences(Config.PREF_USER,
                Context.MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        Bundle bundle = getArguments();
        // TODO: handle null value of arguments
        if (bundle != null) {
            mOrderId = bundle.getInt(Config.KEY_BUNDLE_ORDER_ID);
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        processOrderItems();
    }

    /**
     * Fetch the bid item list from the server.
     */
    private void processOrderItems() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_KEY, "view_ordered_items");
            jsonObject.put(Config.KEY_API_ORDER, mOrderId);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_VIEW_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mViewOrderItemList.clear();
                mAdapter.notifyDataSetChanged();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt(Config.KEY_API_ID);
                        String name = object.getString(Config.KEY_API_NAME);
                        int order = object.getInt(Config.KEY_API_ORDER);
                        String quantity = object.getString(Config.KEY_API_QUANTITY);
                        String brand = object.getString(Config.KEY_API_BRAND);
                        String description = object.getString(Config.KEY_API_DESCRIPTION);
                        if (TextUtils.isEmpty(description))
                            description = "-";
                        int delCharge = object.getInt(Config.KEY_API_DEL_CHARGE);
                        double percentage = object.getDouble(Config.KEY_API_PERCENTAGE);
                        mViewOrderItemList.add(new ViewOrderItem(id, order,
                                delCharge, name, quantity, brand, description, percentage));
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(),
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                try {
                    byte[] bytes = response.data;
                    String data = new String(bytes);
                    if (response.statusCode == 400) {
                        JSONObject errorObject = new JSONObject(data);
                        // TODO: login again in case of invalid user
                        int mError = errorObject.getInt(Config.KEY_API_ERROR);
                        String mMessage = errorObject.getString(Config.KEY_API_MESSAGE);
                        // TODO: remove toast
                        Toast.makeText(getContext(), mMessage, Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 401) {
                        JSONObject errorObject = new JSONObject(data);
                        String errorData = errorObject.getString(Config.KEY_API_DETAIL);
                        // TODO: remove toast
                        Toast.makeText(getContext(), errorData, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(),
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(), "Network error - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " +
                        userPreferences.getString(Config.KEY_PREF_TOKEN, null));
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "view_ordered_items");
    }

    /**
     * View adapter for the recycler view of the order list item.
     */
    private class ViewOrderItemsAdapter
            extends RecyclerView.Adapter<ViewOrderItemsAdapter.ViewOrderItemsHolder> {

        Context context;
        List<ViewOrderItem> mViewOrderItemList;

        public ViewOrderItemsAdapter(Context context, List<ViewOrderItem> mViewOrderItemList) {
            this.context = context;
            this.mViewOrderItemList = mViewOrderItemList;
        }

        @Override
        public ViewOrderItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_order, parent, false);
            return new ViewOrderItemsHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewOrderItemsHolder holder, int position) {
            ViewOrderItem viewOrderItem = mViewOrderItemList.get(position);
            String name = viewOrderItem.getName() + " - " + viewOrderItem.getBrand();
            String quantity = "Quantity: " + viewOrderItem.getQuantity();
            String description = "Description: " + viewOrderItem.getDescription();

            holder.textName.setText(name);
            holder.textDescription.setText(description);
            holder.textQuantity.setText(quantity);
        }

        @Override
        public int getItemCount() {
            return mViewOrderItemList.size();
        }

        protected class ViewOrderItemsHolder extends RecyclerView.ViewHolder {

            private TextView textName, textDescription, textQuantity;

            public ViewOrderItemsHolder(View view) {
                super(view);
                textName = (TextView) view.findViewById(R.id.text_name);
                textDescription = (TextView) view.findViewById(R.id.text_description);
                textQuantity = (TextView) view.findViewById(R.id.text_quantity);
            }
        }
    }
}
