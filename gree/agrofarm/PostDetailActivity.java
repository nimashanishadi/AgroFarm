package com.example.agrofarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agrofarm.Adapters.CommentAdapter;
import com.example.agrofarm.Models.Comment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost;
    TextView txtPostDesc,txtPostDateName,txtPostTitle;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;
    String date;
    String postTitle;
    String postDescription;
    String key1;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef ;

    Firebase url;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //status bar transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getSupportActionBar().hide();


        url = new Firebase("https://agrofarm-cd23b.firebaseio.com/");
        //ini views
        RvComment = findViewById(R.id.rv_comment);
        imgPost = findViewById(R.id.post_detail_img);

        txtPostTitle = findViewById(R.id.post_detal_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_details_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);

        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Firebase.setAndroidContext(this);

        //add comment button cIick function
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAddComment.setVisibility(View.INVISIBLE);
                //DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(PostKey).push();
                firebaseAuth = FirebaseAuth.getInstance();
                String email = firebaseAuth.getCurrentUser().getEmail();
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();

                //PostKey = url.getKey() ;


                Comment comment = new Comment(comment_content,uid,email);
                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("Posts");

                //String key = myRef.push().getKey();
                ///////


                //String key = myRef.child("Posts").push().getKey();
                ///////

                Firebase sdsdsd =url.child("Comment").child(key1) ;

                sdsdsd.push().setValue(comment);




                Toast.makeText(PostDetailActivity.this, "Your Post successfully added."+ email+key1, Toast.LENGTH_SHORT).show();

                editTextComment.setText(" ");
                btnAddComment.setVisibility(View.VISIBLE);
            }
        });

        //bind all data into those views
        //TODO get post data
        //send post detail data to this activity first

        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

        key1 = getIntent().getExtras().getString("postKey");

        PostKey = getIntent().getExtras().getString("userId");

        date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);

        //
        //ini recycle view comment
        iniRvComment();

    }

    private void iniRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));
        //DatabaseReference commentref = firebaseDatabase.getReference("Comment").child(key1);
        Firebase sdsdsd =url.child("Comment").child(key1) ;
        sdsdsd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);

                }

                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private String timestampToString(long time){

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }
}
