package com.example.organaizer.ui.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.AppDatabase;
import com.example.organaizer.data.db.classes.User;
import com.example.organaizer.data.db.interfaces.UserDao;
import com.example.organaizer.ui.dialogFragments.CustomDialogFragment;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTextView = findViewById(R.id.forgot_password_text_view);

        // Подключение к базе данных
        AppDatabase db = App.getInstance().getDatabase();

        // Получение экземпляра интерфейса DAO
        UserDao userDao = db.userDao();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение данных для входа из полей ввода
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Выполнение запроса к базе данных для проверки подлинности пользователя
                User user = userDao.authenticateUser(username, password);

                // Проверка результата запроса
                if (user != null) {
                    App.setUser(user);
                    Intent intent = new Intent(getBaseContext(), MainUserActivity.class);
                    startActivity(intent);
                } else {
                    CustomDialogFragment dialog = new CustomDialogFragment("Внимание", "Данные неверны");
                    dialog.show(getSupportFragmentManager(), "custom");
                }
            }
        });

        // Обработка логики восстановления пароля
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}