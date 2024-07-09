package com.example.organaizer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.AppDatabase;
import com.example.organaizer.data.db.classes.User;
import com.example.organaizer.data.db.interfaces.UserDao;
import com.example.organaizer.ui.dialogFragments.CustomDialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        registerButton = findViewById(R.id.register_button);

        // Подключение к базе данных
        AppDatabase db = App.getInstance().getDatabase();

        // Получение экземпляра интерфейса DAO
        UserDao userDao = db.userDao();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение данных для входа из полей ввода
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^_-])[A-Za-z0-9!@#$%^_-]{6,}$");
                Matcher matcher = pattern.matcher(password);
                // Выполнение запроса к базе данных для регистрации пользователя
                if (matcher.find()){
                    userDao.registerUser(new User(username, password));
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    CustomDialogFragment dialog = new CustomDialogFragment("Внимание", "Пароль слишком легкий");
                    dialog.show(getSupportFragmentManager(), "custom");
                }
            }
        });
    }
}