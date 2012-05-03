package ch.openech.client.ewk.event;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import ch.openech.mj.toolkit.ProgressListener;


// Swing Bug: ProgressMontior cannot be modal
public class ThreadSafeProgressMonitor extends ProgressMonitor implements ProgressListener {
	private final Component parentComponent;
	
	public ThreadSafeProgressMonitor(Component parentComponent, Object message, String note, int min, int max) {
		super(parentComponent, message, note, min, max);
		this.parentComponent = parentComponent;
	}
	
	public void invokeSetMaximum(final int m) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ThreadSafeProgressMonitor.this.setMaximum(m);
			}
		});
	}

	public void invokeSetProgress(final int vn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ThreadSafeProgressMonitor.this.setProgress(vn);
			}
		});
	}

	
	public void invokeSetNote(final String note) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ThreadSafeProgressMonitor.this.setNote(note);
			}
		});
	}

	public void showInformation(final String information) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ThreadSafeProgressMonitor.this.close();
				JOptionPane.showMessageDialog(parentComponent, information);
			}
		});
	}
	
	@Override
	public void showProgress(final int value, final int maximum) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ThreadSafeProgressMonitor.this.setProgress(value);
				ThreadSafeProgressMonitor.this.setMaximum(maximum);
			}
		});
	}

}
