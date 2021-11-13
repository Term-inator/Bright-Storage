package com.example.bright_storage.api;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.nlp.AipNlp;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.repository.StorageUnitRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * 已测试：
 * 在抽屉中添加面包
 * 去除抽屉/去除抽屉中的面包
 * 将抽屉里的面包移动到柜子里
 * （告诉我）面包在哪（里/儿）/（告诉我）面包的位置
 */
public class Analyzer {
    private final String APP_ID = "24504515";
    private final String API_KEY = "bTG7ohvMjG6Md5x8XQ3BxGOd";
    private final String SECRET_KEY = "6SOL17NGM0dP4MA9SDAA45uyHtpUzdW4";

    private TreeMap<String, Integer> dict = new TreeMap<>();
    private TreeMap<String, Word> sentence = new TreeMap<>();
    private StorageUnitRepository storageUnitRepo = new StorageUnitRepository();

    public Analyzer() {
        this.init_dict();
    }

    /**
     * 调用百度接口
     * @param client
     * @param s 要解析的字符串
     * @throws JSONException
     */
    private void depParser(AipNlp client, String s) throws JSONException {
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        JSONObject res = client.depParser(s, null);
        System.out.println(res.toString(2));
        JSONArray items = res.getJSONArray("items");

        for(int i = 0; i < items.length(); ++i) {
            Word w = JSON.parseObject(items.getJSONObject(i).toString(), Word.class);
            this.sentence.put(w.getId(), w);
        }
    }

    private void init_dict() {
        dict.put("插", 1);
        dict.put("存", 1);
        dict.put("放", 1);
        dict.put("插入", 1);
        dict.put("创建", 1);
        dict.put("存入", 1);
        dict.put("放入", 1);
        dict.put("加入", 1);
        dict.put("添加", 1);
        dict.put("增加", 1);
        dict.put("新建", 1);
        dict.put("新增", 1);

        dict.put("删除", 2);
        dict.put("移除", 2);
        dict.put("去除", 2);

        dict.put("移", 3);
        dict.put("移动", 3);
        dict.put("转移", 3);

        dict.put("哪", 4);
        dict.put("哪儿", 4);
        dict.put("哪里", 4);
        dict.put("位置", 4);
    }

    /**
     * 生成去除本次操作符的字符串
     * @param type -1 -> 核心词左侧； 1 -> 核心词右侧
     * @param head_id 核心词id
     * @param ban_id 操作符id
     * @return
     */
    private String banText(int type, String head_id, String ban_id) {
        int h_id = Integer.parseInt(head_id);
        int b_id = Integer.parseInt(ban_id);
        String s = "";
        int id = type == -1 ? 1 : h_id + 1;
        for(; id <= this.sentence.size() && -type*id <= -type*h_id; ++id) {
            if(id == b_id || id == h_id) {
                continue;
            }
            String key = String.valueOf(id);
            s += this.sentence.get(key).getWord();
        }
        return s;
    }

    /**
     * 顺路径一层一层深入，找到物品的父节点id
     * @param s 物品名称（包含所有修饰词，如定语、状语等）
     * @return
     */
    private long match(String s) {
        if(s.length() == 0) {
            System.out.println("NO!!!");
            return -1;
        }
        long p_id = 0L; // 父节点id
        StorageUnitQuery storageUnit = new StorageUnitQuery();
        for(int i = 0; i < s.length(); ++i) {
            storageUnit.setLocalParentId(p_id);
            List<StorageUnit> data = this.storageUnitRepo.query(storageUnit);

            boolean found = false;
            for (StorageUnit su : data) {
                System.out.println(su.getName());
                String name = su.getName();
                if (s.indexOf(name) != -1) {
                    i += name.length();
                    --i; // 抵消for的++i
                    p_id = su.getLocalId(); // 更新p_id
                    found = true;
                    break;
                }
            }

            if(!found && p_id == 0) {
                return -1;
            }
        }
        return p_id;
    }

