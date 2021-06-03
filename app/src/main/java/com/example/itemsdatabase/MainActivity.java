package com.example.itemsdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import static com.example.itemsdatabase.DBHelper.DATABASE_NAME;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase rDatabase;
    private Adapter rAdapter;
    private EditText rEditTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("My items Database");



        DBHelper dbHelper = new DBHelper(this);
        rDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rAdapter = new Adapter(this, getAllItems());
        recyclerView.setAdapter(rAdapter);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);


        rEditTextName = findViewById(R.id.edittext_name);

        Button buttonAdd = findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(v -> addItem());


        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_EMAIL, new String[] {"example@domain.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "BackUp Of Personal Details");
            i.putExtra(Intent.EXTRA_TEXT, "Attached below is a back up of your personal details");
            i.setType("application/octet-stream");
            i.putExtra(Intent.EXTRA_STREAM,(getDatabasePath(DATABASE_NAME)));
            startActivity(Intent.createChooser(i, "Send e-mail"));

        });
    }

    private void addItem() {
        if (rEditTextName.getText().toString().trim().length() == 0) {
            return;
        }
        String name = rEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(Contracter.RecordsEntry.COLUMN_NAME, name);
        rDatabase.insert(Contracter.RecordsEntry.TABLE_NAME, null, cv);
        rAdapter.swapCursor(getAllItems());

        rEditTextName.getText().clear();
    }
    private void removeItem(long id) {
        rDatabase.delete(Contracter.RecordsEntry.TABLE_NAME,
                Contracter.RecordsEntry._ID + "=" + id, null);
        rAdapter.swapCursor(getAllItems());
    }


    private Cursor getAllItems() {
        return rDatabase.query(
                Contracter.RecordsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contracter.RecordsEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

}