package lab;

import JavaTeacherLib.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class SysProgrammingMainAlgorithm {

    public static void main(String[] args) {
        byte[] readline = new byte[80];
        boolean result;
        String fileName;
        MyLang testLang = null;
        int codeAction, llk = 1, textLen;
        String[] menu = {"*1.  Прочитати граматику з файла  ",
                " 2.  Надрукувати граматику",
                " 3.  Пошук ліворекурсивних нетерміналів",
                " 4. Вихід з системи"
        };
        do {
            codeAction = 0;
            String upr;
            for (String ss : menu) System.out.println(ss); // вивести меню
            System.out.println("Введіть код дії або end:");
            try {
                textLen = System.in.read(readline);
                upr = new String(readline, 0, textLen, "ISO-8859-1");
                if (upr.trim().equals("end")) return;
                codeAction = new Integer(upr.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // перевірка на виконання усіх попередніх дій
            result = false;
            switch (codeAction) {
                case 1: //1. Прочитати граматику з файла",
                    System.out.print("Введіть ім'я файлу граматики:");
                    try {
                        textLen = System.in.read(readline);
                        fileName = new String(readline, 0, textLen, "ISO-8859-1");
                        fileName = fileName.trim();
                    } catch (Exception ee) {
                        System.out.println("Системна помилка: " + ee.toString());
                        return;
                    }
                    System.out.print("Введіть значення параметра k : ");
                    try {
                        textLen = System.in.read(readline);
                        String llkText = new String(readline, 0, textLen, "ISO-8859-1");
                        llkText = llkText.trim();
                        llk = Integer.parseInt(llkText);
                    } catch (Exception ee) {
                        System.out.println("Системна помилка: " + ee.toString());
                        return;
                    }
                    testLang = new MyLang(fileName, llk);
                    if (!testLang.isCreate()) break;  //не створили об'єкт
                    System.out.println("Граматика прочитана успішно");
                    result = true;
                    for (int jj = 0; jj < menu.length; jj++) {
                        if (menu[jj].substring(0, 1).equals(" ")) continue;
                        menu[jj] = menu[jj].replace(menu[jj].charAt(0), '*');
                    }
                    break;
                case 2:  // Надрукувати граматику
                    testLang.printGramma();
                    break;
                case 3:    //Пошук ліворекурсивних нетерміналів"
                    testLang.leftRecursNonnerminal();
                    //findleftRecursNoterminal(testLang);
                    break;
                case 4: // rtrtrtr
                    return;

            }  // кінець switch
            // блокуємо елемент обробки
            if (result) // функція виконана успішно
                if (menu[codeAction - 1].substring(0, 1).equals("*"))
                    menu[codeAction - 1] = menu[codeAction - 1].replace('*', '+');
        } while (true);  //глобальний цикл  обробки

    }  // кінець main

    public static LlkContext[] firstK(MyLang lang) {
        int[] llkWord = new int[10];
        boolean upr = false;
        int[] term = lang.getTerminals();
        int[] nonterm = lang.getNonTerminals();
        LlkContext[] llkTrmContext = lang.getLlkTrmContext();
        LlkContext[] rezult = new LlkContext[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int iter = 0;

        int ii;
        for (ii = 0; ii < rezult.length; ++ii) {
            rezult[ii] = new LlkContext();
        }

        label125:
        do {
            upr = false;
            ++iter;
            Iterator i$ = lang.getLanguarge().iterator();

            while (true) {
                while (true) {
                    if (!i$.hasNext()) {
                        continue label125;
                    }

                    Node tmp = (Node) i$.next();
                    int[] tmprole = tmp.getRoole();

                    for (ii = 0; ii < nonterm.length && tmprole[0] != nonterm[ii]; ++ii) {
                    }

                    if (tmprole.length == 1) {
                        if (rezult[ii].addWord(new int[0])) {
                            upr = true;
                        }
                    } else {
                        int ii0;
                        int ii1;
                        for (ii0 = 1; ii0 < tmprole.length; ++ii0) {
                            if (tmprole[ii0] > 0) {
                                for (ii1 = 0; ii1 < term.length && term[ii1] != tmprole[ii0]; ++ii1) {
                                }

                                tmpLlk[ii0 - 1] = llkTrmContext[ii1];
                            } else {
                                for (ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[ii0]; ++ii1) {
                                }

                                if (rezult[ii1].calcWords() == 0) {
                                    break;
                                }

                                tmpLlk[ii0 - 1] = rezult[ii1];
                            }
                        }

                        if (ii0 == tmprole.length) {
                            int multCount = tmprole.length - 1;

                            for (ii1 = 0; ii1 < multCount; ++ii1) {
                                mult[ii1] = 0;
                                maxmult[ii1] = tmpLlk[ii1].calcWords();
                            }

                            int realCalc = 0;

                            for (ii1 = 0; ii1 < multCount; ++ii1) {
                                if (ii1 == 0) {
                                    realCalc = tmpLlk[ii1].minLengthWord();
                                } else {
                                    int minLength = tmpLlk[ii1].minLengthWord();
                                    if (realCalc >= lang.getLlkConst()) {
                                        break;
                                    }

                                    realCalc += minLength;
                                }
                            }

                            realCalc = ii1;

                            while (true) {
                                try {
                                    Method m = MyLang.class.getDeclaredMethod("newWord", int.class, LlkContext[].class, int[].class, int.class);
                                    m.setAccessible(true);
                                    llkWord = (int[]) (m.invoke(lang, lang.getLlkConst(), tmpLlk, mult, realCalc));
                                } catch (Exception e) {
                                }
                                //llkWord = lang.newWord(lang.getLlkConst(), tmpLlk, mult, realCalc);
                                if (rezult[ii].addWord(llkWord)) {
                                    upr = true;
                                }

                                try {
                                    Method m = MyLang.class.getDeclaredMethod("newCalcIndex", int[].class, int[].class, int.class);
                                    m.setAccessible(true);
                                    if (!(boolean) m.invoke(lang, mult, maxmult, realCalc))
                                        break;
                                } catch (Exception e) {
                                }

                                /*if (!lang.newCalcIndex(mult, maxmult, realCalc)) {
                                    break;
                                }*/
                            }
                        }
                    }
                }
            }
        } while (upr);
        return rezult;
    }

    public static LlkContext[] followK(MyLang lang) {
        LinkedList<Node> lan = lang.getLanguarge();
        LlkContext[] firstK = lang.getFirstK();
        int[] term = lang.getTerminals();
        int[] nonterm = lang.getNonTerminals();
        LlkContext[] llkTrmContext = lang.getLlkTrmContext();
        LlkContext[] rezult = new LlkContext[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int iter = 0;

        int ii;
        for (ii = 0; ii < rezult.length; ++ii) {
            rezult[ii] = new LlkContext();
        }

        int aaxioma = lang.getAxioma();

        for (ii = 0; ii < nonterm.length && nonterm[ii] != aaxioma; ++ii) {
        }

        rezult[ii].addWord(new int[0]);

        boolean upr;
        label149:
        do {
            upr = false;
            ++iter;
            Iterator i$ = lan.iterator();

            while (true) {
                int[] tmprole;
                do {
                    if (!i$.hasNext()) {
                        continue label149;
                    }

                    Node tmp = (Node) i$.next();
                    tmprole = tmp.getRoole();

                    for (ii = 0; ii < nonterm.length && tmprole[0] != nonterm[ii]; ++ii) {
                    }

                    if (ii == nonterm.length) {
                        return null;
                    }
                } while (rezult[ii].calcWords() == 0);

                int leftItem = ii;

                for (int jj = 1; jj < tmprole.length; ++jj) {
                    if (tmprole[jj] <= 0) {
                        int multCount = 0;

                        int ii1;
                        for (int ii0 = jj + 1; ii0 < tmprole.length; ++ii0) {
                            if (tmprole[ii0] > 0) {
                                for (ii1 = 0; ii1 < term.length && term[ii1] != tmprole[ii0]; ++ii1) {
                                }

                                tmpLlk[multCount++] = llkTrmContext[ii1];
                            } else {
                                for (ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[ii0]; ++ii1) {
                                }

                                tmpLlk[multCount++] = firstK[ii1];
                            }
                        }

                        tmpLlk[multCount++] = rezult[leftItem];

                        for (ii1 = 0; ii1 < multCount; ++ii1) {
                            mult[ii1] = 0;
                            maxmult[ii1] = tmpLlk[ii1].calcWords();
                        }

                        int realCalc = 0;

                        for (ii1 = 0; ii1 < multCount; ++ii1) {
                            if (ii1 == 0) {
                                realCalc = tmpLlk[ii1].minLengthWord();
                            } else {
                                int minLength = tmpLlk[ii1].minLengthWord();
                                if (realCalc >= lang.getLlkConst()) {
                                    break;
                                }

                                realCalc += minLength;
                            }
                        }

                        realCalc = ii1;

                        for (ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[jj]; ++ii1) {
                        }

                        while (true) {
                            try {
                                Method m = MyLang.class.getDeclaredMethod("newWord", int.class, LlkContext[].class, int[].class, int.class);
                                m.setAccessible(true);
                                int[] llkWord = (int[]) m.invoke(lang, lang.getLlkConst(), tmpLlk, mult, realCalc);
                                if (rezult[ii1].addWord(llkWord)) {
                                    upr = true;
                                }
                            } catch (Exception e) {
                            }

                            try {
                                Method m = MyLang.class.getDeclaredMethod("newCalcIndex", int[].class, int[].class, int.class);
                                m.setAccessible(true);
                                if (!(boolean) m.invoke(lang, mult, maxmult, realCalc))
                                    break;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        } while (upr);
        return rezult;
    }

    static void tesrReadWrite(String fname) {
        String readline;
        BufferedReader s;
        BufferedWriter bw;
        try {
            s = new BufferedReader(new FileReader(fname));
            bw = new BufferedWriter(new FileWriter("c:\\rez.txt"));
            // s=new FileInputStream (fname);
            //s=new FileInputStream ("C:\\Eclipse\\C1.txt");
            //s=new FileInputStream ("C:\\test1.txt");
            while (s.ready()) {
                readline = s.readLine();
                System.out.println(readline);
                //System.out.println("Read Line");
                //bw.write(readline, 0,readline.length() );
                //bw.write((int)'\r'); bw.flush();
                //System.out.println("Print Line");
            }

            //bw.close();
        } catch (Exception ee) {
            System.out.print("File: " + fname + "not found\n");
            //return;
        }
    }

    public static void findleftRecursNoterminal(MyLang lang) {
        int[] setNoTerminals = new int[lang.getNonTerminals().length];
        int[] noTerminals = lang.getNonTerminals();
        int[] eps = lang.createEpsilonNonterminals();
        int exists = 0;
        int noexists = 0;

        for (int i = 0; i < noTerminals.length; ++i) {
            int count = 0;
            int count1 = 1;
            exists = 0;
            setNoTerminals[count] = noTerminals[i];

            do {
                LinkedList<Node> language = lang.getLanguarge();
                int cnt = 0;
                label:
                while (true) {
                    int[] rule;
                    do {
                        if (cnt >= language.size()) {
                            break label;
                        }
                        Node tmp = language.get(cnt);
                        rule = tmp.getRoole();
                        cnt++;
                    } while (rule[0] != setNoTerminals[count]);

                    int count_i;
                    for (count_i = 1; count_i < rule.length && rule[count_i] <= 0 && rule[count_i] != setNoTerminals[0]; ++count_i) {
                        int count_j;
                        for (count_j = 0; count_j < count1 && rule[count_i] != setNoTerminals[count_j]; ++count_j) {
                        }

                        if (count_j == count1) {
                            setNoTerminals[count1++] = rule[count_i];
                        }

                        if (eps == null) {
                            break;
                        }

                        for (count_j = 0; count_j < eps.length && rule[count_i] != eps[count_j]; ++count_j) {
                        }

                        if (count_j == eps.length) {
                            break;
                        }
                    }

                    if (count_i != rule.length && rule[count_i] == setNoTerminals[0]) {
                        System.out.print("Нетермінал: " + lang.getLexemaText(setNoTerminals[0]) + " ліворекурсивний \n");
                        exists++;
                        noexists++;
                        break;
                    }
                }

                if (exists != 0) {
                    break;
                }

                ++count;
            } while (count < count1);
        }

        if (noexists == 0) {
            System.out.print("В граматиці відсутні ліворекурсивні нетермінали \n");
        }
    }

    public static void ifIsLL1(MyLang lang) {
        LlkContext[] first = firstK(lang);
        lang.setFirstK(first);
        //lang.printFirstkContext();

        LlkContext[] follow = followK(lang);
        lang.setFollowK(follow);
        //lang.printFollowkContext();

        int[] nonterm = lang.getNonTerminals();

        for (int j = 0; j < nonterm.length; ++j) {
            LlkContext tmp_first = first[j];
            LlkContext tmp_follow = follow[j];

            Set<String> set_first = new HashSet<>();
            for (int ii = 0; ii < tmp_first.calcWords(); ++ii) {
                int[] word = tmp_first.getWord(ii);
                if (word.length == 0) {
                    set_first.add("e");
                } else {
                    for (int ii1 = 0; ii1 < word.length; ++ii1) {
                        set_first.add(lang.getLexemaText(word[ii1]));
                    }
                }
            }

            Set<String> set_follow = new HashSet<>();
            for (int ii = 0; ii < tmp_follow.calcWords(); ++ii) {
                int[] word = tmp_follow.getWord(ii);
                if (word.length == 0) {
                    set_first.add("e");
                } else {
                    for (int ii1 = 0; ii1 < word.length; ++ii1) {
                        set_first.add(lang.getLexemaText(word[ii1]));
                    }
                }
            }

            if (set_follow.retainAll(set_first) == true) {
                System.out.println(false);
                return;
            }
        }
        System.out.println(true);
    }
}
