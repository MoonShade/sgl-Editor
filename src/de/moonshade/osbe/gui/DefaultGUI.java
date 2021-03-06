/*
 * Copyright (c) 2011 Dominik Halfkann.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Dominik Halfkann
 */

/**
 * 
 */
package de.moonshade.osbe.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import de.moonshade.osbe.Main;
import de.moonshade.osbe.menuhandler.Action;
import de.moonshade.osbe.menuhandler.MenuHandler;
import de.moonshade.osbe.serializable.Options;

/**
 * Default graphical user interface.
 * <p>
 * This Class contains all the Information about anything the user sees, like
 * windows. Designed using Swing.
 * 
 * @author Dominik Halfkann
 * @since 1.6
 */
public class DefaultGUI implements GUI {

	// Some variables
	private String title;
	private String filename = "";
	private String unsaved = "";
	private Main main;
	private Main windowClosingHandler = null;
	private MenuHandler handler = null;
	private Options options = null;

	// Swing object variables
	private JFrame mainFrame = null;
	private JPanel mainContent = null;
	private JMenuBar menuBar = null;
	private JTabbedPane tabbedPaneLeft = null;
	private JTabbedPane tabbedPaneCenter = null;
	private JTabbedPane objectTabbedPaneLeft = null;
	private JTabbedPane objectTabbedPaneCenter = null;
	private JTree explorerTree;
	private JTabbedPane fileContentContainer;
	private RTextScrollPane sourceContentScroll = null;
	private RSyntaxTextArea sourceContentArea = null;
	private RTextScrollPane objectContentScroll = null;
	private RSyntaxTextArea objectContentArea = null;
	private JList objectMethodList;
	private DefaultListModel objectMethodListModel = null;
	private JPanel methodListToolBar;
	private ArrayList<JMenu> menuList = new ArrayList<JMenu>();

	public DefaultGUI(Main main, Options options) {
		this.main = main;
		this.options = options;
	}

	@Override
	public void createMenu(String title) {
		JMenu jmenu = new JMenu(title);
		menuBar.add(jmenu);
		menuList.add(jmenu);
	}

