package com.example.jay.perdiem.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Reports implements Serializable {

    private String name;
    private String startDate;
    private String endDate;
    private String reportID;
    private ArrayList<Expense> Expenses;




    public Reports(){

    }

    @SuppressWarnings("unused")
    public Reports(String userID, String Name, String StartDate, String EndDate){
        this.name = Name;
        this.startDate = StartDate;
        this.endDate = EndDate;
        //noinspection unused
        String status = "Not Approved";
        this.Expenses = new ArrayList<>();

        Expense e = new Expense("null","null","null");
        Expenses.add(e);


    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getId() {
//        return id;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setId(String id) {
//        this.id = id;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public String getName() {
        return name;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setName(String name) {
//        this.name = name;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public String getDate() {
        return startDate;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setDate(String date) {
//        this.startDate = date;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public ArrayList<Expense> getExpenses() {
        return Expenses;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setExpenses(ArrayList<Expense> expenses) {
//        Expenses = expenses;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public void addExpense(Expense expense){
       if(Expenses != null){
           Expenses.add(expense);
       }else{
           Expenses = new ArrayList<>();
           Expenses.add(expense);
       }
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void removeExpense(Expense expense){
//        Expenses.remove(expense);
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public String getEndDate() {
        return endDate;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getStatus() {
//        return status;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)



// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setStatus(String status) {
//        this.status = status;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public String getIdentifier() {
        return reportID;
    }

    public void setIdentifier(String identifier) {
        this.reportID = identifier;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void addExpenseToList(Expense expense){
//        this.Expenses.add(expense);
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)


}
