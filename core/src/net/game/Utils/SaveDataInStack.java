package net.game.Utils;

import java.util.Iterator;
import java.util.Stack;

public class SaveDataInStack<T> {
    private int length;
    private Stack<T> data;
    SaveDataInStack(){
        length = 0;
        data = new Stack<T>();
    }
    public void push(T item){
        data.push(item);
        length++;
    }
    public void pop(){
        data.pop();
    }
    public T getById(int id){
        Stack<T> temp = data;
        for(int i = 0; i < (length - id); ++i){
            temp.pop();
        }
        T res = null;
        Iterator<T> iter = temp.iterator();
        while(iter.hasNext()){
            res = iter.next();
        }

        return res;
    }
}
