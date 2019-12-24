package com.temukangen.amikomhilang.home;

import com.temukangen.amikomhilang.R;

import java.util.ArrayList;

public class HomeData {

    public static String[] name = new String[]{
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci"
    };

    public static String[] description = new String[]{
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci",
            "Sandal Jepit",
            "Kunci"
    };

    public static int[] image = new int[]{
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani,
            R.drawable.ahmad_yani
    };

    static ArrayList<Home> getListData() {
        ArrayList<Home> list = new ArrayList<>();
        for (int position = 0; position < name.length; position++) {
            Home home = new Home();
            home.setName(name[position]);
            home.setDescription(description[position]);
            home.setImage(image[position]);
            list.add(home);
        }
        return list;
    }

}
