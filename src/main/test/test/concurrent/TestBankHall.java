package concurrent;

import org.junit.Test;

public class TestBankHall {
    @Test
    public void testBankHall(){
        // 指定bankhall的柜员数量和出票窗口
        BankHall bankHall = new BankHall(3, 5);
        // 指定客户数量
        int customerNum = 20;

        // 模拟客户抽号
        for (int i = 0; i < customerNum; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Customer customer = new Customer(bankHall);
            customer.start();
        }
        //
        bankHall.shutDown(5);
    }
}
