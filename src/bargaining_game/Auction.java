package bargaining_game;

import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.NumberFormatter;

class MyException extends Exception //제시금액이 보유금액보다 많을 시 발생하는 예외
{
	public MyException() 
	{
		JOptionPane.showMessageDialog(null, "제시금액이 보유금액보다 많네요! 다시 입력하세요","금액 제한", JOptionPane.WARNING_MESSAGE);
	}
}

class MyException_2 extends Exception //협상 제한된 물품을 사려고 할때 발생하는 예외
{
	public MyException_2() 
	{
		JOptionPane.showMessageDialog(null, "협상 제한된 물품입니다.","협상 제한", JOptionPane.WARNING_MESSAGE);
	}
}

class MyException_3 extends Exception //정해진 숫자 범위를 넘어선 숫자, 문자를 입력할 때 발생하는 예외
{
	public MyException_3() 
	{
		JOptionPane.showMessageDialog(null, "정해준 숫자 범위를 넘어섰습니다.","숫자범위 제한", JOptionPane.WARNING_MESSAGE);
	}
}


public class Auction extends Bargain {
	
	String eb_item[]= {"아기 공룡 옷", "이케아 의자", "아이패드 프로 5세대 12.9인치 256GB 스페이스",
	        "애플 워치 3", "캣휠", "롤렉스 신형 씨드웰러 126600", "사우스케이프 가디건", "철제 선반",
	        "컴퓨터 본체", "반스 260", "에보니아 2인 소파", "자라 뽀글이 자켓 s", "에어팟 프로 풀박스",
	        "세탁기,건조기 앵글 선반", "맥북에어 M1 기본형 스페이스 그레이", "포도당 비타민", "3달 사용한 책상",
	        "거울", "아기 자라옷","키티 6공 다이어리","잔망루피 스파오 겨울잠옷 M","스벅 기프티콘 아메리카노 tall","맥북 에어 m1 (램 16G/256GB)","유니폼 브릿지 패딩",
	        "전동스쿠터 미니 할리","쉐이딩","캘빈클라인 CK 빈티지 안경 동글이","버버리 셔츠 M","몰랑이 10cm 인형"};

	String eb_quality[] = {"D","S","A","C","B","D","B","A","S","S","B","A","D","A","C","D","S","A","D",
			"B","D","S","A","C","B","C","C","B","B"}; //품질 리스트
	
	int eb_price[] = {30000,10000,12000000,30000,100000,16500000,70000,15000,100000,
		          20000,30000,15000,180000,85000,990000,15000,15000,15000,3000,10000,22000,4000,1090000,130000,450000,8000,50000,
		          22000,20000}; //가격 리스트
	
	int eb_can_price[] = {18600,9000,960000,21000,75000,9900000,50400,12000,92000,17600,
		              22200,12000,117000,72250,693000,9000,13500,12000,1800,7500,13640,4000,872000,91000,333000,5600,
		              35500,16500,15000}; //협상가능  최소 가격 리스트
	int eb_manner_temperature[] = {1, 3, 3, 2, 2, 1, 2, 3, 3, 3, 2, 3, 1, 3, 2, 1, 3, 2, 1,2,1,3,2,1,2,1,1,2,2}; //등급별 협상 가능 횟수 리스트
	
	int out_count = 0; //외부 카운트 변수
	int in_count = 0; //내부 카운트 변수
	int ran_num = 0;
	int money_A = 0; 
	int money_B = 0; //플레이어의 보유 금액
	int showing_money_A = 0; //플레이어 A의 제시 금액
	int showing_money_B = 0; //플레이어 B의 제시 금액
	int rank_A = 0;// 몇개 샀는지 확인
	int rank_B=0;
	Random rd = new Random();
	
