package com.dalpak.bringit.adapters;

import android.view.DragEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.OrderModel;

import java.util.List;

public class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private Listener listener;

    DragListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) event.getLocalState();
                int viewId = v.getId();
                //final int tvEmptyListTop = R.id.tvEmptyListTop;
               // final int tvEmptyListBottom = R.id.tvEmptyListBottom;
                final int rvPreparing = R.id.rvPreparing;
                final int rvPacking = R.id.rvPacking;
                final int rvCooking = R.id.rvCooking;
                final int rvReceived = R.id.rvReceived;
                final int rvSent = R.id.rvSent;
                String draggedToStr = "";

                switch (viewId) {
                  //  case tvEmptyListTop:
                   // case tvEmptyListBottom:
                    case rvPreparing:
                    case rvPacking:
                    case rvCooking:
                    case rvReceived:
                    case rvSent:

                        RecyclerView target;
                        switch (viewId) {
                          //  case tvEmptyListTop:
                            case rvPreparing:
                                target =  v.getRootView().findViewById(rvPreparing);
                                draggedToStr = "preparing";
                                break;
                          //  case tvEmptyListBottom:
                            case rvPacking:
                                target =  v.getRootView().findViewById(rvPacking);
                                draggedToStr = "packing";
                                break;
                            case rvCooking:
                                target =  v.getRootView().findViewById(rvCooking);
                                draggedToStr = "cooking";
                                break;
                            case rvReceived:
                                target =  v.getRootView().findViewById(rvReceived);
                                draggedToStr = "received";
                                break;
                            case rvSent:
                                target =  v.getRootView().findViewById(rvSent);
                                draggedToStr = "sent";
                                break;
                            default:
                                target = (RecyclerView) v.getParent();
                                positionTarget = (int) v.getTag();
                        }

                        if (viewSource != null) {
                            RecyclerView source = (RecyclerView) viewSource.getParent();

                            OrderRv adapterSource = (OrderRv) source.getAdapter();
                            int positionSource = (int) viewSource.getTag();
                            int sourceId = source.getId();

                            OrderModel list = adapterSource.getList().get(positionSource);
                            List<OrderModel> listSource = adapterSource.getList();

                            OrderRv adapterTarget = (OrderRv) target.getAdapter();
                            List<OrderModel> customListTarget = adapterTarget.getList();
                            if (positionTarget >= 0) {
                                customListTarget.add(positionTarget, list);
                            } else {
                                customListTarget.add(list);
                            }
                            adapterTarget.changeStatus(listSource.get(positionSource).getOrder_id(), positionSource, positionTarget +1, true, draggedToStr);

                            listSource.remove(positionSource);
                            adapterSource.updateList(listSource);
                            adapterSource.notifyDataSetChanged();

                            adapterTarget.updateList(customListTarget);
                            adapterTarget.notifyDataSetChanged();

                            if (sourceId == rvPacking && adapterSource.getItemCount() < 1) {
                                listener.setEmptyListBottom(true);
                            }
//                            if (viewId == tvEmptyListBottom) {
//                                listener.setEmptyListBottom(false);
//                            }
                            if (sourceId == rvPreparing && adapterSource.getItemCount() < 1) {
                                listener.setEmptyListTop(true);
                            }
//                            if (viewId == tvEmptyListTop) {
//                                listener.setEmptyListTop(false);
//                            }
                        }
                        break;
                }
                break;
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }
}