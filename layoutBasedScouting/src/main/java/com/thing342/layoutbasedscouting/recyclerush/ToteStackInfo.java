package com.thing342.layoutbasedscouting.recyclerush;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by wes on 1/24/15.
 */
public class ToteStackInfo
{
    public boolean flag;
    private String info;

    public ToteStackInfo()
    {
        flag = true;
    }

    public ToteStackInfo(List<ToteStack> stackList)
    {
        flag = false;
        int cans = 0, noodles = 0, stackHeights[] = new int[6];

        for (ToteStack t : stackList) {
            if (t.hasCan()) cans++;
            if (t.hasNoodle()) noodles++;
            stackHeights[t.getTotes() - 1]++;
        }

        StringBuilder s = new StringBuilder();
        s.append('[');
        s.append(cans).append(' ');
        s.append(noodles).append(' ');

        for (int i : stackHeights) s.append(i).append(' ');
        s.append(']');
        info = s.toString();
    }

    public ToteStackInfo(String parse)
    {
        flag = false;
        this.info = parse;
    }

    public List<ToteStack> getStacks()
    {
        LinkedList<ToteStack> stacks = new LinkedList<>();
        if (!flag) {
            String text = info.replace("[", "").replace("]", "");
            Scanner scan = new Scanner(text);
            int cans = scan.nextInt();
            int noodles = scan.nextInt();

            for (int size = 1; scan.hasNextInt(); size++) {
                int num = scan.nextInt();
                for (int i = 0; i < num; i++) stacks.add(new ToteStack(size, false, false));
            }

            for (int i = stacks.size() - 1; i >= 0; i--) {
                ToteStack ts = stacks.get(i);
                if (cans > 0) {
                    ts.setCan(true);
                    cans--;
                    if (noodles > 0) {
                        ts.setNoodle(true);
                        noodles--;
                    }
                }
            }
        }

        return stacks;
    }

    public void setStacks(List<ToteStack> stackList)
    {
        flag = true;
        int cans = 0, noodles = 0, stackHeights[] = new int[6];

        for (ToteStack t : stackList) {
            if (t.hasCan()) cans++;
            if (t.hasNoodle()) noodles++;
            stackHeights[t.getTotes() - 1]++;
        }

        StringBuilder s = new StringBuilder();
        s.append('[');
        s.append(cans).append(' ');
        s.append(noodles).append(' ');

        for (int i : stackHeights) s.append(i).append(' ');
        s.append(']');
        info = s.toString();

    }

    @Override
    public String toString()
    {
        return info;
    }
}
