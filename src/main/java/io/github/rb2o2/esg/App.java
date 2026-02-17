package io.github.rb2o2.esg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        new AppFrame();
    }
}

class AppFrame extends JFrame {
    private static final double MAX_CHARGE = 10.0;
    private final Color p1 = new Color(19, 200, 42);
    private final Color p2 = new Color(181, 0, 75);
    private final Color c1 = new Color(0,255,0);
    private final Color c2 = new Color(255,0,0);
    private final Color[][] colorMesh;
    private final List<Double[]> moves = new ArrayList<>();
    private int moveN = 1;
    private double chargeP1 = MAX_CHARGE;
    private double chargeP2 = MAX_CHARGE;
    private final Mesh2D mesh = new Mesh2D(64, 64, 64);
    public AppFrame() {
        setLayout(new BorderLayout());
        colorMesh = new Color[64][64];
        for (var i = 0; i < 64; i++) {
            var b = new Color[64];
            for (var j = 0; j < 64; j++) {
                b[j] = Color.WHITE;
            }
            colorMesh[i] = b;
        }
        var layout = new GridBagLayout();
        var inputPanel = new JPanel(layout);
        var textFieldX = new JTextField("0.5");
        textFieldX.setColumns(6);
        var labelX = new JLabel("x:");
        var textFieldY = new JTextField("0.5");
        textFieldY.setColumns(6);
        var labelY = new JLabel("y:");
        var textFieldC = new JTextField("1.0");
        textFieldC.setColumns(6);
        var labelC = new JLabel("c:");
        var scoreText = new JLabel("0 : 0");
        var chargeText = new JLabel("Charge: 10.0");
        var panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                var g2d = (Graphics2D) g;
                for (var i = 0; i < colorMesh.length; i++) {
                    for (var j = 0; j < colorMesh[0].length; j++) {
                        g2d.setPaint(colorMesh[i][j]);
                        var r = new Rectangle(i * 8, j * 8, 8, 8);
                        g2d.draw(r);
                        g2d.fill(r);
                    }
                }
                for (var i = 0; i < moves.size(); i++) {
                    g2d.setPaint(i%2 == 0? c2:c1);
                    var r = new Rectangle((int)Math.floor(moves.get(i)[0] * 512)-1,
                            (int) Math.floor(moves.get(i)[1] * 512)-1, 2, 2);
                    g2d.draw(r);
                    g2d.fill(r);
                }
                var x = (int)Math.floor(Double.parseDouble(textFieldX.getText())*512);
                var y = (int)Math.floor(Double.parseDouble(textFieldY.getText())*512);
                g2d.setPaint(Color.BLACK);
                g2d.drawLine(x-5, y, x-3, y);
                g2d.drawLine(x+3, y, x+5, y);
                g2d.drawLine(x, y-5, x, y-3);
                g2d.drawLine(x, y+3, x, y+5);

            }
        };
        var okMoveButton = new JButton();
        okMoveButton.setForeground(p2);
        okMoveButton.setText("Move 1");
        okMoveButton.addActionListener((ActionEvent a) -> {
            var c = Double.parseDouble(textFieldC.getText());
            double remaining = moves.size() % 2 == 0 ? chargeP1 : chargeP2;
            if (c > remaining || c <= 0) {
                JOptionPane.showMessageDialog(this, "Charge must be in (0, " + String.format("%.1f", remaining) + "]");
                return;
            }
            if (moves.size() % 2 == 0) { chargeP1 -= c; } else { chargeP2 -= c; }
            var mv = new Double[] {
                    Double.parseDouble(textFieldX.getText()),
                    Double.parseDouble(textFieldY.getText()),
                    c};
            moves.add(mv);
            okMoveButton.setForeground(moves.size()%2 ==0?p2:p1);
            okMoveButton.setText(". . .");
            mesh.updateWithMove(mv);
            var scoreP1 = 0;
            var scoreP2 = 0;
            for (var i = 0; i < 64; i++) {
                for (var j = 0; j < 64; j++) {
                    if (mesh.uvalues[i][j] >= 0) {

                        colorMesh[i][j] = p1;
                        scoreP1++;
                    } else {
                        colorMesh[i][j] = p2;
                        scoreP2++;
                    }
                }
            }
            scoreText.setText("<html><font color='red'>%d</font> : <font color='green'>%d</font></html>".formatted(scoreP2,scoreP1));
            okMoveButton.setText("Move %d".formatted(++moveN));
            double curCharge = moves.size() % 2 == 0 ? chargeP1 : chargeP2;
            chargeText.setText("Charge: " + String.format("%.1f", curCharge));
            if (chargeP1 <= 0 && chargeP2 <= 0) {
                okMoveButton.setEnabled(false);
                String msg = scoreP1 > scoreP2 ? "Player 1 wins" : scoreP2 > scoreP1 ? "Player 2 wins" : "Draw";
                JOptionPane.showMessageDialog(this, msg);
            }
            panel.repaint();
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                var x = e.getX();
                var y = e.getY();
                textFieldX.setText(String.format(Locale.US, "%5.4f", x/512.));
                textFieldY.setText(String.format(Locale.US, "%5.4f", y/512.));
                panel.repaint();
            }
        });
        mesh.initMoves(List.of());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(690, 560);
        setTitle("ESG v.0.1");
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(512,512));
        add(panel, BorderLayout.CENTER);
        var gc = new GridBagConstraints() {{this.fill = GridBagConstraints.BOTH;}};
        layout.setConstraints(labelX, gc);
        inputPanel.add(labelX);
        layout.setConstraints(textFieldX, gc);
        inputPanel.add(textFieldX);
        layout.setConstraints(labelY, gc);
        inputPanel.add(labelY);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(textFieldY, gc);
        inputPanel.add(textFieldY);
        gc.gridwidth = 1;
        layout.setConstraints(labelC, gc);
        inputPanel.add(labelC);
        layout.setConstraints(textFieldC, gc);
        inputPanel.add(textFieldC);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(scoreText, gc);
        inputPanel.add(scoreText);
        layout.setConstraints(chargeText, gc);
        inputPanel.add(chargeText);
        gc.gridwidth = 4;
        layout.setConstraints(okMoveButton, gc);
        inputPanel.add(okMoveButton);
        add(inputPanel, BorderLayout.EAST);
        setVisible(true);
    }
}
