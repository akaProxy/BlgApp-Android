package com.johndaniel.novaviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by JohnDaniel on 2014-02-12.
 * This is a viev you can use to view a schedule for a certain day in novasoftwares schedule system.
 * Just tell this view the schoolid using {@link #setSchoolId(String)} to set the school, then a id for a class or personl schedule using
 * {@link #setId(String)} to view a schedule. The view has also support for special weeks {@link #setWeekNo(int)}
 * and period {@link #setPeriod(String)}
 */
public class NovaView extends WebView {
    String schoolId;
    int weekNo;
    Context context;
    String day = "";
    String schoolClass = "";
    int mode = 2;
    String period = ""; //If week left blank it will load the whole season.
    public static int viewWidth = 0;
    public static int viewHeight = 0;


    final String URL_PART_1 = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&";
    //After which you insert schooldid

    final String URL_PART_2 = "sv-se&type=-1&";
    //After which you insert class id, period, week, mode

    final String URL_PART_3 = "printer=0&colors=32&head=0&clock=0&foot=0&";
    //After which you insert day, width and height

    final String URL_PART_LAST = "maxwidth=3420&maxheight=3682"; //"maxwidth=1420&maxheight=682";


    final String SCHOOLID = "schoolid=";
    final String ID = "id=";
    final String PERIOD = "period=";
    final String WEEK = "week=";
    final String MODE = "mode=";
    final String DAY = "day=";
    final String WIDTH = "width=";
    final String HEIGHT = "height=";


    public final static int SHOW_DATE= 2;
    public final static int SHOW_WEEK_NO = 1;
    public final static int SHOW_NONE = 0;

    public final static int DAY_MONDAY = 1;
    public final static int DAY_TUESDAY = 2;
    public final static int DAY_WEDNESDAY = 3;
    public final static int DAY_THURSDAY = 4;
    public final static int DAY_FRIDAY = 5;
    public final static int DAY_FULL_WEEK = 0;


    public NovaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NovaView, 0 , 0);
        //Handle id's and stuff
        try {
            schoolId = a.getString(R.styleable.NovaView_schoolId);
        }
        finally {
            a.recycle();
        }
        //This is the url of n2c schedule week 7 in swedish
        /*http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&
        schoolid=52550/             //School. Thorildsplan = 80710, Blackeberg = 52550
        sv-se&
        type=-1&                     //Nada...
        id=n2c&                    //Class. N2c, N3c etc.
        period=&
        week=7&                     //Week number.
        mode=0&                     //0 = show nothing under the days, 1 = show week, 2 = show date,
        printer=0&                  //Either 0 or 1, and 1 is fucked up... Guessing 1 means true, and shows the names of the mentors.
        colors=32&                  //Let it be set to 32. Couldn't change it.
        head=0&                     //Didn't do a shit...
        clock=0&                    //Doesn't seem to work...
        foot=0&                     //Nada, just like head.
        day=0&                      //0 = the full week. 1 = monday. 2 = tuesday, 3 = mon and tue, 4 = wed, 5 = mon and wed, 6 = tue and wed, 7 = mon tue and wed, 8 = thursday, 9 = mon and thur, 10 = tue and thur, 11 = mon tue and thur, 12 = wed and thur, 13 = mon wed and thur, 14 = tue wed and thur, 15 = mon tue wed thur, 16 = FRIDAY!!!, 17 = mon and fri, 18 = tue and fri, 19 = mon tue and fri (shit is ridiculous), 20 = wed and fri, 21 = mon wed fri

                                    /*
                                    * 0 = full week
                                    * 1 = monday
                                    * 2 = tuesday
                                    * 4 = wed
                                    * 8 = thursday
                                    * 16 = friday
                                    * (you see the pattern? Let's create )

        width=1420&                 //Doesn't do anything
        height=682&                 //Nada...
        maxwidth=1420&              //Probably shouldn't change this
        maxheight=682*/

        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);

        viewHeight = getHeight();
        viewWidth = getWidth();
    }

    /**
     * Will set the id of the school which NovaView will try to fetch a schedule from.
     * The id can be obtained through the image url.
     * @param id is the schoold id. (schoolid=x from the url)
     */
    public void setSchoolId(String id){
        schoolId = id;
    }

    /**
     * Which week do you want NovaView to show? Leave blank if you'd like to see a whole
     * term.
     * @param week
     */
    public void setWeekNo(int week){
        weekNo = week;
    }

    /**
     * Use this method to set the id of the class to show.
     * It can be a class id, for example N2c in Blackeberg, or a personal id number.
     * @param classId
     */
    public void setId(String classId){
        schoolClass = classId;
    }





    /**
     * Use this method to place a top banner in you schedule with information about week number or date.
     * Use {@link #SHOW_DATE} to show the date at the top, {@link #SHOW_WEEK_NO} to show the week number
     * or {@link #SHOW_NONE} to disable the top banner.
     *
     * @param mode is the requested mode.
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Use this method to set the period to view. At blackebergs gymnasium we have Ht and Vt, for Autumn and respectively spring.
     * @param period is the period you want to view.
     */
    public void setPeriod(String period){
        this.period = period;
    }

    /**
     * Will set the day of the week to show.
     * Use this method to place a top banner in you schedule with information about week number or date.
     * Use {@link #DAY_MONDAY} to show monday, and {@link #DAY_TUESDAY} to show tuesday, etc.
     * Use {@link #DAY_FULL_WEEK} to view the whole week.
     *
     * @param day is the day to show.
     */
    public void setDay(int day){
        if (day > -1)
            switch(day){
                case 0:
                    this.day = Integer.toString(0);
                    break;
                case 1:
                    this.day = Integer.toString(1);
                    break;
                case 2:
                    this.day = Integer.toString(2);
                    break;
                case 3:
                    this.day = Integer.toString(4); //THIS IS NOT WRONG! CHECK THE LONG COMMENT ABOVE
                    break;
                case 4:
                    this.day = Integer.toString(8);
                    break;
                case 5:
                    this.day = Integer.toString(16);
                    break;
            }
    }

    public void loadSchoolUrl(){
        loadUrl(generateSchoolUrl());
    }

    private String generateSchoolUrl(){
        String url = URL_PART_1 + SCHOOLID + schoolId + "&" +
                URL_PART_2 + ID + schoolClass + "&" + PERIOD + period + "&" + WEEK + weekNo + "&" + MODE + mode + "&" +
                URL_PART_3 + DAY + day + "&" + WIDTH + viewWidth + "&" + HEIGHT + viewHeight + "&" +
                URL_PART_LAST;

        return url;
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);

        viewHeight = getHeight();
        viewHeight = pxToDp(viewHeight);

        viewWidth = getWidth();
        viewWidth = pxToDp(viewWidth);
        loadSchoolUrl();

    }

    private int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
    private int pxToDp(int px){
        return (int) ((px / (getResources().getDisplayMetrics().density)) + 0.5f);
    }
}
