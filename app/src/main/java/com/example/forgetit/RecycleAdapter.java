package com.example.forgetit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * recycle view ου περιεχει καρτες του χρηστη και εμφανιζεται στην δραστηριοτητα
 * account.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    /**
     * κενος κατασκευαστης (duh)
     */
    public  RecycleAdapter(){

    }
    static Context c;
    static String appUser;
    Map<String,ArrayList<String>> data;//λεξικα
    ArrayList<String> keys;//λιστα με τα κλειδια του λεξικου
    boolean dataNullFlag;//true αν το recycleView εχει δεδομενα να παρουσιασει, false δεν εχει.

    /**
     * Αν το data ειναι null αυτο σημαινει πως ο χρηστης δεν εχει δεδομενα αποθηκευμενα και χρειαζεται ειδικη μεταχειριση. Το dataNullFlag λαμβανει την τιμη false.
     *
     * Διαφορετικα δεχομαστε ενα γεματο λεξικο, απο αυτο αποθηκευουμε ολα τα κλειδια σε μια προσπελασιμη δομη
     * @param data ειναι ενα hashMap που εχει τα δεδομενα ενος χρηστη (εχει ολες τις τριπλετες (ετικετα-ονομα_χρηστη-συνθηματικο)
     *             το κλειδι του λεξικου ειναι το ονομα_χρηστη και καθε κλειδι δειχνει σε μια λιστα συμβολοσειρων [συνθηματικο, ετικετα]
     * @param appUser το ονομα του χρηστη που εχει εισελθει στην δραστηριοτητα account
     * @param c αφορα τη δραστηριοτητα που καλεσε το recycleView στην οποια εμφανιζει τα αντιστοιχα μηνυματα προς την οθονη .
     */
    public  RecycleAdapter(Map<String, ArrayList<String>> data, Context c,String appUser) {
        this.c=c;
        this.appUser=appUser;
        if (data != null)
        {
            dataNullFlag=false;
            keys=new ArrayList<>();
            Set<String> AllKeys = data.keySet();//ενα συνολο με ολα τα κλειδια
            if (AllKeys.size() != 0)
            {
                this.data = data;
                for (String name : AllKeys)
                {
                    if (name != null)
                        keys.add(name);//αποθηκευω τα κλειδια σε μια προσπελασιμη δομη
                }

            }


        }
        else {
            dataNullFlag=true;
        }
    }





    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);//συνδεση με το περιεχομενο του RecycleView(τις καρτες)
        return new ViewHolder(v);
    }

    /**
     * καθε καρτα στην λιστα αναφερεται με αυτη την μεθοδο.
     * holder ειναι ενα στοιχειο της λιστας και position η θεση του
     * αναλογα με τα δεδομενα που εχουν ερθει στον κονστρακτορα γεμιζω με δεδομενα καθε στοιχειο(holder).
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {
        if(dataNullFlag){
            holder.cardTitle.setText(R.string.ADDRECORDSMESS);
            holder.imageView.setVisibility(View.GONE);
            holder.password.setVisibility(View.GONE);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.edit.setVisibility(View.INVISIBLE);
            holder.username.setVisibility(View.GONE);
            holder.passwordCopyPaste.setVisibility(View.GONE);
            holder.usernameCopyPaste.setVisibility(View.GONE);

        }else{

                 String username= keys.get(position);
                 ArrayList<String> temp;
                 temp= data.get(username);

                 final int positionOfPassword=0,positionOfLabel=1;//το κλειδι του λεξικου ειναι το ονομα_χρηστη και καθε κλειδι δειχνει σε μια λιστα συμβολοσειρων [συνθηματικο,ετικετα]
                 if(keys.size()!=0)
                 {
                     holder.username.setText(username);
                     holder.password.setText(temp.get(positionOfPassword));
                     holder.cardTitle.setText(temp.get(positionOfLabel));
                     holder.imageView.performClick();
                 }
             }


    }

    /**
     * @return τον αριθμο των στοιχειων στην λιστα
     */
    @Override
    public int getItemCount() {
        if( dataNullFlag) {
            return 1;
        }
        else
            return keys.size();
    }


    /**
     * μια κλαση που αναπαριστα ενα στοιχειο της λιστας ως μια ενοτητα που αποτελειται απο
     * 3 κουμπια και 3 λεζαντες(γινεται συνδεση με τα στοιχεια της καρτας)
     */
    static class ViewHolder extends RecyclerView.ViewHolder{
ImageButton edit,delete;//2 κουμπια-εικονες για την λειτουργια της επεξεργασιας μιας καρτας και διαγραφη της.
ImageView imageView;
Boolean PasswordVisibilityflag;
Button usernameCopyPaste,passwordCopyPaste;//κουμπια που αντιγραφουν τα text των textView στα clipboard του κινητου.

TextView username,password,cardTitle;//λεζαντες της καρτας με το ονομα ,το συνθηματικο και τον τιτλο της καρτας.
String passwordText;//διατηρει την πραγματικη τιμη του κωδικου οταν το password textView ειναι μη ορατο.(δλδ εχει ως text "**..**"


        /**
         * κονστρακτορας
         * @param itemView αναπαριστα ενα στοιχειο της λιστας ως μια ενοτητα
         *  δινονται τιμες σε καθε view που αναφερθηκε παραπανω και περιεχεται στο itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //συνδεση των στοιχειων με τα αντικειμενα της καρτας
            PasswordVisibilityflag =true;
            imageView=itemView.findViewById(R.id.imageView);
            edit=itemView.findViewById(R.id.imageButton2);
            delete=itemView.findViewById(R.id.imageButton);
            username=itemView.findViewById(R.id.username);
            password=itemView.findViewById(R.id.password);
            cardTitle=itemView.findViewById(R.id.cardLabel);
            usernameCopyPaste=itemView.findViewById(R.id.button8);
            passwordCopyPaste=itemView.findViewById(R.id.button9);








            /**
             *  action listener του κουμπιου usernameCopyPaste για την αντιγραφη του κειμενου στο username TextView στο προχειρο της συσκευης
            */
                usernameCopyPaste.setOnClickListener(v->{
                String textToCopy = username.getText().toString();

                ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);
                clipboardManager.setPrimaryClip(clipData);

            });
             /**
             *  action listener του κουμπιου passwordCopyPaste για την αντιγραφη του κειμενου στο passwordText TextView στο clipboard της συσκευης
             */
            passwordCopyPaste.setOnClickListener(v->{
                String textToCopy = passwordText;
                ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);
                clipboardManager.setPrimaryClip(clipData);
            });


            /**
             *  action listener του edit για την εναρξη της δραστηριοτητας επεξεργασιας (addActivity) με βαση τα δεδομενα που υπαρχουν πανω στο cardView.
             *  Πριν ξεκινησει η διαδικασια, διαγραφω την εγγραφη απο την βαση και κραταω τα στοιχεια προσωρινα στη νεα δραστηριοτητα.
             */
            edit.setOnClickListener(v -> {


                DataBase db=DataBase.getInstance(c);
                Intent edit=new Intent(c, AddActivity.class);
                edit.putExtra("AppUser",appUser);
                edit.putExtra("Title",cardTitle.getText());
                edit.putExtra("Username",username.getText());
                edit.putExtra("Password",passwordText);
                db.deleteUserContext(appUser,cardTitle.getText().toString());
                edit.putExtra("flag",false);//ενα φλαγκ που μου επιτρεπει οταν ξεκινησει το activity addActivity να διαχωρισω αν ειναι εντιντ ή απλη προσθηκη
                c.startActivity(edit);

            });
            /**
             *  action listener του delete για την διαγραφη των στοιχειων της καρτας απο την βαση και στην συνεχεια ανανέωση του RecycleView.
             */
            delete.setOnClickListener(v -> {

                    DataBase db=DataBase.getInstance(c);
                    db.deleteUserContext(appUser,cardTitle.getText().toString());
                    db.close();
                    AccountActivity activity= (AccountActivity) c;
                    activity.onResume();

            });
            /**
             *  action listener του imageView για την εμφανιση ή την αποκρυψη του περιεχομενου του textView password.
             */
            imageView.setOnClickListener(v -> {
                if(PasswordVisibilityflag){
                    passwordText=password.getText().toString();
                    imageView.setImageResource(R.drawable.closed);
                    password.setText("*********");


                }
                else {
                    imageView.setImageResource(R.drawable.open);
                    password.setText(passwordText);

                }
                PasswordVisibilityflag =!PasswordVisibilityflag;
            });
        }
    }


}
