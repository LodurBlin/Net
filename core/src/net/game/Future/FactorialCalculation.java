package net.game.Future;

import net.game.Future.GetRes;

public class FactorialCalculation implements GetRes {
    private int res;
    FactorialCalculation(int n){
        res = calculate(n);
    }
    int calculate(int n){
        if(n == 1) return 1;
        else return n*(n-1);
    }

    @Override
    public int getResult() {
        return this.res;
    }
}
