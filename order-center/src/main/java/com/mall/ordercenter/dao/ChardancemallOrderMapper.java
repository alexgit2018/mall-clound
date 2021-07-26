package com.mall.ordercenter.dao;

import com.mall.ordercenter.domain.ChardancemallOrder;
import com.mall.ordercenter.domain.ChardancemallOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChardancemallOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    long countByExample(ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int deleteByExample(ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int insert(ChardancemallOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int insertSelective(ChardancemallOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    ChardancemallOrder selectOneByExample(ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    ChardancemallOrder selectOneByExampleSelective(@Param("example") ChardancemallOrderExample example, @Param("selective") ChardancemallOrder.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    List<ChardancemallOrder> selectByExampleSelective(@Param("example") ChardancemallOrderExample example, @Param("selective") ChardancemallOrder.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    List<ChardancemallOrder> selectByExample(ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    ChardancemallOrder selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") ChardancemallOrder.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    ChardancemallOrder selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    ChardancemallOrder selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ChardancemallOrder record, @Param("example") ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ChardancemallOrder record, @Param("example") ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ChardancemallOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ChardancemallOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") ChardancemallOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chardancemall_order
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}