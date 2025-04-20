package com.example.mystore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView cartListView;
    Button checkoutButton;

    ArrayList<CartItem> cartItems;
    SharedPreferences prefs;
    final String CART_KEY = "cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        checkoutButton = findViewById(R.id.checkoutButton);

        prefs = getSharedPreferences("RentFitData", MODE_PRIVATE);
        loadCart();

        CartAdapter adapter = new CartAdapter();
        cartListView.setAdapter(adapter);

        checkoutButton.setOnClickListener(v -> {
            cartItems.clear();
            adapter.notifyDataSetChanged();
            saveCart();
            Toast.makeText(this, "تم إتمام الطلب بنجاح!", Toast.LENGTH_SHORT).show();
        });
    }

    void loadCart() {
        Gson gson = new Gson();
        String json = prefs.getString(CART_KEY, "[]");
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        cartItems = gson.fromJson(json, type);
    }

    void saveCart() {
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(cartItems);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    class CartAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cartItems.size();
        }

        @Override
        public Object getItem(int position) {
            return cartItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_cart, null);

            ImageView img = view.findViewById(R.id.cartItemImage);
            TextView name = view.findViewById(R.id.cartItemName);
            TextView size = view.findViewById(R.id.cartItemSize);
            TextView duration = view.findViewById(R.id.cartItemDuration);

            CartItem item = cartItems.get(i);

            img.setImageResource(item.imageResId);
            name.setText(item.name);
            size.setText("المقاس: " + item.size);
            duration.setText("المدة: " + item.duration);

            return view;
        }
    }
}
