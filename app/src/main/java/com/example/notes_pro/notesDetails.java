package com.example.notes_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import io.grpc.okhttp.internal.Util;

public class notesDetails extends AppCompatActivity {
EditText titleEdt,contentEdt;
ImageButton saveNote;
TextView pageTitle;
ImageView deleteN;
String title,content,docId;
boolean isEditMode=false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        titleEdt=findViewById(R.id.notes_title_edt);
        contentEdt=findViewById(R.id.content_edt);
        saveNote=findViewById(R.id.save_note_btn);
        pageTitle=findViewById(R.id.page_title);
        deleteN=findViewById(R.id.delete_igv);

        //Receiving the data
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty())
        {
            isEditMode=true;
        }


        titleEdt.setText(title);
        contentEdt.setText(content);

        if(isEditMode)
        {
            pageTitle.setText("Edit Your Note");
            deleteN.setVisibility(View.VISIBLE);
        }

        saveNote.setOnClickListener((v)->saveNotes());
        deleteN.setOnClickListener((v)->deleteNotesFromFirebase());
    }

    void saveNotes(){
        String noteTitle=titleEdt.getText().toString();
        String contentText=contentEdt.getText().toString();
        if(contentText==null||contentText.isEmpty())
        {
            contentEdt.setError("Cannot save empty note");
            return;
        }
         Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(contentText);
        note.setTimestamp(Timestamp.now());

        saveNoteToFireBase(note);
    }

    void saveNoteToFireBase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //if in edit mode then it will update it
            documentReference= utility.getCollectionReferencesForNotes().document(docId);
        }
        else{
            // if not in edit mode then it will create new nne
            documentReference= utility.getCollectionReferencesForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        //note added
                        utility.showtoast(notesDetails.this,"Note saved successfully");
                        finish();
                    }
                    else{
                        //not added
                        utility.showtoast(notesDetails.this,"Cannot save notes at the moment");
                    }
            }
        });
    }

    void deleteNotesFromFirebase(){
        DocumentReference documentReference;

        documentReference= utility.getCollectionReferencesForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //note deleted
                    utility.showtoast(notesDetails.this,"Note deleted successfully");
                    finish();
                }
                else{
                    utility.showtoast(notesDetails.this,"Failed delete notes at the moment");
                }
            }
        });
    }

}

