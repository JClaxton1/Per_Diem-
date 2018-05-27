package com.example.jay.perdiem.objects;

@SuppressWarnings("EmptyMethod")
public class Expense {

    private String ID;
    private final String name;
    private final String total;
    // --Commented out by Inspection (5/26/18, 10:59 PM):private String location;

    // private Bitmap recieptImage;

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public Expense(){
//
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    @SuppressWarnings("unused")
    public Expense(String Name, String Date, String Total){
        this.name = Name;
        this.total = Total;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setName(String name) {
//        this.name = name;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getDate() {
//        return date;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setDate(String date) {
//        this.date = date;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    public String getTotal() {
        return total;
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setTotal(String total) {
//        this.total = total;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getLocation() {
//        return location;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public void setLocation(String location) {
//        this.location = location;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getMemo() {
//        return memo;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    @SuppressWarnings({"unused", "EmptyMethod"})
    public void setMemo(String memo) {
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getCat() {
//        return category;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    @SuppressWarnings("unused")
    public void setCat(String cat) {
    }

// --Commented out by Inspection START (5/26/18, 10:54 PM):
//    public String getURL() {
//        return URL;
//    }
// --Commented out by Inspection STOP (5/26/18, 10:54 PM)

    @SuppressWarnings("unused")
    public void setURL(String URL) {
    }

    /*
    public Bitmap getRecieptImage() {
        return recieptImage;
    }

    public void setRecieptImage(Bitmap recieptImage) {
        this.recieptImage = recieptImage;
    }
*/
}
