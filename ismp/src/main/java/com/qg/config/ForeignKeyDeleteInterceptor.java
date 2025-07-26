package com.qg.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.qg.domain.ForeignKeyInformation;
import com.qg.utils.ForeignKeyDeleteUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Slf4j
@Component
public class ForeignKeyDeleteInterceptor implements InnerInterceptor {

    @Autowired
    private ForeignKeyDeleteUtils foreignKeyDeleteUtils;
    @Autowired
    private ApplicationContext applicationContext;
    
    private final Map<String, Object> tableMapperMap = new HashMap<>();
    private final Map<String, TableInfo> tableInformationMap = new HashMap<>();

    /**
     * 执行级联逻辑删除
     * @param executor
     * @param mappedStatement
     * @param parameter
     */
    @Override
    public void beforeUpdate(Executor executor, MappedStatement mappedStatement, Object parameter) {
        // 判断当前操作是否为逻辑删除
        if (isLogicDeleteOperation(mappedStatement, parameter)) {
            // 获取表信息
            TableInfo tableInfo = getTableInfo(mappedStatement);
            if (tableInfo != null) {
                // 获取被删除记录的主键值
                Object idValue = getDeleteIdValue(parameter, tableInfo);
                if (idValue != null) {
                    // 执行级联逻辑删除
                    cascadeLogicDelete(tableInfo.getTableName(), idValue);
                }
            }
        }
    }

    /**
     * 判断是否是逻辑删除操作
     * @param mappedStatement
     * @param parameter
     * @return
     */
    private boolean isLogicDeleteOperation(MappedStatement mappedStatement, Object parameter) {
        // 判断sql语句是否为删除
        if (mappedStatement.getSqlCommandType() == SqlCommandType.DELETE) {
            // 判断是否启用了逻辑删除
            TableInfo tableInfo = getTableInfo(mappedStatement);
            return tableInfo != null && tableInfo.isWithLogicDelete();
        }

        // 处理更新操作中的逻辑删除
        if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE) {
            if (parameter != null) {
                // 获取实体类的表信息
                TableInfo tableInfo = getTableInfoByParameter(parameter);
                if (tableInfo != null && tableInfo.isWithLogicDelete()) {
                    // 获取逻辑删除字段信息
                    TableFieldInfo logicDeleteField = tableInfo.getLogicDeleteFieldInfo();
                    // 获取参数中设置的逻辑删除字段值
                    Object deletedValue = tableInfo.getPropertyValue(parameter, logicDeleteField.getProperty());
                    // 比较是否等于逻辑删除值
                    return logicDeleteField.getLogicDeleteValue().equals(deletedValue);
                }
            }
        }

