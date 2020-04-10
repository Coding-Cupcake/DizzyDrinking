package com.patrick.dizzydrinking;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class ContactActivity extends ListActivity {

    public static ArrayList<String> contactList;
    public static ArrayList<String> partialContactList;

    public static String[] allContacts;
    public static String[] chosenContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);

        //Set the font
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/steelfish_rg.ttf");

        partialContactList = new ArrayList<>();
        final ArrayList<String> currentUserList;

        contactList = new ArrayList<>();
        Cursor phones;

        phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (!contactList.contains(name))
                contactList.add(name);

        }
        phones.close();

        String[] list = new String[contactList.size()];
        for (int i = 0; i < contactList.size(); i++) {

            list[i] = contactList.get(i);

        }
        allContacts = list;

        if (getIntent().getStringArrayExtra(("ownList")) != null)
            currentUserList = new ArrayList<>(Arrays.asList(getIntent().getStringArrayExtra(("ownList"))));
        else currentUserList = new ArrayList<>();
        for (String currentUser : currentUserList) {
            for (int i = 0; i < allContacts.length; i++) {
                if (currentUser.equals(allContacts[i])) {
                    partialContactList.add(currentUser);
                }

            }
        }

        final ListView listview = getListView();
        listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        listview.setTextFilterEnabled(true);
        Arrays.sort(allContacts);


        //Choose Names
        Button chooseNames = (Button) findViewById(R.id.chooseNames);
        chooseNames.setTypeface(myFont);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, allContacts));

        final ArrayList<String> currentUserList2;
        if (getIntent().getStringArrayExtra(("ownList")) != null)
            currentUserList2 = new ArrayList<>(Arrays.asList(getIntent().getStringArrayExtra(("ownList"))));
        else currentUserList2 = new ArrayList<>();

        //Check already selected users
        for (String currentlyExistingEntry : currentUserList2) {
            for (int i = 0; i < allContacts.length; i++) {
                if (currentlyExistingEntry.equals(allContacts[i]))
                    listview.setItemChecked(i, true);
            }
        }


        //Send Button
        ImageButton sendContacts = (ImageButton) findViewById(R.id.sendContacts);
        sendContacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<String> currentUserList;
                ArrayList<String> toBeRemoved = new ArrayList<>();

                if (getIntent().getStringArrayExtra(("ownList")) != null)
                    currentUserList = new ArrayList<>(Arrays.asList(getIntent().getStringArrayExtra(("ownList"))));
                else currentUserList = new ArrayList<>();

                for (String currentUser : currentUserList) {
                    for (int i = 0; i < allContacts.length; i++) {
                        if (currentUser.equals(allContacts[i]) && !listview.isItemChecked(i)) {
                            toBeRemoved.add(currentUser);
                        }

                    }
                }

                currentUserList.removeAll(toBeRemoved);


                if (currentUserList != null) {
                    for (String user : currentUserList) {
                        if (!partialContactList.contains(user))
                            partialContactList.add(user);
                    }
                }

                chosenContacts = new String[partialContactList.size()];

                for (int i = 0; i < partialContactList.size(); i++)
                    chosenContacts[i] = partialContactList.get(i);

                Arrays.sort(chosenContacts);

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.putExtra("resultList", chosenContacts);
                startActivity(intent);

            }
        });


        //Cancel Button
        ImageButton cancelContacts = (ImageButton) findViewById(R.id.cancelList);
        cancelContacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.putExtra("resultList", getIntent().getStringArrayExtra(("ownList")));
                startActivity(intent);

            }


        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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

    public void onListItemClick(ListView parent, View v, int position, long id) {

        CheckedTextView item = (CheckedTextView) v;

        if (!(partialContactList.contains(item.getText().toString()))) {
            partialContactList.add(item.getText().toString());
        } else {
            partialContactList.remove(item.getText().toString());
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }
}

