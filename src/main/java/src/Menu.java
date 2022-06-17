/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.SwingUtilities;

/**
 *
 * @author harryhe
 */
public class Menu extends MouseAdapter {

    private Board board;
    private Handler handler;
    private Game game;
    private Opponent opponent1;
    private Opponent opponent2;
    public ArrayList<Button> buttons;

    private Color textLightGray;
    private Color backColor;
    private Color lightBackColor;
    private Color winBoxColor;
    private Color darkPiece;
    private Color lightPiece;
    private Font header;
    private Font tag;
    private Font title;
    private Font f1;
    private FontLoader fl;

    private boolean playTab;
    private boolean showWinBox;
    private boolean showEvoBox;
    private boolean entryEndResult;
    private int matchType;
    private int persType;
    private int oppPerson1;
    private int oppPerson2;

    private int hoverIndex;

    public Menu(Board board, Handler handler, Game game,
            Opponent opponent1, Opponent opponent2) {
        this.board = board;
        this.handler = handler;
        this.game = game;
        this.opponent1 = opponent1;
        this.opponent2 = opponent2;
        this.buttons = new ArrayList<>();

        textLightGray = new Color(179, 179, 178);
        backColor = new Color(38, 37, 34);
        lightBackColor = new Color(48, 46, 43);
        winBoxColor = new Color(156, 183, 90);
        darkPiece = new Color(68, 65, 64);
        lightPiece = new Color(236, 236, 236);
        header = new Font("helvetica", Font.BOLD, 20);
        tag = new Font("helvetica", Font.PLAIN, 10);
        title = new Font("helvetica", Font.BOLD, 40);

        fl = new FontLoader();
        f1 = fl.loadFont("/FreeSerif.ttf", 100);

        playTab = true;
        showWinBox = false;
        entryEndResult = true;
        matchType = 0;
        // 0 PvP
        // 1 PvC
        // 2 CvC
        persType = 0;
        // 0 White
        // 1 Ramdom
        // 2 Black
        oppPerson1 = 0;
        oppPerson2 = 0;
        // 0 is agressive
        // 1 is defensive
        // 2 is positional
        // 3 is balanced
        setMatchType();
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getX() == 770
                    && buttons.get(i).getY() == 100) {
                buttons.get(i).setSelected(true);
            } else if (buttons.get(i).getX() == 770
                    && buttons.get(i).getY() == 190) {
                buttons.get(i).setSelected(true);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // You should do the mouse checks after finalizing button locations
        // Here you should check if the analysis or play tab is clicked
        if (playTab) {

            // Buttons for match type
            if (mouseOver(mx, my, 770, 100, 60, 60)) {
                matchType = 0;
                setMatchType();
                // Match type selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
                // Perspective selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 190) {
                        if (persType == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 190) {
                        if (persType == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 190) {
                        if (persType == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
            } else if (mouseOver(mx, my, 840, 100, 60, 60)) {
                matchType = 1;
                setMatchType();
                // Match type selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
                // Perspective selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 190) {
                        if (persType == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 190) {
                        if (persType == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 190) {
                        if (persType == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
                // Opponent selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 755
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }

                    } else if (buttons.get(i).getX() == 815
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 875
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 935
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 3) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
            } else if (mouseOver(mx, my, 910, 100, 60, 60)) {
                matchType = 2;
                setMatchType();
                // Match type selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 100) {
                        if (matchType == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
                // Perspective selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 190) {
                        if (persType == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 190) {
                        if (persType == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 190) {
                        if (persType == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
                // Opponent 1 selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 755
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }

                    } else if (buttons.get(i).getX() == 815
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 875
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 935
                            && buttons.get(i).getY() == 280) {
                        if (oppPerson1 == 3) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
                // Opponent 2 selection
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 755
                            && buttons.get(i).getY() == 360) {
                        if (oppPerson2 == 0) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }

                    } else if (buttons.get(i).getX() == 815
                            && buttons.get(i).getY() == 360) {
                        if (oppPerson2 == 1) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 875
                            && buttons.get(i).getY() == 360) {
                        if (oppPerson2 == 2) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    } else if (buttons.get(i).getX() == 935
                            && buttons.get(i).getY() == 360) {
                        if (oppPerson2 == 3) {
                            buttons.get(i).setSelected(true);
                        } else {
                            buttons.get(i).setSelected(false);
                        }
                    }
                }
            }
            // Buttons for pers type
            if (mouseOver(mx, my, 770, 190, 60, 60)) {
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(true);
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(false);
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(false);
                    }
                }
                persType = 0;
            } else if (mouseOver(mx, my, 840, 190, 60, 60)) {
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(false);
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(true);
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(false);
                    }
                }
                persType = 1;
            } else if (mouseOver(mx, my, 910, 190, 60, 60)) {
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.get(i).getX() == 770
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(false);
                    } else if (buttons.get(i).getX() == 840
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(false);
                    } else if (buttons.get(i).getX() == 910
                            && buttons.get(i).getY() == 190) {
                        buttons.get(i).setSelected(true);
                    }
                }
                persType = 2;
            }
            if (matchType > 0) {
                if (mouseOver(mx, my, 755, 280, 50, 50)) {
                    oppPerson1 = 0;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                } else if (mouseOver(mx, my, 815, 280, 50, 50)) {
                    oppPerson1 = 1;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                } else if (mouseOver(mx, my, 875, 280, 50, 50)) {
                    oppPerson1 = 2;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                } else if (mouseOver(mx, my, 935, 280, 50, 50)) {
                    oppPerson1 = 3;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 280) {
                            if (oppPerson1 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                }
            }
            if (matchType == 2) {
                if (mouseOver(mx, my, 755, 360, 50, 50)) {
                    oppPerson2 = 0;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                } else if (mouseOver(mx, my, 815, 360, 50, 50)) {
                    oppPerson2 = 1;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                } else if (mouseOver(mx, my, 875, 360, 50, 50)) {
                    oppPerson2 = 2;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                } else if (mouseOver(mx, my, 935, 360, 50, 50)) {
                    oppPerson2 = 3;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).getX() == 755
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 0) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }

                        } else if (buttons.get(i).getX() == 815
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 1) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 875
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 2) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        } else if (buttons.get(i).getX() == 935
                                && buttons.get(i).getY() == 360) {
                            if (oppPerson2 == 3) {
                                buttons.get(i).setSelected(true);
                            } else {
                                buttons.get(i).setSelected(false);
                            }
                        }
                    }
                }
            }
            // Maybe replace with board, or handler
            synchronized (board) {
                if (mouseOver(mx, my, 730, 650, 270, 50)) {
                    if (matchType == 0) {
                        board.playerType = 0;
                        board.endResult = -1;
                        entryEndResult = true;
                        if (persType == 0) {
                            board.wPers = true;
                        } else if (persType == 1) {
                            Random rand = new Random();
                            if (rand.nextInt(2) == 0) {
                                board.wPers = true;
                            } else {
                                board.wPers = false;
                            }
                        } else {
                            board.wPers = false;
                        }
                        board.resetBoard();
                    } else if (matchType == 1) {
                        board.playerType = 1;
                        board.endResult = -1;
                        entryEndResult = true;
                        if (persType == 0) {
                            board.wPers = true;
                        } else if (persType == 1) {
                            Random rand = new Random();
                            if (rand.nextInt(2) == 0) {
                                board.wPers = true;
                            } else {
                                board.wPers = false;
                            }
                        } else {
                            board.wPers = false;
                        }
                        opponent1.setWhiteSide(!board.wPers);
                        opponent1.setPersonality(oppPerson1);
                        board.resetBoard();
                    }
                    // Implement more later
                    // Add for matchType 2 here
                }
            }

            if (mouseOver(mx, my, 475, 265, 40, 40) && showWinBox) {
                showWinBox = false;
            }
            if (board.selectEvo) {
                if (mouseOver(mx, my, 475, 265, 40, 40)) {
                    board.selectEvo = false;
                    board.undoProper(false);
                    board.evoType = -1;
                    board.evoPiece = new Piece(-1, -1, -1, board, handler, -1);
                    board.evoMove = new Move(-1, -1, -1, -1,
                            0.0, new Piece(-1, -1, 1, board, handler, -1),
                            new Pair(-1, -1), new Pair(-1, -1), -1, false, false, -1);
                } else if(mouseOver(mx, my, 240, 330, 60, 80)){
                    // Definetly true becuase the user is doing the action
                    if(board.evoPiece.type == 1){
                        board.setEvoType(9, true, false);
                    } else if(board.evoPiece.type == 2){
                        board.setEvoType(10, true, false);
                    }
                } else if(mouseOver(mx, my, 305, 330, 60, 80)){
                    // Definetly true becuase the user is doing the action
                    if(board.evoPiece.type == 1){
                        board.setEvoType(5, true, false);
                    } else if(board.evoPiece.type == 2){
                        board.setEvoType(6, true, false);
                    }
                } else if(mouseOver(mx, my, 370, 330, 60, 80)){
                    // Definetly true becuase the user is doing the action
                    if(board.evoPiece.type == 1){
                        board.setEvoType(7, true, false);
                    } else if(board.evoPiece.type == 2){
                        board.setEvoType(8, true, false);
                    }
                } else if(mouseOver(mx, my, 435, 330, 60, 80)){
                    // Definetly true becuase the user is doing the action
                    if(board.evoPiece.type == 1){
                        board.setEvoType(3, true, false);
                    } else if(board.evoPiece.type == 2){
                        board.setEvoType(4, true, false);
                    }
                }
            }

        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            }
        }
        return false;
    }

    public void tick() {
        if (playTab) {
            if (MouseInfo.getPointerInfo() != null) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(
                        p, this.game);
                int mx = p.x;
                int my = p.y;
                if (mouseOver(mx, my, 730, 650, 270, 50)) {
                    if (hoverIndex >= 0 && !buttons.isEmpty()) {
                        buttons.get(hoverIndex).setHover(true);
                    }
                } else {
                    if (hoverIndex >= 0 && !buttons.isEmpty()) {
                        buttons.get(hoverIndex).setHover(false);
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(backColor);
        g.fillRect(720, 19, 290, 691);
        g.setColor(lightBackColor);
        if (playTab) {
            g.fillRect(865, 19, 145, 50);
        } else {
            g.fillRect(720, 19, 145, 50);
        }

        if (playTab) {
            g.setColor(Color.white);
            g.setFont(header);
            g.drawString("Play", 772, 50);
            g.drawString("Analysis", 900, 50);

            if (matchType == 0) {
                g.setFont(tag);
                g.drawString("MATCH TYPE", 840, 90);
                g.drawString("I PLAY AS", 840, 180);
            } else if (matchType == 1) {
                g.setFont(tag);
                g.drawString("MATCH TYPE", 840, 90);
                g.drawString("I PLAY AS", 840, 180);
                g.drawString("OPPONENT TYPE", 825, 270);
            } else if (matchType == 2) {
                g.setFont(tag);
                g.drawString("MATCH TYPE", 840, 90);
                g.drawString("I PLAY AS", 840, 180);
                g.drawString("OPPONENT 1 TYPE", 820, 270);
                g.drawString("OPPONENT 2 TYPE", 820, 350);
            }

            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).render(g);
            }
            if (board.endResult >= 0 && entryEndResult) {
                showWinBox = true;
                entryEndResult = false;
            }
            if (board.endResult >= 0 && showWinBox) {
                g.setColor(backColor);
                g.fillRect(211, 261, 308, 158);
                g.setColor(winBoxColor);
                g.fillRect(215, 265, 300, 150);

//                g.setColor(Color.blue);
//                g.fillRect(475, 265, 40, 40);
                g.setColor(Color.white);
                g.setFont(title);
                g.drawString("Game Over", 260, 320);
                g.setFont(header);
                g.setColor(Color.black);
                g.drawString("X", 495, 285);
                if (board.endResult == 1) {
                    g.setColor(Color.white);
                    g.drawString("White Wins", 312, 360);
                    g.drawString("By Checkmate", 300, 385);
                } else if (board.endResult == 0) {
                    g.drawString("Black Wins", 312, 360);
                    g.drawString("By Checkmate", 300, 385);
                } else {
                    g.setColor(Color.gray);
                    g.drawString("Stalemate", 322, 375);
                }
            }
            if (board.selectEvo) {
                g.setColor(backColor);
                g.fillRect(211, 261, 308, 158);
                g.setColor(winBoxColor);
                g.fillRect(215, 265, 300, 150);

                g.setColor(Color.white);
                g.setFont(title);
                g.drawString("Choose Piece", 235, 320);
                g.setFont(header);
                g.setColor(Color.black);
                g.drawString("X", 495, 285);
                
//                g.setColor(Color.blue);
//                g.fillRect(240, 330, 60, 80);
//                g.fillRect(305, 330, 60, 80);
//                g.fillRect(370, 330, 60, 80);
//                g.fillRect(435, 330, 60, 80);
                
                if (board.evoPiece.type == 1) {
                    // Queen
                    g.setFont(f1);
                    g.setColor(lightPiece);
                    g.drawString(Character.toString('\u265B'), (int) 230, (int) 400);
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u2655'), (int) 230, (int) 400);

                    // Horse
                    g.setFont(f1);
                    g.setColor(lightPiece);
                    g.drawString(Character.toString('\u265E'), (int) 295, (int) 400);
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u2658'), (int) 295, (int) 400);

                    // Rook
                    g.setFont(f1);
                    g.setColor(lightPiece);
                    g.drawString(Character.toString('\u265C'), (int) 360, (int) 400);
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u2656'), (int) 360, (int) 400);

                    // Bishop
                    g.setFont(f1);
                    g.setColor(lightPiece);
                    g.drawString(Character.toString('\u265D'), (int) 425, (int) 400);
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u2657'), (int) 425, (int) 400);
                } else if (board.evoPiece.type == 2) {
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u265B'), (int) 230, (int) 400);

                    // Horse
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u265E'), (int) 295, (int) 400);

                    // Rook
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u265C'), (int) 360, (int) 400);

                    // Bishop
                    g.setFont(f1);
                    g.setColor(darkPiece);
                    g.drawString(Character.toString('\u265D'), (int) 425, (int) 400);
                }
            }

        }
    }

    public void setMatchType() {
        //buttons = new ArrayList<>();
        ArrayList<Button> temp = new ArrayList<>();
        if (matchType == 0) {
            temp.add(new ImageButton(770, 100, 60, 60,
                    new Color(234, 234, 200), "/PlayerPlayer.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(840, 100, 60, 60,
                    new Color(234, 234, 200), "/PlayerComputer.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(910, 100, 60, 60,
                    new Color(234, 234, 200), "/ComputerComputer.png", new Color(114, 148, 83)));
            temp.add(new IconButton(770, 190, 60, 60, 0, 60, '\u2654',
                    new Color(114, 148, 83)));
            temp.add(new IconButton(840, 190, 60, 60, 1, 60, '?',
                    new Color(114, 148, 83)));
            temp.add(new IconButton(910, 190, 60, 60, 2, 60, '\u265A',
                    new Color(114, 148, 83)));
            temp.add(new TextButton(730, 650, 270, 50,
                    new Color(135, 163, 88), 20, new Color(156, 183, 90),
                    new Color(251, 251, 251), "New Game"));
        } else if (matchType == 1) {
            temp.add(new ImageButton(770, 100, 60, 60,
                    new Color(234, 234, 200), "/PlayerPlayer.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(840, 100, 60, 60,
                    new Color(234, 234, 200), "/PlayerComputer.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(910, 100, 60, 60,
                    new Color(234, 234, 200), "/ComputerComputer.png", new Color(114, 148, 83)));
            temp.add(new IconButton(770, 190, 60, 60, 0, 60, '\u2654',
                    new Color(114, 148, 83)));
            temp.add(new IconButton(840, 190, 60, 60, 1, 60, '?',
                    new Color(114, 148, 83)));
            temp.add(new IconButton(910, 190, 60, 60, 2, 60, '\u265A',
                    new Color(114, 148, 83)));

            temp.add(new ImageButton(755, 280, 50, 50,
                    new Color(234, 234, 200), "/Aggressive.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(815, 280, 50, 50,
                    new Color(234, 234, 200), "/Defensive.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(875, 280, 50, 50,
                    new Color(234, 234, 200), "/Positional.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(935, 280, 50, 50,
                    new Color(234, 234, 200), "/Balanced.png", new Color(114, 148, 83)));

            temp.add(new TextButton(730, 650, 270, 50,
                    new Color(135, 163, 88), 20, new Color(156, 183, 90),
                    new Color(251, 251, 251), "New Game"));
        } else if (matchType == 2) {
            temp.add(new ImageButton(770, 100, 60, 60,
                    new Color(234, 234, 200), "/PlayerPlayer.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(840, 100, 60, 60,
                    new Color(234, 234, 200), "/PlayerComputer.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(910, 100, 60, 60,
                    new Color(234, 234, 200), "/ComputerComputer.png", new Color(114, 148, 83)));
            temp.add(new IconButton(770, 190, 60, 60, 0, 60, '\u2654',
                    new Color(114, 148, 83)));
            temp.add(new IconButton(840, 190, 60, 60, 1, 60, '?',
                    new Color(114, 148, 83)));
            temp.add(new IconButton(910, 190, 60, 60, 2, 60, '\u265A',
                    new Color(114, 148, 83)));

            temp.add(new ImageButton(755, 280, 50, 50,
                    new Color(234, 234, 200), "/Aggressive.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(815, 280, 50, 50,
                    new Color(234, 234, 200), "/Defensive.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(875, 280, 50, 50,
                    new Color(234, 234, 200), "/Positional.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(935, 280, 50, 50,
                    new Color(234, 234, 200), "/Balanced.png", new Color(114, 148, 83)));

            temp.add(new ImageButton(755, 360, 50, 50,
                    new Color(234, 234, 200), "/Aggressive.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(815, 360, 50, 50,
                    new Color(234, 234, 200), "/Defensive.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(875, 360, 50, 50,
                    new Color(234, 234, 200), "/Positional.png", new Color(114, 148, 83)));
            temp.add(new ImageButton(935, 360, 50, 50,
                    new Color(234, 234, 200), "/Balanced.png", new Color(114, 148, 83)));

            temp.add(new TextButton(730, 650, 270, 50,
                    new Color(135, 163, 88), 20, new Color(156, 183, 90),
                    new Color(251, 251, 251), "New Game"));
        }
        buttons = temp;
        hoverIndex = getHoverButton();
    }

    private int getHoverButton() {
        for (int i = 0; i < buttons.size(); i++) {
            // Curde method
            if (buttons.get(i).getX() == 730
                    && buttons.get(i).getY() == 650) {
                return i;
            }
        }
        return -1;
    }

    public int getPersp() {
        return this.persType;
    }

    public int getPersonality1() {
        return this.oppPerson1;
    }

    public int getPersonality2() {
        return this.oppPerson2;
    }
}
