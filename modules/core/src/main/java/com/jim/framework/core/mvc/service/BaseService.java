package com.jim.framework.core.mvc.service;

/**
 * 不带有Example的Service接口
 */
public interface BaseService<Record> {

    /**
     * 根据主键删除记录
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);
    /**
     * 插入记录
     * @param record
     * @return
     */
    int insert(Record record);

    /**
     * 插入记录有效字段
     * @param record
     * @return
     */
    int insertSelective(Record record);

    /**
     * 根据主键查询记录
     * @param id
     * @return
     */
    Record selectByPrimaryKey(Integer id);
    /**
     * 根据主键更新记录有效字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Record record);
    /**
     * 根据主键更新记录，附带BLOB字段
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(Record record);
    /**
     * 根据主键更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(Record record);
    /**
     * 根据主键批量删除记录
     * @param ids
     * @return
     */
    int deleteByPrimaryKeys(String ids);
    /**
     * 初始化mapper
     */
    void initMapper();

}
