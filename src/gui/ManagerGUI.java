package gui;

import io.IOReader;
import io.IOWriter;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class ManagerGUI extends JPanel {

    static JPanel panel;
    static JFrame frame;

    static DefaultListModel<Game> listModel = new DefaultListModel<>();
    static JList<Game> gameList = new JList<>(listModel);

    public static ArrayList<Game> games = new ArrayList<>();

    static int defaultWidth = 1200;
    static int defaultHeight = 900;

    public ManagerGUI() {
        setFocusable(true);
        setPreferredSize(new Dimension(defaultWidth, defaultHeight));
    }

    static void main(String[] args) {
        panel = new ManagerGUI();
        panel.setLayout(new BorderLayout());
        frame = new JFrame("Multi Mod Manager");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel gameListPanel = new JPanel();
        gameListPanel.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setFont(new Font(gameList.getFont().getFontName(), Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(gameList);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(defaultWidth * 3 / 4, defaultHeight / 3));
//        scrollPane.setMaximumSize(new Dimension(defaultWidth * 3 / 4, defaultHeight / 3));
        gameListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton select = new JButton("Select Game");
        JButton edit = new JButton("Edit Game");
        JButton add = new JButton("Add Game");
        select.setPreferredSize(new Dimension(defaultWidth / 4, defaultHeight / 8));
        edit.setPreferredSize(new Dimension(defaultWidth / 4, defaultHeight / 8));
        add.setPreferredSize(new Dimension(defaultWidth / 4, defaultHeight / 8));
        buttonsPanel.add(add);
        buttonsPanel.add(edit);
        buttonsPanel.add(select);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int invDialogScale = 4;
                JDialog dialog = new JDialog(frame, "Add Game", true);
                dialog.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                dialog.setLayout(new GridLayout(0, 1));

                float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale/ 20f);

                JPanel nameRow = new JPanel(new FlowLayout());
                JLabel nameLabel = new JLabel("Game Name: ");
                nameLabel.setFont(nameLabel.getFont().deriveFont(fontSize));
                nameRow.add(nameLabel);
                JTextField nameField = new JTextField(10);
                nameField.setFont(nameField.getFont().deriveFont(fontSize));
                nameRow.add(nameField);
                dialog.add(nameRow);

                JPanel pathRow = new JPanel(new FlowLayout());
                JLabel pathLabel = new JLabel("Folder Path: ");
                pathLabel.setFont(pathLabel.getFont().deriveFont(fontSize));
                pathRow.add(pathLabel);
                JTextField pathField = new JTextField(10);
                pathField.setFont(pathField.getFont().deriveFont(fontSize));
                pathRow.add(pathField);
                dialog.add(pathRow);

                JPanel buttonRow = new JPanel(new FlowLayout());
                JButton cancel = new JButton("Cancel");
                JButton ok = new JButton("OK");
                Dimension buttonSize = new Dimension(screenSize.width / invDialogScale / 3, screenSize.height / invDialogScale / 6);
                cancel.setPreferredSize(buttonSize);
                cancel.setFont(cancel.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                ok.setPreferredSize(buttonSize);
                ok.setFont(ok.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try  {
                            Game game = new Game(nameField.getText(), pathField.getText());
                            games.add(game);
                            listModel.addElement(game);
                            IOWriter.writeGame(game);
                            dialog.dispose();
                        }
                        catch (IllegalArgumentException ex) {
                            JDialog error = new JDialog(frame, "Error", true);
                            error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                            error.setLayout(new GridLayout(0, 1));
                            error.add(new JPanel());

                            JLabel errorLabel = new JLabel("Name and path cannot be empty.");
                            errorLabel.setFont(errorLabel.getFont().deriveFont(fontSize));
                            JPanel errorLabelPanel = new JPanel(new FlowLayout());
                            errorLabelPanel.add(errorLabel);
                            error.add(errorLabelPanel);

                            JPanel errorButtonPanel = new JPanel(new FlowLayout());
                            JButton errorButton = new JButton("OK");
                            errorButton.setPreferredSize(buttonSize);
                            errorButton.setFont(errorButton.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                            errorButtonPanel.add(errorButton);
                            error.add(errorButtonPanel);

                            errorButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    error.dispose();
                                }
                            });

                            error.pack();
                            error.setLocationRelativeTo(null);
                            error.setVisible(true);
                        }
                    }
                });

                buttonRow.add(cancel);
                buttonRow.add(ok);
                dialog.add(buttonRow);



                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game game = gameList.getSelectedValue();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int invDialogScale = 4;
                if (game == null) {
                    float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale/ 20f);
                    Dimension buttonSize = new Dimension(screenSize.width / invDialogScale / 3, screenSize.height / invDialogScale / 6);


                    JDialog error = new JDialog(frame, "Error", true);
                    error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                    error.setLayout(new GridLayout(0, 1));
                    error.add(new JPanel());

                    JLabel errorLabel = new JLabel("Please select a game.");
                    errorLabel.setFont(errorLabel.getFont().deriveFont(fontSize));
                    JPanel errorLabelPanel = new JPanel(new FlowLayout());
                    errorLabelPanel.add(errorLabel);
                    error.add(errorLabelPanel);

                    JPanel errorButtonPanel = new JPanel(new FlowLayout());
                    JButton errorButton = new JButton("OK");
                    errorButton.setPreferredSize(buttonSize);
                    errorButton.setFont(errorButton.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                    errorButtonPanel.add(errorButton);
                    error.add(errorButtonPanel);

                    errorButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            error.dispose();
                        }
                    });

                    error.pack();
                    error.setLocationRelativeTo(null);
                    error.setVisible(true);

                }
                else {
                    JDialog dialog = new JDialog(frame, "Editing " + game.getName(), true);
                    dialog.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                    dialog.setLayout(new GridLayout(0, 1));

                    float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale/ 20f);

                    JPanel nameRow = new JPanel(new FlowLayout());
                    JLabel nameLabel = new JLabel("Game Name: ");
                    nameLabel.setFont(nameLabel.getFont().deriveFont(fontSize));
                    nameRow.add(nameLabel);
                    JTextField nameField = new JTextField(10);
                    nameField.setFont(nameField.getFont().deriveFont(fontSize));
                    nameField.setText(game.getName());
                    nameRow.add(nameField);
                    dialog.add(nameRow);

                    JPanel pathRow = new JPanel(new FlowLayout());
                    JLabel pathLabel = new JLabel("Folder Path: ");
                    pathLabel.setFont(pathLabel.getFont().deriveFont(fontSize));
                    pathRow.add(pathLabel);
                    JTextField pathField = new JTextField(10);
                    pathField.setFont(pathField.getFont().deriveFont(fontSize));
                    pathField.setText(game.getFolderPath());
                    pathRow.add(pathField);
                    dialog.add(pathRow);

                    JPanel buttonRow = new JPanel(new FlowLayout());
                    JButton cancel = new JButton("Cancel");
                    JButton ok = new JButton("OK");
                    Dimension buttonSize = new Dimension(screenSize.width / invDialogScale / 3, screenSize.height / invDialogScale / 6);
                    cancel.setPreferredSize(buttonSize);
                    cancel.setFont(cancel.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                    ok.setPreferredSize(buttonSize);
                    ok.setFont(ok.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                    cancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dialog.dispose();
                        }
                    });
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                game.setFolderPath(pathField.getText());
                                if (!game.getName().equals(nameField.getText())) {
                                    IOWriter.deleteGame(game);
                                    game.setName(nameField.getText());
                                    listModel.set(0, listModel.get(0));
                                }
                                IOWriter.writeGame(game);
                                dialog.dispose();

                            }
                            catch (IllegalArgumentException ex) {
                                JDialog error = new JDialog(frame, "Error", true);
                                error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                                error.setLayout(new GridLayout(0, 1));
                                error.add(new JPanel());

                                JLabel errorLabel = new JLabel("Name and path cannot be empty.");
                                errorLabel.setFont(errorLabel.getFont().deriveFont(fontSize));
                                JPanel errorLabelPanel = new JPanel(new FlowLayout());
                                errorLabelPanel.add(errorLabel);
                                error.add(errorLabelPanel);

                                JPanel errorButtonPanel = new JPanel(new FlowLayout());
                                JButton errorButton = new JButton("OK");
                                errorButton.setPreferredSize(buttonSize);
                                errorButton.setFont(errorButton.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                                errorButtonPanel.add(errorButton);
                                error.add(errorButtonPanel);

                                errorButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        error.dispose();
                                    }
                                });

                                error.pack();
                                error.setLocationRelativeTo(null);
                                error.setVisible(true);
                            }
                        }
                    });

                    buttonRow.add(cancel);
                    buttonRow.add(ok);
                    dialog.add(buttonRow);


                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                }


            }
        });


        panel.add(gameListPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = frame.getContentPane().getWidth();
                int height = frame.getContentPane().getHeight();

                Dimension buttonSize = new Dimension(width / 4, height / 8);
                for (JButton button : new JButton[]{select, edit, add}) {
                    button.setPreferredSize(buttonSize);
                    button.setFont(button.getFont().deriveFont((float)(buttonSize.height * 0.3)));
                }

                float listFontSize = Math.max(12f, width / 40f);
                gameList.setFont(gameList.getFont().deriveFont(listFontSize));

                int scrollPaneMargin = Math.max(10, frame.getWidth() / 30);
                gameListPanel.setBorder(BorderFactory.createEmptyBorder(scrollPaneMargin, scrollPaneMargin, scrollPaneMargin, scrollPaneMargin));

                panel.revalidate();
                System.out.println("Pane:" + scrollPane.getPreferredSize());
                System.out.println("List: " + gameList.getPreferredSize());
                System.out.println(gameList.getParent());
            }
        });


        IOWriter.makeFolder();
        IOReader.readGames();
        for (Game game : games) {
            listModel.addElement(game);
        }
    }
}
