package com.example.bordia98.notes;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class notesactivity extends AppCompatActivity  {

        private RecyclerView mynoteslist;
        private GridLayoutManager mygrid;
        private DatabaseReference notesdatabase;
        FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);
        mygrid = new GridLayoutManager (this,2,GridLayoutManager.VERTICAL,false);
        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("YOUR NOTES");
        setSupportActionBar(toolbar);
        mauth = FirebaseAuth.getInstance();
        mynoteslist = (RecyclerView)findViewById(R.id.main_noteslist);
        mynoteslist.setHasFixedSize(true);
        mynoteslist.setLayoutManager(mygrid);
        mynoteslist.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        notesdatabase = FirebaseDatabase.getInstance().getReference().child("notes").child(mauth.getCurrentUser().getUid());
        onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menunotes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){
            case R.id.logout:{
                logout();
                return true;
            }
            case R.id.addnote:{
                addnote();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void addnote() {
        Intent i = new Intent(getApplicationContext(),writing.class);
        startActivity(i);
    }

    private void logout() {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();

        final Query query = notesdatabase.orderByChild("Time");
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseRecyclerAdapter<notemodel,note_view_holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<notemodel, note_view_holder>(

                        notemodel.class,
                        R.layout.simple_note_layout,
                        note_view_holder.class,
                        query

                ) {
                    @Override
                    protected void populateViewHolder(final note_view_holder viewHolder, notemodel model, int position) {

                        final String noteid = getRef(position).getKey();
                        Log.d("user",noteid);
                        notesdatabase.child(noteid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("Title") && dataSnapshot.hasChild("Time")) {
                                    String title = dataSnapshot.child("Title").getValue().toString();
                                    String timestamp = dataSnapshot.child("Time").getValue().toString();
                                    Log.d("user", title);
                                    viewHolder.setNoteTitle(title);
                                    gettime to = new gettime();
                                    viewHolder.setNoteTime(to.gettime(Long.parseLong(timestamp), getApplicationContext()));

                                    viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(getApplicationContext(), writing.class);
                                            i.putExtra("noteid", noteid);
                                            startActivity(i);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                };

                mynoteslist.setAdapter(firebaseRecyclerAdapter);

            }
        });

        th.start();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
