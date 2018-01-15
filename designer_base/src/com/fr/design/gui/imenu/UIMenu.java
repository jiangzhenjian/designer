package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UIMenu extends JMenu {
	private static final float REC = 8f;
	private JPopupMenu popupMenu;

	public UIMenu(String name) {
		super(name);
		setName(name);
		setRolloverEnabled(true);
		setBackground(UIConstants.NORMAL_BACKGROUND);
	}

	@Override
	public String getText() {
		if (this.getParent() instanceof JPopupMenu) {
			return StringUtils.BLANK + super.getText();
		}
		return "  " + super.getText();
	}

	public JPopupMenu getPopupMenu() {
		ensurePopupMenuCreated();
		popupMenu.setOpaque(false);
		popupMenu.setBorder(new Border() {

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				Graphics2D g2d = (Graphics2D) g;
				int rec = (int) REC;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				g2d.setColor(UIConstants.UIPOPUPMENU_LINE_COLOR);
				g2d.drawRoundRect(x, y, width - 1, height - 1,rec, rec);
				if (!(UIMenu.this.getParent() instanceof JPopupMenu)) {
					g.setColor(UIConstants.NORMAL_BACKGROUND);
					g.drawLine(1, 0, UIMenu.this.getWidth() - 2, 0);
				}
			}

			@Override
			public boolean isBorderOpaque() {
				return false;
			}

			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(5, 2, 10, 10);
			}
		});
		return popupMenu;
	}

	protected void ensurePopupMenuCreated() {
		if (popupMenu == null) {
			this.popupMenu = new JPopupMenu() {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					int rec = (int) REC;
					Shape shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), REC, REC);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setColor(UIConstants.NORMAL_BACKGROUND);
					g2d.fillRoundRect(0, 0, getWidth(), getHeight(), rec, rec);
					g2d.setClip(shape);
					super.paintComponent(g2d);
				}

			};
			popupMenu.setInvoker(this);
			if (popupMenu.getComponentCount() != 0) {
				System.out.println(popupMenu.getComponentCount());
			}
			popupListener = createWinListener(popupMenu);
		}
	}

	/**
	 *画界面
	 */
	@Override
	public void updateUI() {
		setUI(new UIMenuUI());
	}

	/**
	 * 判断popupmeu是否隐藏
	 * @return  如果隐藏 返回true
	 */
	public boolean isPopupMenuVisible() {
		ensurePopupMenuCreated();
		return popupMenu.isVisible();
	}


	/**
	 * 设置popupmenu位置
	 * @param x
	 * @param y
	 */
	public void setMenuLocation(int x, int y) {
		super.setMenuLocation(x, y);
		if (popupMenu != null) {
			popupMenu.setLocation(x, y);
		}
	}

	/**
	 * 向popupmenu添加 JMenuItem
	 * @param menuItem 菜单项
	 * @return    菜单项
	 */
	public JMenuItem add(JMenuItem menuItem) {
		ensurePopupMenuCreated();
		return popupMenu.add(menuItem);
	}

	/**
	 * 添加组件
	 * @param c   组件
	 * @return    组件
	 */
	public Component add(Component c) {
		ensurePopupMenuCreated();
		popupMenu.add(c);
		return c;
	}

	/**
	 * 向指定位置添加组件
	 * @param c       组件
	 * @param index     位置
	 * @return   组件
	 */
	public Component add(Component c, int index) {
		ensurePopupMenuCreated();
		popupMenu.add(c, index);
		return c;
	}


	/**
	 * 添加分隔符
	 */
	public void addSeparator() {
		ensurePopupMenuCreated();
		popupMenu.addSeparator();
	}

	/**
	 * 添加menuitem到指定位置
	 * @param s      字符
	 * @param pos     位置
	 */
	public void insert(String s, int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		popupMenu.insert(new JMenuItem(s), pos);
	}

	/**
	 * 添加么会特么到指定位置
	 * @param mi     菜单项
	 * @param pos   位置
	 * @return       菜单项
	 */
	public JMenuItem insert(JMenuItem mi, int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}
		ensurePopupMenuCreated();
		popupMenu.insert(mi, pos);
		return mi;
	}

	/**
	 * 添加到指定位置
	 * @param a      事件
	 * @param pos   位置
	 * @return       菜单项
	 */
	public JMenuItem insert(Action a, int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		JMenuItem mi = new JMenuItem(a);
		mi.setHorizontalTextPosition(JButton.TRAILING);
		mi.setVerticalTextPosition(JButton.CENTER);
		popupMenu.insert(mi, pos);
		return mi;
	}

	/**
	 *  添加分隔符到指定位置
	 * @param index  指定位置
	 */
	public void insertSeparator(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		popupMenu.insert(new JPopupMenu.Separator(), index);
	}

	/**
	 * 得到子元素
	 * @return  子元素
	 */
	public MenuElement[] getSubElements() {
		return popupMenu == null ? new MenuElement[0] : new MenuElement[]{popupMenu};
	}
}