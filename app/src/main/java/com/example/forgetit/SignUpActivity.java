package com.example.forgetit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;

import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends LoginActivity {

    EditText newUserNameView,newPassWordView, confirmPassView;//το πεδιο ονομα του χρηστη και των κωδικων προσβασης
    TextView passwordSuggestion;//μια λεζαντα που ενημερωνει τον χρηστη για τους αποδεκτους κωδικους προσβασης
    CheckBox check;//ενα τσεκμποξ για τον χρηστη που θελει προστασια κωδικου(να εχει ειδικους χαρακτηρες κτλπ)
    CharSequence password,username,confirmPassText;//εδω αποθηκευονται τα inputs του χρηστη στα editText

    ToggleButton toggleForPassword;//κουμπι για την εμφανιση ή οχι του κωδικου



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        newPassWordView=(EditText)findViewById(R.id.editTextTextPassword);
        newUserNameView=(EditText) findViewById(R.id.editTextText2);
        passwordSuggestion=(TextView)findViewById(R.id.textView5);
        confirmPassView =(EditText)findViewById(R.id.ConfirmPass);
        check=(CheckBox) findViewById(R.id.checkBox);
        passwordSuggestion.setVisibility(View.INVISIBLE);
        toggleForPassword=findViewById(R.id.toggleButton2);
        toggleForPassword.setChecked(false);
        makePasswordVisible(toggleForPassword);

    }





    /**
     * συλλογη των δεδομενα που εισηγαγε ο χρηστης
     */
    public void collectUserInput(){
        password=newPassWordView.getText();
        username=newUserNameView.getText();
        confirmPassText= confirmPassView.getText();


    }

    /**
     * αποθηκευση κατα τον τερματισμο της δραστηριοτητας τα δεδομενα του χρηστη
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        collectUserInput();
        outState.putCharSequence("P",password);
        outState.putCharSequence("U",username);
        outState.putCharSequence("C",confirmPassText);
        outState.putBoolean("check", check.isChecked());






    }
    /**
     * ανακτω τα δεδομενα που αποθηκευτηκαν κατα τον τερματισμο της δραστηριοτητας
     */
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            newUserNameView.setText(savedInstanceState.getCharSequence("U"));
            newPassWordView.setText(savedInstanceState.getCharSequence("P"));
            confirmPassView.setText(savedInstanceState.getCharSequence("C"));
            check.setChecked(savedInstanceState.getBoolean("check"));

            if(check.isChecked()){
                passwordSuggestion.setVisibility(View.VISIBLE);
            }
        }
        else{
            newUserNameView.setText("");
            newPassWordView.setText("");
            check.setChecked(false);
            confirmPassView.setText("");


        }

    }

    /**
     * μεθοδος προγραμματιστικης λογικης του κουμπιου submit
     * συλλεγει τα δεδομενα που πληκτρολογησε ο χρηστης μεσω της  collectUserInput()
     * και εμφανιζει καταλληλα μηνυματα μεσω Toast (πχ οταν ο χρηστης παταει το κουμπι ενω εχει κενες απαντησεις
     * το συστημα τον προτρεπει να τις συμπληρωσει για να προχωρησει)
     * @param v
     */
    public void submit(View v) {
        int flag=0;//αν το flag ειναι μηδεν σημαινει πως περασε ολους τους ελεγχους η εγγραφη και μπορει να τοποθετηθει στην βαση
        collectUserInput();
        //αν το username ειναι κενο αποριπτεται η προσπαθεια εγγραφης
        if (username.toString().isEmpty()) {
            Toast.makeText(this, R.string.emptyUserNameForm, Toast.LENGTH_SHORT).show();
            return;
        }
        if(validUsername()){
            Toast.makeText(this, R.string.forbittenUsername, Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.toString().contains(" "))
        {
            Toast.makeText(this, R.string.NoSpace, Toast.LENGTH_SHORT).show();
            return;
        }
        //αν το password ειναι κενο αποριπτεται η προσπαθεια εγγραφης
        if (password.toString().isEmpty()) {
            Toast.makeText(this, R.string.typePassWord, Toast.LENGTH_SHORT).show();
            return;
        }
        //αν ο κωδικοι δεν ταιριαζουν
        if (!password.toString().equals(confirmPassText.toString()))
        {
            Toast.makeText(this, R.string.passwordMatch, Toast.LENGTH_SHORT).show();
            return;
        }
        //αν το τσεκμποξ για τον ισχυρο κωδικο ειναι επιλεγμενο κανε τους σωστους ελεγχους
        if (check.isChecked()) {
            flag=1;
            if (validPassword() == 0 && password.toString().length() >= 8) {
                flag=0;
            }
            else {
                Toast.makeText(this, validPassword(), Toast.LENGTH_SHORT).show();
            }
        }
        if(flag==0) {

            SQLSign();
        }
    }

    /**
     * διαδικασια κατα την οποια διαγραφονται οι τελευταιοι κενοι χαρακτηρες απο μια συμβολοσειρα
     * @param aWord μια συμβολοσειρα με κενους χαρακτηρες στο τελος της
     * @return μια συμβολοσειρα χωρις κενους χαρακτηρες στο τελος της
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
         * διαδικασια προσθεσης ενος νεου προφιλ στην βαση(αν δεν υπαρχει ηδη φυσικα)
         */
    public void SQLSign(){


        DataBase db=DataBase.getInstance(this);

        String usernameString=saveTheLastSpace(username.toString());

        if( db.addProfile(usernameString, password.toString())){
            db.newUserTable(username.toString());
            newUserNameView.setText("");
            newPassWordView.setText("");
            confirmPassView.setText("");
            Toast.makeText(this, R.string.SignedIn, Toast.LENGTH_SHORT).show();

            Intent newActivity=new Intent(this, AccountActivity.class);
            newActivity.putExtra("USER",usernameString);

            startActivity(newActivity);


        }
        else {
            Toast.makeText(this, "This username already exists", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * μια μεθοδος που ελεγχει την εγκυροτητα του ονοματος χρηστη.
     * @return
     */
    public boolean validUsername(){

        String p=username.toString();

        Pattern special = Pattern.compile ("[^,:'.!@#$%&*()+=|<>?{}\\[\\]~-]");


        Matcher hasSpecial = special.matcher(p);


       return !hasSpecial.find();
    }

    /**
     * μια μεθοδος που ελεγχει την ισχυ του κωδικου προσβασης και επιστρεφει το αναλογο μηνυμα.
     * @return
     */
    public int validPassword(){

        String p=password.toString();
        Pattern letterSmall = Pattern.compile("[a-z]");
        Pattern letterBig=Pattern.compile("[A-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        Matcher hasSmallLetter = letterSmall.matcher(p);
        Matcher hasDigit = digit.matcher(p);
        Matcher hasSpecial = special.matcher(p);
        Matcher hasLetterBig=letterBig.matcher(p);

        //  return hasLetter.find() && hasDigit.find() && hasSpecial.find() && hasLetterBig.find()&& p.length()>=8;
        if(!hasSmallLetter.find()){

            return R.string.errorSmallLetter;
        }
        if(!hasDigit.find()){
            return R.string.errorDigit;
        }
        if(!hasLetterBig.find()){
            return R.string.errorBigLetter;
        }
        if(!hasSpecial.find()){
            return R.string.errorSpecialLetter;
        }
        if(p.length()<8)
            return R.string.errorLength;
        return 0;
    }
    public void suggestionShow(View v) {
        if (passwordSuggestion.isShown()) {
            passwordSuggestion.setVisibility(View.INVISIBLE);
        }
        else {
            passwordSuggestion.setVisibility(View.VISIBLE);
        }
    }


    /**
     * διαδικασια κατα την οποια ο κωδικος στις φορμες newPassWordView, confirmPassView να ειναι ορατος.
     * @param v
     */
    public void makePasswordVisible(View v){

        int selectionStart = newPassWordView.getSelectionStart();
        int selectionEnd = newPassWordView.getSelectionEnd();

        int confirmStartSelection= confirmPassView.getSelectionStart();
        if(toggleForPassword.isChecked()){
            //toggleForPassword.setBackgroundDrawable(getDrawable(R.drawable.isee));

            newPassWordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            confirmPassView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        else {
          //  toggleForPassword.setBackgroundDrawable(getDrawable(R.drawable.cantsee));;
            newPassWordView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPassView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        newPassWordView.setSelection(selectionStart, selectionEnd);
        confirmPassView.setSelection(confirmStartSelection);
    }


public void back(View v){
        finish();
}
}
