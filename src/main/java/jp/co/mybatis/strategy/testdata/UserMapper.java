package jp.co.mybatis.strategy.testdata;

import org.apache.ibatis.annotations.Select;

public interface UserMapper {
	@Select("select * from user")
	public User getUser();
}
