package gui;

import io.IOReader;
import io.IOWriter;
import model.Game;
import model.Mod;

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
    static DefaultListModel<Mod> modListModel = new DefaultListModel<>();

    static JList<Game> gameList = new JList<>(listModel);
    static JList<Mod> modList = new JList<>(modListModel);


    public static ArrayList<Game> games = new ArrayList<>();

    static int defaultWidth = 1200;
    static int defaultHeight = 900;

    static Game activeGame;

    public ManagerGUI() {
        setFocusable(true);
        setPreferredSize(new Dimension(defaultWidth, defaultHeight));
    }

    public static void main(String[] args) {
        panel = new ManagerGUI();
        panel.setLayout(new BorderLayout());
        frame = new JFrame("Multi Mod Manager");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        CardLayout cardLayout = new CardLayout();
        JPanel cards = new JPanel(cardLayout);

        JPanel gameListPanel = new JPanel();
        gameListPanel.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setFont(new Font(gameList.getFont().getFontName(), Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setPreferredSize(new Dimension(defaultWidth * 3 / 4, defaultHeight / 3));
//        scrollPane.setMaximumSize(new Dimension(defaultWidth * 3 / 4, defaultHeight / 3));
        gameListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton select = new JButton("Select Game");
        JButton edit = new JButton("Edit Game");
        JButton add = new JButton("Add Game");
        JButton delete = new JButton("Delete Game");
        select.setPreferredSize(new Dimension(defaultWidth / 4, defaultHeight / 8));
        edit.setPreferredSize(new Dimension(defaultWidth / 4, defaultHeight / 8));
        add.setPreferredSize(new Dimension(defaultWidth / 4, defaultHeight / 8));
        buttonsPanel.add(add);
        buttonsPanel.add(delete);
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

                            JLabel errorLabel = new JLabel(ex.getMessage());
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
                                    gameList.repaint();
                                }
                                IOWriter.writeGame(game);
                                dialog.dispose();

                            }
                            catch (IllegalArgumentException ex) {
                                JDialog error = new JDialog(frame, "Error", true);
                                error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                                error.setLayout(new GridLayout(0, 1));
                                error.add(new JPanel());

                                JLabel errorLabel = new JLabel(ex.getMessage());
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

        delete.addActionListener(new ActionListener() {
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
                    JDialog dialog = new JDialog(frame, "Warning!", true);
                    dialog.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                    dialog.setLayout(new GridLayout(0, 1));

                    float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale/ 20f);

                    JPanel textRow = new JPanel(new FlowLayout());
                    JLabel textLabel = new JLabel("Delete " + game.getName() + "?");
                    textLabel.setFont(textLabel.getFont().deriveFont(fontSize));
                    textRow.add(textLabel);
                    dialog.add(textRow);

                    JPanel textRow2 = new JPanel(new FlowLayout());
                    JLabel textLabel2 = new JLabel("This cannot be undone.");
                    textLabel2.setFont(textLabel2.getFont().deriveFont(fontSize));
                    textRow2.add(textLabel2);
                    dialog.add(textRow2);


                    JPanel buttonRow = new JPanel(new FlowLayout());
                    JButton cancel = new JButton("Cancel");
                    JButton ok = new JButton("Proceed");
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
                            game.disableMod();
                            IOWriter.deleteGame(game);
                            listModel.removeElement(game);
                            games.remove(game);
                            dialog.dispose();
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
        cards.add(panel, "MENU");

        JPanel modsPanel = new JPanel(new BorderLayout());

        JLabel gameNameLabel = new JLabel("Game Name");
        JPanel modsNorthPanel = new JPanel(new FlowLayout());
        modsNorthPanel.add(gameNameLabel);
        modsPanel.add(modsNorthPanel, BorderLayout.NORTH);

        JPanel modsWestPanel = new JPanel(new GridLayout(7, 1));
        JLabel activeVersionText = new JLabel("Active version:");
        JLabel activeVersion = new JLabel("Version");
        JPanel activeVersionTextPanel = new JPanel(new FlowLayout());
        JPanel activeVersionPanel = new JPanel(new FlowLayout());
        activeVersionTextPanel.add(activeVersionText);
        activeVersionPanel.add(activeVersion);
        modsWestPanel.add(activeVersionTextPanel);
        modsWestPanel.add(activeVersionPanel);
        modsPanel.add(modsWestPanel, BorderLayout.WEST);
        JButton selectMod = new JButton("Select Mod");
        JButton addMod = new JButton("Add Mod");
        JButton deleteMod = new JButton("Delete Mod");
        JButton back = new JButton("Back");
        JButton deselectMod = new JButton("Deselect Mod");
        JPanel selectModPanel = new JPanel(new FlowLayout());
        JPanel addModPanel = new JPanel(new FlowLayout());
        JPanel deleteModPanel = new JPanel(new FlowLayout());
        JPanel backPanel = new JPanel(new FlowLayout());
        JPanel deselectModPanel = new JPanel(new FlowLayout());
        selectModPanel.add(selectMod);
        addModPanel.add(addMod);
        deleteModPanel.add(deleteMod);
        backPanel.add(back);
        deselectModPanel.add(deselectMod);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activeGame = null;
                cardLayout.show(cards,"MENU");
                modListModel.clear();
            }
        });
        addMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int invDialogScale = 4;
                JDialog dialog = new JDialog(frame, "Add Mod", true);
                dialog.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                dialog.setLayout(new GridLayout(0, 1));

                float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale/ 20f);

                JPanel nameRow = new JPanel(new FlowLayout());
                JLabel nameLabel = new JLabel("Mod Name: ");
                nameLabel.setFont(nameLabel.getFont().deriveFont(fontSize));
                nameRow.add(nameLabel);
                JTextField nameField = new JTextField(10);
                nameField.setFont(nameField.getFont().deriveFont(fontSize));
                nameRow.add(nameField);
                dialog.add(nameRow);

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
                            Mod mod = new Mod(nameField.getText());
                            activeGame.addMod(mod);
                            modListModel.addElement(mod);
                            IOWriter.writeGame(activeGame);
                            dialog.dispose();
                        }
                        catch (IllegalArgumentException ex) {
                            JDialog error = new JDialog(frame, "Error", true);
                            error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                            error.setLayout(new GridLayout(0, 1));
                            error.add(new JPanel());

                            JLabel errorLabel = new JLabel(ex.getMessage());
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
        selectMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mod mod = modList.getSelectedValue();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int invDialogScale = 4;
                if (mod == null) {
                    float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale / 20f);
                    Dimension buttonSize = new Dimension(screenSize.width / invDialogScale / 3, screenSize.height / invDialogScale / 6);


                    JDialog error = new JDialog(frame, "Error", true);
                    error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                    error.setLayout(new GridLayout(0, 1));
                    error.add(new JPanel());

                    JLabel errorLabel = new JLabel("Please select a mod.");
                    errorLabel.setFont(errorLabel.getFont().deriveFont(fontSize));
                    JPanel errorLabelPanel = new JPanel(new FlowLayout());
                    errorLabelPanel.add(errorLabel);
                    error.add(errorLabelPanel);

                    JPanel errorButtonPanel = new JPanel(new FlowLayout());
                    JButton errorButton = new JButton("OK");
                    errorButton.setPreferredSize(buttonSize);
                    errorButton.setFont(errorButton.getFont().deriveFont((float) (buttonSize.height * 0.3)));
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
                    activeGame.enableMod(mod);
                    activeVersion.setText(mod.getName());
                    IOWriter.writeGame(activeGame);
                }
            }
        });
        deselectMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!activeGame.getEnabledMod().isEmpty()) {
                    activeGame.disableMod();
                    activeVersion.setText("Vanilla");
                    IOWriter.writeGame(activeGame);

                }
            }
        });
        deleteMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mod mod = modList.getSelectedValue();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int invDialogScale = 4;
                if (mod == null) {
                    float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale / 20f);
                    Dimension buttonSize = new Dimension(screenSize.width / invDialogScale / 3, screenSize.height / invDialogScale / 6);


                    JDialog error = new JDialog(frame, "Error", true);
                    error.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                    error.setLayout(new GridLayout(0, 1));
                    error.add(new JPanel());

                    JLabel errorLabel = new JLabel("Please select a mod.");
                    errorLabel.setFont(errorLabel.getFont().deriveFont(fontSize));
                    JPanel errorLabelPanel = new JPanel(new FlowLayout());
                    errorLabelPanel.add(errorLabel);
                    error.add(errorLabelPanel);

                    JPanel errorButtonPanel = new JPanel(new FlowLayout());
                    JButton errorButton = new JButton("OK");
                    errorButton.setPreferredSize(buttonSize);
                    errorButton.setFont(errorButton.getFont().deriveFont((float) (buttonSize.height * 0.3)));
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
                    JDialog dialog = new JDialog(frame, "Warning!", true);
                    dialog.setPreferredSize(new Dimension(screenSize.width / invDialogScale, screenSize.height / invDialogScale));
                    dialog.setLayout(new GridLayout(0, 1));

                    float fontSize = Math.max(12f, (float) screenSize.width / invDialogScale/ 20f);

                    JPanel textRow = new JPanel(new FlowLayout());
                    JLabel textLabel = new JLabel("Delete " + mod.getName() + "?");
                    textLabel.setFont(textLabel.getFont().deriveFont(fontSize));
                    textRow.add(textLabel);
                    dialog.add(textRow);

                    JPanel textRow2 = new JPanel(new FlowLayout());
                    JLabel textLabel2 = new JLabel("Your files will remain.");
                    textLabel2.setFont(textLabel2.getFont().deriveFont(fontSize));
                    textRow2.add(textLabel2);
                    dialog.add(textRow2);


                    JPanel buttonRow = new JPanel(new FlowLayout());
                    JButton cancel = new JButton("Cancel");
                    JButton ok = new JButton("Proceed");
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
                            if (mod.getName().equals(activeGame.getEnabledMod())) {
                                activeGame.disableMod();
                            }
                            modListModel.removeElement(mod);
                            activeGame.removeMod(mod);
                            IOWriter.writeGame(activeGame);
                            dialog.dispose();
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


        modsWestPanel.add(selectModPanel);
        modsWestPanel.add(deselectModPanel);
        modsWestPanel.add(addModPanel);
        modsWestPanel.add(deleteModPanel);
        modsWestPanel.add(backPanel);


        JPanel modsCenterPanel = new JPanel(new BorderLayout());
        modListModel = new DefaultListModel<>();
        modList = new JList<>(modListModel);
        modList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modList.setFont(new Font(modList.getFont().getFontName(), Font.PLAIN, 20));
        JScrollPane modScrollPane = new JScrollPane(modList);
        modScrollPane.setPreferredSize(new Dimension(defaultWidth * 3 / 4, defaultHeight / 3));
        modsCenterPanel.add(modScrollPane, BorderLayout.CENTER);
        modsPanel.add(modsCenterPanel, BorderLayout.CENTER);


        cards.add(modsPanel, "MODS");

        select.addActionListener(new ActionListener() {
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
                    activeGame = game;
                    cardLayout.show(cards, "MODS");
                    gameNameLabel.setText(activeGame.getName());
                    if (!activeGame.getEnabledMod().isEmpty()) {
                        activeVersion.setText(activeGame.getEnabledMod());
                    }
                    else {
                        activeVersion.setText("Vanilla");
                    }
                    for (Mod mod : activeGame.getMods()) {
                        modListModel.addElement(mod);
                    }
                }
            }
        });

        frame.add(cards);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = frame.getContentPane().getWidth();
                int height = frame.getContentPane().getHeight();

                Dimension buttonSize = new Dimension(width / 5, height / 8);
                for (JButton button : new JButton[]{select, edit, add, delete}) {
                    button.setPreferredSize(buttonSize);
                    button.setFont(button.getFont().deriveFont((float)(buttonSize.width * 0.1)));
                }

                Dimension modButtonSize = new Dimension(width / 6, height / 10);
                for (JButton button : new JButton[]{selectMod, addMod, deleteMod, back, deselectMod}) {
                    button.setPreferredSize(modButtonSize);
                    button.setFont(button.getFont().deriveFont((float)(modButtonSize.width * 0.1)));
                }


                float listFontSize = Math.max(12f, width / 40f);
                gameList.setFont(gameList.getFont().deriveFont(listFontSize));
                modList.setFont(modList.getFont().deriveFont(listFontSize));

                int scrollPaneMargin = Math.max(10, frame.getWidth() / 30);
                gameListPanel.setBorder(BorderFactory.createEmptyBorder(scrollPaneMargin, scrollPaneMargin, scrollPaneMargin, scrollPaneMargin));
                buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, scrollPaneMargin, 0));

                float gameTitleFontSize = Math.max(12f, width / 20f);
                gameNameLabel.setFont(gameNameLabel.getFont().deriveFont(gameTitleFontSize));

                float gameInfoFontSize = Math.max(12f, width / 40f);
                activeVersionText.setFont(activeVersionText.getFont().deriveFont(gameInfoFontSize));
                activeVersion.setFont(activeVersion.getFont().deriveFont(gameInfoFontSize));
                int gameInfoMargin = Math.max(10, frame.getWidth() / 30);
                modsWestPanel.setBorder(BorderFactory.createEmptyBorder(gameInfoMargin, gameInfoMargin, gameInfoMargin, gameInfoMargin));

                modsCenterPanel.setBorder(BorderFactory.createEmptyBorder(scrollPaneMargin, 0, scrollPaneMargin, scrollPaneMargin));

                panel.revalidate();
            }
        });


        IOWriter.makeFolder();
        IOReader.readGames();
        for (Game game : games) {
            listModel.addElement(game);
        }
    }
}
