package net.jcip.examples.chapter10;

import java.util.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/12 8:42
 */
public class TestMap {
    private static final Map<String, HeatInfoItem> map = new HashMap<>();

    private static List<HeatInfoItem> result = new ArrayList<>();

    private static List<HeatInfoItem> initList = new ArrayList<>();

    static {
        HeatInfoItem item1 = new HeatInfoItem();
        item1.setInfoCode("item4");
        HeatInfoItem item2 = new HeatInfoItem();
        item2.setInfoCode("item5");
        HeatInfoItem item3 = new HeatInfoItem();
        item3.setInfoCode("item6");
        initList.add(item1);
        initList.add(item2);
        initList.add(item3);
    }

    private static List<HeatInfoItem> updateList = new ArrayList<>();

    static {


    }

    public static void main(String[] args) {


//       Thread anotherThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    for (int i=0; i< : result) {
//                        System.out.println(heatInfoItem);
//                    }
//                }
//            }
//        });
//       anotherThread.start();
//
//        Executor executor = Executors.newFixedThreadPool(1);
//        executor.execute(new Task(initList));
////        while (true) {
////            executor.execute(new Task(updateList));
////        }
        HeatInfoItem item1 = new HeatInfoItem();
        item1.setInfoCode("item1");
        HeatInfoItem item2 = new HeatInfoItem();
        item2.setInfoCode("item2");
        item1.setDeleted(true);
        HeatInfoItem item3 = new HeatInfoItem();
        item3.setInfoCode("item3");
        updateList.add(item1);
        updateList.add(item2);
        updateList.add(item3);
        int updateListHashCode = updateList.hashCode();
        List<HeatInfoItem> tempList = updateList;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateList = initList;
            }
        }).start();
//        while (true) {
//            tempList.forEach(System.out::println);

//        }
        while (tempList.hashCode() == updateListHashCode){

        }
        System.out.println("tempList has updated");

    }

    public static class Task implements Runnable {
        private List<HeatInfoItem> updateList;

        public Task(List<HeatInfoItem> updateList) {
            this.updateList = updateList;
        }

        private void updateMap(List<HeatInfoItem> list) {
            if (list == null || list.size() == 0) {
                return;
            }
            for (HeatInfoItem heatInfoItem : list) {
                //用最新数据替换老数据
                map.put(heatInfoItem.getInfoCode(), heatInfoItem);
            }
        }

        @Override
        public void run() {
            updateMap(updateList);
            Iterator<Map.Entry<String, HeatInfoItem>> iterator = map.entrySet().iterator();
            Random random = new Random();
            List<HeatInfoItem> tempResult = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, HeatInfoItem> item = iterator.next();
                HeatInfoItem heatInfoItem = item.getValue();
                //如果数据已删除
                if (heatInfoItem.getDeleted()) {
                    iterator.remove();
                } else {
                    //todo 这里是否需要复制一份?
                    //由于热度值需要定时刷新，所以放在这边加载。
                    double heat = random.nextInt(1000);
                    heatInfoItem.setHeat(heat);
                    heatInfoItem.setPriority(heat);
                    tempResult.add(heatInfoItem);
                }
            }
            result = tempResult;
            //模拟定时操作。
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
