package ch.openech.test.ui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class SwingRunner extends BlockJUnit4ClassRunner {

	public SwingRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
	
	@Override
	public void run(final RunNotifier arg0) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					SwingRunner.super.run(arg0);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
