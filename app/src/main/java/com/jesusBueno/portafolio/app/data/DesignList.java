package com.jesusBueno.portafolio.app.data;

import java.util.ArrayList;

/**
 * Created by Jesus on 9/03/14.
 */
public class DesignList {
    private static ArrayList<Design>designList;
    public static ArrayList<Design> getDesignList(){return designList;}
    public static void setDesignList(ArrayList<Design>designList){DesignList.designList=designList;}
}
