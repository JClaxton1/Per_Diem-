package com.example.jay.perdiem.adaptors;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jay.perdiem.R;
import com.example.jay.perdiem.objects.Expense;
import com.example.jay.perdiem.objects.Reports;

import java.util.ArrayList;

public class ReportAdaptor extends ArrayAdapter<Reports> {

    private final Context mContext;
    private ArrayList<Reports> reportList;

    public ReportAdaptor(@NonNull Context context, @LayoutRes ArrayList<Reports> list) {
        super(context, 0 , list);
        mContext = context;
        if(reportList != null){
            reportList.clear();
        }
        reportList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_listview,parent,false);

        Reports report = reportList.get(position);

        TextView reportName = listItem.findViewById(R.id.report_name);
        TextView reportDates = listItem.findViewById(R.id.report_dates);
        TextView reportTotal = listItem.findViewById(R.id.reports_totals);
        reportName.setText(report.getName());
        reportDates.setText(report.getDate()+"-"+report.getEndDate());

        if(report.getExpenses() == null){
            reportTotal.setText("$0.00");
        }else{
            double tempDouble = 0;
            ArrayList<Expense> temp = report.getExpenses();
            for(Expense i : temp){
                    String total = i.getTotal();
                    if(!total.equals("null")){

                        if (Double.parseDouble(i.getTotal()) > 0){
                            tempDouble += Double.parseDouble(i.getTotal());
                        }

                    }


            }


            reportTotal.setText("$"+ String.format("%.2f", tempDouble));

            }






        return listItem;
    }
}
