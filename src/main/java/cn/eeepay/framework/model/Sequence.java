package cn.eeepay.framework.model;

/**
 * table sequence
 * desc 提示语
 * 
 * @author tans
 */
public class Sequence {
	private String name;
	private Integer currentValue;
	private Integer increment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}

	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}

	public Sequence() {
	}

	public Sequence(String name, Integer currentValue, Integer increment) {
		this.name = name;
		this.currentValue = currentValue;
		this.increment = increment;
	}
}