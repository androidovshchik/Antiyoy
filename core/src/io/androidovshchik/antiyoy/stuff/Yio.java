package io.androidovshchik.antiyoy.stuff;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.YioGdxGame;

public class Yio {
    public static double angle(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            if (y2 > y1) {
                return 1.5707963267948966d;
            }
            if (y2 < y1) {
                return 4.71238898038469d;
            }
            return 0.0d;
        } else if (x2 >= x1) {
            return Math.atan((y2 - y1) / (x2 - x1));
        } else {
            return 3.141592653589793d + Math.atan((y2 - y1) / (x2 - x1));
        }
    }

    static float maxElement(ArrayList<Float> list) {
        if (list.size() == 0) {
            return 0.0f;
        }
        float max = ((Float) list.get(0)).floatValue();
        for (int i = 1; i < list.size(); i++) {
            if (((Float) list.get(i)).floatValue() > max) {
                max = ((Float) list.get(i)).floatValue();
            }
        }
        return max;
    }

    public static double roundUp(double value, int length) {
        double d = Math.pow(10.0d, (double) length);
        return ((double) ((int) (0.45d + (value * d)))) / d;
    }

    public static void removeByIterator(ArrayList<?> list, Object object) {
        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == object) {
                iterator.remove();
                return;
            }
        }
    }

    public static void addByIterator(ArrayList<?> list, Object object) {
        list.listIterator().add(object);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }

    public static float fastDistance(double x1, double y1, double x2, double y2) {
        return (float) (Math.abs(x2 - x1) + Math.abs(y2 - y1));
    }

    public static float radianToDegree(double angle) {
        return (float) (57.29577951308232d * angle);
    }

    public static void printStackTrace() {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> decodeStringToArrayList(String string, String delimiters) {
        ArrayList<String> res = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(string, delimiters);
        while (tokenizer.hasMoreTokens()) {
            res.add(tokenizer.nextToken());
        }
        return res;
    }

    public static void syncSay(String message) {
        synchronized (System.out) {
            System.out.println(message);
        }
    }

    public static String getDate() {
        return new SimpleDateFormat("dd.MM.yyyy  HH:mm").format(new Date());
    }

    public static String convertObjectToString(Object object) {
        String s = object.toString();
        return s.substring(s.indexOf("@"));
    }

    public static double getRandomAngle() {
        return 6.283185307179586d * YioGdxGame.random.nextDouble();
    }
}
