package com.tianxue.boot;

/**
 * @Author tianxue
 * @Date 2020/6/2 10:44 下午
 */
public class volatilec {
    public static void main(String[] args) {
        Autobum autobum = new Autobum();
        new Thread(autobum).start();
        for (; ; ) {
           //add  synchronized (autobum) {
                if (autobum.flag) {
                    System.out.println("有点东西！！！");
                }
          //  }
        }
    }

    public static class Autobum implements Runnable {

        public boolean flag = false;
//        add volatile

        @Override
        public void run() {
            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println("flag is ----------------------------------" + flag);
        }
    }
}
