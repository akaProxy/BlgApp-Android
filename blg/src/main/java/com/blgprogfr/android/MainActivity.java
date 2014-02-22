package com.blgprogfr.android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity implements OnFragmentInteractionListener{
    private DrawerLayout mDrawerLayout;
    private String[] mNavTexts;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment currentFragmentShowing;

    private ArrayList<Item> items = new ArrayList<Item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            currentFragmentShowing = new PlaceholderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, currentFragmentShowing)
                    .commit();
        }
        initDrawer();

    }
    private void initDrawer(){
        SectionItem timeSection = new SectionItem("Skoltid");
        String[] timeStuff = getResources().getStringArray(R.array.nav_time_stuff);

        SectionItem socialSection = new SectionItem("Blackebergs flöden");
        String[] socialStuff = getResources().getStringArray(R.array.nav_social_stuff);

        SectionItem foreningSection = new SectionItem("Föreningar");
        String[] foreningStuff = getResources().getStringArray(R.array.nav_foreningar_stuff);

        SectionItem karenSection = new SectionItem("Kåren");
        String[] karenStuff = getResources().getStringArray(R.array.nav_karen_stuff);

        writeIntoAdapter(items, timeSection, timeStuff);
        writeIntoAdapter(items, socialSection, socialStuff);
        writeIntoAdapter(items, foreningSection, foreningStuff);
        writeIntoAdapter(items, karenSection, karenStuff);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.open_drawer,
                R.string.close_drawer
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mNavTexts = getResources().getStringArray(R.array.nav_texts);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new MyNavAdapter(this, items));

        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNavTexts));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void writeIntoAdapter(ArrayList<Item> items, SectionItem sectionItem, String[] stuff) {
        items.add(sectionItem);
        for (int i = 0; i < stuff.length; i++){
            items.add(new EntryItem(stuff[i]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void selectItem(int position){
        FragmentManager tm = getFragmentManager();
        switch(position){

            case 1:
                currentFragmentShowing = new ScheduleFragment();
                break;

            case 2:
                currentFragmentShowing = new PlaceholderFragment();
                break;

            case 3:
                currentFragmentShowing = new PlaceholderFragment();
                break;

            case 5:
                currentFragmentShowing = new SocialFragment();

            case 6:
                break;

            case 8:
                currentFragmentShowing = new AssociationFragment();
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                break;

            case 15:
                currentFragmentShowing = new VoteFragment();
            break;


        }
        tm.beginTransaction().replace(R.id.container, currentFragmentShowing).commit();
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }



    /* ------------ Allt vi behöver för navigationen ------------ */

    /**
     * Det här är ett interface som förklarar Item. Denna behövs i vår adapter.
     */
    private interface Item{
        boolean isSection();
    }

    /**
     * Det här är en section till navigationen. Inget speciellt här.
     * Vi säger helt enkelt att det här itemet är en section och sätter en titel.
     */
    private class SectionItem implements Item{
        String title;
        SectionItem(String title){
            if (title != null){
                this.title = title;
            } else {
                this.title = "";
            }
        }
        @Override
        public boolean isSection() {
            return true;
        }
    }

    /**
     * Förklarar en vanlig text till navigationen.
     * Den berättar att den inte är en section och sätter en titel.
     */
    private class EntryItem implements Item{
        String title;
        EntryItem(String title){
            this.title = title;
        }
        @Override
        public boolean isSection() {
            return false;
        }
    }

    /**
     * Det här en adapter som kollar om det är ett SectionItem eller EntryItem som
     * vi lägger till, och anpassar vilken layout därefter.
     */
    private class MyNavAdapter extends ArrayAdapter<Item>{
        private Context context;
        private ArrayList<Item> items;
        private LayoutInflater li;
        MyNavAdapter(Context context, ArrayList<Item> items){
            super(context, 0, items);
            this.items = items;
            this.context = context;
            li = (LayoutInflater)getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        /**
         * Vi vill inte att SectionItems ska vara klickbara
         * @param position är positionen i vår lista vi hämtar vår Item ifrån
         * @return boolean om det är en sektion eller inte. Sektion innebär false.
         */
        @Override
        public boolean isEnabled(int position) {
            return !items.get(position).isSection();
        }

        @Override
        public boolean areAllItemsEnabled () {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Item i = items.get(position);
            View view = convertView;
            if (i != null){
                /*
                * Den här delen förklarar nästan sig självt. Den kollar vårt items på bestämd position är en
                * SectionItem eller inte, med Items interface.
                */

                if(i.isSection()){
                    SectionItem si = (SectionItem) i;
                    view = li.inflate(R.layout.navigation_section, null);
                    TextView sectionText = (TextView) view.findViewById(R.id.nav_section);
                    sectionText.setText(si.title);
                } else {
                    EntryItem ei = (EntryItem) i;
                    view = li.inflate(R.layout.navigation_list_item, null);
                    TextView entryText = (TextView) view.findViewById(R.id.nav_item);
                    entryText.setText(ei.title);
                }
            }
            return view;
        }
    }
}
