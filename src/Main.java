import java.io.*;
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
            try {
                income = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                throw new WFNExpception("Only one Integer per line expected");
            }
        }
        void print() {
            System.out.println(name);
            for (int i = 0; i < size; i++) {
                System.out.println(city[i]);
            }
            System.out.println(income);
        }
    }

    int mf_size = 64;
    int tail = 0;
    TransportData[] mf = new TransportData[64];
    TransportHandler() {

    }
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

    void save2hdd(String fileName) throws IOException {
        BufferedWriter pw = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < tail; i++) {
            pw.write(mf[i].name + "\n");
            for (int j = 0; j < mf[i].size; j++) {
                pw.write(mf[i].city[j] + "\n");
            }
            pw.write(mf[i].income + "\n");
        }
        pw.close();
    }

    void add() {
        while (true) {
            System.out.println("Enter TMF name or type 0");
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();
            if (name.equals("0")) break;
            TransportData temp = new TransportData();
            temp.name = name;
            int cnt = 0;
            System.out.println("Enter cities or type 0");
            while (true) {
                if (cnt == 10) break;
                String city = sc.nextLine();
                if (city.equals("0")) break;
                temp.city[cnt++] = city;
            }
            System.out.println("Enter income");
            temp.income = Integer.parseInt(sc.nextLine());
            temp.size = cnt;
            temp.print();
            if (tail == mf_size) resize();
            mf[tail++] = temp;
        }

    }

    void delete(String name) {
        for (int i = 0; i < tail; i++) {
            if (mf[i].name.equals(name)) {
                for (int j = i + 1; j < tail; j++) {
                    mf[j - 1] = mf[j];
                }
                tail--;
                i--;
            }
        }
    }
    void saveReport(String fileName, Boolean show) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < tail; i++) {
            bw.write(mf[i].name + " " + mf[i].income + "\n");
            if (show) {
                System.out.println(mf[i].name + " " + mf[i].income);
            }
        }
        bw.close();
    }
}


public class Main {
    public static void main(String[] args)  {
        String[] pair = args[0].split("=");
        TransportHandler th = null;
        if (pair[0].equals("--nofile")) {
            th = new TransportHandler();
        } else if (pair[0].equals("--file")) {
            try {
                th = new TransportHandler(pair[1]);
            } catch (WFNExpception wfnExpception) {
                wfnExpception.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i < args.length; i++) {
            pair = args[i].split("=");
            if (pair[0].equals("--add")) {
                th.add();
            } else if (pair[0].equals("--save")) {
                try {
                    th.save2hdd(pair[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pair[0].equals("--saveReport")) {
                try {
                    th.saveReport(pair[1], false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pair[0].equals("--saveReportP")) {
                try {
                    th.saveReport(pair[1], true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pair[0].equals("--delete")) {
                th.delete(pair[1]);
            }
        }
    }
}