	@Override
	public void game_tutorial() {
		
		// TODO Auto-generated method stub
		
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		label.setText("<html>------------------------------------[경매장터 게임]-------------------------------------" 
	            	+ "<br>1) <b>8개</b>의 상품이 <b>이미지, 품질, 가격 정보</b>와 함께 제시됩니다." 
	            	+ "<br>2)물건은 <b>8번 거래</b>할 수 있습니다."
	            	+ "<br>3)<b>플레이어 A와 B</b>가 번갈아 가며 가격을 입력합니다."
	            	+ "<br>4)둘 중 판매자와 협상에 성공한 사람이 거래하게 됩니다." 
	            	+ "<br>   이때, 판매자의 매너온도에 따라 협상 가능한 횟수가 다릅니다."
	            	+ "<br>---- <b>협상 가능 횟수가 0</b>이 되면, <b>거래 가능 횟수 -1</b>"        
	            	+ "<br>---- 다시 물건을 고르는 페이지로 넘어갑니다."  
	            	+ "<br>5) 모든 거래가 끝나면 <b>거래 버튼</b>을 누르세요."
	            	+ "<br>6)게임이 종료되면, 남은 돈과 산 물건의 개수에 따라 <b>랭킹</b>이 나옵니다."
	            	+ "<br>7)플레이어 A와 B중 물건을 <b>더 많이 산 사람</b>이 승리합니다.<br>"
	            	+ "<br>최대한 많은 물건을 사는게 목표입니다! <br>꼭 8개의 물건을 모두 사보자구요~</html>");

		label.setFont(new Font("SanSerif",Font.BOLD,20));
		label.setBounds(80, 100, 650, 400); //설명 입력

		Container c = frame.getContentPane();
		c.setLayout(null);
		JButton b1 = new JButton("게임 시작");
		b1.setBounds(300,500,200,50);
	    c.add(b1);
	    c.add(label); //버튼과 글씨 출력
		
		frame.setVisible(true);
		frame.setSize(800,800);
		frame.setTitle("당근마켓 게임");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); //창설정
		
		
	
