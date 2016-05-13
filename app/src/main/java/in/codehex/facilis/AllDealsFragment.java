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
import in.codehex.facilis.model.OrderItem;


/**
 * A fragment that is used to display all the deals list in the {@link ViewOrdersFragment} class.
 */
public class AllDealsFragment extends Fragment {

    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<OrderItem> mOrderItemList;
    ViewOrdersAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mCount = 0;
    boolean isEnded = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int previousTotal = 0;
    boolean loading = true;
    int visibleThreshold = 5;

    public AllDealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_deals, container, false);

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.all_deal_list);

        mOrderItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViewOrdersAdapter(getContext(), mOrderItemList);
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
                resetData();
                mRefreshLayout.setRefreshing(true);
                processOrders();
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                resetData();
                mRefreshLayout.setRefreshing(true);
                processOrders();
            }
        });
    }

    /**
     * Reset the count for all the integers and values of booleans to default.
     */
    private void resetData() {
        mCount = 0;
        previousTotal = 0;
        loading = true;
        isEnded = false;
    }

    /**
     * Fetch all the deals list from the server.
     */
    private void processOrders() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_DEAL_TYPE, "all_deals");
            jsonObject.put(Config.KEY_API_START, mCount);
            jsonObject.put(Config.KEY_API_END, mCount + 10);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_HOME_PAGE_DEALS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mCount == 0) {
                    mOrderItemList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() < 10)
                        isEnded = true;

                    if (!isEnded) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt(Config.KEY_API_ID);
                            int orderId = object.getInt(Config.KEY_API_ORDER_ID);
                            JSONObject postedByObject = object
                                    .getJSONObject(Config.KEY_API_POSTED_BY);
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
                            mOrderItemList.add(mCount + i, new OrderItem(id, orderId,
                                    postedById, leastCost, average, counter,
                                    postedByFirstName, postedByLastName,
                                    postedDate, days, userImg));
                            mAdapter.notifyItemInserted(mCount + i);
                        }
                        mCount = mCount + 10;
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
                mRefreshLayout.setRefreshing(false);
                NetworkResponse response = error.networkResponse;
                try {
                    byte[] bytes = response.data;
                    String data = new String(bytes);
                    if (response.statusCode == 400) {
                        JSONObject errorObject = new JSONObject(data);
                        int mError = errorObject.getInt(Config.KEY_API_ERROR);
                        String mMessage = errorObject.getString(Config.KEY_API_MESSAGE);
                        if (mError == 400)
                            isEnded = true;
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

        AppController.getInstance().addToRequestQueue(stringRequest, "all_deals");
    }

    /**
     * View adapter for the recycler view of the view orders list item.
     */
    private class ViewOrdersAdapter
            extends RecyclerView.Adapter<ViewOrdersAdapter.ViewOrdersHolder> {

        Context context;
        List<OrderItem> mOrderItemList;

        public ViewOrdersAdapter(Context context, List<OrderItem> mOrderItemList) {
            this.context = context;
            this.mOrderItemList = mOrderItemList;
        }

        @Override
        public ViewOrdersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);
            return new ViewOrdersHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewOrdersHolder holder, int position) {
            final OrderItem orderItem = mOrderItemList.get(position);
            String name = orderItem.getPostedByFirstName() + " "
                    + orderItem.getPostedByLastName();
            String dp = orderItem.getUserImg();
            String duration = String.valueOf(orderItem.getDays()) + " left";
            String posted = String.valueOf(orderItem.getPostedDate());
            String item = String.valueOf(orderItem.getCounter()) + " items";
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
                    ((MainActivity) getActivity()).showOrderItems(Config.KEY_FRAGMENT_ALL_DEALS,
                            orderItem.getOrderId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mOrderItemList.size();
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
