package komota.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

class JTextFieldTest extends JFrame{
  public static void main(String args[]){
    JTextFieldTest frame = new JTextFieldTest("タイトル");
    frame.setVisible(true);
  }

  JTextArea text2 = null;

  JTextFieldTest(String title){
    setTitle(title);
    setBounds(100, 100, 300, 250);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel p = new JPanel();

    JLabel label1 = new JLabel();
    label1.setText("なぜ、そのように動かしましたか？");
    this.text2 = new JTextArea(6, 20);


    JButton next = new JButton("次へ進む");
    next.addActionListener(new MyActionListener());

    p.add(label1);
//   p.add(text1);
    p.add(text2);
//    p.add(text3);
    p.add(next);
    Container contentPane = getContentPane();
    contentPane.add(p, BorderLayout.CENTER);
  }


  class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			System.out.println(JTextFieldTest.this.text2.getText());
			JTextFieldTest.this.dispose();
		}

  }
}