        return false;
    }

    /**
     * 根据参数获取表信息
     * @param parameter 方法参数
     * @return 表信息
     */
    private TableInfo getTableInfoByParameter(Object parameter) {
        Class<?> parameterClass = parameter.getClass();
        // 如果Object被Spring AOP或MyBatis代理，获取实际类型
        if (parameterClass.getName().contains("com.sun.proxy.$Proxy")) {
            parameterClass = parameterClass.getSuperclass();
        }
        // 通过mybatis-plus获取表信息
        return TableInfoHelper.getTableInfo(parameterClass);
    }

    /**
     * 获取表信息
     * @param mappedStatement MappedStatement对象
     * @return 表信息
     */
    private TableInfo getTableInfo(MappedStatement mappedStatement) {
        try {
            // 从MappedStatement的ID中提取mapper接口全类名
            String mappedStatementId = mappedStatement.getId();
            String mapperClassName = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
            Class<?> mapperClass = Class.forName(mapperClassName);

            // 获取mapper接口的泛型参数（实体类）
            Type[] genericInterfaces = mapperClass.getGenericInterfaces();
            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType parameterizedType) {
                    // BaseMapper的泛型参数即为实体类
                    Class<?> entityClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    return TableInfoHelper.getTableInfo(entityClass);
                }
            }
        } catch (Exception e) {
            // 日志记录：获取表信息失败
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * 获取被删除记录的主键值
     * @param parameter 方法参数
     * @param tableInfo 表信息
     * @return 主键值
     */
    private Object getDeleteIdValue(Object parameter, TableInfo tableInfo) {
        // 处理deleteById方法的参数（直接是ID值）
        if (parameter != null && isSimpleType(parameter.getClass())) {
            return parameter;
        }

        // 处理其他情况（参数为实体对象）
        if (parameter != null) {
            return tableInfo.getPropertyValue(parameter, tableInfo.getKeyProperty());
        }

        return null;
    }

    /**
     * 判断是否是简单类型（基本类型及其包装类、String）
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Boolean.class ||
                clazz == Character.class ||
                clazz == Byte.class ||
                clazz == Short.class;
    }

    /**
     * 执行级联逻辑删除
     * @param tableName 主表名
     * @param idValue 主表被删除记录的主键值
     */
    private void cascadeLogicDelete(String tableName, Object idValue) {
        // 获取当前表的所有外键关联信息
        Set<ForeignKeyInformation> foreignKeys = foreignKeyDeleteUtils.getForeignKey(tableName);

        for (ForeignKeyInformation fk : foreignKeys) {
            String fkTable = fk.getForeignKeyTable();      // 外键表名
            String fkColumn = fk.getForeignKeyColumn();    // 外键列名

            // 执行当前外键表的逻辑删除
            executeLogicDelete(fkTable, fkColumn, idValue);

            // 递归处理外键表的关联表（实现多级级联删除）
            cascadeLogicDelete(fkTable, idValue);
        }
    }

    /**
     * 执行单个表的逻辑删除
     * @param tableName 表名
     * @param columnName 外键列名
     * @param idValue 主表主键值
     */
    private void executeLogicDelete(String tableName, String columnName, Object idValue) {
        // 获取表对应的Mapper
        Object mapper = getMapperByTableName(tableName);
        if (mapper == null) {
            System.err.println("未找到表 " + tableName + " 对应的Mapper，无法执行级联删除");
            return;
        }

        // 获取表信息
        TableInfo tableInfo = getTableInfoByTableName(tableName);
        if (tableInfo == null) {
            System.err.println("未找到表 " + tableName + " 的表信息，无法执行级联删除");
            return;
        }

        // 创建更新条件
        LambdaUpdateWrapper<Object> updateWrapper = new LambdaUpdateWrapper<>();
        // 设置外键条件
        updateWrapper.eq(getColumnFunction(tableInfo, columnName), idValue);
        // 设置逻辑删除字段值
        updateWrapper.setSql(tableInfo.getLogicDeleteFieldInfo().getColumn() + " = " + tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue());

        // 使用反射调用update方法
        try {
            Method updateMethod = mapper.getClass().getMethod("update", Object.class, com.baomidou.mybatisplus.core.conditions.Wrapper.class);
            updateMethod.invoke(mapper, null, updateWrapper);
        } catch (Exception e) {
            throw new RuntimeException("执行级联删除失败", e);
        }
    }

    /**
     * 获取字段对应的Lambda函数
     * @param tableInfo 表信息
     * @param columnName 数据库列名
     * @return 字段对应的Lambda函数
     */
    private com.baomidou.mybatisplus.core.toolkit.support.SFunction<Object, ?> getColumnFunction(TableInfo tableInfo, String columnName) {
        // 查找列名对应的属性名
        String propertyName = null;
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (fieldInfo.getColumn().equals(columnName)) {
                propertyName = fieldInfo.getProperty();
                break;
            }
        }

        if (propertyName == null) {
            throw new RuntimeException("表 " + tableInfo.getTableName() + " 中未找到列 " + columnName + " 对应的属性");
        }

        // 通过反射生成SFunction
        try {
            // 获取getter方法
            String getterMethodName = "get" + this.capitalize(propertyName);
            Method getterMethod = tableInfo.getEntityType().getMethod(getterMethodName);

            // 生成SFunction lambda表达式
            return obj -> {
                try {
                    return getterMethod.invoke(obj);
                } catch (Exception e) {
                    throw new RuntimeException("调用getter方法失败", e);
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("生成字段 " + propertyName + " 的Lambda表达式失败", e);
        }
    }

    /**
     * 根据表名获取Mapper
     * @param tableName 表名
     * @return Mapper对象
     */
    private Object getMapperByTableName(String tableName) {
        return tableMapperMap.computeIfAbsent(tableName, key -> {
            // 将表名转换为Mapper Bean名称（例如：user_info -> userInfoMapper）
            String mapperBeanName = convertTableNameToMapperBeanName(tableName);
            return applicationContext.getBean(mapperBeanName);
        });
    }

    /**
     * 根据表名获取表信息
     * @param tableName 表名
     * @return 表信息
     */
    private TableInfo getTableInfoByTableName(String tableName) {
        return tableInformationMap.computeIfAbsent(tableName, key -> {
            Object mapper = getMapperByTableName(tableName);
            Class<?> entityClass = getEntityClass(mapper);
            return TableInfoHelper.getTableInfo(entityClass);
        });
    }

    /**
     * 获取Mapper的实体类
     */
    private Class<?> getEntityClass(Object mapper) {
        Type[] interfaces = mapper.getClass().getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                // 第一个泛型参数是实体类
                return (Class<?>) parameterizedType.getActualTypeArguments()[0];
            }
        }
        return null;
    }

    /**
     * 将表名转换为Mapper Bean名称
     * 规则：下划线转驼峰，首字母小写，加上"Mapper"后缀
     */
    private String convertTableNameToMapperBeanName(String tableName) {
        String[] parts = tableName.split("_");
        StringBuilder beanName = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                beanName.append(parts[i]);
            } else {
                beanName.append(this.capitalize(parts[i]));
            }
        }
        return StringUtils.firstToLowerCase(beanName.toString()) + "Mapper";
    }

    private String capitalize(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}