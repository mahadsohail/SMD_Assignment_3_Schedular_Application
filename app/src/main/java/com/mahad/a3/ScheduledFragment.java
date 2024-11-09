package com.mahad.a3;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ScheduledFragment extends Fragment {

    private ListView scheduledListView;
    private ArrayList<Product> scheduledProducts;
    private ProductDB productDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduled, container, false);
        scheduledListView = view.findViewById(R.id.scheduledListView);

        productDB = new ProductDB(getContext());
        productDB.open();

        // Fetch only "scheduled" products
        scheduledProducts = productDB.fetchProductsByStatus("scheduled");
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, scheduledProducts);
        scheduledListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productDB.close();
    }
}
