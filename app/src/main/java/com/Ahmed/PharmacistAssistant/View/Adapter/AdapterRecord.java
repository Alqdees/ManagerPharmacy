package com.Ahmed.PharmacistAssistant.View.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.Ahmed.PharmacistAssistant.Controler.database.DB;
import com.Ahmed.PharmacistAssistant.View.Activity.AddActivity;
import com.Ahmed.PharmacistAssistant.Controler.database.DBSqlite;
import com.Ahmed.PharmacistAssistant.View.Activity.MainActivity;
import com.Ahmed.PharmacistAssistant.model.Model;
import com.Ahmed.PharmacistAssistant.R;
import com.Ahmed.PharmacistAssistant.View.Activity.RecordDetailActivity;
import java.util.ArrayList;

public class AdapterRecord extends RecyclerView.Adapter<AdapterRecord.HolderRecord> {
    private Context context;
    private ArrayList<Model> arrayList;
    private DBSqlite dbSqlite;
    @SuppressLint("NotifyDataSetChanged")
    public AdapterRecord(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbSqlite = new DBSqlite(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_record,parent,false);
        return new HolderRecord(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, @SuppressLint("RecyclerView") int position) {

        Model model = arrayList.get(position);
        String id = model.getId();
        String name = model.getName();
        String cost = model.getCost();
        String sell = model.getSell();
        String code = model.getCode();
        String date = model.getDate();
        holder.tv_name.setText(name);
        holder.tv_cost.setText(cost);
        holder.tv_sell.setText(sell);
        holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordDetailActivity.class);
                intent.putExtra("ID", id);
                context.startActivity(intent);
            }
        });
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMoreDialog(position,
                        "" + id,
                        "" + name,
                        "" + code,
                        "" + cost,
                        "" + sell,
                        ""+date);

            }
        });
//        holder.addBtn.setOnClickListener((View view) -> {
//            addData(name);
//        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showMoreDialog(int position, String id, String name, String code, String cost, String sell,String date) {
        String[] option = {"حذف","تعديل"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if (i == 0){
//                   حذف
                   dbSqlite = new DBSqlite(context);
                   dbSqlite.deletedItem(String.valueOf(id));
                   dbSqlite.close();
                   ((MainActivity)context).onStart();
               }else if (i == 1){
//                   تعديل
                   Intent intent =new Intent(context, AddActivity.class);
                   intent.putExtra("ID",id);
                   intent.putExtra("NAME",name);
                   intent.putExtra("CODE",code);
                   intent.putExtra("COST",cost);
                   intent.putExtra("SELL",sell);
                   intent.putExtra("DATE",date);
                   System.out.println(date);

                   intent.putExtra("isEditMode",true);
                   context.startActivity(intent);
               }
            }
        });
        builder.create().show();
    }
    @Override
    public int getItemCount() {
        return arrayList.isEmpty() ? 0 : arrayList.size();
    }

    class HolderRecord extends RecyclerView.ViewHolder{
        TextView tv_name,tv_cost,tv_sell;
        ImageButton moreBtn,addBtn;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_sell = itemView.findViewById(R.id.tv_sell);
            moreBtn = itemView.findViewById(R.id.moreBtn);
//            addBtn = itemView.findViewById(R.id.send);
        }

    }

    void addData( String name){
        DB db = new DB(context);
        if (db.addOrder(name)){
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

}
