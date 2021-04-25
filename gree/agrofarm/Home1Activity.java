package com.example.agrofarm;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.agrofarm.Fragments.AboutusFragment;
import com.example.agrofarm.Fragments.DashboardFragment;
import com.example.agrofarm.Fragments.HomeFragment;
import com.example.agrofarm.Fragments.QuizFragment;
import com.example.agrofarm.Models.Post;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
//import android.app.FragmentManager;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Home1Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference myRef ;
    FirebaseAuth mAuth;
    FirebaseUser user;
    //FirebaseDatabase db = FirebaseDatabase.getInstance();
    Firebase url;



    private static final int REQUESTCODE =2 ;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private static final int PReqCode= 2;
    Dialog popAddPost;
    ImageView popupUserImage,popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    private String userID;
    long maxId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //////////////////
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        ///////////////////


        //ini popup
        iniPopup();
        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                popAddPost.show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        url = new Firebase("https://agrofarm-cd23b.firebaseio.com/");

        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fragmentManager.beginTransaction().replace(R.id.content_frame,new DashboardFragment()).commit();
        navigationView.setNavigationItemSelectedListener(this);
        /*
        if (savedInstanceState == null){
        fragmentManager.beginTransaction().replace(R.id.content_frame,new FirstFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_dashboard);
        }
         */

    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here we need open the gallery
                checkAndRequestForPermission();
            }
        });
    }
    private void checkAndRequestForPermission(){
        if (ContextCompat.checkSelfPermission(Home1Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home1Activity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(Home1Activity.this,"Please accept the required permission",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(Home1Activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else {
            openGallery();  //EVERYTHING GOES WELL WELL, WE HAVE PERMISSION TO ACCESS USER GALLERY
        }

    }

    private void openGallery() {
        // wait user to pick up an image
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== RESULT_OK && requestCode== REQUESTCODE && data != null){
            pickedImgUri = data.getData();
            popupPostImage.setImageURI(pickedImgUri);
            
        }
    }


    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //ini popup widgets
        popupPostImage = popAddPost.findViewById(R.id.imageView3);
        popupTitle = popAddPost.findViewById(R.id.textView_Title);
        popupDescription = popAddPost.findViewById(R.id.textView_description);
        popupAddBtn = popAddPost.findViewById(R.id.imageView4);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);


        Firebase.setAndroidContext(this);




        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                if (!popupTitle.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty() && pickedImgUri != null){
                    //save the image in the firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    StorageReference imageFiePath = storageReference.child(pickedImgUri.getLastPathSegment());


                    imageFiePath.putFile(pickedImgUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    imageFiePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                           //myRef =FirebaseDatabase.getInstance().getReference().child("Posts");
                                            String imageDownloadLink = uri.toString();
                                            //get the email of thr current user
                                            mAuth = FirebaseAuth.getInstance();
                                            String email = mAuth.getCurrentUser().getEmail();
                                            String title = popupTitle.getText().toString();
                                            String descr = popupDescription.getText().toString();

                                            //myRef.child("Posts").child(userID).setValue(post);
                                            //myRef = database.getReference();
                                            //String key = myRef.getKey();
                                            //post.setPostKey(key);

                                            //working part
                                            /*
                                            Firebase mfire = url.child("Posts") ;
                                            mfire.push().setValue(post);*/

                                            Firebase mfire = url.child("Posts") ;
                                            String key = mfire.push().getKey();
                                            Post post = new Post(title,descr,imageDownloadLink,email,key);
                                            mfire.child(key).setValue(post);
                                            post.setPostKey(key);




                                            Toast.makeText(Home1Activity.this, "Your Post successfully added."+ key, Toast.LENGTH_SHORT).show();
                                            popupClickProgress.setVisibility(View.INVISIBLE);
                                            popupAddBtn.setVisibility(View.VISIBLE);
                                            popAddPost.dismiss();

                                        }
                                    });


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Home1Activity.this, "Failed to add you post ", Toast.LENGTH_SHORT).show();
                                }
                            });




                }
                else {
                    showMessage("Please verity all input fields and choose Post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });


    }



    private void showMessage(String message) {
        Toast.makeText(Home1Activity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_dashboard) {
            getSupportActionBar().setTitle("Dashboard");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new DashboardFragment()).commit();

        } else if (id == R.id.nav_aboutus) {
            getSupportActionBar().setTitle("About Us");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new AboutusFragment()).commit();
        } else if (id == R.id.nav_needhelp) {
            getSupportActionBar().setTitle("Need Help ?");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new QuizFragment()).commit();
        } else if (id == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new HomeFragment()).commit();
        } else if (id == R.id.nav_send) {

        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
