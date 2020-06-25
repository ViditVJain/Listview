package com.example.example1;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import java.util.ArrayList;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends Activity
{
    private MyAdapter myAdapter;
    private ListView myList;
    private FloatingActionButton fabutton;
    public ArrayList<ListItem> listOFItems;
    public String[] a = {"Gloves", "Jersey", "Studs"};

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myList = (ListView) findViewById(R.id.MyList);
        myList.setItemsCanFocus(true);
        listOFItems=new ArrayList<>();
        addDataInTOArray();

        fabutton = (FloatingActionButton) findViewById(R.id.fb);
        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.customdialog);

                Button dialogButton1 = (Button) dialog.findViewById(R.id.No);
                dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button dialogButton2 = (Button) dialog.findViewById(R.id.Yes);
                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ListItem TemplistItem=new ListItem();
                        TemplistItem.image1 = R.drawable.mu;
                        String textdescription = ((EditText)dialog.findViewById(R.id.fortext)).getText().toString();
                        TemplistItem.text1 = " " + textdescription;

                        listOFItems.add(TemplistItem);
                        myAdapter.refreshAdapter();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(new ListViewAdapter(myList), new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>()
        {
            @Override
            public boolean canDismiss(int position) { return true; }
            @Override
            public void onDismiss(ListViewAdapter view, int position) { myAdapter.remove(position); }
        });
        myList.setOnTouchListener(touchListener);
        myList.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (touchListener.existPendingDismisses()) { touchListener.undoPendingDismiss(); }
                else {
                    Toast.makeText(MainActivity.this, "Position " + position, LENGTH_SHORT).show(); }
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context context;
        private  ArrayList<ListItem> myItems;

        public MyAdapter(Context context, ArrayList<ListItem> listOFItems) {
            this.context = context;
            this.myItems = listOFItems;
        }

        public void remove(int position) {
            myItems.remove(position);
            notifyDataSetChanged();
        }

        public void refreshAdapter(){ notifyDataSetChanged(); }

        @Override
        public int getCount() {
           // return myItems.size();
            return  myItems != null? myItems.size() : 0;
        }

        @Override
        public Object getItem(int position) { return position; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder  holder=null;
            if (convertView == null) {

                LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item, parent,false);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.textView);
                holder.image1 = (ImageView) convertView.findViewById(R.id.imageView);
                holder.caption = (EditText) convertView.findViewById(R.id.ItemCaption);
                holder.spinner = (Spinner) convertView.findViewById(R.id.spin);
                holder.rootLay = (RelativeLayout) convertView.findViewById(R.id.root_lay);
                holder.cb = (CheckBox) convertView.findViewById(R.id.cbox);
                holder.rb = (RadioButton) convertView.findViewById(R.id.rbutton);

                convertView.setTag(holder);
            }
            else { holder = (ViewHolder) convertView.getTag(); }

            ListItem item = myItems.get(position);
            holder.text1.setText(item.getText1());
            holder.image1.setImageResource(item.getImage1());

            if (myItems.get(position).isChcekboxChecked()){ holder.cb.setChecked(true); }
            else { holder.cb.setChecked(false); }
            if (myItems.get(position).isRadioChecked()){ holder.rb.setChecked(true); }
            else { holder.rb.setChecked(false); }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, a);
            holder.spinner.setAdapter(adapter);

            holder.caption.setText(item.getCaption());
            holder.caption.setId(position);

            holder.rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((RadioButton) v).isChecked()) { myItems.get(position).setRadioChecked(true); }
                    else { myItems.get(position).setRadioChecked(false); }
                }
            });

            holder.cb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) { myItems.get(position).setChcekboxChecked(true); }
                    else { myItems.get(position).setChcekboxChecked(false); }
                }
            });

//            holder.rootLay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v){
//                        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
//                        al.setMessage("Delete the Item?").setCancelable(false).setPositiveButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
//                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                myItems.remove(position);
//                                myAdapter.notifyDataSetChanged();
//                            }});
//                        AlertDialog alert=al.create();
//                        alert.setTitle("Action");
//                        alert.show();
//                    }
//            });

            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ((ListItem) myItems.get(position)).caption = Caption.getText().toString();
                    }
                }
            });

            holder.spinner.setSelection(item.getSpinnerIndex());
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    myItems.get(position).setSpinnerIndex(arg0.getSelectedItemPosition());
                }
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        EditText caption;
        TextView text1;
        ImageView image1;
        Spinner spinner;
        RelativeLayout rootLay;
        CheckBox cb;
        RadioButton rb;
    }

    class ListItem {
        private String text1;
        private int image1;
        private String caption;
        private int spinnerIndex;
        private boolean isChcekboxChecked;
        private boolean isRadioChecked;

        public int getSpinnerIndex() {
            return spinnerIndex;
        }

        public void setSpinnerIndex(int spinnerIndex) {
            this.spinnerIndex = spinnerIndex;
        }

        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        public int getImage1() {
            return image1;
        }

        public void setImage1(int image1) {
            this.image1 = image1;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) { this.caption = caption; }

        public boolean isChcekboxChecked() { return isChcekboxChecked; }

        public void setChcekboxChecked(boolean chcekboxChecked) { isChcekboxChecked = chcekboxChecked; }

        public boolean isRadioChecked() { return isRadioChecked; }

        public void setRadioChecked(boolean radioChecked) { isRadioChecked = radioChecked; }
    }


    public void addDataInTOArray(){
        for (int i=1;i<51;i++) {
            ListItem listItem = new ListItem();
            listItem.setRadioChecked(false);
            listItem.setChcekboxChecked(false);
            listItem.setCaption("");
            //set Image
            if (i == 1 || i == 11 || i == 21 || i == 31 || i == 41) { listItem.image1 = R.drawable.mu; }
            else if (i == 2 || i == 12 || i == 22 || i == 32 || i == 42) { listItem.image1 = R.drawable.mc; }
            else if (i == 3 || i == 13 || i == 23 || i == 33 || i == 43) { listItem.image1 = R.drawable.liv; }
            else if (i == 4 || i == 14 || i == 24 || i == 34 || i == 44) { listItem.image1 = R.drawable.th; }
            else if (i == 5 || i == 15 || i == 25 || i == 35 || i == 45) { listItem.image1 = R.drawable.ch; }
            else if (i == 6 || i == 16 || i == 26 || i == 36 || i == 46) { listItem.image1 = R.drawable.ar; }
            else if (i == 7 || i == 17 || i == 27 || i == 37 || i == 47) { listItem.image1 = R.drawable.bar; }
            else if (i == 8 || i == 18 || i == 28 || i == 38 || i == 48) { listItem.image1 = R.drawable.ajax; }
            else if (i == 9 || i == 19 || i == 29 || i == 39 || i == 49) { listItem.image1 = R.drawable.bm; }
            else if (i == 10 || i == 20 || i == 30 || i == 40 || i == 50) { listItem.image1 = R.drawable.juve; }
            listItem.setText1(""+i);
            listOFItems.add(listItem);
        }
        myAdapter=new MyAdapter(MainActivity.this,listOFItems);
        myList.setAdapter(myAdapter);
    }
}