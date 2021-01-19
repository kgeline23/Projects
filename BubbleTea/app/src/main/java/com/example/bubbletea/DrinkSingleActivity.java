package com.example.bubbletea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bubbletea.Adapters.CommentListAdapter;
import com.example.bubbletea.Classes.Comment;
import com.example.bubbletea.Classes.Drink;
import com.example.bubbletea.Classes.Category;
import com.example.bubbletea.Classes.Store;
import com.example.bubbletea.Classes.Type;
import com.example.bubbletea.Fragments.CommentAddFragment;
import com.example.bubbletea.Fragments.CommentListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DrinkSingleActivity extends AppCompatActivity {

    FrameLayout fragListComm, fragAddComm;
    TextView tvStore, tvName, tvStoreName, tvType, tvCategory, tvPrice, tvSugar, tvTemp, tvBoba, tvExtra;
    Button btOpenAdd, btAddComm;
    Drink currentDrink;
    public static String drink_id;
    FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth;
    Boolean extra = false;
    EditText etExtra;
    PopupWindow popup;
    RelativeLayout rlPop;
    ConstraintLayout clDrink;
    ListView lvComments;
    CommentListAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_single);
        tvStore = findViewById(R.id.tvStoreName);
        fragAddComm = findViewById(R.id.fragComAdd);
        fragListComm = findViewById(R.id.fragComList);
        btOpenAdd = findViewById(R.id.btnAddComFragment); //opens comment fragment
        btAddComm = findViewById(R.id.btnAddCom); //pushes data into DB
        lvComments = findViewById(R.id.lvComments);
        database = FirebaseDatabase.getInstance();
        cAdapter = new CommentListAdapter(DrinkSingleActivity.this, R.layout.row_comment);

        //get passed parcelable
        final Intent i = getIntent();
        currentDrink = i.getExtras().getParcelable("data");
        drink_id = currentDrink.id;
        fillDetails();

        //go to single store page
        tvStore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(DrinkSingleActivity.this, StoreSingleActivity.class);
                i.putExtra("storeId", currentDrink.store_id);
                startActivity(i);
            }
        });

        //list of comment show on create
        fragListComm.setVisibility(View.VISIBLE);
        loadComments();

        //hide add comment
        fragAddComm.setVisibility(View.INVISIBLE);

        //show add comment
        btOpenAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fragAddComm.setVisibility(View.VISIBLE);
                fragListComm.setVisibility(View.INVISIBLE);
            }
        });

        //check if extra is selected, if yes then show  edit text box
        Switch sExtra = findViewById(R.id.switchExtra);
        etExtra = findViewById(R.id.etExtra);
        sExtra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    etExtra.setVisibility(View.VISIBLE);
                    extra = true;
                }
                else
                {
                    etExtra.setVisibility(View.INVISIBLE);
                    extra = false;
                }
            }
        });

        btAddComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrinkSingleActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                addComment();
                loadComments();
            }
        });

        //Listener for comment list
        lvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Comment com = ((Comment) cAdapter.getItem(position));
                showPopup(com);
            }
        });
    }

    void fillDetails()
    {
        tvName = findViewById(R.id.tvStore);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvType = findViewById(R.id.tvDrinkType);
        tvCategory = findViewById(R.id.tvStoreTime);
        tvPrice = findViewById(R.id.tvDrinkPrice);
        tvName = findViewById(R.id.tvStore);

        tvName.setText(currentDrink.name);
        String price = "" + currentDrink.price;
        tvPrice.setText(currentDrink.price);

        database.getReference("categories/"+currentDrink.category_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Category c = snapshot.getValue(Category.class);
                    tvCategory.setText(c.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference("type/"+currentDrink.type_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Type t = snapshot.getValue(Type.class);
                    tvType.setText(t.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference("stores/"+currentDrink.store_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Store s = snapshot.getValue(Store.class);
                    tvStoreName.setText(s.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void addComment()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();

        Switch sSugar = findViewById(R.id.switchSugar);
        RadioGroup rgTemp = findViewById(R.id.rbTemp);
        RadioButton rbTemp, rbBobba;
        RadioGroup rgBobba = findViewById(R.id.rbBoba);
        Toast.makeText(DrinkSingleActivity.this, "get info", Toast.LENGTH_SHORT).show();

        //get sugar bool
        boolean sugar = sSugar.isChecked();

        //get temperature answer
        int tempId = rgTemp.getCheckedRadioButtonId();
        rbTemp = findViewById(tempId);
        String temp = rbTemp.getText().toString();

        //get bobba answer
        int bobbaId = rgBobba.getCheckedRadioButtonId();
        rbBobba = findViewById(bobbaId);
        String bobba = rbBobba.getText().toString();

        //get current user id
        String user =  mFirebaseAuth.getInstance().getCurrentUser().getEmail();

        //get current drink id
        String drinkId = DrinkSingleActivity.drink_id;

        //che if extra was added
        Comment curComm;

        if (extra) {
            curComm = new Comment(user, drinkId, temp, bobba, etExtra.getText().toString(), sugar);
            Toast.makeText(DrinkSingleActivity.this, "with extra", Toast.LENGTH_LONG).show();

        } else
        {
            curComm = new Comment(user, drinkId,temp, bobba, sugar);
            Toast.makeText(DrinkSingleActivity.this, "without", Toast.LENGTH_LONG).show();

        }


        FirebaseDatabase.getInstance().getReference("comments").push().setValue(curComm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DrinkSingleActivity.this, "Comment Added", Toast.LENGTH_LONG).show();
                        //list of comment show on create
                        fragListComm.setVisibility(View.VISIBLE);
                        //hide add comment
                        fragAddComm.setVisibility(View.INVISIBLE);
                    }
                });


    }

    void loadComments()
    {
        ListView lvComments = findViewById(R.id.lvComments);
        lvComments.setAdapter(cAdapter);

        cAdapter.clear();
        cAdapter.notifyDataSetChanged();

        Query q = database.getReference("comments").orderByChild("drink_id").equalTo(drink_id);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Comment curCom = ds.getValue(Comment.class);
                        curCom.id = ds.getKey();
                        cAdapter.add(curCom);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    void showPopup(Comment com)
    {
        clDrink = findViewById(R.id.clDrink);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.popup_comment,null);

        // Initialize a new instance of popup window
        popup = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(400);

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            popup.setElevation(5.0f);
        }
        tvSugar = customView.findViewById(R.id.tvSugar);
        tvTemp = customView.findViewById(R.id.tvTemp);
        tvBoba = customView.findViewById(R.id.tvBoba);
        tvExtra = customView.findViewById(R.id.tvExtra);

        if(com.enough_sugar)
            tvSugar.setText("Yes");
        else tvSugar.setText("No");

        if(com.extra != null)
            tvExtra.setText(com.extra);

        tvTemp.setText(com.better_temp);
        tvBoba.setText(com.topping);

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ibClose);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                popup.dismiss();
            }
        });

        // Finally, show the popup window at the center location of root relative layout
        popup.showAtLocation(clDrink, Gravity.CENTER,0,0);
    }

    void startAddFrag()
    {
        Intent intent = new Intent(this, CommentAddFragment.class);
        startActivity(intent);
    }

    void startListFrag()
    {
        Intent intent = new Intent(this, CommentListFragment.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.action_drinks:
                //TODO: pass with store id null
                Intent i = new Intent(this, DrinkListActivity.class);
                i.putExtra("store_id", "0");
                startActivity(i);
                return true;
            case R.id.action_store:
                startActivity(new Intent(this, StoreListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}