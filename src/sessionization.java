import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Author: @DilipKunderu
 */
public class sessionization {
    private static final String logLocation = System.getProperty("user.dir" )+ "/input/log.csv";
    private static final String timeOutLocation = System.getProperty("user.dir" )+ "/input/inactivity_period.txt";
    private static long inactivity;
    private static Path path;
    private static Map<String, Node> map;
    private static StringBuilder sb;
    private static DateFormat df;
    private static int sbCount = 0;

    public static void main (String[] args) {
        File outFile = new File (System.getProperty("user.dir" )+ "/output/sessionization.txt");
        map = new HashMap<>();
        try {
            inactivity = setInactivityPeriod();
            processInputFile ();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int setInactivityPeriod() {
        int res = 0;
        path = Paths.get(timeOutLocation);
        try(BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))){
            String s = null;

            while ((s = reader.readLine()) != null) {
                res = Integer.parseInt(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static void processInputFile() throws IOException {
        //data is chronologically listed in the csv file
        path = Paths.get (logLocation);

        try(BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))){
            String ss = reader.readLine();
            System.out.println(ss);
            String s = null;
            String[] t;
            MinHeap hp = new MinHeap();
            String ip, date, time, cik, accession, extention, pr_key;
            long stamp;

            while ((s = reader.readLine()) != null) {
                t = s.split(",");

                ip = t[0];
                date = t[1];
                time = t[2];
                cik = t[4];
                accession = t[5];
                extention = t[6];

                stamp = convertToMillis(t[1], t[2]);
                pr_key = t[4] + t[5] + t[6];

                while(!hp.isEmpty() && hp.peek().val < stamp) {
                    Node nd = hp.remove();
                    map.remove(nd.key);
                    print(nd);
                }

                if (!map.containsKey(ip)) {
                    Node nd = new Node(ip, stamp, stamp,stamp + inactivity * 1000,1);
                    nd.set.add(pr_key);
                    hp.add(nd);
                    map.put(ip, nd);
                } else {
                    Node nd = map.get(ip);
                    if (!nd.set.contains(pr_key)) {
                        nd.count++;
                        nd.end = stamp;
                        nd.set.add(pr_key);
                        hp.increaseKey(nd, stamp+(inactivity*1000));
                    }
                }
            }
            while(!hp.isEmpty()) {
                Node nd = hp.remove();
                map.remove(nd.key);
                print(nd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Stream<String> stringStream = Files.lines(path, Charset.forName("UTF-8"));
    }

    private static void print(Node nd) {
        sb = new StringBuilder();
        sb.append(nd.key);
        sb.append(",");
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 =new Date(nd.start);
        sb.append(df.format(d1));
        sb.append(",");
        Date d2 = new Date(nd.end);
        sb.append(df.format(d2));
        sb.append(",");
        long dd = (nd.end-nd.start)/(1000) + 1;
        sb.append(dd);
        sb.append(",");
        sb.append(nd.count);
        sbCount++;

        if (sbCount == 10) {
            try {
                writeToFile(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            sbCount = 0;
        }
    }

    private static void writeToFile (String s) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir" )+ "/output/sessionization.txt", true));
        writer.write(s);
        writer.close();
    }

    private static long convertToMillis(String s, String s1) throws ParseException {
        String myDate = s + " " + s1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = sdf.parse(myDate);
        return date.getTime();
    }
}
