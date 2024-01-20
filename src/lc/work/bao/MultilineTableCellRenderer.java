package lc.work.bao;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

class MultilineTableCellRenderer extends JTextArea implements TableCellRenderer {
	//起渲染器作用，可以使得content内题目换行
    public MultilineTableCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((String) value);
        return this;
    }
}
