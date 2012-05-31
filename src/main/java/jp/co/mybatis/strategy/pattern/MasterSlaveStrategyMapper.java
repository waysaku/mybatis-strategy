package jp.co.mybatis.strategy.pattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import jp.co.mybatis.strategy.Master;

import org.apache.ibatis.binding.BindingException;



public class MasterSlaveStrategyMapper<S> implements StrategyMapper {
	private S masterMapper;
	private S slaveMapper;
	private Class<?> interfaceClass;


	public void setMasterMapper(S masterMapper) {
		this.masterMapper = masterMapper;
	}

	public void setSlaveMapper(S slaveMapper) {
		this.slaveMapper = slaveMapper;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Class<T> generateMapper() {
		ClassLoader classLoader = interfaceClass.getClassLoader();
		Class<?>[] interfaces = new Class[]{interfaceClass};
		
		return (Class<T>) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				S mapper = method.getAnnotation(Master.class) != null ? masterMapper : slaveMapper;

				Method[] methods = mapper.getClass().getMethods();
				for(Method m : methods) {
					if(m.equals(method.getName())) {
						return m.invoke(mapper, args);
					}
				}
				return null;
			}
		});
	}


}
