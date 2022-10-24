package com.baeldung.objectsize;

import org.junit.Test;
import org.omg.CORBA.ARG_IN;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectSizeUnitTest {

    public static void main(String[] args) {
        System.out.println(GraphLayout.parseInstance("POR").toPrintable());

        List<Object> list = new ArrayList<>();
        System.out.println(GraphLayout.parseInstance(list).toPrintable());
        list.add(new Object());
        System.out.println(GraphLayout.parseInstance(list).toPrintable());
        System.out.println(GraphLayout.parseInstance(new Object()).toPrintable());


        long stringSize = 24 * (3 * 100*100*100) + 103 * 24 ;
        long arrayListSize = 3*100*100*100 * 40;
        System.out.println(stringSize + "B");
        System.out.println(arrayListSize + "B");

    }
    @Test
    public void nestedMap(){
        Map nestedMap = new HashMap();
        List<String> typeKey = new ArrayList<>();
        typeKey.add("TYPE1");
        typeKey.add("TYPE2");
        typeKey.add("TYPE3");
        List<String> portKey = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            portKey.add("PORT"+i);
        }
        add(nestedMap, typeKey, portKey, new Object(), 0);
        long total = GraphLayout.parseInstance(nestedMap).totalSize();
        System.out.println("3*100*100*100*1 nested map: " + total + "B");

        int l0 = 0;
        int l1 = 3 * 1;
        int l2 = 3 * 100 * 1;
        int l3 = 3 * 100 * 100 * 1;
        long perStringSize = GraphLayout.parseInstance("PORT0").totalSize();
        long overCal = ((l0+l1+l2+l3 - 1) * 100 * perStringSize);
        System.out.println("per port string size: " + perStringSize);
        System.out.println("all over calculate string size: " + overCal);
        System.out.println("actually nested map size: " + (total - overCal) + "B");

    }

    private void add(Map nestedMap, List<String> typeKey, List<String> portKey, Object o, int idx) {
        List<String> keyList = new ArrayList<>();
        if (idx == 0){
            keyList = typeKey;
        }else if (idx >0 && idx <3){
            keyList = portKey;
        }
        if (idx == 3){
            for (String key : portKey) {
                ((List)nestedMap.computeIfAbsent(key.intern(), k->new ArrayList<>())).add(o);
            }
        }else{
            for (String key : keyList) {
                Map subMap = (Map) nestedMap.computeIfAbsent(key.intern(), k->new HashMap<>());
                add(subMap, typeKey, portKey, o, idx + 1);
            }
        }
    }

    @Test
    public void printingTheVMDetails() {
        System.out.println(VM.current().details());
    }

    @Test
    public void printingTheProfClassLayout() {
        System.out.println(ClassLayout.parseClass(Professor.class).toPrintable());
    }

    @Test
    public void printingTheCourseClassLayout() {
        System.out.println(ClassLayout.parseClass(Course.class).toPrintable());
    }

    @Test
    public void printingACourseInstanceLayout() {
        String ds = "Data Structures";
        Course course = new Course(ds);

        System.out.println("The shallow size is :" + VM.current().sizeOf(course));

        System.out.println(ClassLayout.parseInstance(course).toPrintable());
        System.out.println(ClassLayout.parseInstance(ds).toPrintable());
        System.out.println(ClassLayout.parseInstance(ds.toCharArray()).toPrintable());

        System.out.println(GraphLayout.parseInstance(course).totalSize());
        System.out.println(GraphLayout.parseInstance(course).toFootprint());
        System.out.println(GraphLayout.parseInstance(course).toPrintable());
    }
}
