package com.demo.admin.service.impl;

import com.demo.admin.pojo.vo.TableItemVO;
import com.demo.admin.pojo.vo.TableListVO;
import com.demo.admin.service.TableService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 表格业务实现，启动时生成 30 条模拟数据。
 */
@Service
public class TableServiceImpl implements TableService {

    /** 可选的发布状态 */
    private static final List<String> STATUSES = Arrays.asList("published", "draft", "deleted");

    /** 内存中的表格数据 */
    private final List<TableItemVO> items = new ArrayList<TableItemVO>();

    private final Random random = new Random();

    /** 初始化模拟数据 */
    @PostConstruct
    public void init() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 1; i <= 30; i++) {
            items.add(new TableItemVO(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Demo title " + i + " for learning backend development",
                    STATUSES.get(random.nextInt(STATUSES.size())),
                    "name",
                    formatter.format(new Date(System.currentTimeMillis() - random.nextInt(30) * 86400000L)),
                    300 + random.nextInt(4700)
            ));
        }
    }

    @Override
    public TableListVO list() {
        return new TableListVO(items.size(), items);
    }
}
