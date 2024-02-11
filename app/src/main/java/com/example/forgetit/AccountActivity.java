package com.example.forgetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * αυτη η κλαση αντιπροσωπευει την δραστηριοτητα της εφαρμογης που ο χρηστης εχει προσβαση στα δεδομενα που εχει αποθηκευσει(ενα πληθος απο κωδικους προσβασης ,ονοματα χρηστη
 * και πληροφοριες συνδεσης (cardLabel)
 */
public class AccountActivity extends AppCompatActivity {
TextView welcome;//ενα απλο TextView που περιεχει ενα μηνυμα καλοσοριματος
RecyclerView recycleView;//ενα  RecyclerView που περιεχει τις πληροφοριες του χρηστη
RecyclerView.LayoutManager manager;//μανατζερ του recycleView

String username;//To username του χρηστη στην εφαρμογη μας.
DataBase db;//η βαση δεδομενων της εφαρμογης μας
RecyclerView.Adapter<RecycleAdapter.ViewHolder> adapter;//ο adapter που χρησιμοποιουμε στο recycleView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_account);


        db=DataBase.getInstance(this);//αρχικοποιηση της βασης




        username = getIntent().getCharSequenceExtra("USER").toString();//λαμβανω μεσω ιντεντ το ονομα που εδωσε ο χρηστης στην εφαρμογη(απο την δραστηριοτητα main/signUp)

        welcome=findViewById(R.id.TextView4);

        String w=getString(R.string.welcome);
        welcome.setText(w+" "+username+".");

        recyclerViewFiller();



    }
   private void  recyclerViewFiller(){
        //αρχικοποιηση του RecycleView
        recycleView=findViewById(R.id.Recycle);
        manager=new LinearLayoutManager(this);

        recycleView.setLayoutManager(manager);
        Map <String, ArrayList<String>> a=db.getUserContex(username);
        adapter=new RecycleAdapter(a,this,username);

        recycleView.setAdapter(adapter);
    }



   @Override
    public void onResume() {
        super.onResume();
        recyclerViewFiller();


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        username = savedInstanceState.getCharSequence("USER").toString();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("USER",username);

        

    }



    /**
     *ενα κουμπι αποσυνδεσης τερματιζει την δραστηριοτητα και καθαριζει το stack ωστε να γυρισουμε στο mainActivity ως αρχικη.
     *
     * @param v
     */
    public void logOut(View v){
        db.close();
        Toast.makeText(this,R.string.LogOff,Toast.LENGTH_SHORT).show();
        Intent a=new Intent(this, LoginActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }


    /**
     * διαδικασια προσθεσης νεων δεδομενων προς αποθηκευση στην βαση δεδομενων
     * δημιουργειται μια νεα δραστηριοτητα με την ιδιοτητα μιας φορμας στην οποια ο χρηστης δινει δεδομενα
     * τα δεδομενα επιστρεφονται και τοποθετουνται στην βαση
     * @param v
     */
   public void addNewData(View v){
       Intent activityAdd=new Intent(this, AddActivity.class);
       activityAdd.putExtra("AppUser",username);

       activityAdd.putExtra("Title","");
       activityAdd.putExtra("Username","");
       activityAdd.putExtra("Password","");

       startActivity(activityAdd);




   }



}