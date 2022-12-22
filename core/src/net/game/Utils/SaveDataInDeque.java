package net.game.Utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Stack;

public class SaveDataInDeque<T> {
    private int length;
    private ArrayDeque<T> data;
    SaveDataInDeque(){
        length = 0;
        data = new ArrayDeque<T>();

    }
    public void offer(T item){
        data.offer(item);
        length++;
    }
    public T pop(){
        return data.pop();
    }
    public T getById(int id){
        ArrayDeque<T> temp = data;
        T res = null;
        for(int i = 0; i < (length - id); ++i){
            res = temp.pop();
        }

        return res;
    }

}
