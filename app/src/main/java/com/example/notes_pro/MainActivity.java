package com.example.notes_pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
FloatingActionButton addNoteButton;
RecyclerView recyclerView;
ImageButton menuBtn;
NoteAdapter noteAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteButton=findViewById(R.id.add_note_btn);
        recyclerView=findViewById(R.id.recycler_view);
        menuBtn=findViewById(R.id.menu_btn);

        addNoteButton.setOnClickListener((v)->startActivity(new Intent(MainActivity.this,notesDetails.class)));

        menuBtn.setOnClickListener((v)->showMenu());

        setupRecyclerView();

    }

    void showMenu()
    {
        //displays menu
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout")
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,Login_Activity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    void setupRecyclerView(){
        Query query = utility.getCollectionReferencesForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter=new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}