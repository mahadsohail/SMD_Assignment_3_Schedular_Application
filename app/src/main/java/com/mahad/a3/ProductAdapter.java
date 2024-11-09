package com.mahad.a3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> products;
    private ProductDB productDB;
    private OnProductMoveListener moveListener;


    public interface OnProductMoveListener {
        void onProductMoved();
    }


    public ProductAdapter(Context context, ArrayList<Product> products, ProductDB productDB) {
        super(context, 0, products);
        this.products = products;
        this.productDB = productDB;
        this.moveListener = moveListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_product, parent, false);
        }

        Product product = products.get(position);

        TextView titleTextView = convertView.findViewById(R.id.productTitle);
        TextView dateTextView = convertView.findViewById(R.id.productDate);
        TextView priceTextView = convertView.findViewById(R.id.productPrice);
        ImageButton moveButton = convertView.findViewById(R.id.moveToScheduledButton);

        titleTextView.setText(product.getName());
        dateTextView.setText(product.getDate());
        priceTextView.setText(String.valueOf(product.getPrice()));


        moveButton.setOnClickListener(v -> {
            // Update product status in database
            productDB.updateProductStatus(product.getId(), "scheduled");
            products.remove(position);
            notifyDataSetChanged();
            Toast.makeText(getContext(), "Product moved to Scheduled", Toast.LENGTH_SHORT).show();


            if (moveListener != null) {
                moveListener.onProductMoved();
            }
        });

        return convertView;
    }
}
