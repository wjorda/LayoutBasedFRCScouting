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
        int[][] stacks = new int[6][3];

        for (int[] stack : stacks) for (int i = 0; i < stack.length; i++) stack[i] = 0;

        for (ToteStack stack : stackList) {
            stacks[stack.getTotes() - 1][0]++;
            if (stack.hasCan()) stacks[stack.getTotes() - 1][1]++;
            if (stack.hasNoodle()) stacks[stack.getTotes() - 1][2]++;
        }

        StringBuilder sb = new StringBuilder("[");
        for (int[] stack : stacks)
            for (int i : stack) sb.append(i + " ");

        sb.deleteCharAt(sb.length()).append("]");
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

            for (int height = 1; scan.hasNext(); height++) {
                int num = scan.nextInt(), cans = scan.nextInt(), noodles = scan.nextInt();
                for (int i = 0; i < num; i++) {
                    ToteStack t = new ToteStack(height, false, false);
                    if (cans > 0) {
                        t.setCan(true);
                        cans--;
                    }
                    if (noodles > 0) {
                        t.setNoodle(true);
                        noodles--;
                    }
                    stacks.add(t);
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
