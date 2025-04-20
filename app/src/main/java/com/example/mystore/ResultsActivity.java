package com.example.mystore;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    ListView resultsListView;
    ArrayList<ClothingItem> clothingItems;
    ClothingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        String category = getIntent().getStringExtra("category");
        Log.d("CATEGORY_RECEIVED", "الفئة المستلمة: " + category);
        clothingItems = getSampleData(category);
        resultsListView = findViewById(R.id.resultsListView);
        clothingItems = getSampleData(category);
        adapter = new ClothingAdapter();
        resultsListView.setAdapter(adapter);
        Button viewCartBtn = findViewById(R.id.viewCartBtn);
        EditText searchInput = findViewById(R.id.searchInput);




        viewCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


    }

    private ArrayList<ClothingItem> getSampleData(String category) {
        ArrayList<ClothingItem> list = new ArrayList<>();
        if (category.equals("for men")) {
        list.add(new ClothingItem("Men's formal suit", "200 nis/daily", R.drawable.img2,50));
        list.add(new ClothingItem("leather jacket", "100 nis/weekly", R.drawable.img3,8));
        list.add(new ClothingItem("T-shirt", "100 nis/weekly", R.drawable.img7,10));
            list.add(new ClothingItem("formal shirt", "70 nis/daily", R.drawable.img8,44));}

        else if (category.equals("for women")) {
        list.add(new ClothingItem("Wome'n dress", "200 nis/weekly", R.drawable.img,7));
        list.add(new ClothingItem("Women's abaya", "100 nis/weekly", R.drawable.img4,44));
        list.add(new ClothingItem("Women's skirt", "80 nis/daily", R.drawable.img9,23));
        list.add(new ClothingItem(" Hoodie", "90 nis/weekly", R.drawable.img6,23));
        list.add(new ClothingItem("Women's jeans", "80 nis/weekly", R.drawable.img10,12));}


        else if (category.equals("for kids")) {

        list.add(new ClothingItem("kid's dress", "60 nis/daily", R.drawable.img5,43));
        }
        return list;
    }


    class ClothingAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return clothingItems.size();
        }

        @Override
        public Object getItem(int i) {
            return clothingItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_clothing, null);

            Spinner sizeSpinner = view.findViewById(R.id.sizeSpinner);
            RadioGroup rentDurationGroup = view.findViewById(R.id.rentDurationGroup);
            ImageView img = view.findViewById(R.id.itemImage);
            TextView name = view.findViewById(R.id.itemName);
            TextView price = view.findViewById(R.id.itemPrice);
            Button addBtn = view.findViewById(R.id.addToCartBtn);
            TextView stockText = new TextView(ResultsActivity.this);

            ClothingItem item = clothingItems.get(i);

            img.setImageResource(item.imageResId);
            name.setText(item.name);
            price.setText(item.price);
            stockText.setText("الكمية المتبقية: " + item.quantity);
            stockText.setPadding(0, 8, 0, 0);
            ((ViewGroup) view).addView(stockText);

            // إذا القطعة خلصت
            if (item.quantity == 0) {
                addBtn.setText("SOLD OUT");
                addBtn.setEnabled(false);
            }

            addBtn.setOnClickListener(v -> {
                if (item.quantity > 0) {
                    String selectedSize = sizeSpinner.getSelectedItem().toString();

                    int selectedDurationId = rentDurationGroup.getCheckedRadioButtonId();
                    String duration = "غير محدد";
                    if (selectedDurationId != -1) {
                        RadioButton selected = view.findViewById(selectedDurationId);
                        duration = selected.getText().toString();
                    }

                    // أضف للسلّة
                    CartItem cartItem = new CartItem(item.name, selectedSize, duration, item.imageResId);
                    SharedPreferences prefs = getSharedPreferences("RentFitData", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = prefs.getString("cart", "[]");
                    Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
                    ArrayList<CartItem> currentCart = gson.fromJson(json, type);
                    currentCart.add(cartItem);

                    prefs.edit().putString("cart", gson.toJson(currentCart)).apply();

                    Toast.makeText(ResultsActivity.this, item.name + " تمت إضافته للسلة ✅", Toast.LENGTH_SHORT).show();

                    // تقليل الكمية وتحديث العرض
                    item.quantity--;
                    stockText.setText("الكمية المتبقية: " + item.quantity);

                    if (item.quantity == 0) {
                        addBtn.setText("SOLD OUT");
                        addBtn.setEnabled(false);
                    }
                }
            });

            return view;
        }
    }




    class ClothingItem {
        String name, price, category;
        int imageResId;
        int quantity;


        ClothingItem(String name, String price, int imageResId,int quantity) {
            this.name = name;
            this.price = price;
            this.imageResId = imageResId;
            this.quantity = quantity;
        }
    }

        }