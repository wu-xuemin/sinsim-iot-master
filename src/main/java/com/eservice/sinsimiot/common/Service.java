package com.eservice.sinsimiot.common;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T> {
    //持久化
    boolean save(T model);

    //批量持久化
    boolean save(List<T> models);

    //通过主鍵刪除
    boolean deleteById(Object id);

    //批量刪除 eg：ids -> “1,2,3,4”
    boolean deleteByIds(String ids);

    //更新
    boolean update(T model);

    //通过ID查找
    T findById(Object id);

    T findBy(String fieldName, Object value) throws TooManyResultsException; //通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

    List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”

    List<T> findByCondition(Condition condition);//根据条件查找

    List<T> findAll();//获取所有
}
