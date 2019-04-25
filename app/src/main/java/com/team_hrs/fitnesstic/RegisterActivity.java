package com.team_hrs.fitnesstic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.team_hrs.fitnesstic.R;
import com.team_hrs.fitnesstic.helpers.InputValidation;
import com.team_hrs.fitnesstic.model.User;
import com.team_hrs.fitnesstic.sql.DatabaseHelper1;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutAge;
    private TextInputLayout textInputLayoutWeight;
    private TextInputLayout textInputLayoutHeight;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    public TextInputEditText textInputEditTextAge;
    public TextInputEditText textInputEditTextHeight;
    public TextInputEditText textInputEditTextWeight;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private AppCompatTextView appCompatTextViewBmiLink;


    private InputValidation inputValidation;
    private DatabaseHelper1 databaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();


        Spinner spinner = (Spinner) findViewById(R.id.gender_choice); //For Gender Dropdown Menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Gender,android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


    initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutAge = (TextInputLayout) findViewById(R.id.textInputLayoutAge);
        textInputLayoutWeight = (TextInputLayout) findViewById(R.id.textInputLayoutWeight);
        textInputLayoutHeight = (TextInputLayout) findViewById(R.id.textInputLayoutHeight);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextAge = (TextInputEditText) findViewById(R.id.textInputEditTextAge);
        textInputEditTextWeight = (TextInputEditText) findViewById(R.id.textInputEditTextWeight);
        textInputEditTextHeight = (TextInputEditText) findViewById(R.id.textInputEditTextHeight);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

        appCompatTextViewBmiLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewBmiLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        appCompatTextViewBmiLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper1(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;

            case R.id.appCompatTextViewBmiLink:

              //  intentBmi.putExtra("Height",String.valueOf(textInputLayoutHeight));
                //intentBmi.putExtra("Weight",String.valueOf(textInputLayoutWeight));
                int minimum_age=12; //Minimum age requirement
                float bmi_of_body = 0; //Initialization
                float height_of_person = (Float.parseFloat(textInputEditTextHeight.getText().toString())/100);
                //Taking height in centimetres and converting into Metres
                float weight_of_body = Float.parseFloat((textInputEditTextWeight.getText().toString())); //Storing the weight of body

                bmi_of_body = (weight_of_body/(height_of_person*height_of_person)); //Calculating the BMI of body (bmi= weight/height*height)

                if (minimum_age<(Integer.parseInt(textInputEditTextAge.getText().toString()))) {

                    Toast.makeText(this, "Welcome " + textInputEditTextName.getText()+" \nBMI: "+ bmi_of_body, Toast.LENGTH_LONG).show(); //For check if the btn is working
                    Intent intentBmi = new Intent(getApplicationContext(), BMI.class);
                    intentBmi.putExtra("BMI",String.valueOf(bmi_of_body));
                    startActivity(intentBmi);
                    //Intent intent = new Intent(this, BMI.class);
                    //startActivity(intent);
                } //End of if-statement
                else {
                    Toast.makeText(this,"You are UnderAge",Toast.LENGTH_LONG).show();

                } //End of Else-statement
                break;
                //End of Conditions
        } //End of the next_screen_action Method(Button Action)



    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}