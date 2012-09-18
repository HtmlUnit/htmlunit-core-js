package net.sourceforge.htmlunit;

import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

/**
 * Runs the unit test, expecting them to fail when run with unpatched Rhino version
 * (in fact when system property {@link #PROPERTY_RHINO} is set to {@link #PROPERTY_RHINO_VALUE_ORIGINAL}).
 * @author Marc Guillemot
 */
public class RhinoPatchedRunner extends JUnit4ClassRunner {

	private boolean runWithOriginalRhino;
	public static final String PROPERTY_RHINO = "rhino";
	public static final String PROPERTY_RHINO_VALUE_ORIGINAL = "original";


	public RhinoPatchedRunner(final Class<?> klass) throws InitializationError {
		super(klass);
		
		runWithOriginalRhino = PROPERTY_RHINO_VALUE_ORIGINAL.equalsIgnoreCase(System.getProperty(PROPERTY_RHINO));
	}


	@Override
	protected TestMethod wrapMethod(final Method method) {
		if (runWithOriginalRhino) {
			return new TestMethod(method, getTestClass())
			{
				@Override
				protected Class<? extends Throwable> getExpectedException() {
					return Throwable.class;
				}
			};
		}
		else {
			return super.wrapMethod(method);
		}
	}
	
}