	@Override
	public void createMenuItem(String title, int id, final Action action, KeyStroke keyStroke) {
		JMenuItem jmenuitem = new JMenuItem(title);
		final GUI gui = this;
		jmenuitem.setAccelerator(keyStroke);
		jmenuitem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.doAction(action, gui);
			}
		});
		menuList.get(id).add(jmenuitem);

	}

	@Override
	public RSyntaxTextArea getContentArea() {
		return sourceContentArea;
	}

	@Override
	public JPanel getContentPanel() {
		// TODO Auto-generated method stub
		return mainContent;
	}

	private JTree getExplorer() {
		if (explorerTree == null) {
			explorerTree = new JTree();
			explorerTree.setDragEnabled(true);
		}
		return explorerTree;
	}

	private JTabbedPane getFileContentContainer() {
		if (fileContentContainer == null) {
			fileContentContainer = new JTabbedPane();
			fileContentContainer.add("SGL/OOSBL", getObjectContentScroll());
			fileContentContainer.add("Generated Storyboard Code", getSourceContentScroll());

		}
		return fileContentContainer;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return mainFrame.getHeight();
	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return mainFrame.getLocation();
	}

	@Override
	public String getMainClassContent() {
		return objectContentArea.getText();
	}

	private JPanel getMainContent() {
		if (mainContent == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(2);
			mainContent = new JPanel();
			mainContent.setLayout(borderLayout);
			// mainContent.add(getSourceContentScroll(), BorderLayout.CENTER);
			mainContent.add(getFileContentContainer(), BorderLayout.CENTER);

			/*
			 * mainContent.add(getStatusBar(), BorderLayout.SOUTH);
			 * mainContent.add(getToolBar(), BorderLayout.NORTH);
			 */
		}
		return mainContent;
	}

	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();

		}
		return menuBar;
	}

	private JPanel getMethodListHeadPanel() {
		JPanel methodListHeadPanel = new JPanel();
		methodListHeadPanel.setPreferredSize(new Dimension(3000, 20));
		methodListHeadPanel.setMaximumSize(new Dimension(3000, 20));
		methodListHeadPanel.add(new JLabel("Methods:"));
		return methodListHeadPanel;
	}

	private JPanel getMethodListToolBar() {
		if (methodListToolBar == null) {
			methodListToolBar = new JPanel();
			methodListToolBar.setPreferredSize(new Dimension(3000, 28));
			methodListToolBar.setMaximumSize(new Dimension(3000, 28));
			methodListToolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JButton add = new JButton(new ImageIcon("icons/add.png"));
			add.setPreferredSize(new Dimension(30, 25));
			add.setMaximumSize(new Dimension(30, 25));
			add.setToolTipText("Create a new method");
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					objectMethodListModel.addElement(JOptionPane
							.showInputDialog("Enter the name of the new method:"));
				}
			});
			methodListToolBar.add(add);
			JButton del = new JButton(new ImageIcon("icons/delete.png"));
			del.setPreferredSize(new Dimension(30, 25));
			del.setMaximumSize(new Dimension(30, 25));
			del.setToolTipText("Delete the selected method");
			del.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					objectMethodListModel.remove(objectMethodList.getSelectedIndex());
				}
			});
			methodListToolBar.add(del);
			JButton edit = new JButton(new ImageIcon("icons/pencil.png"));
			edit.setPreferredSize(new Dimension(30, 25));
			edit.setMaximumSize(new Dimension(30, 25));
			edit.setToolTipText("Change the name of the selected method");
			edit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					objectMethodListModel.set(objectMethodList.getSelectedIndex(),
							JOptionPane.showInputDialog("Change the name of the method:"));
				}
			});
			methodListToolBar.add(edit);

		}
		return methodListToolBar;
	}

	private RSyntaxTextArea getObjectContentArea() {
		if (objectContentArea == null) {
			objectContentArea = new RSyntaxTextArea();

			// Adding Auto-Completition
			SGLCompletitionProvider scp = new SGLCompletitionProvider();
			AutoCompletion ac = new AutoCompletion(scp.getProvider());
			ac.setAutoActivationEnabled(true);
			// ac.setAutoActivationDelay(500);
			ac.setParameterAssistanceEnabled(true);
			ac.setShowDescWindow(true);
			ac.setAutoCompleteEnabled(true);
			ac.setListCellRenderer(new SGLCellRenderer());
			ac.setAutoCompleteSingleChoices(false);
			ac.setChoicesWindowSize(420, 150);
			ac.setDescriptionWindowSize(350, 250);
			ac.install(objectContentArea);

			/*
			 * Note: At this point, I set my syntax highlighter for osu files.
			 * It was created with the TokenTokenMaker, so it's not that
			 * perfect.
			 */
			objectContentArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SGL);

			objectContentArea.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					// Just to delete (saved) in the title, if changes were made
					unsaved = "";
					if (filename.equals("")) {
						mainFrame.setTitle(title);
					} else {
						mainFrame.setTitle(title + " - " + filename + unsaved);
					}
					
				}
			});

			// objectContentArea.setText("\n\nif (2 > 2) {\n   int t = 1\n   t = t + 1\n} else if (2 == 2) {\n   int x = 2\n   x = x + 1\n}");
			// objectContentArea.setText("\n Sprite test = new Sprite(\"sb/test\")\n test.move(10,20)\n test.move(100,20,40)\n test.move(100,200,20,40,30,60)");
			// objectContentArea.setText(" Sprite test = new Sprite(\"sb/test\")\n at (100) test.move(20,40)");
			// objectContentArea.setText(" int t = 1\n int x = rand(t,2)");
			// objectContentArea
			// .setText("if (2 == 2) {\n  Sprite test = new Sprite(\"sb/test\")\n  at (rand(1,10)*100) test.move(rand(10,20),30)\n  test.moveSpeedUp(100, 200, 10, 20, 10, 30)\n}");

		}
		return objectContentArea;
	}

	private RTextScrollPane getObjectContentScroll() {
		if (objectContentScroll == null) {
			objectContentScroll = new RTextScrollPane();
			objectContentScroll.setViewportView(getObjectContentArea());
			objectContentScroll.setLineNumbersEnabled(true);
		}
		return objectContentScroll;
	}

	private JList getObjectMethodList() {
		if (objectMethodList == null) {
			objectMethodListModel = new DefaultListModel();
			objectMethodListModel.addElement("Jane Doe");
			objectMethodListModel.addElement("John Smith");
			objectMethodListModel.addElement("Kathy Green");

			objectMethodList = new JList(objectMethodListModel);
		}
		return objectMethodList;
	}

	private JScrollPane getObjectMethodScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(getObjectMethodList());
		return scrollPane;
	}

	private JSplitPane getObjectSplitPanel() {
		JPanel objectListPanel = new JPanel();
		// JSplitPane.VERTICAL_SPLIT,getObjectMethodList(),
		// getMethodListToolBar()
		objectListPanel.setLayout(new BorderLayout(2, 2));
		objectListPanel.add(getMethodListHeadPanel(), BorderLayout.NORTH);
		objectListPanel.add(getObjectMethodScrollPane(), BorderLayout.CENTER);
		objectListPanel.add(getMethodListToolBar(), BorderLayout.SOUTH);

		JSplitPane objectSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, objectListPanel,
				getObjectContentScroll());
		// splitPaneLeft.setOneTouchExpandable(true);
		objectSplitPanel.setDividerSize(2);
		// splitPaneLeft.set
		objectSplitPanel.setDividerLocation(100);
		return objectSplitPanel;
	}

	private RTextScrollPane getSomething() {

		RTextScrollPane something = new RTextScrollPane();
		something = new RTextScrollPane();
		something.setViewportView(getSourceContentArea());
		something.setLineNumbersEnabled(true);

		return something;
	}

	/**
	 * This method initializes sourceContentArea
	 * 
	 * @return org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
	 */
	private RSyntaxTextArea getSourceContentArea() {
		if (sourceContentArea == null) {
			sourceContentArea = new RSyntaxTextArea();
			/*
			 * Note: At this point, I set my syntax highlighter for osu files.
			 * It was created with the TokenTokenMaker, so it's not that
			 * perfect.
			 */
			sourceContentArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_OSU);

		}
		return sourceContentArea;
	}

	/**
	 * This method initializes sourceContentScroll
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private RTextScrollPane getSourceContentScroll() {
		if (sourceContentScroll == null) {
			sourceContentScroll = new RTextScrollPane();
			sourceContentScroll.setViewportView(getSourceContentArea());
			sourceContentScroll.setLineNumbersEnabled(true);
		}
		return sourceContentScroll;
	}

	private JSplitPane getSplitSections() {
		JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getTabbedPaneLeft(),
				getTabbedPaneCenter());
		// splitPaneLeft.setOneTouchExpandable(true);
		splitPaneLeft.setDividerSize(2);
		// splitPaneLeft.set
		splitPaneLeft.setDividerLocation(150);
		return splitPaneLeft;
	}

	private JTabbedPane getTabbedPaneCenter() {
		if (tabbedPaneCenter == null) {
			tabbedPaneCenter = new JTabbedPane();
		}
		return tabbedPaneCenter;
	}

	private JTabbedPane getTabbedPaneLeft() {
		if (tabbedPaneLeft == null) {
			tabbedPaneLeft = new JTabbedPane();
		}
		return tabbedPaneLeft;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return mainFrame.getWidth();
	}

	@Override
	public void init(String title) {
		if (mainFrame == null) {

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mainFrame = new JFrame();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setSize(options.getLastWidth(), options.getLastHeight());
			mainFrame.setLocation(options.getLastLocation());
			mainFrame.setTitle(title);
			mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("icons/sgl.png"));
			this.title = title;

			mainFrame.setJMenuBar(getMenuBar());
			mainFrame.setContentPane(getMainContent());
			initLayout();
		}
	}

	private void initLayout() {
		// tabbedPaneLeft.add("Explorer", getExplorer());
		// tabbedPaneCenter.add("something...ing.osb",
		// getFileContentContainer());
	}

	@Override
	public void onClose(Main handler) {
		windowClosingHandler = handler;
		mainFrame.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				windowClosingHandler.onGUIClosed();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void setMenuHandler(MenuHandler handler) {
		// TODO Auto-generated method stub
		this.handler = handler;
	}

	@Override
	public File showFileChooser(String title) {
		if (Main.debug)
			System.out.print("clicked");
		JFileChooser fc = new JFileChooser(options.getLastPath());
		fc.setFileFilter(new SGLFileFilter());
		fc.setPreferredSize(new Dimension(640, 480));
		AbstractButton button = SwingUtils.getDescendantOfType(AbstractButton.class, fc, "Icon",
				UIManager.getIcon("FileChooser.detailsViewIcon"));
		button.doClick();
		// fc.setApproveButtonText(title);

		if (fc.showDialog(mainFrame, title) == JFileChooser.APPROVE_OPTION) {
			options.setLastPath(fc.getSelectedFile().getParentFile().getAbsolutePath());
			return fc.getSelectedFile();
		}

		return null;
	}

	@Override
	public void start() {
		mainFrame.setVisible(true);

	}

	@Override
	public RSyntaxTextArea getMainClassContentArea() {
		// TODO Auto-generated method stub
		return objectContentArea;
	}

	@Override
	public void setFileName(String name) {
		// TODO Auto-generated method stub
		this.filename = name;
		if (name.equals("")) {
			mainFrame.setTitle(title);
		} else {
			mainFrame.setTitle(title + " - " + filename + unsaved);
		}
	}

	@Override
	public String getFileName() {
		return filename;
	}

	@Override
	public void setSaved(boolean saved) {
		// TODO Auto-generated method stub
		if (saved) {
			unsaved = " (saved)";
			mainFrame.setTitle(title + " - " + filename + unsaved);
		} else
			unsaved = "";
	}

}
