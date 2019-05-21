package com.example.androidnc.cuoikyandroid;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.ViewHolder> {
    List<PhoneModel> models;
    Context mContext;
    private String TAG = "FireBase-Log";

    public MyViewAdapter(Context context, List<PhoneModel> models) {
        this.mContext = context;
        this.models = models;
    }

    public Object getItem(int position) {
        return models.get(position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PhoneModel md = models.get(position);
        final int po = models.get(position).getId();
        holder.edtid.setText(String.valueOf(md.getId()));
        holder.edtprice.setText(String.valueOf(md.getPrice()));
        holder.edtproducer.setText(md.getProducer());
        holder.edtdescription.setText(md.getDescription());
        holder.edtname.setText(md.getName());
        holder.buttondelete.setOnClickListener(new View.OnClickListener() {
            String PhoneName;
            @Override
            public void onClick(View v) {
                PhoneName = holder.edtname.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AdvancedAndroidFinalTest");
                Query applesQuery = ref.orderByChild("product_name").equalTo(PhoneName);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        MainActivity.Phonelist.remove(position);
                        MainActivity.adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
        holder.buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogThem();
            }
        });
        holder.buttonedit.setOnClickListener(new View.OnClickListener() {
            int PhoneID;
            long addprice;
            String addname,addproducer,adddescreption;
            @Override
            public void onClick(View v) {
                PhoneID = Integer.parseInt(holder.edtid.getText().toString());
                addprice = Long.parseLong(holder.edtprice.getText().toString());
                addname =holder.edtname.getText().toString();
                addproducer =holder.edtproducer.getText().toString();
                adddescreption =holder.edtdescription.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AdvancedAndroidFinalTest");
                Query applesQuery = ref.orderByChild("id").equalTo(PhoneID);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("product_name", addname);
                            map.put("price", addprice);
                            map.put("producer", addproducer);
                            map.put("description", adddescreption);
                            appleSnapshot.getRef().updateChildren(map);
                        }
                       onstart();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

    }
//    public void deleteItem(int position){
//        String key = keys.get(position);
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AdvancedAndroidFinalTest");
//        ref.child(key).removeValue();
//    }
    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText edtid;
        private EditText edtprice;
        private EditText edtname;
        private EditText edtproducer;
        private EditText edtdescription;
        Button buttondelete;
        Button buttonedit;
        Button buttonadd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.buttondelete = (Button)itemView.findViewById(R.id.remove_btn);
            this.buttonedit = (Button)itemView.findViewById(R.id.edit_btn);
            this.buttonadd = (Button)itemView.findViewById(R.id.add_btn);
            this.edtid = (EditText) itemView.findViewById(R.id.Phone_edt_id);
            this.edtprice = (EditText)itemView.findViewById(R.id.Phone_edt_price);
            this.edtname = (EditText)itemView.findViewById(R.id.Phone_edt_name);
            this.edtproducer = (EditText)itemView.findViewById(R.id.Phone_edt_producer);
            this.edtdescription = (EditText)itemView.findViewById(R.id.Phone_edt_description);
        }

    }

    public void DialogThem(){
        final Dialog dialog = new Dialog(mContext);
        //bo o title trong dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        final EditText name = (EditText) dialog.findViewById(R.id.edt_name);
        final EditText price = (EditText) dialog.findViewById(R.id.edt_price);
        final EditText producer = (EditText) dialog.findViewById(R.id.edt_producer);
        final EditText descreption = (EditText) dialog.findViewById(R.id.edt_descreption);
        final EditText id = (EditText) dialog.findViewById(R.id.edt_id);
        Button btnadd = (Button) dialog.findViewById(R.id.btn_add);
        Button btnno = (Button) dialog.findViewById(R.id.btn_no);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int addid;
                final long addprice;
                final String addname,addproducer,adddescreption;
                addid = Integer.parseInt(id.getText().toString());
                addprice = Long.parseLong(price.getText().toString());
                addname =name.getText().toString();
                addproducer =producer.getText().toString();
                adddescreption =descreption.getText().toString();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AdvancedAndroidFinalTest").push();
//        DatabaseReference namesRef = rootRef.child("names");
                Map<String, Object> map = new HashMap<>();
                map.put("id", addid);
                map.put("product_name", addname);
                map.put("price", addprice);
                map.put("producer", addproducer);
                map.put("description", adddescreption);
                rootRef.updateChildren(map);
                dialog.dismiss();
                onstart();
            }
        });

        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onstart(){
        MainActivity.Phonelist = new ArrayList<>();
        MainActivity.reference = FirebaseDatabase.getInstance().getReference("AdvancedAndroidFinalTest");
        MainActivity.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        PhoneModel p = dataSnapshot1.getValue(PhoneModel.class);
                        MainActivity.Phonelist.add(p);
                    }
                    MainActivity.adapter = new MyViewAdapter(mContext, MainActivity.Phonelist);
                    MainActivity.recyclerView.setAdapter(MainActivity.adapter);
                    MainActivity.adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