		b1.addActionListener(new ActionListener() { //게임 시작 버튼 눌렀을 때
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				game_start(); //게임시작
				frame.setVisible(false); //이전 창 안보이게 하기
			}	
		} ); 
		
	}

	@Override
	public void game_start() {
		// TODO Auto-generated method stub	
			
		Random rd = new Random();	
		String item[] = new String [8];
		String cart[] = new String [8]; //장바구니
		String quality[] = new String [8];
		int price [] = new int[8];
		int can_price [] = new int[8]; //게임에 쓸 8칸 배열
		int manner_temperature [] = new int[8];
		int ma_count[] = new int[8];
		int temp[] = new int[8];
		int a = 8; //남은 물건 개수
		int bargain_limit_out[] = new int[1]; //협상 기회 8번 외부 카운트
		int bargain_limit_in[] = new int[8];  //협상 기회 실패한 횟수 세는 내부 카운트;
		int bargain_limit_so[] = new int[8];  //팔리면 거래 제한해주는
		bargain_limit_out[0] = 0;
		NumberFormat numberFormat = NumberFormat.getInstance();
		
		JFrame frame = new JFrame();
		Container c = frame.getContentPane();
		c.setLayout(null); 
		//JLabel label = new JLabel();
		JLabel label_1 = new JLabel();
		JLabel label_2 = new JLabel();
		JLabel label_3 = new JLabel();
		JLabel label_4 = new JLabel();
		JLabel label_5 = new JLabel();
		JLabel label_6 = new JLabel();
		JLabel label_7 = new JLabel();
		JLabel label_8 = new JLabel(); //setIcon이 안되는 관계로 아이콘 라벨은 노가다를...
		//JLabel label[] = new JLabel[8];
		
		JLabel na_label_1 = new JLabel();
		JLabel na_label_2 = new JLabel();
		JLabel na_label_3 = new JLabel();
		JLabel na_label_4 = new JLabel();
		JLabel na_label_5 = new JLabel();
		JLabel na_label_6 = new JLabel();
		JLabel na_label_7 = new JLabel();
		JLabel na_label_8 = new JLabel(); //이름 라벨
		
		JLabel ra_label_1 = new JLabel();
		JLabel ra_label_2 = new JLabel();
		JLabel ra_label_3 = new JLabel();
		JLabel ra_label_4 = new JLabel();
		JLabel ra_label_5 = new JLabel();
		JLabel ra_label_6 = new JLabel();
		JLabel ra_label_7 = new JLabel();
		JLabel ra_label_8 = new JLabel(); //랭크 라벨
		
		JLabel pr_label_1 = new JLabel();
		JLabel pr_label_2 = new JLabel();
		JLabel pr_label_3 = new JLabel();
		JLabel pr_label_4 = new JLabel();
		JLabel pr_label_5 = new JLabel();
		JLabel pr_label_6 = new JLabel();
		JLabel pr_label_7 = new JLabel();
		JLabel pr_label_8 = new JLabel(); //가격 라벨
		
		JLabel ma_label_1 = new JLabel();
		JLabel ma_label_2 = new JLabel();
		JLabel ma_label_3 = new JLabel();
		JLabel ma_label_4 = new JLabel();
		JLabel ma_label_5 = new JLabel();
		JLabel ma_label_6 = new JLabel();
		JLabel ma_label_7 = new JLabel();
		JLabel ma_label_8 = new JLabel(); //매너온도(=협상 횟수) 라벨
		
		JLabel limit_label = new JLabel(); //외부 카운트 라벨
		
		JButton b1 = new JButton("<html>      게임 결과보기<br>(외부 카운트가 0이 되면 실행가능)</html>");
		b1.setBounds(450,700,230,80); //결과보기 
		c.add(b1);
		
		
		ImageIcon so_icon = new ImageIcon(Danngn.class.getResource("/image/00.png"));
		Image so_img = so_icon.getImage();
		Image so_updateImg = so_img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
		ImageIcon so_updateIcon =  new ImageIcon(so_updateImg); //sold out(협상 성사시) 이미지 조정			
		
		ImageIcon[] eb_icon = new ImageIcon[30]; //29개 
		//ImageIcon[] icon = new ImageIcon[8];
		//레이아웃을 내 마음대로 설정 가능
		Image[] img = new Image[8];
		Image[] updateImg = new Image[8];
		ImageIcon[] updateIcon = new ImageIcon[8];
		//Icon showing_Icon; //협상때 보여줄 아이콘
		//ImageIcon so_img = new ImageIcon(Danngn.class.getResource("/image/00.png"));
		
		for(int i=0;i<8;i++)
		{
			temp[i] = rd.nextInt(29); //0~28까지의 난수
			for(int j=0;j<i;j++)
			{
				if(temp[i]==temp[j])
				{
					i--; //같은값, 즉 중복이라면 1을 빼주어 다시 랜덤을 돌려줌
				}
			}
		}
		
		for(int i=0;i<29;i++)
		{
			eb_icon[i] =  new ImageIcon(Danngn.class.getResource("/image/"+String.valueOf(i+2)+".png")); //이미지 폴더에서 이미지 추출
		}
		
		for(int i=0;i<8;i++)
		{
			item[i] = eb_item[temp[i]];
			quality[i] = eb_quality[temp[i]];
			price[i] = eb_price[temp[i]];
			can_price[i] = eb_can_price[temp[i]];
			manner_temperature[i] = eb_manner_temperature[i];
			
			bargain_limit_in[i] = 0; //매너온도, 거래제한 변수 다 0으로 초기화 해주기
			
		}
		
		for(int sum_price:can_price)
		{
			money_A += sum_price;	
			money_B += sum_price;
		}
		
		money_A = (int)(money_A * 1.2); //플레이어 보유 금액 == (8개 협상가능 최소 가격 합 x 1.2)
		money_B = (int)(money_A * 1.2); //플레이어 보유 금액 == (8개 협상가능 최소 가격 합 x 1.2)
		
		for(int i=0;i<8;i++)
		{
			/*label[i].setText(item[i]);
			label[i].setFont(new Font("SanSerif",Font.BOLD,18));
			if(i<4)
			{
				label[i].setBounds((80*(i+1))+(300*i), 200, 650, 400);
			}
			else
			{
				label[i].setBounds((80*(i+1))+(300*i), 500, 650, 400);
			} */
			
			
			img[i] = eb_icon[temp[i]].getImage();
		    updateImg[i] = img[i].getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
			updateIcon[i] = new ImageIcon(updateImg[i]); //무작위로 이미지 8칸 할당
		}
		
		label_1.setIcon(updateIcon[0]);
		label_1.setBounds(80,100,200,100);
		c.add(label_1);
		
		label_2.setIcon(updateIcon[1]);
		label_2.setBounds(380,100,200,100);
		c.add(label_2);
		
		label_3.setIcon(updateIcon[2]);
		label_3.setBounds(680,100,200,100);
		c.add(label_3);
		
		label_4.setIcon(updateIcon[3]);
		label_4.setBounds(980,100,200,100);
		c.add(label_4);
		
		label_5.setIcon(updateIcon[4]);
		label_5.setBounds(80,400,200,100);
		c.add(label_5);
		
		label_6.setIcon(updateIcon[5]);
		label_6.setBounds(380,400,200,100);
		c.add(label_6);
		
		label_7.setIcon(updateIcon[6]);
		label_7.setBounds(680,400,200,100);
		c.add(label_7);
		
		label_8.setIcon(updateIcon[7]);
		label_8.setBounds(980,400,200,100);
		c.add(label_8); //아이콘 8칸 이미지 배치
		
		na_label_1.setText(item[0]);
		na_label_1.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_1.setBounds(80, 50, 650, 400);
		c.add(na_label_1);
		
		na_label_2.setText(item[1]);
		na_label_2.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_2.setBounds(380, 50, 650, 400);
		c.add(na_label_2);
		
		na_label_3.setText(item[2]);
		na_label_3.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_3.setBounds(680, 50, 650, 400);
		c.add(na_label_3);
		
		na_label_4.setText(item[3]);
		na_label_4.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_4.setBounds(980, 50, 650, 400);
		c.add(na_label_4);
		
		na_label_5.setText(item[4]);
		na_label_5.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_5.setBounds(80, 350, 650, 400);
		c.add(na_label_5);
		
		na_label_6.setText(item[5]);
		na_label_6.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_6.setBounds(380, 350, 650, 400);
		c.add(na_label_6);
		
		na_label_7.setText(item[6]);
		na_label_7.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_7.setBounds(680, 350, 650, 400);
		c.add(na_label_7);
		
		na_label_8.setText(item[7]);
		na_label_8.setFont(new Font("SanSerif",Font.BOLD,16));
		na_label_8.setBounds(980, 350, 650, 400);
		c.add(na_label_8); //이름 텍스트 배치
		
		ra_label_1.setText(quality[0]);
		ra_label_1.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_1.setBounds(80, 80, 650, 400);
		c.add(ra_label_1);
		
		ra_label_2.setText(quality[1]);
		ra_label_2.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_2.setBounds(380, 80, 650, 400);
		c.add(ra_label_2);
		
		ra_label_3.setText(quality[2]);
		ra_label_3.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_3.setBounds(680, 80, 650, 400);
		c.add(ra_label_3);
		
		ra_label_4.setText(quality[3]);
		ra_label_4.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_4.setBounds(980, 80, 650, 400);
		c.add(ra_label_4);
		
		ra_label_5.setText(quality[4]);
		ra_label_5.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_5.setBounds(80, 380, 650, 400);
		c.add(ra_label_5);
		
		ra_label_6.setText(quality[5]);
		ra_label_6.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_6.setBounds(380, 380, 650, 400);
		c.add(ra_label_6);
		
		ra_label_7.setText(quality[6]);
		ra_label_7.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_7.setBounds(680, 380, 650, 400);
		c.add(ra_label_7);
		
		ra_label_8.setText(quality[7]);
		ra_label_8.setFont(new Font("SanSerif",Font.BOLD,16));
		ra_label_8.setBounds(980, 380, 650, 400);
		c.add(ra_label_8); //품질 라벨 배치
		
		pr_label_1.setText(numberFormat.format(price[0]));
		pr_label_1.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_1.setBounds(80, 110, 650, 400);
		c.add(pr_label_1);
		
		pr_label_2.setText(numberFormat.format(price[1]));
		pr_label_2.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_2.setBounds(380, 110, 650, 400);
		c.add(pr_label_2);
		
		pr_label_3.setText(numberFormat.format(price[2]));
		pr_label_3.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_3.setBounds(680, 110, 650, 400);
		c.add(pr_label_3);
		
		pr_label_4.setText(numberFormat.format(price[3]));
		pr_label_4.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_4.setBounds(980, 110, 650, 400);
		c.add(pr_label_4);
		
		pr_label_5.setText(numberFormat.format(price[4]));
		pr_label_5.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_5.setBounds(80, 410, 650, 400);
		c.add(pr_label_5);
		
		pr_label_6.setText(numberFormat.format(price[5]));
		pr_label_6.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_6.setBounds(380, 410, 650, 400);
		c.add(pr_label_6);
		
		pr_label_7.setText(numberFormat.format(price[6]));
		pr_label_7.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_7.setBounds(680, 410, 650, 400);
		c.add(pr_label_7);
		
		pr_label_8.setText(numberFormat.format(price[7]));
		pr_label_8.setFont(new Font("SanSerif",Font.BOLD,16));
		pr_label_8.setBounds(980, 410, 650, 400);
		c.add(pr_label_8); //가격 라벨 배치
		
		ma_label_1.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[0]-ma_count[0]));
		ma_label_1.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_1.setBounds(80, 140, 650, 400);
		c.add(ma_label_1);
		
		ma_label_2.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[1]-ma_count[1]));
		ma_label_2.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_2.setBounds(380, 140, 650, 400);
		c.add(ma_label_2);
		
		ma_label_3.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[2]-ma_count[2]));
		ma_label_3.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_3.setBounds(680, 140, 650, 400);
		c.add(ma_label_3);
		
		ma_label_4.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[3]-ma_count[3]));
		ma_label_4.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_4.setBounds(980, 140, 650, 400);
		c.add(ma_label_4);
		
		ma_label_5.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[4]-ma_count[4]));
		ma_label_5.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_5.setBounds(80, 440, 650, 400);
		c.add(ma_label_5);
		
		ma_label_6.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[5]-ma_count[5]));
		ma_label_6.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_6.setBounds(380, 440, 650, 400);
		c.add(ma_label_6);
		
		ma_label_7.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[6]-ma_count[6]));
		ma_label_7.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_7.setBounds(680, 440, 650, 400);
		c.add(ma_label_7);
		
		ma_label_8.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[7]-ma_count[7]));
		ma_label_8.setFont(new Font("SanSerif",Font.BOLD,16));
		ma_label_8.setBounds(980, 440, 650, 400);
		c.add(ma_label_8);
		
		limit_label.setText("남은 외부 카운트 횟수 : " + bargain_limit_out[0] );
		limit_label.setFont(new Font("SanSerif",Font.BOLD,16));
		limit_label.setBounds(930, 760,300,20);
		c.add(limit_label);

		JLabel input_label = new JLabel(); //입력받는 칸을 설명하는 라벨
		
		input_label.setText("선택할 물건의 번호를 고르세요. ex) 1");
		pr_label_7.setFont(new Font("SanSerif",Font.BOLD,20));
		input_label.setBounds(700, 570, 650, 400);
		c.add(input_label);
		
		NumberFormatter limit = new NumberFormatter();
	    limit.setValueClass(Integer.class);
	    limit.setMinimum(new Integer(1));
	    limit.setMaximum(new Integer(8));
	    
	    JFormattedTextField input = new JFormattedTextField(limit); //입력 테스트 박스 
		input.setBounds(930, 760,300,20);
	    input.setColumns(10);
	    c.add(input);
	    
	    limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
	    limit_label.setFont(new Font("SanSerif",Font.BOLD,16));
	    limit_label.setBounds(930, 740, 300, 20);
		c.add(limit_label);
		
	    /*JFormattedTextField limit_field = new JFormattedTextField(limit);
	    limit_field.setBounds(930, 800, 300, 20);
	    limit_field.setColumns(10);
	    
	    c.add(limit_field); */
	    
		
		input.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					    System.out.println( bargain_limit_out[0]);
					    
					    int num=Integer.parseInt(input.getText()); //살 물건의 숫자 입력받기
					    if((num>0&&num<9)) //만약 숫자 범위를 벗어나거나 
					    {
					    	
					    }
					    else if ((manner_temperature[num-1]- bargain_limit_in[num-1] == 0)||(bargain_limit_so[num-1]>0)) //협상할 수 없는 물건(기회 소진or 이미 사버림)를 골랐을 시 협상제한 예외 던지기
					    {
					    	bargain_limit_out[0]++;
					    	limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
					    	//limit_label.setText("남은 외부 카운트 횟수 : " + 8-bargain_limit_out[0] );
					    	throw new MyException_2();
					    }
					    else
					    {
					    	throw new MyException_3();
					    }

					    JOptionPane choice = new JOptionPane(); 
						Icon showing_Icon = updateIcon[num-1];
						String num_str_A=(String)choice.showInputDialog("<html>아이템: "+item[num-1]+"<br>상태: "+quality[num-1]+"<br>가격: "+numberFormat.format(price[num-1])+"<br>플레이어 A. 협상금액을 말씀해주세요</html>");
						showing_money_A = Integer.parseInt(num_str_A); //협상금액 제시하기
						String num_str_B=(String)choice.showInputDialog("<html>아이템: "+item[num-1]+"<br>상태: "+quality[num-1]+"<br>가격: "+numberFormat.format(price[num-1])+"<br>플레이어 B. 협상금액을 말씀해주세요</html>");
						showing_money_B = Integer.parseInt(num_str_B); //협상금액 제시하기
						
						if ((showing_money_A >= can_price[num-1]) && (showing_money_B >= can_price[num-1])) // 만약 제시금액>= 협상가능가격이면
						{
							if((showing_money_A > money_A) && (showing_money_B > money_B)) //제시금액>보유금엑 이라면
							{
								
								throw new MyException(); //예외발생
								
							}
							else if(showing_money_A > money_A) //제시금액 보유금액
							{
								
							}
							else if(showing_money_B > money_B)
							{
								
							}
							else if(showing_money_A == showing_money_B)
							{
								
							}
							else if(manner_temperature[num-1]-bargain_limit_in[num-1]==0)
                            {
                                 JOptionPane.showMessageDialog(null, "협상 기회를 다 써버렸습니다.","실패", JOptionPane.WARNING_MESSAGE);
                            }
							else //아니면
							{
								if (showing_money_A > showing_money_B)
								{
									
									JOptionPane.showMessageDialog(null, "A에게 협상되었습니다","성공", JOptionPane.WARNING_MESSAGE);
									money_A = money_A - showing_money_A; //보유금액에서 협상 금액빼기
									rank_A++;
								}
								else
								{
									JOptionPane.showMessageDialog(null, "B에게 협상되었습니다","성공", JOptionPane.WARNING_MESSAGE);
									money_B = money_B - showing_money_B; //보유금액에서 협상 금액빼기
									rank_B++;
									
								}	
								
								
								switch(num) 
								{  
								   case 1:
									   label_1.setIcon(so_updateIcon); //협상되면 이미지 sold out 으로 바꿔준뒤
									   bargain_limit_out[0]++; //물품 다시 거래 못하도록 제한 걸어줌
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++; //물품 이미 거래된거 표시
									   break;
								   case 2:
									   label_2.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								   case 3:
									   label_3.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								   case 4:
									   label_4.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								   case 5:
									   label_5.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								   case 6:
									   label_6.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								   case 7:
									   label_7.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								   case 8:
									   label_8.setIcon(so_updateIcon);
									   bargain_limit_out[0]++;
									   limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
									   bargain_limit_so[num-1]++;
									   break;
								}
									
							}
						}
						else
						{
							if(manner_temperature[num-1]-bargain_limit_in[num-1]==0)// 만약 협상기회가 0이 된다면 
							{
							  //걍 넘어가기	 
								JOptionPane.showMessageDialog(null, "협상 기회를 다 써버렸습니다.","실패", JOptionPane.WARNING_MESSAGE);
							}
							else
							{
								switch(num)
								{  
								    case 1:
								    	bargain_limit_in[0]++;  //협상횟수 1 줄어들고 남은 협상횟수 출력하기.
								    	ma_label_1.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));	
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 2:
								    	bargain_limit_in[1]++;
								    	ma_label_2.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 3:
								    	bargain_limit_in[2]++;
								    	ma_label_3.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 4:
								    	bargain_limit_in[3]++;
								    	ma_label_4.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 5:
								    	bargain_limit_in[4]++;
								    	ma_label_5.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 6:
								    	bargain_limit_in[5]++;
								    	ma_label_6.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 7:
								    	bargain_limit_in[6]++;
								    	ma_label_7.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								    case 8:
								    	bargain_limit_in[7]++;
								    	ma_label_8.setText("남은 협상 횟수 : " + Integer.toString(manner_temperature[num-1]-bargain_limit_in[num-1]));
								    	if((manner_temperature[num-1]-bargain_limit_in[num-1])==0)
								    	{
								    		bargain_limit_out[0]++;
								    		limit_label.setText("남은 외부카운트:" + (8-bargain_limit_out[0]));
								    	}
								    	break;
								
								}
									
									switch(rd.nextInt(3)+1) //게임 못한다고 까고 다시
									{
									    case 1:
									    	JOptionPane.showMessageDialog(null, "다른 곳을 알아보세요","실패", JOptionPane.WARNING_MESSAGE);
									    	break;
									    case 2:
									    	JOptionPane.showMessageDialog(null, "물가를 모르시는군요!","실패", JOptionPane.WARNING_MESSAGE);
									    	break;
									    case 3:
									    	JOptionPane.showMessageDialog(null, "당신에게는 이 물건을 살 자격이 없어요.","실패", JOptionPane.WARNING_MESSAGE);
									    	break;
									}
					     }
							
							
							
						}
						
				      }
				   catch(ArrayIndexOutOfBoundsException n_2) 
				      {
					   //JOptionPane.showMessageDialog(null, "1~8까지의 수를 입력하세요","입력 제한", JOptionPane.WARNING_MESSAGE);
				      } 
				   catch (MyException e1)
				      {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				      }  
				   catch (MyException_2 e1) 
				      {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				      } 
				   catch (MyException_3 e1) 
				      {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				      }
				   catch (NumberFormatException e1)
				      {
					   
				      }
						    
			}
			
		}, "login", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_FOCUSED); //엔터키로 입력받음
		
		c.add(input);
		
		b1.addActionListener(new ActionListener() { //게임 시작 버튼 눌렀을 때
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 if(bargain_limit_out[0]==8)
				    {
				    	game_calculate(rank_A,rank_B,money_A,money_B); //게임시작
						frame.setVisible(false); //이전 창 안보이게 하기
				    }
				
			}	
		} ); 
		
		
		
		/*input.add(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			    int num=Integer.parseInt(input.getText()); //숫자 입력받고
			    
				JOptionPane choice = new JOptionPane(); //살 물건 고르기
				Icon showing_Icon = updateIcon[num-1];
				//String num_str=(String)choice.showInputDialog(null,"협상금액을 말씀해주세요","협상", (Integer) null,showing_Icon,null, null);
				
				
				/*int showing_money = Integer.parseInt(num_str);  //입력받은 문자열을 정수형으로 바꿔주는 메소드
				if (showing_money >= can_price[num-1]) // 만약 제시금액>= 협상가능가격이면
				{
					if(showing_money> money) //제시금액>보유금엑 이라면
					{
						System.out.println("제시금액이 보유금액보다 많네요! 다시 입력하세요");
						System.out.println("현재 보유금액: "+money); //보유금액 보여주기
						
					}
					else //아니면
					{

						System.out.printf("협상 되었습니다!");
						System.out.println("\n");
						
						 item[num-1]= "팔림/";
						price[num-1] = -1;
						can_price[num-1] = -1; //다 쓴값 팔림/-1로 비우기 
					
						Integer[] price_1 = Arrays.stream(price).boxed().toArray(Integer[]::new);
						Integer[] can_price_1 = Arrays.stream(can_price).boxed().toArray(Integer[]::new);
						Integer[] manner_temperature_1 = Arrays.stream(manner_temperature).boxed().toArray(Integer[]::new);
						
						item = removeElementUsingCollection(item, num-1 );
						quality = removeElementUsingCollection(quality, num-1 );
						price_1 =removeElementUsingCollection(price_1, num-1 );
						can_price_1 = removeElementUsingCollection(can_price_1, num-1 );
						manner_temperature_1 = removeElementUsingCollection(manner_temperature_1, num-1 );
						
						price = Arrays.stream(price_1).mapToInt(i->i).toArray();
						can_price = Arrays.stream(can_price_1).mapToInt(i->i).toArray();
						manner_temperature = Arrays.stream(manner_temperature_1).mapToInt(i->i).toArray();
						
						money = money - showing_money; //보유금액에서 협상 금액빼기
						
						out_count++; //외부카운트++
						rank++;
						a--;
						break; //협상 됬으니 탈출 
					}
				 }
				 else //제시금액<협상가능 가격 이면
				 {
					switch(rd.nextInt(3)+1) //게임 못한다고 까고 다시
					{
					    case 1:
					    	System.out.println("다른 곳을 알아보세요");
					    	break;
					    case 2:
					    	System.out.println("물가를 모르시군요");
					    	break;
					    case 3:
					    	System.out.println("당신에게는 이 물건을 살 자격이 없어요.");
					    	break;
					}
					in_count++;//내부 카운트++
				 }
				
	    	 }*/
				
				// TODO Auto-generated method stub
				
			
		
		
		
		
			
		
		
		/*ImageIcon icon = new ImageIcon(
				Danngn.class.getResource("/image/2.png")
				);
		Image img = icon.getImage();
		Image updateImg = img.getScaledInstance(165, 100, Image.SCALE_SMOOTH);
		ImageIcon updateIcon = new ImageIcon(updateImg);
		
		label.setIcon(updateIcon);
		label.setBounds(300,500,200,100);
		label.setHorizontalAlignment(JLabel.CENTER);
		c.add(label); */
		
		
		
				
		
		frame.setVisible(true);
		frame.setSize(1280,900); //JFrame 크기
		frame.setTitle("경매장터 게임"); //타이틀 표시해줌.
		frame.setLocationRelativeTo(null); //창이 가운데 나오게 해줌
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); //창을 닫으면 정상적으로 프로그램 종료 
		
		
		
		
		
		
		
				
	} //당근마켓

	

	public void game_calculate(int rank_A,int rank_B,int money_A,int money_B) {
		// TODO Auto-generated method stub
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		JButton b1 = new JButton("다시 게임하기");
		JButton b2 = new JButton("게임 종료");
		b1.setBounds(315,350,200,50);
		b2.setBounds(315,500,200,50);
		
		if(rank_A==rank_B)
		{
			if( money_A == money_B )
			{
				label.setText("무승부 입니다");
			}
			else if( money_A > money_B )
			{
				label.setText("플레이어A의 승리입니다");
			}
			else
			{
				label.setText("플레이어 B의 승리입니다");
			}
			
		}
		else if( rank_A > rank_B )
		{
			label.setText("플레이어A의 승리입니다");
			
		}
		else
		{
			label.setText("플레이어 B의 승리입니다");
			
		}
		/*switch(rank)
		{
		case 1: case 2: case 3:
			label.setText("<html>당신의 랭크는 C입니다.<br>당신의 남은 돈은 "+money+"원입니다.</html>");
			break;
		case 4: case 5: case 6:
			label.setText("<html>당신의 랭크는 B입니다.<br>당신의 남은 돈은 "+money+"원입니다.</html>");
			break;
		case 7:
			label.setText("<html>당신의 랭크는 A점입니다.<br>당신의 남은 돈은 "+money+"원입니다.</html>");
			break;
		case 8:
			label.setText("<html>당신의 랭크는 S점입니다.<br>당신의 남은 돈은 "+money+"원입니다.</html>");
			break;*/
		
		label.setFont(new Font("SanSerif",Font.BOLD,18));
		label.setBounds(80, 100, 650, 400); //설명 입력
		Container c = frame.getContentPane();
		
		c.setLayout(null);
		c.add(b1);
		c.add(b2);
		c.add(label);
		
		b1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Main(); //게임시작
				frame.setVisible(false); //이전 창 안보이게 하기
				
			}
			
		});
		
		b2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0); 				
			}
			
	    });
		
		
		frame.setVisible(true);
		frame.setSize(800,800);
		frame.setTitle("경매장터 게임");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); //창설정
		
		
		
	}

}
