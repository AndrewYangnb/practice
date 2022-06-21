package com.redisson;

import java.util.Iterator;
import java.util.Map;

import com.util.RedissonAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RExecutorService;
import org.redisson.api.RMap;
import org.redisson.api.WorkerOptions;
import org.redisson.api.mapreduce.RCollator;
import org.redisson.api.mapreduce.RCollector;
import org.redisson.api.mapreduce.RMapReduce;
import org.redisson.api.mapreduce.RMapper;
import org.redisson.api.mapreduce.RReducer;


public class MapReduceExample {

    public static final Logger logger = LogManager.getLogger(MapReduceExample.class.getName());

    // 实现RMapper类, 把Map中的每个元素转换为用作Reducer处理的 键值对
    public static class WordMapper implements RMapper<String, String, String, Integer> {
        // 重写map方法
        // collector 跨所有mapper任务共享的实例
        @Override
        public void map(String key, String value, RCollector<String, Integer> collector) {
            // 根据正则表达式的匹配拆分此字符串 匹配a-z、A-Z所有的字符
            // Mapper将map中的value用空格分开
            String[] words = value.split("[^a-zA-Z]");
            for (String word : words) {
                // 存储键和值 键,值都可用作归纳  返回的是Object
                collector.emit(word, 1);
            }
            logger.info(collector);
        }
    }

    // 实现RReducer归纳器接口,用来将映射器生成的键值对列表进行归纳整理;
    public static class WordReducer implements RReducer<String, Integer> {
        // Reduce 归纳，key值相同的value数量
        // 参数为 (键, values的集合)
        @Override
        public Integer reduce(String reducedKey, Iterator<Integer> iter) {
            // 定义一个int sum, 用来计算具有相同key的value个数
            int sum = 0;
            while (iter.hasNext()) {
                Integer i = iter.next();
                sum += i;
            }
            return sum;
        }

    }


    // 收集器collator, 将Reduce以后的结果化简为单一一个对象
    public static class WordCollator implements RCollator<String, Integer, Integer> {

        // 对Reducer整理之后的结果--Map<String, Integer> 计算所有value的个数
        @Override
        public Integer collate(Map<String, Integer> resultMap) {
            // value的数量, 作返回值
            int result = 0;
            // .values() 方法,返回映射中的values的集合,即此时为Integer的集合
            for (Integer count : resultMap.values()) {
                // 遍历集合中的每个元素,相加
                result += count;
            }
            // 返回value总的数量
            return result;
        }

    }

    public static void main(String[] args) {

        // 连接redis数据库，
        RedissonAdapter.get("server").getExecutorService(RExecutorService.MAPREDUCE_NAME).registerWorkers(WorkerOptions.defaults().workers(3));

        // 新建一个RMap
        RMap<String, String> map = RedissonAdapter.get("server").getMap("myMap");

        // 给Map中添加键值映射
        map.put("1", "Alice was beginning to get very tired");
        map.put("2", "of sitting by her sister on the bank and");
        map.put("3", "of having nothing to do once or twice she");
        map.put("4", "had peeped into the book her sister was reading");
        map.put("5", "but it had no pictures or conversations in it");
        map.put("6", "and what is the use of a book");
        map.put("7", "thought Alice without pictures or conversation");

        // 作为结果参考

        // 创建RMapReduce实例, 为上文中的定义的map作MapReduce
        RMapReduce<String, String, String, Integer> mapReduce = map
                // 返回与键相关联的RCountDownLatch 实例
                .<String, Integer>mapReduce()
                // 使用 mapper 映射器
                .mapper(new WordMapper())
                // 使用 reducer 归纳器
                .reducer(new WordReducer());

        // 使用定义的整理器 返回 MapReduce之后 value的总和
        Integer count = mapReduce.execute(new WordCollator());
        System.out.println("Count " + count);

        // 执行mapReduce实例, 返回归纳过的 键和值(个数)的映射
        Map<String, Integer> resultMap = mapReduce.execute();
        System.out.println("Result " + resultMap);

        RedissonAdapter.get("server").shutdown();
    }
}
