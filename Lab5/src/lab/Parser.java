package lab;

import JavaTeacherLib.MyLang;
import JavaTeacherLib.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private MyLang lang;
    private byte[] lexema;
    private BufferedReader bufferedReader;
    private String programData;
    private int codlex;
    private int poslex;
    private int lenlex;
    private int remainingData;
    private int maxLen;
    private int[] scUprTable;
    private int[][] prsUprTable;

    public Parser(MyLang lang) {
        this.lang = lang;
        this.lexema = new byte[180];
        this.prsUprTable = this.lang.getUprTable();
        this.scUprTable = new int[256];

        setClass();
        readFromFile();
    }

    private void readFromFile() {
        try {
            this.bufferedReader = new BufferedReader(new FileReader("test.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClass(){
        this.setClass("()[];,+-^=*", 0);
        this.setClass("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_", 1);
        this.setClass("0123456789", 2);
    }

    private void setClass(String str, int initCode) {
        for (int i = 0; i < str.length(); ++i) {
            this.scUprTable[str.charAt(i)] = initCode + 1;
        }

    }

    public void parse() {
        this.codlex = this.Ai();
        if (!this.f_Ai(this.lang.getAxioma())) {
            while (true) {
                if (this.codlex == -1) {
                    System.out.println("Syntax errors");
                    break;
                }
                this.codlex = this.Ai();
            }
        }
        try {
            this.bufferedReader.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private boolean f_Ai(int nonterminal) {
        int nonterminalIndex = this.lang.indexNonterminal(nonterminal);
        int termCol;
        if (this.codlex == -1) {
            termCol = this.lang.getTerminals().length;
        } else {
            termCol = this.lang.indexTerminal(this.codlex);
        }

        if (this.prsUprTable[nonterminalIndex][termCol] == 0) {
            System.out.println("Syntax error:  " + this.lang.getLexemaText(nonterminal) + " " + this.lang.getLexemaText(this.codlex) + " " + this.getLexema());
            System.out.println("Syntax errors");

            try {
                this.bufferedReader.close();
                return false;
            } catch (Exception var9) {
                return false;
            }
        } else {
            int rule = this.prsUprTable[nonterminalIndex][termCol];
            int count = 0;
            Node nodetmp = null;

            for (Node node : this.lang.getLanguarge()) {
                ++count;
                if (rule == count) {
                    nodetmp = node;
                    break;
                }
            }

            int[] rules = nodetmp.getRoole();

            for (int i = 1; i < rules.length; ++i) {
                if (rules[i] > 0) {
                    if (rules[i] == this.codlex) {
                        this.codlex = this.Ai();
                    } else {
                        if (!this.lang.getLexemaText(rules[i]).equals("else")) {
                            System.out.println("\nMissing:" + this.lang.getLexemaText(rules[i])+"\n");
                            return false;
                        }
                        i += 2;
                    }
                } else if (!this.f_Ai(rules[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public String getLexema() {
        byte[] textLexema = new byte[this.lenlex];

        for (int ii = 0; ii < this.lenlex; ++ii) {
            textLexema[ii] = this.lexema[ii];
        }

        return new String(textLexema);
    }






























    public int Ai() {
        int num = 0;
        this.lenlex = 0;
        int litera = 0;
        int lexemaClass = 0;
        String error = "^[]{},?: ; \t\n\u0000";

        try {
            int count;
            while (this.bufferedReader.ready() || this.poslex <= this.maxLen) {
                if (this.programData == null) {
                    this.programData = this.bufferedReader.readLine();
                    System.out.println(this.programData);
                    this.poslex = 0;
                    this.maxLen = this.programData.length();
                }

                while (this.remainingData != 0 || this.poslex <= this.maxLen) {
                    if (this.remainingData != 0) {
                        litera = this.remainingData;
                        this.remainingData = 0;
                    } else {
                        if (this.poslex < this.programData.length()) {
                            litera = this.programData.charAt(this.poslex);
                        }
                        if (this.programData.length() == this.poslex) {
                            litera = 10;
                        }
                        ++this.poslex;
                    }
                    litera &= 255;

                    int lexClass = this.scUprTable[litera];
                    switch (num) {
                        case 0:
                            if (litera != 10)
                                if (litera != 9 && litera != 13 && litera != 32 && litera != 8) {
                                    this.lenlex = 0;
                                    switch (lexClass) {
                                        case 1:
                                            this.lexema[this.lenlex++] = (byte) litera;
                                            return this.lang.getLexemaCode(this.lexema, this.lenlex, 0);
                                        case 2:
                                            this.lexema[this.lenlex++] = (byte) litera;
                                            lexemaClass = 1;
                                            num = 21;
                                            break;
                                        case 3:
                                            this.lexema[this.lenlex++] = (byte) litera;
                                            lexemaClass = 2;
                                            num = 31;
                                    }

                                    if (num == 0) {
                                        this.lexema[this.lenlex++] = (byte) litera;
                                        lexemaClass = 5;
                                        if (litera == 46) {
                                            num = 4;
                                        } else if (litera == 58) {
                                            num = 5;
                                        } else if (litera == 47) {
                                            num = 6;
                                        } else if (litera == 62) {
                                            num = 7;
                                        } else if (litera == 60) {
                                            num = 8;
                                        } else if (litera == 33) {
                                            num = 9;
                                        } else if (litera == 39) {
                                            num = 10;
                                        } else {
                                            num = 11;
                                        }
                                    }
                                }
                        default:
                            break;
                        case 4:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera >= 48 && litera <= 57) {
                                num = 32;
                                break;
                            }

                            if (litera == 46) {
                                return 268435741;
                            }

                            this.remainingData = litera;
                            --this.lenlex;
                            return 268435464;
                        case 5:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera == 61) {
                                return 268435577;
                            }

                            this.remainingData = litera;
                            --this.lenlex;
                            return 268435530;
                        case 6:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera != 42) {
                                this.remainingData = litera;
                                --this.lenlex;
                                return 268435710;
                            }

                            num = 61;
                            break;
                        case 7:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera == 61) {
                                return 268435683;
                            }

                            this.remainingData = litera;
                            --this.lenlex;
                            return 268435681;
                        case 8:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera == 61) {
                                return 268435684;
                            }

                            this.remainingData = litera;
                            --this.lenlex;
                            return 268435680;
                        case 9:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera == 61) {
                                return 268435682;
                            }

                            this.remainingData = litera;
                            --this.lenlex;
                            num = 11;
                            break;
                        case 10:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera == 39) {
                                return 268435472;
                            }
                            break;
                        case 11:
                            if (error.indexOf(litera) >= 0) {
                                this.remainingData = litera;
                                num = 0;
                            }
                            break;
                        case 21:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (lexClass != 2 && lexClass != 3) {
                                this.remainingData = litera;
                                --this.lenlex;
                                count = this.lang.getLexemaCode(this.lexema, this.lenlex, 0);
                                if (count == -1) {
                                    return 268435470;
                                }
                                return count;
                            }
                            break;
                        case 31:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera >= 48 && litera <= 57) {
                                break;
                            }

                            if (litera != 46) {
                                this.remainingData = litera;
                                --this.lenlex;
                                return 268435468;
                            }

                            num = 32;
                            break;
                        case 32:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera >= 48 && litera <= 57) {
                                break;
                            }

                            if (litera != 101 && litera != 69) {
                                this.remainingData = litera;
                                --this.lenlex;
                                return 268435468;
                            }

                            num = 33;
                            break;
                        case 33:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera >= 48 && litera <= 57) {
                                num = 35;
                            } else {
                                if (litera != 43 && litera > 45) {
                                    num = 11;
                                    continue;
                                }
                                num = 34;
                            }
                            break;
                        case 34:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera >= 48 && litera <= 57) {
                                num = 35;
                                break;
                            }
                            num = 11;
                            this.remainingData = litera;
                            --this.lenlex;
                            break;
                        case 35:
                            this.lexema[this.lenlex++] = (byte) litera;
                            if (litera >= 48 && litera <= 57) {
                                break;
                            }

                            this.remainingData = litera;
                            --this.lenlex;
                            return 268435468;
                        case 61:
                            if (litera == 42) {
                                num = 62;
                            }
                            break;
                        case 62:
                            if (litera == 47) {
                                num = 0;
                                this.lenlex = 0;
                            } else {
                                num = 61;
                            }
                    }
                }
                this.programData = null;
            }

            if (this.lenlex == 0) {
                return -1;
            } else {
                switch (lexemaClass) {
                    case 1:
                        count = this.lang.getLexemaCode(this.lexema, this.lenlex, 0);
                        if (count != -1) {
                            return count;
                        }
                        return 268435462;
                    case 2:
                        return 268435464;
                    case 3:
                        return -1;
                    case 4:
                        return 268435464;
                    case 5:
                        this.lang.getLexemaCode(this.lexema, this.lenlex, 0);
                    default:
                        return -1;
                }
            }
        } catch (IOException ex) {
            //ex.printStackTrace();
            return -1;
        }
    }
}
