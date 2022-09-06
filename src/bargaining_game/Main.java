package bargaining_game;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Container;

public class Main extends JFrame
	{
		Danngn da = new Danngn(); //�떦洹쇰쭏耳� 媛앹껜 �깮�꽦
		Bungae bu = new Bungae(); //踰덇컻�옣�꽣 媛앹껜 �깮�꽦
		Auction au = new Auction(); //寃쎈ℓ�옣�꽣 媛앹껜 �깮�꽦
		
		Music mu = new Music();
		mu.setDaemon(true);
		mu.start();
	
		public Main() //硫붿씤 �깮�꽦�옄
		{
			
			
			
			Container c = getContentPane();
			c.setLayout(null); //�젅�씠�븘�썐�쓣 �궡 留덉쓬��濡� �꽕�젙 媛��뒫
			
			JLabel label = new JLabel();
			ImageIcon logo = new ImageIcon(Main.class.getResource("/image/�삊�긽寃뚯엫.png"));
			Image logo_img = logo.getImage();
			Image logo_updating = logo_img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			ImageIcon logo_updateIcon = new ImageIcon(logo_updating);
			label.setIcon(logo_updateIcon);
			label.setBounds(300, 100, 400, 200);
			
			JButton b1 = new JButton("寃뚯엫 �떆�옉");
			b1.setBounds(300,310,200,50); //踰꾪듉 �쐞移� 議곗젙
			JButton b2 = new JButton("寃뚯엫 諛⑸쾿");
			b2.setBounds(300,430,200,50);
			JButton b3 =new JButton("寃뚯엫 醫낅즺");
			b3.setBounds(300,550,200,50);
			c.add(b1);
			c.add(b2);
			c.add(b3);
			c.add(label);
			
			
			
			setVisible(true); //李쎌씠 蹂댁엫
			setSize(800,800); //JFrame �겕湲�
			setTitle("�삊�긽 寃뚯엫"); //���씠�� �몴�떆�빐以�.
			setLocationRelativeTo(null); //李쎌씠 媛��슫�뜲 �굹�삤寃� �빐以�
			setDefaultCloseOperation(EXIT_ON_CLOSE); //李쎌쓣 �떕�쑝硫� �젙�긽�쟻�쑝濡� �봽濡쒓렇�옩 醫낅즺 
			
			b1.addActionListener(new ActionListener() { //寃뚯엫 �떆�옉 踰꾪듉 �닃���쓣 �븣
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					new Start_Choice(); //寃뚯엫�떆�옉_�꽑�깮李� �떎�뻾
					setVisible(false); //�씠�쟾 李� �븞蹂댁씠寃� �븯湲�
					
				}	
			});
			
			b2.addActionListener(new ActionListener() { //寃뚯엫 �꽕紐� 踰꾪듉 �닃���쓣 �븣
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					new Tutorial_Choice(); //寃뚯엫�꽕紐�_�꽑�깮李� �떎�뻾
					setVisible(false); //�씠�쟾 李� �븞蹂댁씠寃� �븯湲�
					
				}	
			});
			
			b3.addActionListener(new ActionListener() { //寃뚯엫 醫낅즺 踰꾪듉 �닃���쓣 �븣
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.exit(0); //�봽濡쒓렇�옩 醫낅즺
					
				}	
			});
			
			
		}
	
		public class Start_Choice extends JFrame{ //寃뚯엫�떆�옉_�꽑�깮李�
			Start_Choice(){
				
					JOptionPane choice = new JOptionPane();
					String num_str=choice.showInputDialog("<html>寃뚯엫�쓣 �꽑�깮�븯�꽭�슂.<br>1.�떦洹쇰쭏耳� 2.踰덇컻�옣�꽣 3.寃쎈ℓ�옣�꽣</html>");
					//�뀓�뒪�듃 諛뺤뒪濡� 臾몄옄�뿴�쓣 �엯�젰諛쏅뒗 �떎�씠�뼹濡쒓렇 硫붿냼�뱶(由ы꽩媛�: �궗�슜�옄媛� �엯�젰�븳 臾몄옄�뿴�씠�굹 null)
					if(num_str == null || num_str.equals(""))
					{
						choice.setVisible(false);
						new Main();
					}
					else 
					{
						
						int num = Integer.parseInt(num_str); //�엯�젰諛쏆� 臾몄옄�뿴�쓣 �젙�닔�삎�쑝濡� 諛붽퓭二쇰뒗 硫붿냼�뱶
						{
							if(num==1)
							{
								da.game_start(); //�떦洹쇰쭏耳� �떆�옉
								setVisible(false);
								
				    		    
							}    
							else if(num==2)
				    	    {
				    	    	bu.game_start(); //踰덇컻�옣�꽣 �떆�옉
				    	    	setVisible(false);
				    	    	
				    	    	
				    	    }    	 
				    	    else if(num==3)
				    	    {
				    	    	au.game_start(); //寃쎈ℓ�옣�꽣 �떆�옉
				    	    	setVisible(false);
				    	    	
				    	    	
				    	    }
				    	    else
				    	    {
				    	    	JOptionPane.showMessageDialog(null, "1~3�쓽 �닔瑜� �엯�젰�븯�꽭�슂","�젣�븳", JOptionPane.WARNING_MESSAGE);
				    	    } 
							
							
				    	 }
						
					}
				    }	
				
			 }
	
	
		public class Tutorial_Choice extends JFrame{ //寃뚯엫�꽕紐�_�꽑�깮李�
			Tutorial_Choice(){
				
					JOptionPane choice = new JOptionPane();
					String num_str=choice.showInputDialog("<html>ㄱ</html>");
					//�뀓�뒪�듃 諛뺤뒪濡� 臾몄옄�뿴�쓣 �엯�젰諛쏅뒗 �떎�씠�뼹濡쒓렇 硫붿냼�뱶(由ы꽩媛�: �궗�슜�옄媛� �엯�젰�븳 臾몄옄�뿴�씠�굹 null)
					if(num_str == null || num_str.equals(""))
					{
						choice.setVisible(false);
						new Main();
					}
					else
					{
						int num = Integer.parseInt(num_str); //�엯�젰諛쏆� 臾몄옄�뿴�쓣 �젙�닔�삎�쑝濡� 諛붽퓭二쇰뒗 硫붿냼�뱶
						if(num==1)
						{
							da.game_tutorial(); //�떦洹쇰쭏耳� �꽕紐�
							
			    		   
						}    
						else if(num==2)
			    	    {
			    	    	bu.game_tutorial(); //踰덇컻�옣�꽣 �꽕紐�
			    	    	
			    	    	
			    	    }    	 
			    	    else if(num==3)
			    	    {
			    	    	au.game_tutorial(); //寃쎈ℓ�옣�꽣 �꽕紐�
			    	   	
			    	    }
			    	    else
			    	    {
			    	    	JOptionPane.showMessageDialog(null, "1~3�쓽 �닔瑜� �엯�젰�븯�꽭�슂","�젣�븳", JOptionPane.WARNING_MESSAGE);
			    	    }
					}
					
			    	      	 
			        }
				
				}
	
		
	    
	
	
		
		public static void main(String[] args) //硫붿씤�븿�닔
		{
			new Main();
		}
	
		
	}
	
	

