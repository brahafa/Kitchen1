package com.dalpak.bringit.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.StockRV;
import com.dalpak.bringit.models.StockModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StockFragment extends Fragment {

    private RecyclerView stockRv;

    public StockFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        stockRv = view.findViewById(R.id.rvStock);
        if (getArguments().getSerializable("valuesArray") != null) {
            List<StockModel> stockModelList = ((ArrayList<StockModel>) getArguments().getSerializable("valuesArray"));
            initData(stockModelList);
        }
        return view;
    }

    private void initData(List<StockModel> stockModelList) {
        LayoutManager mLayoutManager;
        if (stockModelList != null && stockModelList.size() > 0 && stockModelList.get(0).getObject_type().equals("deal")) {
            mLayoutManager = new LinearLayoutManager(getActivity());
            stockRv.setBackgroundColor(Color.parseColor("#e6ebf4"));
        } else {
            mLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        }
        stockRv.setLayoutManager(mLayoutManager);
        StockRV stockRV = new StockRV(getActivity(), stockModelList);
        stockRv.setAdapter(stockRV);
    }

}
