package com.example.newenvi;

import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;


import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.newenvi.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;




public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;
    private ActivityMainBinding binding;
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageUri = createUri();
        registerPictureLauncher();

        binding.btnTakePicture.setOnClickListener(view -> {
        checkCameraPermissionAndOpenCamera();
                });

        /*binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_MainActivity_to_FirstFragment);
            }
        });*/
        Button myButton = findViewById(R.id.button);
       myButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              myButton.setVisibility(View.GONE);
              findViewById(R.id.btnTakePicture).setVisibility(View.GONE);
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
             Fragment firstFragment = new FirstFragment();
             transaction.replace(R.id.container, firstFragment);
             transaction.addToBackStack(null);
             transaction.commit();
      }
   });
    }



private Uri createUri(){
    File imageFile = new File(getApplicationContext().getFilesDir(),"camera_photos.jpg");
    return FileProvider.getUriForFile(getApplicationContext(), "com.example.newenvi.fileProvider", imageFile);
}
private void registerPictureLauncher(){
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                         try {
                             if(result){
                                 binding.ivUser.setImageURI(null);
                                 binding.ivUser.setImageURI(imageUri);
                             }
                         }catch (Exception exception) {
                             exception.getStackTrace();
                         }
                    }
                }
        );
}
public void checkCameraPermissionAndOpenCamera(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE );
        }else {
            takePictureLauncher.launch(imageUri);
        }
}
@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 takePictureLauncher.launch(imageUri);
            } else{
                Toast.makeText(this, "Camera permission denied, please allow permission to take a picture.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}