package com.redisson;

import com.util.RedissonAdapter;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redisson/object-examples
 */

public class ObjectsExamples {
    public static void main(String[] args) throws Exception {

        // Object AtomicDouble - atomicDouble = 0
        RAtomicDouble atomicDouble = RedissonAdapter.get("object").getAtomicDouble("Test-atomicDouble");
        // 当前值减一 -1，返回之前的值
        atomicDouble.getAndDecrement();
        // 当前值加一，返回之前的值
        atomicDouble.getAndIncrement();
        // 在当前值上加 delta
        atomicDouble.addAndGet(1);
        //当前值等于 expect，则将update重新赋值给当前值
        atomicDouble.compareAndSet(2, 100);
        // 当前值减一，返回更新后的值
        atomicDouble.decrementAndGet();

        RedissonAdapter.logger.info(atomicDouble);

        // object-AtomicLong  init = 0
        // short-int-long-float-double
        RAtomicLong atomicLong = RedissonAdapter.get("object").getAtomicLong("Test-atomicLong");
        // 当前值减一，返回旧值
        atomicLong.getAndDecrement();
        //当前值加一，返回旧值
        atomicLong.getAndIncrement();

        RedissonAdapter.logger.info(atomicLong);

        // Batch  使用管道属性，同步或异步执行批量操作
        RBatch batch = RedissonAdapter.get("object").createBatch(BatchOptions.defaults());
        // 设置第一个map  batch中的index = 0
        batch.getMap("test1").fastPutAsync("1", "2");
        // 设置第二个map
        batch.getMap("test2").fastPutAsync("2", "3");
        // 设置第三个map
        batch.getMap("test3").putAsync("2", "5");
        // 设置一个AtomicLong 异步执行增一和返回结果，RFuture表示未来可以获取结果的对象
        RFuture<Long> future = batch.getAtomicLong("counter").incrementAndGetAsync();

        // 执行batch中所有设置的操作，结果为
        BatchResult<?> result = batch.execute();
        // 获取batch执行结果中的index = 3 的结果
        Long counter = (Long) result.getResponses().get(3);

        // 将future和batch的执行结果做对比   true
        RedissonAdapter.logger.info(future.get().equals(counter));


        // BinaryStream - 存储一系列字节bytes
        RBinaryStream stream = RedissonAdapter.get("object").getBinaryStream("Test-BinaryStream");
        // 新建一个字节型数组 values
        byte[] values = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        // 给一个空的容器设置元素；
        stream.trySet(values);
        // Stores element into the holder.
        stream.set(values);

        // 返回输入流对象(数据库读取)，允许读二进制流
        InputStream is = stream.getInputStream();
        // 字符串生成器，用于单个线程正在使用字符串缓冲区的地方
        StringBuilder sb = new StringBuilder();
        int ch;
        // 从输入流中读取字节数据 范围0-255，如果下个字节没有数据返回-1
        while((ch = is.read()) != -1) {
            RedissonAdapter.logger.info(ch);
            // 给字符串缓冲区 在末尾添加字符
            sb.append((char) ch);
        }
        // 定义字符串 承接StringBuilder中的字符
        String str = sb.toString();

        // 定义stream的输出流，从内存给数据库写数据
        OutputStream os = stream.getOutputStream();
        for (byte c : values) {
            // 把特定元素写进输出流
            os.write(c);
        }

        // Object-BitSet
        RBitSet bs = RedissonAdapter.get("object").getBitSet("Test-bitset");
        // {0, 1, 2, 3, 4}
        bs.set(0,5);
        // {1, 2, 3, 4}
        bs.clear(0,1);
        // 5
        bs.length();
        // 取反 ; 与 bs.and(BitSet bs1) ; 或 or(); 异或 xor()
        bs.not();
        // return numbers of bits
        bs.cardinality();

        RedissonAdapter.logger.info(bs);
        RedissonAdapter.logger.info(bs.getName());

        // BloomFilter
        RBloomFilter<String> bloomFilter = RedissonAdapter.get("object").getBloomFilter("Test-bloomFilter");
        // 初始化布隆过滤器
        bloomFilter.tryInit(100_000_000, 0.03);

        bloomFilter.add("a");
        bloomFilter.add("b");

        // 返回布隆过滤器的预期插入量   100000000
        bloomFilter.getExpectedInsertions();
        // 返回元素存在的错误概率
        bloomFilter.getFalseProbability();
        // 返回每个元素使用的哈希迭代量  5
        bloomFilter.getHashIterations();
        RedissonAdapter.logger.info(bloomFilter.getHashIterations());

        // RBucket —— Object holder. Max size of object is 512MB
        RBucket<String> bucket = RedissonAdapter.get("object").getBucket("Test-bucket");
        bucket.set("123");
        // bucket中的值等于"123", 则替换成 "4569"
        boolean isUpdated = bucket.compareAndSet("123", "4569");
        // "321"替换"4569"
        String prevObject = bucket.getAndSet("321");
        // bucket 为空，则设置值
        boolean isSet = bucket.trySet("901");
        // 返回对象的字节长度
        long objectSize = bucket.size();
        // 给元素设置超时时间
        bucket.set("value", 10, TimeUnit.SECONDS);

        // GeoExamples——地理空间元素容器
        RGeo<String> geo = RedissonAdapter.get("object").getGeo("Test-Geo");
        // 初始化Geo条目，params: 经度，纬度，成员
        GeoEntry entry = new GeoEntry(13.24581, 12.24578, "Walter");
        geo.add(entry);
        geo.add(15.21421, 37.21554,"Hein");
        // 返回成员之间的距离，GeoUnit.距离单位
        Double dist = geo.dist("Walter","Hein",GeoUnit.METERS);
        RedissonAdapter.logger.info(dist);
        // 返回Map——对象成员映射其地理位置
        Map<String, GeoPosition> pos = geo.pos("Walter", "Hein");
        RedissonAdapter.logger.info(pos);

        // HyperLogLog——统计基数,使用概率算法
        RHyperLogLog<String> hyperLogLog1 = RedissonAdapter.get("collection").getHyperLogLog("Test-HyperLogLog1");
        // 添加元素
        hyperLogLog1.add("1");
        hyperLogLog1.add("2");
        hyperLogLog1.add("3");
        hyperLogLog1.addAll(Arrays.asList("10", "20", "30"));

        // 创建另一个 HyperLogLog 对象
        RHyperLogLog<String> hyperLogLog2 = RedissonAdapter.get("collection").getHyperLogLog("Test-HyperLogLog2");
        // 添加元素
        hyperLogLog2.add("4");
        hyperLogLog2.add("5");
        hyperLogLog2.add("6");

        // 合并多个HyperLogLog实例，合并后的基数近似于合并前基数的并集
        hyperLogLog2.mergeWith(hyperLogLog1.getName());
        // 返回多个HyperLogLog实例并集的近似基数
        hyperLogLog2.countWith(hyperLogLog1.getName());

        // ReferenceExamples——引用，与垃圾回收相关
        RMap<String, RBucket<String>> data = RedissonAdapter.get("object").getMap("Test-map(String, Bucket)");
        RBucket<String> bucket1 = RedissonAdapter.get("object").getBucket("Test-Bucket-O");
        bucket1.set("5");
        bucket1.set("7");
        data.put("bucket",bucket1);

        RBucket<String> bucket2 = data.get("bucket");

        // SSL——连接redis默认使用SSL
        // redis - defines to use SSL for Redis connection
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379");


        // Script——Redis脚本功能接口
        RBucket<String> bucket_foo = RedissonAdapter.get("object").getBucket("foo");
        bucket_foo.set("bar");


        RScript script = RedissonAdapter.get("object").getScript(StringCodec.INSTANCE);
        // 以只读模式执行脚本
        String result1 = script.eval(RScript.Mode.READ_ONLY, "return redis.call('get','foo')", RScript.ReturnType.VALUE);

        // 将 lua 脚本加载到所有 redis 主实例的 Redis 缓存中
        String sha1 = script.scriptLoad("return redis.call('get','foo')");

        // call lua script by sha digest
//        result1 = RedissonAdapter.get("object").getScript().evalSha(RScript.Mode.READ_ONLY, sha1, RScript.ReturnType.VALUE, Collections.emptyList());


        RedissonAdapter.get("object").shutdown();

    }
}
