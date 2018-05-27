package com.example.jay.perdiem.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jay.perdiem.R;
import com.example.jay.perdiem.objects.Expense;

import java.util.ArrayList;

public class ExpenseAdaptor extends ArrayAdapter<Expense> {

    private final Context mContext;
    private ArrayList<Expense> expenseList;

    public ExpenseAdaptor( Context context, ArrayList<Expense> list) {
        super(context,0,list);
        mContext = context;
        if(expenseList != null){
            expenseList.clear();
        }
        expenseList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_listview,parent,false);

        Expense expense = expenseList.get(position);

        TextView reportName = listItem.findViewById(R.id.report_name);
        TextView reportDates = listItem.findViewById(R.id.report_dates);
        TextView reportTotal = listItem.findViewById(R.id.reports_totals);
        reportName.setText(expense.getName());
        reportDates.setText(expense.getID());

        if (expense.getTotal().matches("[0-9]+")){
            double tempDouble = 0;
            tempDouble += Double.parseDouble(expense.getTotal());
            reportTotal.setText("$"+ String.format("%.2f", tempDouble));
        }






        return listItem;
    }
}
