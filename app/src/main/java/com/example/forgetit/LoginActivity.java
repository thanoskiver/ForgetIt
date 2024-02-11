package com.example.forgetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText passwordView;
    EditText usernameView;
    CharSequence password,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordView=(EditText) findViewById(R.id.Password);
        usernameView=(EditText) findViewById(R.id.editText);


    }
    /**
     οταν η δραστηριοτητα τερματιζει αποθηκευω τα στοιχεια που εδωσε ο χρηστης στο outState με την μορφη λεξιλογιου(key/value)
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        password= passwordView.getText();
        username= usernameView.getText();
        outState.putCharSequence("password", password);
        outState.putCharSequence("user", username);

    }

    /**
     * οταν η δραστηριοτητα επανεκκινει ανακτω τα στοιχεια που εβαλα στο outState μεσα απο το savedInstanceState
     * αν το bundle αυτο ειναι κενο, δηλαδη δεν εδωσε τιποτα ο χρηστης απλα ξαναρχικοποιω τα views
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            password=savedInstanceState.getCharSequence("password");
            username=savedInstanceState.getCharSequence("user");
            passwordView.setText(password);
            usernameView.setText(username);
        }
        else {
            passwordView.setText("");
            usernameView.setText("");
        }

    }

    /**
     * παιρνει μια συμβολοσειρα ελεγχει αν ο τελευταιος χαρακτηρας του ειναι κενο. Αν ειναι το σβηνει αλλιως την επιστρεφει απαραλλαχτη .
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
     * συλλεγω τα δεδομενα που εχει πληκτρολογησει ο χρηστης
     */
    public boolean getInputs(){

        password= passwordView.getText();
        username= usernameView.getText();
        username=saveTheLastSpace(username.toString());
        if(username.toString().equals("")){
            Toast.makeText(this, R.string.emptyUserNameForm, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.toString().equals(""))
        {
            Toast.makeText(this, R.string.typePassWord, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true ;

    }

    /**
     * ο χρηστης πατησε το κουμπι signIn και κανω ελεγχο αν υπαρχει στην βαση το ονομα χρηστη, και στην συνεχεια αν ο κωδικος ταυτιζεται με τον αντιστοιχο στην βαση.
     * @param v
     */
    public void signIn(View v)
    {
        if(getInputs())
        {
            DataBase db = DataBase.getInstance(this);
            String result = db.findProfile(username.toString());


            if (result.equals("")) {
                Toast.makeText(this, R.string.noUsername, Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.toString().equals(result)) {
                Toast.makeText(this, R.string.signedInSucc, Toast.LENGTH_SHORT).show();
                Intent newActivity = new Intent(this, AccountActivity.class);
                newActivity.putExtra("USER", username);

                startActivity(newActivity);
            } else {
                Toast.makeText(this, R.string.signedInUnsecc, Toast.LENGTH_SHORT).show();

            }
        }


    }
    /**
     * ο χρηστης πατησε το κουμπι signup και δημιουργω μια νεα δραστηριοτητα που ο χρηστης θα κανει signup
     * @param v
     */
    public void signUp(View v){
        Intent a=new Intent(this,SignUpActivity.class);
        startActivity(a);

    }

}