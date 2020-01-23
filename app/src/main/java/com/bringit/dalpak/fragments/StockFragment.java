package com.bringit.dalpak.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bringit.dalpak.R;
import com.bringit.dalpak.adapters.StockRV;
import com.bringit.dalpak.models.StockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {

    RecyclerView stockRv;
    private View view;

    public StockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock, container, false);
        stockRv = view.findViewById(R.id.rvStock);
        if (getArguments().getSerializable("valuesArray") != null) {
            List<StockModel> stockModelList = ((ArrayList<StockModel>) getArguments().getSerializable("valuesArray"));
            initData(stockModelList);
        }
        return view;
    }

    private void initData(List<StockModel> stockModelList) {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        stockRv.setLayoutManager(mLayoutManager);
        StockRV stockRV = new StockRV(getActivity(), stockModelList);
        stockRv.setAdapter(stockRV);
    }

}
