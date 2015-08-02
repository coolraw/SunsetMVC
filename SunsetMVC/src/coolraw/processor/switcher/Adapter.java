package coolraw.processor.switcher;

public interface Adapter {
	public boolean support(Object parameterType);
	public Object Switch(Object parameter);
}
