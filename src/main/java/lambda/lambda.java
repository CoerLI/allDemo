package lambda;

import java.util.Arrays;
import java.util.List;

/**
 * lambda表达式的语法
 * （ parameters ）-> expression
 * （ parameters ）-> { statements; }
 *  examples:
 *
 *  () -> 5             无参，返回5
 *
 *  x -> 2 * x          传参x，返回 2 * x
 *
 * （x ， y）-> x - y    传参x、y，返回 x - y
 *
 * （int x，int y） —> x + y   传参x，y，返回 x + y
 *
 * （String s） -> System.out.println(s)  传参s，执行语句
 *
 *  双冒号语法:
 *  分支/初始化项目/远程提交/回撤/tag
 */
public class lambda {
    public static void main(String[] args) {
        String[] arr = {"no1","no2","no3"};
        List<String> lists = Arrays.asList(arr);
        lists.forEach((list) -> System.out.print(list+" "));

        lists.forEach(System.out :: println);

//        匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类");

            }
        }).start();

//        lambda写法, 传参为空，执行的方法是sout
        new Thread(() -> System.out.println("lambda写法")).start();
    }
}