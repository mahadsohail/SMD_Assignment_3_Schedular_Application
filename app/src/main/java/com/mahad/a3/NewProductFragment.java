package com.mahad.a3;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NewProductFragment extends Fragment {

    private ListView listView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private ProductDB productDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);

        listView = view.findViewById(R.id.listView);
        productDB = new ProductDB(getContext());
        productDB.open();

        // Fetch only "new" products
        productList = productDB.fetchProductsByStatus("new");
        productAdapter = new ProductAdapter(getContext(), productList, productDB);
        listView.setAdapter(productAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> openAddProductDialog());

        return view;
    }

    private void openAddProductDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        EditText nameEditText = dialogView.findViewById(R.id.editTextName);
        EditText dateEditText = dialogView.findViewById(R.id.editTextDate);
        EditText priceEditText = dialogView.findViewById(R.id.editTextPrice);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Product")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameEditText.getText().toString();
                    String date = dateEditText.getText().toString();
                    String priceString = priceEditText.getText().toString();

                    if (!name.isEmpty() && !date.isEmpty() && !priceString.isEmpty()) {
                        try {
                            int price = Integer.parseInt(priceString);
                            long result = productDB.insert(name, date, price);

                            if (result != -1) {
                                // Refresh the ListView with only new products
                                productList.clear();
                                productList.addAll(productDB.fetchProductsByStatus("new"));
                                productAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Product Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid price", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productDB.close();
    }
}
