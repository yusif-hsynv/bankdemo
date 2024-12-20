package az.orient.bankdemo.scheduler;

public class MyThread extends Thread {
    public void run() {
        try {
            while (true) {
                System.out.println("Qrup61");
                Thread.sleep(3000);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
