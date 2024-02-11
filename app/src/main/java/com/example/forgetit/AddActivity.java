package com.example.forgetit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * φορμα για την προσθηκη δεδομενων απο τον χρηστη.
 *
 * η φορμα αποτελειται απο 3 editText για την εισοδο δεδομενων απο τον χρηστη ,ενα κουμπι οπισθοδρομησης και ενα κουμπι καταθεσης των δεδομενων.
 * Ο χρηστης μπορει να εισελθει σε αυτην την δραστηριοτητα είτε για την δημιουργια νεας εγγραφης είτε για την επεξεργασια μιας ηδη υπαρχουσας.
 * Στην περιπτωση που ερχεται με σκοπο την επεξεργασια της υπαρχουσαν πρεπει να σημείωσουμε πως την σβηνουμε προσωρινα απο την βαση δεδομενων και σε περιπτωση που ο χρηστης επιλεξει να μην
 * αλλαξει κατι σε αυτην, η κλαση την ξαναδημιουργει ωστε να μην διαγραφτει μονιμα.
 *
 * Διαφορετικα: δημιουργια νεας εγγραφης μεσω της φορμας και αποθηκευση της στην βαση
 */
public class AddActivity extends AppCompatActivity {
    boolean addFlag;//false αν ειναι editActivity,true αν ειναι add
    String tempLabel,tempUsername,tempPassword;//προσωρινη αποθηκευση των στοιχειων που εχει δωσει ηδη ο χρηστης
    EditText username,password,label;//views που ο χρηστης πληκτρολογει το ονομα ,το συνθηματικο και τον τιτλο της τριπλετας.
    String usernameText,passwordText,labelText,appUsername;//οι αντιστοιχες συμβολοσειρες που θα αποθηκευτουν τα δεδομενα
    TextView activityTitle;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        appUsername=getIntent().getCharSequenceExtra("AppUser").toString();

        activityTitle=findViewById(R.id.textView2);
        submitButton=findViewById(R.id.button5);
        password=findViewById(R.id.editTextText4);
        username=findViewById(R.id.editTextText3);
        label=findViewById(R.id.editTextText);

        addFlag =getIntent().getBooleanExtra("flag",true);


        setViews();

    }



    /**
     * μια μεθοδος που σεταρει τα Views αναλογα με το αν προκειται για προσθηκη ή για επεξεργασια δεδομενων
     */

    private void setViews(){

        TextView label1,label2,label3;
        label1 =findViewById(R.id.textView3);
        label2=findViewById(R.id.textView4);
        label3=findViewById(R.id.textView6);

        if(addFlag) {
            activityTitle.setText(R.string.addIt);
            submitButton.setText(R.string.add);

            username.setText(getIntent().getCharSequenceExtra("Username"));
            password.setText(getIntent().getCharSequenceExtra("Password"));
            label.setText(getIntent().getCharSequenceExtra("Title"));
        }
        else{
            tempLabel =getIntent().getCharSequenceExtra("Title").toString();
            tempUsername=getIntent().getCharSequenceExtra("Username").toString();
            tempPassword=getIntent().getCharSequenceExtra("Password").toString();
            username.setHint(tempUsername);
            password.setHint(tempPassword);
            label.setHint(tempLabel);
            label1.setVisibility(View.VISIBLE);
            label1.setText(R.string.Label);
            label2.setVisibility(View.VISIBLE);
            label2.setText(R.string.username);
            label3.setVisibility(View.VISIBLE);
            label3.setText(R.string.password);
            activityTitle.setText(R.string.saveIt);
            submitButton.setText(R.string.save);

        }

    }
    /**
     * παιρνει μια συμβολοσειρα ελεγχει αν ο τελευταιος χαρακτηρας του ειναι κενο .Αν ειναι το σβηνει αλλιως την επιστρεφει απαραλλαχτη .
     * @param aWord συμβολοσειρα
     * @return μια σειμβολοσειρα που δεν τελειωνει σε κενο
     */
    public String saveTheLastSpace(String aWord) {
        int length;
        length = aWord.length();
        if (length != 0) {
            StringBuilder a = new StringBuilder(aWord);
            while(aWord.charAt(length - 1)==' ') {
                a.deleteCharAt(length - 1);
                aWord = a.toString();
                length=aWord.length();
            }
        }
        return aWord;
    }

    /**
     * μια μεθοδος που συλλεγει τα δεδομενα που εδωσε ο χρηστης στην φορμα
     * @return επιστρεφει true αν ολες οι τιμες που εδωσε ειναι επιτρεπτες/false αν εστω και ενα πεδιο απαντηθηκε εσφαλμενα
     */
    public boolean  collectTheData(){

            usernameText =username.getText().toString();
            usernameText=saveTheLastSpace(usernameText);



            passwordText=password.getText().toString();
            passwordText=saveTheLastSpace( passwordText);

            labelText=label.getText().toString();
            labelText=saveTheLastSpace( labelText);

        if (addFlag){
            if(usernameText.isEmpty()||passwordText.isEmpty()||labelText.isEmpty()){
                Toast.makeText(this,R.string.NoFieldEmpty, Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                return true;
            }

        }else {

            if(usernameText.isEmpty()) {
                username.setText(tempUsername);
                usernameText=tempUsername;
            }
            if(passwordText.isEmpty()) {
                password.setText(tempPassword);
                passwordText=tempPassword;
            }
            if( labelText.isEmpty()){
                label.setText(tempLabel);
                labelText=tempLabel;

            }


            return true;
            }

        }



    /**
     * μεθοδος που τερματιζει την δραστηριοτητα
     * @param view
     */
   public void  back(View view){

       saveOfTheTemps();
       finish();
   }

    /**
     * στην περιπτωση που ο χρηστης πατησει το πισω κουμπι πλοηγησης ,αποθηκευονται τα προσωρινα στοιχεια
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveOfTheTemps();

    }

    /**
     * αποθηκευση των προσωρινων στοιχειων
     */
    public void saveOfTheTemps(){
        if(!addFlag) {
            DataBase db = DataBase.getInstance(this);
            db.addUserContex(appUsername, tempUsername, tempPassword, tempLabel, this);
            db.close();

        }


    }

    /**
     * μεθοδος που απαιτει την συλλογη των δεδομενων
     * (με την προυποθεση οτι η συλλογη ηταν επιτυχης)
     * και τα τοποθετει σε ενα intent ωστε να επιστραφουν στη προηγουμενη δραστηριοτητα ως αποτελεσματα
     * και τερματιζει την τρεχουσα δραστηριοτητα
     *
     * @param v
     */
    public void submit(View v){
       boolean a=collectTheData();
       DataBase db=DataBase.getInstance(this);

        if (a) {

           if(db.addUserContex(appUsername,usernameText,passwordText,labelText,this))
            {
                db.close();
                finish();
                if(addFlag){
                    Toast.makeText(this,R.string.added,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,R.string.saved,Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
}