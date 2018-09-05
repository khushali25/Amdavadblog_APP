package com.example.xps.amdavadblog_app;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunicationFragment extends Fragment {

    TextInputEditText edtemail,edtphone,edtname,edtmessage;
    TextInputLayout txtemail,txtphone,txtname,txtmessage;
    public CommunicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_communication, container, false);

        edtphone = view.findViewById(R.id.phone_edit_text);
        txtphone = view.findViewById(R.id.phone_text_input);

        edtname = view.findViewById(R.id.name_edit_text);
        txtname = view.findViewById(R.id.name_text_input);

        edtemail = view.findViewById(R.id.email_edit_text);
        txtemail = view.findViewById(R.id.email_text_input);

        edtmessage = view.findViewById(R.id.message_edit_text);
        txtmessage = view.findViewById(R.id.message_text_input);
//        edtphone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//    });

    final TextInputEditText[] textInputLayouts = new TextInputEditText[]{edtname,edtemail,edtphone,edtmessage};

        Button button = view.findViewById(R.id.submitbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                boolean noErrors = true;
                final String email = edtemail.getText().toString();
                final String phone = edtphone.getText().toString();

                for (TextInputEditText textInputLayout : textInputLayouts) {
                    String editTextString = textInputLayout.getText().toString();
                    if (editTextString.isEmpty()) {
                        textInputLayout.setError("Field must not be empty");
                        noErrors = false;
                    }
                    else if(!isvalidemail(email))
                    {
                        txtemail.setError("Email is not valid");
                        noErrors = false;
                    }
                    else if(!isvalidphone(phone))
                    {
                        txtphone.setError("Phone number is not valid");
                        noErrors = false;
                    }
                    else {
                        textInputLayout.setError(null);

                    }
                }
                edtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            validateemailEditText(String.valueOf(((EditText) v).getText()));
                        }
                    }

                    private void validateemailEditText(String text) {
                        if (isvalidemail(text)) {
                            txtemail.setError(null);
                        }
                        else{
                            txtemail.setError("Email is not valid");
                        }
                    }
                });
                edtphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            validatephoneEditText(String.valueOf(((EditText) v).getText()));
                        }
                    }

                    private void validatephoneEditText(String text) {
                        if (isvalidphone(text)) {
                            txtphone.setError(null);
                        }
                        else{
                            txtphone.setError("Phone number is not valid");
                        }
                    }
                });
                if (noErrors) {
                    // All fields are valid!
                    edtemail.setText(null);
                    edtmessage.setText(null);
                    edtname.setText(null);
                    edtphone.setText(null);
                    txtname.setFocusable(true);
                    Toast.makeText(getContext(),"Thank you for contact",Toast.LENGTH_LONG).show();

                }
            }

        });

        return view;
    }

    private boolean isvalidphone(String edtphone) {
        return edtphone.length() == 10;
    }

    private boolean isvalidemail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