    /**
     * 查找物品是否在数据库中，不关心其父节点
     * @param s 物品名称（包含所有修饰词，如定语、状语等）
     * @return
     */
    private long search(String s) { //dfs
        if(s.length() == 0) {
            return -1;
        }
        Queue<Long> queue = new LinkedList<>();
        queue.offer(0L); // 父节点id
        StorageUnitQuery storageUnit = new StorageUnitQuery();
        while(!queue.isEmpty()) {
            long id = queue.poll();
            if(id != 0) {
                StorageUnit self = this.storageUnitRepo.findById(id);
                String name = self.getName();
                if(s.indexOf(name) != -1) {
                    return id;
                }
            }

            storageUnit.setLocalParentId(id);
            List<StorageUnit> data = this.storageUnitRepo.query(storageUnit);

            for(StorageUnit su : data) {
                queue.offer(su.getLocalId());
            }
        }
        return -1;
    }

    /**
     * 获取操作的类型
     * @param s 操作符
     * @return
     */
    private int getType(String s) {
        if(this.dict.containsKey(s)) {
            return this.dict.get(s);
        }
        else {
            return 0;
        }
    }

    /**
     * 获取p_id节点的子节点
     * @param p_id
     * @return
     */
    private List<Word> getChildren(String p_id) {
        List<Word> res = new ArrayList<>();
        for (Map.Entry<String, Word> entry : this.sentence.entrySet()) {
            Word w = entry.getValue();
            if(p_id.equals(w.getHead())) {
                res.add(w);
            }
        }
        return res;
    }

    /**
     * 判断是不是做修饰成分
     * @return
     */
    private boolean isDecoration(Word w) {
        String deprel = w.getWord();
        if(deprel == "DE" || deprel == "ADV" || deprel == "ATT" || deprel == "F" || deprel == "COO") {
            return true;
        }
        return false;
    }

    /**
     * 提取新物品的名字
     * @param type -1 -> 新物品在核心词左侧； 1 -> 新物品造核心词右侧
     * @param head_id 核心词id
     * @param ban_id 操作符id
     * @return
     */
    private String getNewItemName(int type, String head_id, String ban_id) {
        int h_id = Integer.parseInt(head_id);
        int b_id = Integer.parseInt(ban_id);
        String s = "";
        int id = type == -1 ? 1 : h_id + 1;
        for(; id <= this.sentence.size() && -type*id <= -type*h_id; ++id) {
            if(id == b_id || id == h_id) {
                continue;
            }
            String key = String.valueOf(id);
            Word word = this.sentence.get(key);
            if(this.isDecoration(word) || Integer.parseInt(word.getHead()) == h_id) {
                s += this.sentence.get(key).getWord();
            }
        }
        return s;
    }

