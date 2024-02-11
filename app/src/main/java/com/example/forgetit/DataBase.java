package com.example.forgetit;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME="myDB.db";
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_TABLE="profiles";

    public static final String ID="USERNAME";
    public static final String PASSWORD="PASSWORD";


    private static final String DATABASE_CREATE="create table "+DATABASE_TABLE +"("+ID +" text primary key, "+ PASSWORD+" text not null)";

    private static final String DATABASE_UPDATE="DROP TABLE IF EXISTS "+DATABASE_TABLE;
    private SQLiteDatabase db;
    public DataBase(Context c, String name,SQLiteDatabase.CursorFactory f,int version)
    {
        super(c,DATABASE_NAME,f,DATABASE_VERSION);

    }

    /**
     * υποχρεωτικη υποσκελιση για την δημιουργία της βασης
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);


    }

    /**
     * υποχρεωτικη υποσκελιση για την ενημερωση του σχηματος της βασης
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_UPDATE);
        onCreate(db);
    }

    /**
     * μεθοδος προσθηκης μιας εγγραφης με την προυποθεση οτι δεν υπαρχει ηδη στην βαση
     * @param username το ονομα του χρηστη της εφαρμογης
     * @param password ο κωδικος του χρηστη
     * @return επιστρεφει true αν εγινε η προσθηκη /false σε αντιθετη περιπτωση
     */
    public boolean addProfile(String username,String password){

        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ID,username);
        values.put(PASSWORD,password);


        if(findProfile(username).equals(""))//οταν η μεθοδος επιστρεφει κενη συμβολοσειρα σημαινει οτι δεν βρηκε την εγγραφη στην βαση αρα μπορει να προστεθει
        {
            db=this.getWritableDatabase();//η find profile κλεινει την βαση και γιαυτο την ξανανοιγω
            db.insert(DATABASE_TABLE,null,values);
            db.close();
            return true;

        }
        else {
            db.close();
            return false;
        }



    }





    /**
     * μεθοδος αναζητης εγγραφης στην βαση μεσω του κλειδιου της (ονομα χρηστη εφαρμογης)
     * @param username ονομα χρηστη εφαρμογης
     * @return επιστρεφει τον κωδικο προσβασης του username ή αν το username δεν υπαρχει στην βαση επιστρεφει την κενη συμβολοσειρα
     */
    public String findProfile(String username){
        db=this.getWritableDatabase();
        String q ="SELECT * FROM " + DATABASE_TABLE + " WHERE " + ID + " = '" + username + "'";

        Cursor c= db.rawQuery(q,null);
        if(c.moveToFirst())
        {
            int a=c.getColumnIndex(PASSWORD);
            String result=c.getString(a);
            c.close();
            db.close();
            return result;

        }
        c.close();
        return "";




    }



    private static DataBase instance;
    public static DataBase getInstance(Context c){
        if(instance==null){
            instance =new DataBase(c.getApplicationContext(),null,null,1);

        }
        return instance;
    }

    final String USERNAMES="USERNAME";

    final String PASSWORD_U="AppUserPassWords";
    final String LABEL="LABEL";


    /**
     * Δημιουργια ενος πινακα για καθε χρηστη που κανει εγγραφη στην εφαρμογη, μεσα στον οποιο αποθηκευονται τα προσωπικα του στοιχεια.
     * @param name το ονομα του χρηστη
     */
    public void newUserTable(String name){

        final String DATABASE_CREATE_PASS="create table "+ name.replace(" ","") +"("+ PASSWORD_U+" text not null, "+USERNAMES+" text not null, "+LABEL+" text  primary key )";
        db=this.getWritableDatabase();
        db.execSQL(DATABASE_CREATE_PASS);

        db.close();


    }

    /**
     * λαμβανει το ονομα του χρηστη της εφαρμογης και ψαχνει στην βαση τον πινακα με τα δεδομενα του
     *
     * @param user ονομα του χρηστη της εφαρμογης
     * @return αν βρει τον πινακα και ο πινακας υπαρχει επιστρεφει ενα  Map με τον κωδικο ως κλειδι που δειχνει μια λιστα [password, label]  /αν δεν βρει τον πινακα ή ο πινακας ειναι αδειος γυρναει null
     */
    public Map<String, ArrayList<String>> getUserContex(String user)
    {
        Map<String,ArrayList<String>> returnOfCursor=new HashMap<>();
        db=this.getWritableDatabase();
        String q ="SELECT * FROM " + user ;
        Cursor res=db.rawQuery(q,null);
        int passWordIntex=res.getColumnIndex(PASSWORD_U);
        int userNameIntex=res.getColumnIndex(USERNAMES);
        int labelIntex=res.getColumnIndex(LABEL);



        if(res==null){//δεν υπαρχει ο πινακας user στην συμβολοσειρα q
            res.close();
            db.close();
            return null;
        }else
        {


            if(res.moveToFirst()) {//if υπαρχουν στοιχεια στον πινακα
                do {

                    String username = res.getString(userNameIntex);
                    String password = res.getString(passWordIntex);
                    String label=res.getString(labelIntex);
                    ArrayList<String> list=new ArrayList<>();
                    list.add(password);
                    list.add(label);
                    returnOfCursor.put(username,list);

                } while (res.moveToNext());//while υπαρχουν επομενα στοιχεια
                res.close();
                db.close();
                return returnOfCursor;
            }
            else {//ο πινακας ειναι αδειος
                res.close();
                db.close();
                return null;
            }
        }
    }

    /**
     *η μεθοδος αυτη αποθηκευει στον πινακα @param user τα δεδομενα που εδωσε ο χρηστης στην φορμα εισαγωγης ακα addActivity
     * @param user ο χρηστης της εφαρμογης
     * @param usernameOfUser το ονομα χρηστη που θελει να αποθηκευει ο χρηστης
     * @param password  ο κωδικος που θελει να αποθηκευσει ο χρηστης
     * @param label  η ετικετα της εγγραφης
     *
     * @return true αν η προσθηκη εγινε με επιτυχια,false αν οχι,εμφανιζοντας το αντιστοιχο μηνυμα στην οθονη.
     *
     */
    public boolean addUserContex(String user,String usernameOfUser,String password,String label,Context c){
        db=this.getWritableDatabase();
        String q="SELECT "+LABEL+" FROM "+user+" WHERE "+LABEL+"='"+label+"'";
        Cursor res;
        res=db.rawQuery(q,null);
        if(!res.moveToFirst()) {
            ContentValues add = new ContentValues();

            add.put(USERNAMES, usernameOfUser);
            add.put(PASSWORD_U, password);
            add.put(LABEL, label);

            db.insert(user, null, add);

            db.close();
            return true;
        }
        else {
           Toast.makeText(c,c.getString(R.string.LabelAllreadyInUSE),Toast.LENGTH_SHORT ).show();
            return false;
        }



    }

    /**
     *
     * @param user ονομα χρηστη για να εντοπιστει ο πινακας με τα στοιχεια του
     * @param label η ετικετα της τριπλετας που ο χρηστης θελει να διαγραψει
     */
    public void deleteUserContext(String user,String label){
        db=this.getWritableDatabase();
        final String q="DELETE FROM "+user+" WHERE "+LABEL+"='"+label+"';";
        db.execSQL(q);
        db.close();
    }

}