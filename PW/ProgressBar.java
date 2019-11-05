package hw4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

@SuppressWarnings("serial")
/**
 * class that creates a progress bar
 * 
 * @author Julio Hernandez & Jonathan Argumedo & Jose Lujan
 *
 */
public class ProgressBar extends JPanel implements ActionListener, PropertyChangeListener {

	private JProgressBar progressBar;
	private Task task;

	/**
	 * 
	 * subclass to create a progressbar panel
	 *
	 */
	class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			int progress = 0;
			while (progress <= 100) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException ignore) {
				}
				progress += 5;
				setProgress(progress);
			}

			return null;
		}
	}

	/**
	 * Set progress bar dimensions
	 */
	public ProgressBar() {
		super(new BorderLayout());

		progressBar = new JProgressBar(0, 100);
		progressBar.setPreferredSize(new Dimension(375, 25));
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.add(progressBar);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();

		add(panel, BorderLayout.PAGE_START);
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	/**
	 * required override method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
