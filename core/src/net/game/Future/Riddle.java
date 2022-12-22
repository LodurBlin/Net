package net.game.Future;
/*
import java.io.*;

public class Riddle implements Serializable {
    private String task;
    private transient String password;

    public Riddle(String task, String password) {
        this.task = task;
        this.password = password;
    }


    @Override
    public String toString() {
        return "Riddle{" +
                "task='" + task + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Riddle igor = new Riddle("Висит груша, нельзя скушать", "Лампочка");
        Riddle renat = new Riddle("Каких камней нет ни в одном море?", "Сухих");
        System.out.println("Before: \n" + igor);
        System.out.println(renat);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Externals.out"));
        out.writeObject(igor);
        out.writeObject(renat);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("Externals.out"));
        igor = (Riddle) in.readObject();
        renat = (Riddle) in.readObject();

        System.out.println("After: \n" + igor);
        System.out.println(renat);
    }
}

 */