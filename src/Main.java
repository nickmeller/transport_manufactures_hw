import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
class WFNExpception extends Exception {
    WFNExpception(String message) {
        super(message);
    }
}


class TransportHandler {
    private class TransportData {
        String name;
        final String[] city = new String[10];
        int size = 0;
        int income;
        void input(Scanner sc) throws WFNExpception {
            name = sc.nextLine();
            while (sc.hasNextLine() && size <= 10 && !sc.hasNextInt()) {
                city[size++] = sc.nextLine();
            }
            if (!sc.hasNextInt()) {
                throw new WFNExpception("Wrong file format");
            }
            income = sc.nextInt();
        }
    }
    int mf_size = 64;
    int tail = 0;
    TransportData[] mf = new TransportData[64];
    TransportHandler(String fileName) throws WFNExpception, FileNotFoundException {
        if (fileName == null) {
            throw new WFNExpception("Null fileName");
        }
        Scanner sc = new Scanner(new FileInputStream(fileName));
        while (sc.hasNextLine()) {
            mf[tail] = new TransportData();
            mf[tail++].input(sc);
            if (tail == mf_size) {
                resize();
            }
        }
    }
    void resize() {
        TransportData[] new_mf = new TransportData[mf_size * 2];
        for (int i = 0; i < mf_size; i++) {
            new_mf[i] = mf[i];
        }
        mf_size *= 2;
        mf = new_mf;
    }
    void print() {
        for (int i = 0; i < tail; i++) {
            System.out.println(mf[i].name);
            for (int j = 0; j < mf[i].size; j++) {
                System.out.println(mf[i].city[j]);
            }
            System.out.println(mf[i].income);
        }
    }
    void print(int limit) {
        for (int i = 0; i < tail; i++) {
            if (mf[i].income < limit)
                continue;
            System.out.println(mf[i].name);
            for (int j = 0; j < mf[i].size; j++) {
                System.out.println(mf[i].city[j]);
            }
            System.out.println(mf[i].income);
        }
    }
}


public class Main {
    public static void main(String[] args) {
        try {
            TransportHandler th = new TransportHandler(args[0]);
            th.print(100);
        } catch (WFNExpception wfnExpception) {
            wfnExpception.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
