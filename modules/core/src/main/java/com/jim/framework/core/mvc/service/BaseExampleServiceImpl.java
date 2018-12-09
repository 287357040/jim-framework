package com.jim.framework.core.mvc.service;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BaseExampleServiceImpl<Mapper, Record, Example> extends BaseServiceImpl<Mapper,Record> implements BaseExampleService<Record, Example> {

    public Mapper mapper;

    @Override
    public int countByExample(Example example) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("countByExample", example);
    }

    @Override
    public int deleteByExample(Example example) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("deleteByExample", example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("deleteByPrimaryKey", id);
    }

    @Override
    public int insert(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("insert", record);
    }

    @Override
    public int insertSelective(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("insertSelective", record);
    }

    @Override
    public List<Record> selectByExampleWithBLOBs(Example example) {
        MethodExecutor<List<Record>> methodExecutor = new MethodExecutor<List<Record>>();
        return methodExecutor.execute("selectByExampleWithBLOBs", example);
    }

    @Override
    public List<Record> selectByExample(Example example) {
        MethodExecutor<List<Record>> methodExecutor = new MethodExecutor<List<Record>>();
        return methodExecutor.execute("selectByExample", example);
    }

    @Override
    public List<Record> selectByExampleWithBLOBsForStartPage(Example example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, false);
        MethodExecutor<List<Record>> methodExecutor = new MethodExecutor<List<Record>>();
        return methodExecutor.execute("selectByExampleWithBLOBsForStartPage", example);
    }

    @Override
    public List<Record> selectByExampleForStartPage(Example example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, false);
        MethodExecutor<List<Record>> methodExecutor = new MethodExecutor<List<Record>>();
        return methodExecutor.execute("selectByExampleForStartPage", example);
    }

    @Override
    public List<Record> selectByExampleWithBLOBsForOffsetPage(Example example, Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit, false);
        MethodExecutor<List<Record>> methodExecutor = new MethodExecutor<List<Record>>();
        return methodExecutor.execute("selectByExampleWithBLOBsForOffsetPage", example);
    }

    @Override
    public List<Record> selectByExampleForOffsetPage(Example example, Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit, false);
        MethodExecutor<List<Record>> methodExecutor = new MethodExecutor<List<Record>>();
        return methodExecutor.execute("selectByExampleForOffsetPage", example);
    }

    @Override
    public Record selectFirstByExample(Example example) {
        MethodExecutor<Record> methodExecutor = new MethodExecutor<Record>();
        return methodExecutor.execute("selectFirstByExample", example);
    }

    @Override
    public Record selectFirstByExampleWithBLOBs(Example example) {
        MethodExecutor<Record> methodExecutor = new MethodExecutor<Record>();
        return methodExecutor.execute("selectFirstByExampleWithBLOBs", example);
    }

    @Override
    public Record selectByPrimaryKey(Integer id) {
        MethodExecutor<Record> methodExecutor = new MethodExecutor<Record>();
        return methodExecutor.execute("selectByPrimaryKey", id);
    }

    @Override
    public int updateByExampleSelective(Record record, Example example) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("updateByExampleSelective", record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(Record record, Example example) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("updateByExampleWithBLOBs", record, example);
    }

    @Override
    public int updateByExample(Record record, Example example) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("updateByExample", record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
        return methodExecutor.execute("updateByPrimaryKey", record);
    }

    @Override
    public int deleteByPrimaryKeys(String ids) {
        if (StringUtils.isBlank(ids)) {
            return 0;
        }
        String[] idArray = ids.split("-");
        int count = 0;
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            Integer id = Integer.parseInt(idStr);
            MethodExecutor<Integer> methodExecutor = new MethodExecutor<Integer>();
            Integer result = methodExecutor.execute("deleteByPrimaryKey", id);
            count += result;
        }
        return count;

    }
}
