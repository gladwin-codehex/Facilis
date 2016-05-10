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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.codehex.facilis.app.AppController;
import in.codehex.facilis.app.Config;
import in.codehex.facilis.app.ItemClickListener;
import in.codehex.facilis.helper.CircleTransform;
import in.codehex.facilis.model.ViewOrderItem;


/**
 * A fragment that is used to display orders list in the {@link MainActivity} class.
 */
public class ViewOrdersFragment extends Fragment {

    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<ViewOrderItem> mViewOrderItemList;
    ViewOrdersAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mCount = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

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
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.view_order_list);

        mViewOrderItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViewOrdersAdapter(getContext(), mViewOrderItemList);
        userPreferences = getActivity().getSharedPreferences(Config.PREF_USER,
                Context.MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    processOrders();
                    loading = true;
                }
            }
        });

        mRefreshLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark, R.color.accent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetCount();
                mRefreshLayout.setRefreshing(true);
                processOrders();
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                resetCount();
                mRefreshLayout.setRefreshing(true);
                processOrders();
            }
        });
    }

    /**
     * Reset the data for all the integers.
     */
    private void resetCount() {
        mCount = 0;
        previousTotal = 0;
        loading = true;
    }

    /**
     * Fetch the orders list from the server.
     */
    private void processOrders() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_KEY, "view_orders");
            jsonObject.put(Config.KEY_API_START, mCount);
            jsonObject.put(Config.KEY_API_END, mCount + 10);
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
                if (mCount == 0) {
                    mViewOrderItemList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt(Config.KEY_API_ID);
                        int orderId = object.getInt(Config.KEY_API_ORDER_ID);
                        JSONObject postedByObject = object.getJSONObject(Config.KEY_API_POSTED_BY);
                        int postedById = postedByObject
                                .getInt(Config.KEY_API_POSTED_BY_ID);
                        String postedByFirstName = postedByObject
                                .getString(Config.KEY_API_POSTED_BY_FIRST_NAME);
                        String postedByLastName = postedByObject
                                .getString(Config.KEY_API_POSTED_BY_LAST_NAME);
                        String postedDate = object.getString(Config.KEY_API_POSTED_DATE);
                        String days = object.getString(Config.KEY_API_DAYS);
                        int leastCost = object.getInt(Config.KEY_API_LEAST_COST);
                        int average = object.getInt(Config.KEY_API_AVERAGE);
                        int counter = object.getInt(Config.KEY_API_COUNTER);
                        String userImg = object.getString(Config.KEY_API_USER_IMAGE);
                        mViewOrderItemList.add(mCount + i, new ViewOrderItem(id, orderId,
                                postedById, leastCost, average, counter,
                                postedByFirstName, postedByLastName,
                                postedDate, days, userImg));
                        mAdapter.notifyItemInserted(mCount + i);
                    }
                    mCount = mCount + 10;
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
                mRefreshLayout.setRefreshing(false);
                NetworkResponse response = error.networkResponse;
                try {
                    byte[] data = response.data;
                    String mError = new String(data);
                    JSONObject errorObject = new JSONObject(mError);
                    String errorData = errorObject.getString(Config.KEY_API_DETAIL);
                    Toast.makeText(getContext(), errorData, Toast.LENGTH_SHORT).show();
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

        AppController.getInstance().addToRequestQueue(stringRequest, "view_orders");
    }

    /**
     * View adapter for the recycler view of the view orders list item.
     */
    private class ViewOrdersAdapter
            extends RecyclerView.Adapter<ViewOrdersAdapter.ViewOrdersHolder> {

        Context context;
        List<ViewOrderItem> viewOrderItemList;

        public ViewOrdersAdapter(Context context, List<ViewOrderItem> viewOrderItemList) {
            this.context = context;
            this.viewOrderItemList = viewOrderItemList;
        }

        @Override
        public ViewOrdersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);
            return new ViewOrdersHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewOrdersHolder holder, int position) {
            ViewOrderItem viewOrderItem = viewOrderItemList.get(position);
            String name = viewOrderItem.getPostedByFirstName() + " "
                    + viewOrderItem.getPostedByLastName();
            String dp = viewOrderItem.getUserImg();
            String duration = String.valueOf(viewOrderItem.getDays()) + " left";
            String posted = String.valueOf(viewOrderItem.getPostedDate());
            String item = String.valueOf(viewOrderItem.getCounter()) + " items";
            holder.textName.setText(name);
            Picasso.with(context).load(dp)
                    .placeholder(R.drawable.ic_person)
                    .transform(new CircleTransform()).into(holder.imgDp);
            holder.textDuration.setText(duration);
            holder.textPosted.setText(posted);
            holder.textItem.setText(item);

            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    // TODO: go to place bid fragment
                }
            });
        }

        @Override
        public int getItemCount() {
            return viewOrderItemList.size();
        }

        protected class ViewOrdersHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

            private TextView textName, textDuration, textPosted, textItem;
            private ImageView imgDp;
            private ItemClickListener itemClickListener;

            public ViewOrdersHolder(View view) {
                super(view);
                textName = (TextView) view.findViewById(R.id.text_name);
                textDuration = (TextView) view.findViewById(R.id.text_duration);
                textPosted = (TextView) view.findViewById(R.id.text_posted);
                textItem = (TextView) view.findViewById(R.id.text_item);
                imgDp = (ImageView) view.findViewById(R.id.img_dp);
                view.setOnClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view, getAdapterPosition(), false);
            }
        }
    }
}
