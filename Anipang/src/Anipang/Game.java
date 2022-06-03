package Anipang;

import java.util.*;
import java.util.stream.Collectors;


class Game {

    private final double[][] board;
    private boolean[][] item_board;
    private double score;
    private final double[] numbers;
    private final Controller controller;
    private int[][] selectedBtn;
    private LinkedList<ArrayList<Object>> bombList;
    private long time;
    boolean chk;

    Game(Controller controller) {
        score = 0;
        board = new double[7][7];
        item_board = new boolean[7][7];
        createBoard();
        numbers = new double[8];
        this.controller = controller;
        bombList = new LinkedList<>();
        time = 60;
        time();

        double number = 0.5;
        for(int i = 0; i < numbers.length; i++)
        {
            numbers[i] = number;
            number += 0.5;
        }
    }

    private void createBoard() // board 에 값을 넣어주는 메서드
    {
        Random random = new Random();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                if(!chk)
                    board[3][3] = -4;


                if (board[i][j] == 0)
                {
                    double num;
                    do {
                        int n = random.nextInt(7) + 2;
                        num = (double) n / 2;
                    }
                    while (!chkNum(num, i, j));
                    board[i][j] = num;
                }
            }
        }
        chk = true;
    }

    private boolean chkNum(double num, int idx1, int idx2) // board에 값이 중복되는지 확인
    {
        if (1 < idx2) //가로 탐색
        {
            int count = 1;
            for (int i = 1; i < 3; i++) {
                if ((num == board[idx1][idx2 - i]))
                    count++;
            }

            if (count > 2)
                return false;
        }

        if (1 < idx1) // 세로 탐색
        {
            int count = 1;
            for (int i = 1; i < 3; i++) {
                if (num == board[idx1 - i][idx2])
                    count++;
            }

            if (count > 2)
                return false;
        }

        return true;
    }

    int getAnimal(int idx1, int idx2) // board의 값에 따라 Img의 값을 반환
    {
        int num = (int) (board[idx1][idx2] * 2);
        int animal = 0;

        switch (num) {
            case 2:
                animal = 0;
                break;

            case 3:
                animal = 1;
                break;

            case 4:
                animal = 2;
                break;

            case 5:
                animal = 3;
                break;

            case 6:
                animal = 4;
                break;

            case 7:
                animal = 5;
                break;

            case 8:
                animal = 6;
                break;

            case -2:
                animal = 7;
                break;

            case -4:
                animal = 8;
                break;

            case -6:
                animal = 9;
                break;

            case -8:
                animal = 10;
                break;

        }
        return animal;
    }

    private void organizeBoard(int idx1, int idx2) // board 떨어뜨리기
    {
        for (int i = idx1; 0 <= i; i--) // 아래에서 위로 탐색
        {
            if (board[i][idx2] == 0) // 3개가 완성되어 지워졌다면
            {
                for (int j = i - 1; 0 <= j; j--) // 위에 있는 보드들을 밑으로 내리는 반복문
                {
                    if (board[j][idx2] != 0) {
                        board[i][idx2] = board[j][idx2];
                        board[j][idx2] = 0;
                        break;
                    }
                }
            }
        }

    }

    double getScore() {
        return score;
    }

    private boolean chkCombination(int i, int j) // 더 이상 결합이 가능한지 체크
    {
        if (i == 0) {
            if (j == 0) {
                if (swap(i, j, i + 1, j))
                    return true;

                return swap(i, j, i, j + 1);
            } else if (j == board.length - 1) {
                if (swap(i, j, i + 1, j))
                    return true;

                return swap(i, j, i, j - 1);
            } else {
                if (swap(i, j, i + 1, j))
                    return true;

                if (swap(i, j, i, j - 1))
                    return true;

                return swap(i, j, i, j + 1);
            }
        } else if (i == board.length - 1) {
            if (j == 0) {
                if (swap(i, j, i - 1, j))
                    return true;

                return swap(i, j, i, j + 1);
            } else if (j == board.length - 1) {
                if (swap(i, j, i - 1, j))
                    return true;

                return swap(i, j, i, j - 1);
            } else {
                if (swap(i, j, i - 1, j))
                    return true;

                if (swap(i, j, i, j - 1))
                    return true;

                return swap(i, j, i, j + 1);
            }
        } else {
            if (j == 0) {
                if (swap(i, j, i + 1, j))
                    return true;

                if (swap(i, j, i - 1, j))
                    return true;

                return swap(i, j, i, j + 1);
            } else if (j == board.length - 1) {
                if (swap(i, j, i + 1, j))
                    return true;

                if (swap(i, j, i - 1, j))
                    return true;

                return swap(i, j, i, j - 1);
            } else {
                if (swap(i, j, i + 1, j))
                    return true;

                if (swap(i, j, i - 1, j))
                    return true;

                if (swap(i, j, i, j - 1))
                    return true;

                return swap(i, j, i, j + 1);
            }
        }
    }

    private boolean swap(int idx1, int idx2, int idx3, int idx4) {
        if (0 < board[idx1][idx2] * 2) // 아이템이 아닐 경우만 확인
        {
            double num = board[idx1][idx2];
            board[idx1][idx2] = board[idx3][idx4];
            board[idx3][idx4] = num;

            HashSet<ArrayList<Integer>> set = new HashSet<>();
            for(double number : numbers)
            {
                set.addAll(chkBoard(number));
            }

            num = board[idx1][idx2];
            board[idx1][idx2] = board[idx3][idx4];
            board[idx3][idx4] = num;

            return set.size() != 0;
        } else
            return false;
    }

    private void refreshBoard() {
        Random random = new Random();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (0 < board[i][j]) // 아이템이 아닌 경우에 새로 뽑겠다
                {
                    double num;
                    do {
                        int n = random.nextInt(7) + 2;
                        num = (double) n / 2;
                    }
                    while (!chkNum(num, i, j));
                    board[i][j] = num;
                }
            }
        }
    }

    private HashSet<ArrayList<Integer>> chkBoard(double num)  // 한번 확인하기 (뭔가 이상함)
    {
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        ArrayList<ArrayList<Integer>> horizonList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> verticalList = new ArrayList<>();

        //여기서 count, 마지막 인덱스, vertical or horizon 을 담은 ArrayList 넘겨주기

        for(int i = 0; i < board.length; i++) // 가로
        {
            int count = 0;
            int idx = 100;

            for(int j = 0; j < board[i].length; j++)
            {
                if(board[i][j] == num)
                {
                    count++;
                    idx = j;
                }

                if(board[i][j] != num || j == board[i].length-1)
                {
                    if(4 <= count)
                    {
                        ArrayList<Object> list = new ArrayList<>();

                        list.add("horizon");
                        list.add(i);
                        list.add(idx);
                        list.add(count);

                        bombList.add(list);
                    }

                    if(3 <= count)
                    {
                        for(int k = 0; k < count; k++)
                        {
                            ArrayList<Integer> al = new ArrayList<>();
                            al.add(i);
                            al.add(idx-k);
                            al.add(count);
                            set.add(al);

                            if(k == 0)
                                horizonList.add(al);
                        }
                    }

                    count = 0;
                    idx = 100;
                }
            }
        }

        for(int i = 0; i < board.length; i++) // 세로
        {
            int count = 0;
            int idx = 100;

            for(int j = 0; j < board[i].length; j++)
            {
                if(board[j][i] == num)
                {
                    count++;
                    idx = j;
                }

                if(board[j][i] != num || j == board[i].length-1)
                {
                    if(4 <= count)
                    {
                        ArrayList<Object> list = new ArrayList<>();
                        list.add("vertical");
                        list.add(idx);
                        list.add(i);
                        list.add(count);

                        bombList.add(list);
                    }

                    if(3 <= count)
                    {
                        for(int k = 0; k < count; k++)
                        {
                            ArrayList<Integer> al = new ArrayList<>();
                            al.add(idx-k);
                            al.add(i);
                            al.add(count);
                            set.add(al);

                            if(k == 0)
                                verticalList.add(al);
                        }

                    }

                    count = 0;
                    idx = 100;
                }
            }
        }

        if(verticalList.size() != 0 && horizonList.size() != 0)
        {
            for(ArrayList<Integer> horizon : horizonList)
            {
                if(horizon.get(2).equals(5))
                    continue;

                for(ArrayList<Integer> vertical : verticalList)
                {
                    if(vertical.get(2).equals(5))
                        continue;

                    chkCross(horizon,vertical);
                }
            }


        }

        return set;
    }

    private void chkCross(ArrayList<Integer> horizon, ArrayList<Integer> vertical)
    {
        int idx1H = horizon.get(0);  // y
        int idx2H = horizon.get(1);  // x
        int countH = horizon.get(2);

        int idx1V = vertical.get(0);  // y
        int idx2V = vertical.get(1);  // x
        int countV = vertical.get(2);


        for(int i = 0; i < countH; i++)
        {
            for(int j = 0; j < countV; j++)
            {
                if(idx1H == idx1V-j && idx2H-i == idx2V)  // 겹치는 부분이 있냐?
                {

                    ArrayList<Object> list = new ArrayList<>();
                    list.add("cross"); //0
                    list.add(idx1H);
                    list.add(idx2H);
                    list.add(countH);
                    list.add(idx1V); // 4
                    list.add(idx2V);
                    list.add(countV);

                    bombList.add(list);

                    // 아이템 제거 4개 짜리
                    removeBomb(list);
                }
            }
        }
    }

    synchronized void switchBoard(int idx1, int idx2, int idx3, int idx4) // 게임진행1
    {
        if((0 < board[idx1][idx2] && board[idx1][idx2] == board[idx3][idx4])
                || (board[idx3][idx4] != -4 && board[idx1][idx2] == -4 && board[idx3][idx4] < 0)
                || (board[idx1][idx2] != -4 && board[idx3][idx4] == -4 && board[idx1][idx2] < 0)) // -4 와 아이템끼리의 교환
            return;

        HashSet<ArrayList<Integer>> removedSet = new HashSet<>();

        double num = board[idx1][idx2];
        board[idx1][idx2] = board[idx3][idx4];
        board[idx3][idx4] = num;
        controller.setInImg();

        selectedBtn = new int[2][2];
        selectedBtn[0][0] = idx1;
        selectedBtn[0][1] = idx2;
        selectedBtn[1][0] = idx3;
        selectedBtn[1][1] = idx4;

        if (board[idx1][idx2] < 0) // 아이템일 경우
        {
           if(board[idx1][idx2] == -4)
                removedSet.addAll(aimBomb(idx1,idx2,idx3,idx4));

           else
               removedSet.addAll(bomb(idx1,idx2));
        }

        if (board[idx3][idx4] < 0) // 아이템일 경우
        {
            if(board[idx3][idx4] == -4)
                removedSet.addAll(aimBomb(idx3,idx4,idx1,idx2));

            else
                removedSet.addAll(bomb(idx3,idx4));
        }

        for(double number : numbers)
        {
            removedSet.addAll(chkBoard(number));
        }

        if(removedSet.size() == 0)
        {
            num = board[idx1][idx2];
            board[idx1][idx2] = board[idx3][idx4];
            board[idx3][idx4] = num;
            controller.setInImg();
        }

        else
            progress(removedSet);
    }

    private void progress(HashSet<ArrayList<Integer>> set) // 게임진행2
    {
        countScore(set);

        for(ArrayList<Integer> al : set)
        {
            organizeBoard(al.get(0), al.get(1));
            controller.setInImg();
        }
        controller.setInImg();
        createBoard();
        // create_board()

        set = new HashSet<>();
        for(double number : numbers)
        {
            set.addAll(chkBoard(number));
        }
        System.out.println("1: "+set.size());
        System.out.println("ietm:"+ bombList);

        // create_board()

        // 이부분 나중에 수정 아이템 중복
        if(set.size() != 0)
            progress(set);

        else // 결합이 가능한지 확인
        {
            boolean chk_com = false;
            loop :for(int i = 0; i < board.length; i++)
            {
                for (int j = 0; j < board[i].length; j++)
                {
                    if(chkCombination(i,j))
                    {
                        chk_com = true;
                        break loop;
                    }
                }
            }

            if(!chk_com)
            {
                System.out.println("refresh");
                refreshBoard();
            }
        }
    }

    private void countScore(HashSet<ArrayList<Integer>> set) // 점수 카운트 및 값 0으로 바꿔주기
    {
        controller.setInImg();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run()
            {
                controller.setOutImg(set);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        }
        catch (InterruptedException e)
        {
            System.out.println("죽었다.");
        }

        // 여기에 아이템 생성이 들어가야 함
        ArrayList<ArrayList<Integer>> item = makeBomb();

        for(ArrayList<Integer> al : set)
        {
            double num = board[al.get(0)][al.get(1)];

            if(0 < num)
                score += 1;

            else
                score += 5;

            board[al.get(0)][al.get(1)] = 0;
        }

        for(ArrayList<Integer> al : item)
        {
            board[al.get(0)][al.get(1)] = al.get(2);
        }


        set_ItemBoard();
        //System.out.println("score: "+score);
        controller.setScore();
    }

    private void removeBomb(ArrayList<Object> cross) // 십자폭탄 완성 시 가로폭탄, 세로 폭탄 지우기
    {
        ArrayList<Integer> al = new ArrayList<>();

        for(int i = 0; i < bombList.size(); i++)
        {
            int Y = (int)bombList.get(i).get(1);
            int X = (int)bombList.get(i).get(2);
            int count = (int)bombList.get(i).get(3);

            if(bombList.get(i).get(0).equals("horizon"))
            {
                loop :for(int j = 0; j < count; j++)
                {
                    for(int k = 0; k < (int)cross.get(3); k++)
                    {
                        if(Y == (int)cross.get(1) && X-j == (int)cross.get(2)-k)
                        {
                            al.add(i);
                            break loop;
                        }
                    }
                }
            }

            else if(bombList.get(i).get(0).equals("vertical"))
            {
                loop: for(int j = 0; j < count; j++)
                {
                    for(int k = 0; k < (int)cross.get(6); k++)
                    {
                        if(Y-j == (int)cross.get(4)-k && X == (int)cross.get(5))
                        {
                            al.add(i);
                            break loop;
                        }
                    }
                }
            }
        }

        al = (ArrayList<Integer>) al.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        for(int idx : al)
            bombList.remove(idx);
    }

    private ArrayList<ArrayList<Integer>> makeBomb() // 아이템 만드는 메소드
    {
        System.out.println("makeBomb: "+bombList);
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();

        // 플레이어가 변경한 인덱스와 맞는지 확인 >> 맞을 경우 거기에 , 아닐 경우 count 를 활용하여 랜덤하게
        for(ArrayList<Object> al : bombList)
        {
            boolean chk = false;

            if(al.get(0).equals("horizon")) // 가로
            {
                int Y = (int) al.get(1);
                int X = (int) al.get(2);
                int count = (int)al.get(3);

                for(int j = 0; j < selectedBtn.length; j++) // 플레이어가 바꾼 블록에서 아이템이 생성될 때
                {
                    for(int i = 0; i < count; i++)
                    {
                        if(selectedBtn[j][0] == Y && selectedBtn[j][1] == X-i)
                        {
                            chk = true;
                            ArrayList<Integer> aa = new ArrayList<>();

                            aa.add(Y);
                            aa.add(X-i);

                            if(count == 5)
                                aa.add(-4);

                            else if(count == 4)
                                aa.add(-1);

                            arr.add(aa);
                        }
                    }
                }

                if(!chk) // 랜덤인 경우
                {
                    ArrayList<Integer> aa = new ArrayList<>();
                    Random random;
                    int num;

                    do {
                        random = new Random();
                        num = random.nextInt(count);
                    }
                    while (!chkCollocate(arr,Y,X-num));

                    aa.add(Y);
                    aa.add(X-num);

                    if(count == 5)
                        aa.add(-4);

                    else if(count == 4)
                        aa.add(-1);

                    arr.add(aa);
                }
            }

            else if(al.get(0).equals("vertical")) // 세로
            {
                int Y = (int) al.get(1);
                int X = (int) al.get(2);
                int count = (int)al.get(3);

                for(int j = 0; j < selectedBtn.length; j++) // 플레이어가 바꾼 블록에서 아이템이 생성될 때
                {
                    for(int i = 0; i < count; i++)
                    {
                        if(selectedBtn[j][0] == Y-i && selectedBtn[j][1] == X)
                        {
                            ArrayList<Integer> aa = new ArrayList<>();
                            chk = true;

                            aa.add(Y-i);
                            aa.add(X);

                            if(count == 5)
                                aa.add(-4);

                            else if(count == 4)
                                aa.add(-2);

                            arr.add(aa);
                        }
                    }
                }

                if(!chk) // 랜덤인 경우
                {
                    ArrayList<Integer> aa = new ArrayList<>();
                    Random random;
                    int num;

                    do {
                        random = new Random();
                        num = random.nextInt(count);
                    }
                    while (!chkCollocate(arr,Y-num, X));

                    aa.add(Y-num);
                    aa.add(X);

                    if(count == 5)
                        aa.add(-4);

                    else if(count == 4)
                        aa.add(-2);

                    arr.add(aa);

                }
            }

            else // 십자
            {
                int countH = (int)al.get(3);
                int countV = (int)al.get(6);
                int YH = (int)al.get(1);
                int XH = (int)al.get(2);
                int YV = (int)al.get(4);
                int XV = (int)al.get(5);

                ArrayList<Integer> aa = new ArrayList<>();

                loop : for(int i = 0; i < selectedBtn.length; i++)
                {
                    for(int j = 0; j < countH; j++) // 가로축 확인
                    {
                        if(selectedBtn[i][0] == YH && selectedBtn[i][1] == XH-j)
                        {

                            chk = true;

                            aa.add(YH);
                            aa.add(XH-j);
                            aa.add(-3);

                            arr.add(aa);

                            break loop;
                        }
                    }
                }

                if(!chk)
                {
                    System.out.println("make cross");
                    Random random = new Random();
                    int num = random.nextInt(2);

                    if(num == 0) // 가로축에 생성
                    {
                        num = random.nextInt(countH);

                        aa.add(YH);
                        aa.add(XH-num);
                    }

                    else // 세로 축에 생성성
                    {
                        num = random.nextInt(countV);

                        aa.add(YV-num);
                        aa.add(XV);
                    }
                    aa.add(-3);
                    arr.add(aa);
                }
            }
        }

        bombList = new LinkedList<>();
        int[] initializeArr = {100,100,100,100};
        initialize_SelectedBtn(initializeArr);

        return arr;
    }

    private HashSet<ArrayList<Integer>> bomb(int idx1, int idx2) // 유저가 고의로 폭탄을 터뜨린게 아닌 경우
    {
        int num = (int) board[idx1][idx2];
        HashSet<ArrayList<Integer>> set = new HashSet<>();

        if(num == -1 && !item_board[idx1][idx2])
            set.addAll(horizonBomb(idx1,idx2));

        else if(num == -2 && !item_board[idx1][idx2])
            set.addAll(verticalBomb(idx1,idx2));

        else if(num == -3 && !item_board[idx1][idx2])
            set.addAll(crossBomb(idx1,idx2));

        else if(num == -4 && !item_board[idx1][idx2])
            set.addAll(aimBomb(idx1,idx2));

        return set;
    }

    private HashSet<ArrayList<Integer>> horizonBomb(int idx1, int idx2) // 가로 폭탄
    {
        item_board[idx1][idx2] = true;
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        for(int i = 0; i < board.length; i++)
        {
            ArrayList<Integer> arrayList = new ArrayList<>();

            if(board[idx1][i] < 0 && i != idx2)
            {
                set.addAll(bomb(idx1,i));
            }

            else
            {
                arrayList.add(idx1);
                arrayList.add(i);
                set.add(arrayList);
            }
        }

        return set;
    }

    private HashSet<ArrayList<Integer>> verticalBomb(int idx1, int idx2) // 세로 폭탄
    {
        item_board[idx1][idx2] = true;
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        for(int i = 0; i < board.length; i++)
        {
            ArrayList<Integer> arrayList = new ArrayList<>();

            if(board[i][idx2] < 0 && (i != idx1)) // 만약 다른 폭탄이 존재 한다면
                set.addAll(bomb(i,idx2));

            else
            {
                arrayList.add(i);
                arrayList.add(idx2);
                set.add(arrayList);
            }
        }

        return set;
    }

    private HashSet<ArrayList<Integer>> crossBomb(int idx1, int idx2) // 십자 폭탄
    {
        item_board[idx1][idx2] = true;
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        set.addAll(horizonBomb(idx1, idx2));
        set.addAll(verticalBomb(idx1, idx2));

        return set;
    }

    private HashSet<ArrayList<Integer>> aimBomb(int idx1, int idx2) // 다른 폭탄이 터졌을 때 이 폭탄도 같이 터질 경우 호출
    {
        item_board[idx1][idx2] = true;

        HashSet<ArrayList<Integer>> set = new HashSet<>();
        ArrayList<Integer> a = new ArrayList<>();

        a.add(idx1);
        a.add(idx2);
        set.add(a);

        Random random = new Random();
        double num = random.nextInt(7)+2;

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                if(board[i][j] == num/2)
                {
                    ArrayList<Integer> arrayList = new ArrayList<>();
                    arrayList.add(i);
                    arrayList.add(j);
                    set.add(arrayList);
                }
            }
        }
        return set;
    }

    private HashSet<ArrayList<Integer>> aimBomb(int idx1, int idx2, int idx3, int idx4) // idx1, idx2는 aim_bomb
    {
        item_board[idx1][idx2] = true;
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        double num = Math.max(board[idx1][idx2],board[idx3][idx4]);

        if(num < 0)
        {
            if(board[idx1][idx2] == board[idx3][idx4]) // aim 폭탄끼리 바꿨을 때
            {
                item_board[idx3][idx4] = true;
                allClear();
            }

            // 아이템 끼리 바꾸었을 경우
            else if(board[idx3][idx4] < 0)
            {
                set.addAll(bomb(idx1,idx2));
            }
        }

        else
        {
            for(int i = 0; i < board.length; i++)
            {
                for(int j = 0; j < board[i].length; j++)
                {
                    if(num == board[i][j] || (i == idx1 && j == idx2) || (i == idx3 && j == idx4))
                    {
                        ArrayList<Integer> al = new ArrayList<>();
                        al.add(i);
                        al.add(j);
                        set.add(al);
                    }
                }
            }
        }

        return set;
    }

    private void allClear() // aim_bomb 끼리의 조합
    {
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        HashSet<ArrayList<Integer>> itemSet = new HashSet<>();

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                if(0 < board[i][j])
                {
                    ArrayList<Integer> al = new ArrayList<>();
                    al.add(i);
                    al.add(j);
                    set.add(al);
                }

                else if(board[i][j] < 0 && i != board.length-1)
                {
                    ArrayList<Integer> al = new ArrayList<>();
                    al.add(board.length-1);
                    al.add(j);
                    itemSet.add(al);
                }
            }
        }

        countScore(set);

        for(ArrayList<Integer> al : itemSet)
        {
            organizeBoard(al.get(0),al.get(1));
        }

        createBoard();
    }

    private void set_ItemBoard() // 탐색했으면 false
    {
        for(int i = 0; i < item_board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                item_board[i][j] = false;
    }

    private void initialize_SelectedBtn(int[] arr)
    {
        selectedBtn[0][0] = arr[0];
        selectedBtn[0][1] = arr[1];
        selectedBtn[1][0] = arr[2];
        selectedBtn[1][1] = arr[3];
    }

    private boolean chkCollocate(ArrayList<ArrayList<Integer>> arrayList, int i, int j) // 아이템 생성칸에 다른 아이템이 있는지 확인
    {
        for(ArrayList<Integer> al: arrayList)
        {
            if(al.get(0) == i && al.get(1) == j)
                return false;
        }
        return true;
    }

    private void time()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    long end = System.currentTimeMillis();
                    while ((end-start)/1000 < 60)
                    {
                        Thread.sleep(1000);
                        end = System.currentTimeMillis();

                        time = 60-((end-start)/1000);
                        controller.setTime(time);
                    }
                }
                catch (InterruptedException e)
                {
                    System.out.println("잠들다가 죽음");
                }
                controller.gameSet();
            }
        });

        thread.start();
    }
}

