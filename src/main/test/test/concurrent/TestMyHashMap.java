package concurrent;

import org.junit.Test;
import structure.MyHashMap;
import org.junit.Assert;

import java.util.HashMap;

public class TestMyHashMap {
    @Test
    public void testMyHashMap() {
        MyHashMap<String, Integer> map = new MyHashMap();
        HashMap m = new HashMap();

        // 未初始化就get，期望null
        Assert.assertNull(map.get("lihang"));

        // get一个不存在的值
        Assert.assertNull(map.get("test"));

        // 正常put，get
        map.put("lihang", 18);
        Assert.assertEquals(18, (long) map.get("lihang"));

        // put相同的值后get，期望覆盖后的内容
        map.put("lihang", 19);
        map.put("lihang", 20);
        map.put("lihang", 21);
        Assert.assertEquals(21, (long) map.get("lihang"));

        // 冲突后get，调整代码测试扩容
        map = new MyHashMap(1);
        map.put("lihang", 18);
        map.put("test", 100);
        map.put("test_2", 50);

        Assert.assertEquals(18, (long) map.get("lihang"));
        Assert.assertEquals(100, (long) map.get("test"));
        Assert.assertEquals(50, (long) map.get("test_2"));

        // 测试size
        Assert.assertEquals(3, map.size());

        // 测试containsKey
        Assert.assertTrue(map.containsKey("lihang"));
        Assert.assertTrue(map.containsKey("test"));
        Assert.assertTrue(map.containsKey("test_2"));

        // 测试remove
        Assert.assertEquals(50,(long)map.remove("test_2"));
        Assert.assertTrue(map.containsKey("lihang"));
        Assert.assertTrue(map.containsKey("test"));
        Assert.assertFalse(map.containsKey("test_2"));
    }
}