package structure;

public class MyHashMap<K extends Comparable, V> {
    // capacity 桶的数量，size 键值对的数量
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACOTR = 0.75f;

    Entry[] table;
    int capacity;
    int size;

    public class Entry<K, V> {
        K key;
        V val;
        Entry next;

        K getKey() {
            return key;
        }

        V getVal() {
            return val;
        }

        Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
    }

    public MyHashMap(int initialCapacity) {
        this.capacity = initialCapacity;
    }

    // put() 和 rehash()都用到了getInex()
    private int getIndex(K key, int capacity) {
        int hashCode = key.hashCode();
        int hash = hashCode ^ (hashCode >> 16);
        return hash & (capacity - 1);
    }

    // 两个地方使用了put，所以put要指定数组
    public void put(K key, V val) {
        putval(table, key, val);
    }

    private void putval(Entry<K, V>[] table, K key, V val) {
        int index;
        // 第一次put则初始化table
        if (table == null) {
            table = resize();
        }
        // 对应位置为null则直接存入
        if (table[index = getIndex(key, table.length)] == null) {
            table[index] = new Entry(key, val);
        } else {
            Entry<K, V> head = table[index];
//            Entry<K, V> cur;
            // 不为空时，遍历链表
            while (true) {
                // 遇到相同key的，覆盖
                if (head.key == key || head.key.equals(key)) {
                    head.val = val;
                    size--;
                    break;
                }
                // 到链表结尾，插入
                if (head.next == null) {
                    head.next = new Entry(key, val);
                    break;
                }
                head = head.next;
            }
        }
//             测试hash碰撞时注释此处
        if (table == this.table) {
            ++size;
            if (size >= DEFAULT_LOAD_FACOTR * capacity) {
                resize();
            }
        }
    }

    private Entry<K, V>[] resize() {
        // 第一次扩容
        if (table == null) {
            table = new Entry[capacity];
        } else {
            // 数组两倍增长
            int newCapacity = capacity << 1;
            // 新数组
            Entry<K, V>[] newTable = new Entry[newCapacity];
            // 遍历旧数组
            for (int i = 0; i < capacity; i++) {
                // 略过为空的桶
                if (table[i] == null) {
                    continue;
                } else {
                    // 找到每个桶的头节点
                    Entry<K, V> cur = table[i];
                    // 遍历并且复制到新数组
                    while (cur != null) {
                        // 传入新数组和要rehash的结点
                        rehash(newTable, cur);
                        cur = cur.next;
                    }
                }
            }
            table = newTable;
            capacity = newCapacity;
        }
        return table;
    }

    // 使用putval方法
    private void rehash(Entry<K, V>[] newTable, Entry<K, V> cur) {
        putval(newTable, cur.key, cur.val);
    }

    public int size() {
        return size;
    }

    public V get(K key) {
        int index;
        if (table == null || table.length == 0 || table.length < (index = getIndex(key, capacity)) || table[index] == null) {
            return null;
        }
        Entry<K, V> e = table[index];
        // 遍历链表
        while (true) {
            if (e == null) {
                break;
            }
            // 找到key相同的，取出
            if (e.key == key || e.key.equals(key)) {
                return e.getVal();
            }
            e = e.next;
        }
        return null;
    }

    public V remove(K key) {
        int index;
        if (table == null || table.length == 0 || table.length < (index = getIndex(key, capacity)) || table[index] == null) {
            return null;
        }
        V val;
        Entry<K, V> pre = table[index];
        if (pre.next == null) {
            if (pre.key == key || pre.key.equals(key)) {
                val = pre.val;
                table[index] = null;
                return val;
            }
            return null;
        }
        Entry<K, V> cur = pre.next;

        // 遍历链表
        while (cur != null) {
            if (cur.key == key || cur.key.equals(key)) {
                val = cur.val;
                if (cur.next == null) {
                    pre.next = null;
                } else {
                    pre.next = cur.next;
                }
                return val;
            } else {
                pre = pre.next;
                cur = cur.next;
            }
        }
        System.out.println(1);
        return null;
    }

    public boolean containsKey(K key) {
        if (get(key) == null) {
            return false;
        }
        return true;
    }
}
