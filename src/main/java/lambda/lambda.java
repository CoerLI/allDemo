package lambda;

import com.sun.org.apache.xml.internal.serializer.ToSAXHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * lambda表达式的意义：简化函数
 *
 * lambda表达式的语法
 * （ parameters ）-> expression
 * （ parameters ）-> { statements; }
 *
 * examples:
 * () -> 5             无参，返回5
 *
 * x -> 2 * x          传参x，返回 2 * x
 *
 *（x ， y）-> x - y    传参x、y，返回 x - y
 *
 *（int x，int y） —> x + y   传参x，y，返回 x + y
 *
 *（String s） -> System.out.println(s)  传参s，执行语句
 *
 * 双冒号语法:
 *
 */
public class lambda {
//    内部类的加载
    private static class Student{
        String name ;
        int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
    public static void main(String[] args) {
        /********************* lambda实现List遍历 ************************/
        String[] arr = {"no1", "no2", "no3"};
        List<String> lists = Arrays.asList(arr);
        lists.forEach((list) -> System.out.print(list + " "));

        lists.forEach(System.out::println);

        /********************* lambda实现new线程 *************************/
//        匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类");
            }
        }).start();

//        lambda写法, 传参为空，执行的方法是sout
        new Thread(() -> System.out.println("lambda写法")).start();
        /********************* lambda实现用comaprator排序 *************************/
        ArrayList<Student> al = new ArrayList<>();
        al.add(new Student("lihang", 20));
        al.add(new Student("lihang_2", 22));
        al.add(new Student("lihang_3", 18));
//        普通方法:匿名内部类
//        al.sort(new Comparator<Student>() {
//            @Override
//            public int compare(Student o1, Student o2) {
//                return o1.age - o2.age;
//            }
//        });
        System.out.println(al);
        //lambda写法
        al.sort((x,y)->x.age - y.age);
        System.out.println(al);
        /********************* lambda实现用comaprator排序 *************************/


    }
}