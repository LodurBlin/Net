package net.game.Utils;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Arrays;
import java.util.Collections;

public class DataSort{
    private Integer[] arr;
    DataSort(int length){
        arr = new Integer[length];
    }
    DataSort(Integer[] Data){
        arr = Data;
    }
    void ToSort(Boolean f){
        if(f) Arrays.sort(arr);
        else Arrays.sort(arr, Collections.<Integer>reverseOrder());
    }

    void ToSort(Boolean f, int begin, int end){
        if(f) Arrays.sort(arr, begin, end);
        else Arrays.sort(arr, begin, end, Collections.<Integer>reverseOrder());
    }

    public Integer[] getArr() {
        return arr;
    }
}
