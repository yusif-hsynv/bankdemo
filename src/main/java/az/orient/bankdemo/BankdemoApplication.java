package az.orient.bankdemo;

import az.orient.bankdemo.scheduler.MyThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankdemoApplication.class, args);
        //MyThread myThread = new MyThread();
        //myThread.start();
    }

}