    public void analyze(String text) throws Exception {
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        this.depParser(client, text);

        // 获取所有可能的操作符
        ArrayList<Integer> type = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
        for (Map.Entry<String, Word> entry : this.sentence.entrySet()) {
            String key = entry.getKey();
            Word value = entry.getValue();

            int tmp = this.getType(value.getWord());
            if(tmp != 0) { // 属于内置操作符
                type.add(tmp);
                id.add(key);
            }
        }

        if(id.isEmpty()) {
            // 找不到合法的操作符
            // TODO 给用户一些提示
            System.out.println("找不到合法的操作符");
            this.err();
            return;
        }

        // 是否找到一个解
        boolean succeed = false;
        // 获取核心词
        Word head = this.getChildren("0").get(0);
        for(int i = 0; i < id.size(); ++i) { // TODO 无法证明存在多解，暂时是贪心
            int op_type = type.get(i);
            // 对于一个字符串的功能，决定操作类型和做StorageUnit的名字是互斥的
            String ban_id = id.get(i);
            if (op_type == 1 || op_type == 3) { // op: 添加、移动 二元操作符
                // 以head为界分割字符串，并ban掉操作符
                String l_word = this.banText(-1, head.getId(), ban_id);
                String r_word = this.banText(1, head.getId(), ban_id);
                System.out.println("l:" + l_word);
                System.out.println("r:" + r_word);
                long l_id = match(l_word);
                long r_id = match(r_word);
                System.out.println(l_id);
                System.out.println(r_id);
                if(l_id == -1 && r_id == -1) { // 都不在数据库中
                    this.err();
                    System.out.println("找不到对应的存储单元");
                    return;
                }
                else if(l_id == -1 || r_id == -1) { // 都为-1的情况已经被特判。此处为一个在；一个不在
                    if(op_type == 3) { // 移动要保证两边都在数据库中
                        this.err();
                        System.out.println("你可能想添加物品？");
                        return;
                    }
                    else if(op_type == 1) {
                        long p_id;
                        String name;
                        if(l_id == -1) { // 右中加左 将A加入B 把A放入B
                            p_id = r_id;
                            name = this.getNewItemName(-1, head.getId(),ban_id);
                        }
                        else { // 左中加右 在A中添加B
                            p_id = l_id;
                            name = this.getNewItemName(1, head.getId(),ban_id);
                        }

                        if(name.length() == 0) {
                            this.err();
                            System.out.println("无法提取新物品的名称");
                            return;
                        }

                        StorageUnit storageUnit = new StorageUnit();
                        storageUnit.setName(name);
                        storageUnit.setLocalParentId(p_id);
                        this.storageUnitRepo.save(storageUnit);
                        succeed = true;
                    }
                }
                else if(l_id != -1 && r_id != -1) { //都存在与数据库
                    if(op_type == 1) {
                        this.err();
                        System.out.println("你可能想移动物品？");
                        return;
                    }
                    else if (op_type == 3) { // TODO 无法证明只有将左移动到右之下
                        long be_to_move = l_id; // 将被移动的StorageUnit的id
                        StorageUnit su = storageUnitRepo.findById(be_to_move);
                        su.setLocalParentId(r_id);
                        this.storageUnitRepo.update(su);
                        succeed = true;
                    }
                }
            }
            if (op_type == 2 || op_type == 4) { // op: 删除或查询 一元操作符
                String l_word = this.banText(-1, head.getId(), ban_id);
                String r_word = "";
                long l_id = 0L, r_id = 0L;
                if(op_type == 2) {
                     l_id = this.match(l_word);
                }
                else if(op_type == 4) {
                    l_id = this.search(l_word);
                }
                long res_id;
                if (l_id == -1) {
                    r_word = this.banText(1, head.getId(), ban_id);
                    if(op_type == 2) {
                        r_id = this.match(r_word);
                    }
                    else if(op_type == 4) {
                        r_id = this.search(r_word);
                    }
                    if (r_id == -1) {
                        this.err();
                        System.out.println("查无此物");
                        System.out.println("l:" + l_word + " " + "l_id:" + l_id);
                        System.out.println("r:" + r_word + " " + "r_id:" + r_id);
                        return;
                    } else {
                        res_id = r_id;
                        System.out.println("r:" + r_word + " " + "r_id:" + r_id);
                    }
                } else {
                    res_id = l_id;
                    System.out.println("l:" + l_word + " " + "l_id:" + l_id);
                }
                if (op_type == 2) {
                    this.cascadeDelete(res_id);
                    succeed = true;
                }
                else if(op_type == 4) {
                    // TODO 根据res_id逐级向上查找路径
                    succeed = true;
                }
            }

            // TODO 贪心
            if(succeed) {
                System.out.println("成功");
                break;
            }
        }
    }

    private void err() {
        System.out.println("error!");
    }

    private void cascadeDelete(long container_id) {
        Queue<Long> queue = new LinkedList<>();
        queue.offer(container_id);
        List<Long> ids = new ArrayList<>();

        while(!queue.isEmpty()) {
            long id = queue.poll();
            ids.add(id);

            StorageUnitQuery storageUnit = new StorageUnitQuery();
            storageUnit.setLocalParentId(id);
            List<StorageUnit> data = this.storageUnitRepo.query(storageUnit);

            for(StorageUnit su : data) {
                queue.offer(su.getLocalId());
            }
        }

        this.storageUnitRepo.deleteByIds(ids);
    }
}

class Word {
    private String id;
    private String word;
    private String postag;
    private String head;
    private String deprel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPostag() {
        return postag;
    }

    public void setPostag(String postag) {
        this.postag = postag;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDeprel() {
        return deprel;
    }

    public void setDeprel(String deprel) {
        this.deprel = deprel;
    }
}

