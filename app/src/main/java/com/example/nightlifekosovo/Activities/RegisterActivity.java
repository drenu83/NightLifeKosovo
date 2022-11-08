package com.example.nightlifekosovo.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nightlifekosovo.Helpers.Constants;
import com.example.nightlifekosovo.R;
import com.example.nightlifekosovo.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "Login";
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private TextInputEditText editTextUsername, editTextName, editTextSurname, editTextEmail, editTextPassword, ediTextConfirmPassword, editTextPhone, editTextBirthday;
    private TextView changeProfile;
    private ImageView profileImage;
    private Spinner spinner;
    Button signUpBtn;
    private DatePickerDialog picker;
    ProgressBar progressBar;
    Uri imageUri;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        profileImage = binding.activityRegisterProfilePicture;
        changeProfile = (TextView) binding.activityRegisterChangeProfilePictureTextView;
        editTextUsername = binding.activityRegisterUsernameTextInput;
        editTextName = binding.activityRegisterNameTextInput;
        editTextSurname = binding.activityRegisterSurnameTextInput;
        editTextEmail = binding.activityRegisterEmailTextInput;
        editTextPassword = binding.activityRegisterPasswordTextInput;
        ediTextConfirmPassword = binding.activityRegisterConfirmPasswordTextInput;
        editTextPhone = binding.activityRegisterPhoneTextInput;
        editTextBirthday = binding.activityRegisterBirthdayTextInput;
        spinner = binding.spinner;
        signUpBtn = binding.activityRegisterSignUpButton;
        progressBar = binding.activityRegisterProgressBar;

        changeProfile.setFocusable(false);
        changeProfile.setClickable(true);


        editTextBirthday.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        changeProfile.setOnClickListener(this);
        changeProfile.setKeyListener(null);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser(Uri imageUri) {
        final String username = editTextUsername.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmPassword = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String birthday = editTextBirthday.getText().toString().trim();
        final String type = spinner.getSelectedItem().toString();

        if (username.isEmpty()) {
            //editTextName.setError(getString(R.string.input_error_name));
            editTextUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }
        if (!(password.length() == confirmPassword.length())) {
            editTextPassword.setError(getString(R.string.input_password_error));
            ediTextConfirmPassword.setError(getString(R.string.input_password_error));
            editTextPassword.requestFocus();
        }

        if (phone.length() < 10) {
            editTextPhone.setError(getString(R.string.input_error));
            editTextPhone.requestFocus();
            return;
        }

        if (birthday.isEmpty()) {
            //  editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextBirthday.requestFocus();
            return;
        }


        // progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String userID = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Username", username);
                            user.put("Name", name);
                            user.put("Surname", surname);
                            user.put("Email", email);
                            user.put("Phone", phone);
                            user.put("Birthday", birthday);
                            user.put("Type", type);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegisterActivity.this, getText(R.string.text_registration_success), Toast.LENGTH_LONG).show();

                                    saveProfilePicture(imageUri);
                                    editor.putBoolean("isLogged", true);
                                    editor.commit();

                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registation error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });




    }

    public void saveProfilePicture(Uri imageUri){
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseUser = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this, "Profile picture could not be uploaded. Please try again later!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (changeProfile == v) {
            try {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.READ_PERMISSION);

                    return;
                }
                imageChooser();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (editTextBirthday == v) {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    editTextBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, year, month, day);
            picker.show();
        }

        if (signUpBtn == v) {
            registerUser(imageUri);
        }
    }

    private void imageChooser() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launchSomeActivity.launch(i);
    }


    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                imageUri = selectedImageUri;
            }
        }
    });



    private String getFileExtension(Uri imageUri) {
        ContentResolver cv = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cv.getType(imageUri));
    }


}